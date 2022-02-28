package net.elytrapvp.elytratournament.event;

import at.stefangeyer.challonge.Challonge;
import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Credentials;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.Participant;
import at.stefangeyer.challonge.model.Tournament;
import at.stefangeyer.challonge.model.enumeration.MatchState;
import at.stefangeyer.challonge.model.enumeration.TournamentType;
import at.stefangeyer.challonge.model.query.ParticipantQuery;
import at.stefangeyer.challonge.model.query.TournamentQuery;
import at.stefangeyer.challonge.rest.RestClient;
import at.stefangeyer.challonge.rest.retrofit.RetrofitRestClient;
import at.stefangeyer.challonge.serializer.Serializer;
import at.stefangeyer.challonge.serializer.gson.GsonSerializer;
import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.LocationUtils;
import net.elytrapvp.elytratournament.utils.MapUtils;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

/**
 * Represents a tournament.
 */
public class Event {
    private final ElytraTournament plugin;
    private final Map<Long, Player> players = new HashMap<>();
    private final Set<Player> spectators = new HashSet<>();
    private int tournamentNumber = 0;

    private final Challonge challonge;
    private Tournament tournament;

    /**
     * Creates the event.
     * @param plugin Plugin instance.
     */
    public Event(ElytraTournament plugin) {
        this.plugin = plugin;

        // Get Player Data
        Player host = plugin.eventManager().host();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(host);
        tournamentNumber = customPlayer.getTournamentsHosted() + 1;
        customPlayer.addTournamentHosted();

        // Connect to Challonge
        Credentials credentials = new Credentials(plugin.settingsManager().getConfig().getString("challonge.username"), plugin.settingsManager().getConfig().getString("challonge.api-key"));
        Serializer serializer = new GsonSerializer();
        RestClient restClient = new RetrofitRestClient();
        challonge = new Challonge(credentials, serializer, restClient);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            // Create the tournament
            try {
                TournamentQuery.TournamentQueryBuilder builder = TournamentQuery.builder();
                builder.name(host.getName() + "'s Tournament #" + tournamentNumber)
                        .gameName("Minecraft")
                        .description(plugin.eventManager().kit().getName() + " tournament on ElytraPvP. Join us at play.elytrapvp.net");

                switch (plugin.eventManager().eventType()) {
                    case SINGLE_ELIMINATION -> builder.tournamentType(TournamentType.SINGLE_ELIMINATION);
                    case DOUBLE_ELIMINATION -> builder.tournamentType(TournamentType.DOUBLE_ELIMINATION);
                }
                tournament = challonge.createTournament(builder.build());

                // Add players to it.
                for(Player player : Bukkit.getOnlinePlayers()) {
                    addParticipant(player);
                }
            }
            catch (DataAccessException exception) {
                tournament = null;
                ChatUtils.chat(host, "&c&lError &8» &cSomething went wrong while creating the tournament! Check console for details.");
                exception.printStackTrace();
            }

            // Run the tournament.
            new BukkitRunnable() {
                @Override
                public void run() {
                    runEvent();
                }
            }.runTask(plugin);
        });
    }

    /**
     * Add a player as a participant.
     * @param player Player to add as a participant.
     * @throws DataAccessException
     */
    private void addParticipant(Player player) throws DataAccessException {
        Participant participant = challonge.addParticipant(tournament, ParticipantQuery.builder().name(player.getName()).build());
        players.put(participant.getId(), player);
    }

    /**
     * Get a player by their tournament id.
     * @param id Tournament id.
     * @return Corresponding player.
     */
    public Player getPlayer(Long id) {
        return players.get(id);
    }

    /**
     * Get a player's tournament id.
     * @param player Player to get id from.
     * @return Corresponding id.
     */
    public Long getPlayerID(Player player) {
        for(Long id : players.keySet()) {
            if(players.get(id).equals(player)) {
                return id;
            }
        }

        return null;
    }

    /**
     * Start the event.
     */
    private void runEvent() {
        // Set the status to RUNNING to update the scoreboard.
        plugin.eventManager().eventStatus(EventStatus.RUNNING);

        // Update scoreboard
        Bukkit.getOnlinePlayers().forEach(player -> new EventScoreboard(plugin, player));

        Bukkit.broadcastMessage(ChatUtils.translate("&a&lTournament &8» &aThe tournament has been started."));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                challonge.startTournament(tournament);

                for(Match match : challonge.getMatches(tournament)) {

                    // Make sure the match hasn't been done yet.
                    if(match.getCompletedAt() != null) {
                        return;
                    }

                    if(match.getPlayer1Id() != null && match.getPlayer2Id() != null) {
                        Player player1 = getPlayer(match.getPlayer1Id());
                        Player player2 = getPlayer(match.getPlayer2Id());

                        if(player1 == null) {
                            match.setForfeited(true);
                            match.setWinnerId(getPlayerID(player2));
                            continue;
                        }
                        else if(player2 == null) {
                            match.setForfeited(true);
                            match.setWinnerId(getPlayerID(player1));
                            continue;
                        }

                        challonge.markMatchAsUnderway(match);

                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Game game = plugin.gameManager().createGame(match);
                                game.addPlayers(player1, player2);
                                game.start();
                            }
                        }.runTask(plugin);
                    }
                }
            }
            catch (DataAccessException exception) {
                ChatUtils.chat(plugin.eventManager().host(), "&c&lError &8» &cSomething went wrong while starting the tournament! Check console for details.");
                exception.printStackTrace();
            }
        });
    }

    public Challonge getChallonge() {
        return challonge;
    }

    public void nextRound() {
        System.out.println("Next Round");
        if(plugin.gameManager().getActiveGames().size() > 0) {
            System.out.println("More Games Left");
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    List<Match> matches = new ArrayList<>();

                    for(Match match : challonge.getMatches(tournament)) {
                        if(match.getState() == MatchState.COMPLETE) {
                            System.out.println("Completed Match");
                            continue;
                        }
                        System.out.println("Incomplete Match");
                        matches.add(match);
                    }

                    System.out.println("Matches: " + matches.size());
                    if(matches.size() > 0) {
                        for(Match match : matches) {

                            if(match.getPlayer1Id() != null && match.getPlayer2Id() != null) {
                                Player player1 = getPlayer(match.getPlayer1Id());
                                Player player2 = getPlayer(match.getPlayer2Id());

                                if(player1 == null) {
                                    match.setForfeited(true);
                                    match.setWinnerId(getPlayerID(player2));
                                    continue;
                                }
                                else if(player2 == null) {
                                    match.setForfeited(true);
                                    match.setWinnerId(getPlayerID(player1));
                                    continue;
                                }

                                challonge.markMatchAsUnderway(match);

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        Game game = plugin.gameManager().createGame(match);
                                        game.addPlayers(player1, player2);
                                        game.start();
                                    }
                                }.runTask(plugin);
                            }
                        }
                    }
                    else {
                        challonge.finalizeTournament(tournament);

                        Map<Player, Integer> players = new HashMap<>();

                        for(Participant participant : challonge.getParticipants(tournament)) {
                            players.put(getPlayer(participant.getId()), participant.getFinalRank());
                        }

                        Map<Player, Integer> rankings = MapUtils.sortByValue(players);
                        List<Player> topPlayers = new ArrayList<>();
                        topPlayers.addAll(rankings.keySet());

                        for(Player player : Bukkit.getOnlinePlayers()) {
                            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                            ChatUtils.centeredChat(player, "&a&l" + plugin.eventManager().host().getName() + "'s Tournament #" + tournamentNumber);
                            ChatUtils.chat(player, "");
                            ChatUtils.centeredChat(player, "&aKit: &f" + plugin.eventManager().kit().getName());
                            ChatUtils.chat(player, "");
                            ChatUtils.centeredChat(player, "&6&lGold: &f" + topPlayers.get(0).getName());
                            ChatUtils.centeredChat(player, "&f&lSilver: &f" + topPlayers.get(1).getName());

                            if(topPlayers.size() >= 3) {
                                ChatUtils.centeredChat(player, "&c&lBronze: &f" + topPlayers.get(2).getName());
                            }
                            else {
                                ChatUtils.centeredChat(player, "&c&lBronze: &fNone");
                            }
                            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                        }

                        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                            plugin.eventManager().eventStatus(EventStatus.NONE);
                            plugin.eventManager().activeEvent(null);
                            plugin.eventManager().bestOf(BestOf.NONE);
                            plugin.eventManager().host(null);
                            plugin.eventManager().kit(null);

                            for(Player player : Bukkit.getOnlinePlayers()) {
                                player.teleport(LocationUtils.getSpawn(plugin));
                            }
                        }, 200);
                    }
                }
                catch (DataAccessException exception) {
                    ChatUtils.chat(plugin.eventManager().host(), "&c&lError &8» &cSomething went wrong starting the next round! Check console for details.");
                    exception.printStackTrace();
                }
            });
        }, 100);
    }
}
