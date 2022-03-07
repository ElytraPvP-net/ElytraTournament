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
import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
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
    private final int tournamentNumber;
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

        // Update bungeecord again, just in case.
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("cancel");
        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "Tournament", out.toByteArray());

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
                        }
                    }.runTask(plugin);
                }
            }
            catch (DataAccessException exception) {
                tournament = null;
                ChatUtils.chat(host, "&c&lError &8» &cSomething went wrong while creating the tournament! Check console for details.");
                exception.printStackTrace();

                plugin.eventManager().activeEvent(null);
                plugin.eventManager().eventStatus(EventStatus.NONE);
                Bukkit.setWhitelist(false);
            }

            // Run the tournament.
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Add 1 to tournaments played counter.
                    plugin.customPlayerManager().getPlayers().forEach(CustomPlayer::addTournamentPlayed);
                    startEvent();
                }
            }.runTask(plugin);
        });
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

    public Challonge getChallonge() {
        return challonge;
    }

    public Map<Long, Player> getPlayers() {
        return players;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public void startEvent() {
        // Set the status to RUNNING to update the scoreboard.
        plugin.eventManager().eventStatus(EventStatus.RUNNING);

        // Update scoreboard
        Bukkit.getScheduler().runTask(plugin, ()-> Bukkit.getOnlinePlayers().forEach(player -> new EventScoreboard(plugin, player)));


        Bukkit.setWhitelist(false);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
            boolean sent2 = false;
            while(!sent2) {
                try {
                    challonge.startTournament(tournament);
                    sent2 = true;
                } catch (DataAccessException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            for(Player player : Bukkit.getOnlinePlayers()) {
                ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                ChatUtils.centeredChat(player, "&a&l" + hostName + "'s Tournament #" + tournamentNumber);
                ChatUtils.chat(player, "");
                ChatUtils.centeredChat(player, "&aKit: &f" + plugin.eventManager().kit().getName());
                ChatUtils.centeredChat(player, "&aTeams: &f1v1 &7(" + plugin.eventManager().bestOf() + ")");
                ChatUtils.chat(player, "");
                ChatUtils.centeredChat(player, "&aBracket: &fhttps://challonge.com/" + tournament.getUrl());
                ChatUtils.chat(player, "");
                ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
            }

            // Give spectator items.
            new BukkitRunnable() {
                @Override
                public void run() {
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        ItemUtils.giveSpectateItems(player);
                    }
                }
            }.runTask(plugin);

            taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, ()-> {
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

                                boolean sent = false;
                                while (!sent) {
                                    try {
                                        challonge.markMatchAsUnderway(match);
                                        sent = true;
                                        Thread.sleep(1000);
                                    }
                                    catch (DataAccessException | InterruptedException exception) {
                                        exception.printStackTrace();
                                    }
                                }

                                try {
                                    Thread.sleep(100);
                                }
                                catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        // Make sures there is an open arena
                                        if(plugin.arenaManager().getOpenArena(plugin.eventManager().kit()) != null) {
                                            Game game = plugin.gameManager().createGame(match);
                                            game.addPlayers(player1, player2);
                                            game.start();
                                        }
                                    }
                                }.runTask(plugin);
                            }
                        }
                    }
                    else {
                        stopEvent();
                        challonge.finalizeTournament(tournament);

                        Map<Participant, Integer> results = new HashMap<>();

                        List<Participant> participants = new ArrayList<>();
                        boolean sent = false;
                        while(!sent) {
                            try {
                                participants.addAll(challonge.getParticipants(tournament));
                                sent = true;
                            }
                            catch (DataAccessException exception) {
                                exception.printStackTrace();

                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }

                        for(Participant participant : participants) {
                            results.put(participant, participant.getFinalRank());
                        }

                        Map<Participant, Integer> rankings = MapUtils.sortByValue(results);
                        List<Participant> top = new ArrayList<>(rankings.keySet());

                        Player gold = Bukkit.getPlayer(top.get(0).getName());
                        if(gold != null) {
                            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(gold);
                            customPlayer.addGoldMedal();
                        }

                        Player silver = Bukkit.getPlayer(top.get(1).getName());
                        if(silver != null) {
                            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(silver);
                            customPlayer.addSilverMedal();
                        }

                        if(top.size() >= 3) {
                            Player bronze = Bukkit.getPlayer(top.get(2).getName());
                            if(bronze != null) {
                                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(bronze);
                                customPlayer.addBronzeMedal();
                            }
                        }

                        for(Player player : Bukkit.getOnlinePlayers()) {
                            ChatUtils.chat(player, "&8&m+-----------------------***-----------------------+");
                            ChatUtils.centeredChat(player, "&a&l" + hostName + "'s Tournament #" + tournamentNumber);
                            ChatUtils.chat(player, "");
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
            },0, 200);
        });
    }

    /**
     * Stops the current event.
     */
    public void stopEvent() {
        Bukkit.getScheduler().cancelTask(taskID);
    }
}
