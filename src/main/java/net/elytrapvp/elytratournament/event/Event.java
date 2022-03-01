package net.elytrapvp.elytratournament.event;

import at.stefangeyer.challonge.Challonge;
import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Credentials;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.Participant;
import at.stefangeyer.challonge.model.Tournament;
import at.stefangeyer.challonge.model.enumeration.MatchState;
import at.stefangeyer.challonge.model.enumeration.TournamentType;
import at.stefangeyer.challonge.model.query.MatchQuery;
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
import net.elytrapvp.elytratournament.utils.item.ItemUtils;
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
    private final Map<Long, Participant> eventParticipants = new HashMap<>();
    private int tournamentNumber = 0;
    private final String hostName;
    private int taskID;

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
        hostName = host.getName();
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
                        .description(plugin.eventManager().kit().getName() + " tournament on ElytraPvP. Join us at play.elytrapvp.net").holdThirdPlaceMatch(true);

                switch (plugin.eventManager().eventType()) {
                    case SINGLE_ELIMINATION -> builder.tournamentType(TournamentType.SINGLE_ELIMINATION);
                    case DOUBLE_ELIMINATION -> builder.tournamentType(TournamentType.DOUBLE_ELIMINATION);
                }
                tournament = challonge.createTournament(builder.build());

                // Add players to it.
                List<ParticipantQuery> queries = new ArrayList<>();
                for(Player player : Bukkit.getOnlinePlayers()) {
                    queries.add(ParticipantQuery.builder().name(player.getName()).build());
                }
                List<Participant> participants = challonge.bulkAddParticipants(tournament, queries);

                for(Participant participant : participants) {
                    //players.put(participant.getId(), Bukkit.getPlayer(participant.getName()));

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            players.put(participant.getId(), Bukkit.getPlayer(participant.getName()));
                            eventParticipants.put(participant.getId(), participant);
                        }
                    }.runTask(plugin);
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
                    startEvent();
                }
            }.runTask(plugin);
        });
    }

    public Participant getParticipant(Long id) {
        return eventParticipants.get(id);
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
    private void runsEvent() {
        // Set the status to RUNNING to update the scoreboard.
        plugin.eventManager().eventStatus(EventStatus.RUNNING);

        // Update scoreboard
        Bukkit.getOnlinePlayers().forEach(player -> new EventScoreboard(plugin, player));

        Bukkit.setWhitelist(false);
        Bukkit.broadcastMessage(ChatUtils.translate("&a&lTournament &8» &aThe tournament has been started."));

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                challonge.startTournament(tournament);
                startEvent();
                /*
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
                 */
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
        if(plugin.gameManager().getActiveGames().size() > 0) {
            return;
        }

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
                try {
                    List<Match> matches = new ArrayList<>();

                    for(Match match : challonge.getMatches(tournament)) {
                        if(match.getState() == MatchState.COMPLETE) {
                            continue;
                        }
                        matches.add(match);
                    }

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

                        Map<Participant, Integer> results = new HashMap<>();

                        for(Participant participant : challonge.getParticipants(tournament)) {
                            results.put(participant, participant.getFinalRank());
                        }

                        Map<Participant, Integer> rankings = MapUtils.sortByValue(results);
                        List<Participant> top = new ArrayList<>(rankings.keySet());


                        for(Player player : Bukkit.getOnlinePlayers()) {
                            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                            ChatUtils.centeredChat(player, "&a&l" + hostName + "'s Tournament #" + tournamentNumber);
                            ChatUtils.centeredChat(player, "&aKit: &f" + plugin.eventManager().kit().getName());
                            ChatUtils.chat(player, "");
                            ChatUtils.centeredChat(player, "&6&lGold: &f" + top.get(0).getName());
                            ChatUtils.centeredChat(player, "&f&lSilver: &f" + top.get(1).getName());

                            if(top.size() >= 3) {
                                ChatUtils.centeredChat(player, "&c&lBronze: &f" + top.get(2).getName());
                            }
                            else {
                                ChatUtils.centeredChat(player, "&c&lBronze: &fNone");
                            }
                            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                        }

                        /*
                        Map<Player, Integer> players = new HashMap<>();

                        for(Participant participant : challonge.getParticipants(tournament)) {
                            players.put(getPlayer(participant.getId()), participant.getFinalRank());
                        }

                        Map<Player, Integer> rankings = MapUtils.sortByValue(players);
                        List<Player> topPlayers = new ArrayList<>();
                        topPlayers.addAll(rankings.keySet());

                        CustomPlayer first = plugin.customPlayerManager().getPlayer(topPlayers.get(0));
                        first.addGoldMedal();

                        CustomPlayer second = plugin.customPlayerManager().getPlayer(topPlayers.get(1));
                        second.addSilverMedal();

                        if(topPlayers.size() > 3) {
                            CustomPlayer third = plugin.customPlayerManager().getPlayer(topPlayers.get(2));
                            third.addBronzeMedal();
                        }



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

                        */

                        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                            plugin.eventManager().eventStatus(EventStatus.NONE);
                            plugin.eventManager().activeEvent(null);
                            plugin.eventManager().bestOf(BestOf.NONE);
                            plugin.eventManager().host(null);
                            plugin.eventManager().kit(null);

                            for(Player player : Bukkit.getOnlinePlayers()) {
                                player.teleport(LocationUtils.getSpawn(plugin));
                                ItemUtils.giveLobbyItems(player);
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

    public Map<Long, Player> getPlayers() {
        return players;
    }

    public void startEvent() {
        // Set the status to RUNNING to update the scoreboard.
        plugin.eventManager().eventStatus(EventStatus.RUNNING);

        // Update scoreboard
        Bukkit.getScheduler().runTask(plugin, ()-> {
            Bukkit.getOnlinePlayers().forEach(player -> new EventScoreboard(plugin, player));
        });


        Bukkit.setWhitelist(false);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
            try {
                challonge.startTournament(tournament);
            } catch (DataAccessException e) {
                e.printStackTrace();
            }

            Bukkit.broadcastMessage(ChatUtils.translate("&a&lTournament &8» &aThe tournament has been started."));

            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
                try {
                    List<Match> matches = new ArrayList<>();

                    for(Match match : challonge.getMatches(tournament)) {
                        if(match.getState() == MatchState.COMPLETE) {
                            continue;
                        }
                        matches.add(match);
                    }

                    if(matches.size() > 0) {
                        for(Match match : matches) {
                            if(match.getUnderwayAt() != null) {
                                return;
                            }

                            if(match.getPlayer1Id() != null && match.getPlayer2Id() != null) {
                                Player player1 = getPlayer(match.getPlayer1Id());
                                Player player2 = getPlayer(match.getPlayer2Id());

                                if(player1 == null) {
                                    match.setForfeited(true);
                                    match.setWinnerId(getPlayerID(player2));

                                    MatchQuery query = MatchQuery.builder()
                                            .winnerId(getPlayerID(player2))
                                            .scoresCsv("0-" + plugin.eventManager().bestOf().getNeededWins())
                                            .build();
                                    challonge.updateMatch(match, query);
                                    continue;
                                }
                                else if(player2 == null) {
                                    match.setForfeited(true);

                                    MatchQuery query = MatchQuery.builder()
                                            .winnerId(getPlayerID(player1))
                                            .scoresCsv(plugin.eventManager().bestOf().getNeededWins() + "-0")
                                            .build();
                                    challonge.updateMatch(match, query);
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
                        stopEvent();
                        challonge.finalizeTournament(tournament);

                        Map<Participant, Integer> results = new HashMap<>();

                        for(Participant participant : challonge.getParticipants(tournament)) {
                            results.put(participant, participant.getFinalRank());
                        }

                        Map<Participant, Integer> rankings = MapUtils.sortByValue(results);
                        List<Participant> top = new ArrayList<>(rankings.keySet());


                        for(Player player : Bukkit.getOnlinePlayers()) {
                            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                            ChatUtils.centeredChat(player, "&a&l" + hostName + "'s Tournament #" + tournamentNumber);
                            ChatUtils.centeredChat(player, "&aKit: &f" + plugin.eventManager().kit().getName());
                            ChatUtils.chat(player, "");
                            ChatUtils.centeredChat(player, "&6&lGold: &f" + top.get(0).getName());
                            ChatUtils.centeredChat(player, "&f&lSilver: &f" + top.get(1).getName());

                            if(top.size() >= 3) {
                                ChatUtils.centeredChat(player, "&c&lBronze: &f" + top.get(2).getName());
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
                                ItemUtils.giveLobbyItems(player);
                            }
                        }, 200);
                    }
                }
                catch (DataAccessException exception) {
                    ChatUtils.chat(plugin.eventManager().host(), "&c&lError &8» &cSomething went wrong starting the next round! Check console for details.");
                    exception.printStackTrace();
                }
            },0, 300);
        });
    }

    public void stopEvent() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
