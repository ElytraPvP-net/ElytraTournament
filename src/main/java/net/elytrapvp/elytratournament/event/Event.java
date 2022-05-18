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

import java.util.*;

/**
 * This class represents a tournament.
 */
public class Event {
    private final ElytraTournament plugin;
    private final Map<Long, String> players = new HashMap<>();
    private final int tournamentNumber;
    private final String hostName;
    private int taskID;
    private final Player host;

    private final Challonge challonge;
    private Tournament tournament;

    /**
     * Creates the event.
     * @param plugin Plugin instance.
     */
    public Event(ElytraTournament plugin) {
        this.plugin = plugin;

        // Get Player Data
        host = plugin.eventManager().host();
        hostName = host.getName();
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(host);
        tournamentNumber = customPlayer.getTournamentsHosted() + 1;
        customPlayer.addTournamentHosted();

        // Connects to Challonge
        Credentials credentials = new Credentials(plugin.settingsManager().getConfig().getString("challonge.username"), plugin.settingsManager().getConfig().getString("challonge.api-key"));
        Serializer serializer = new GsonSerializer();
        RestClient restClient = new RetrofitRestClient();
        challonge = new Challonge(credentials, serializer, restClient);

        // Update bungeecord again, just in case.
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("cancel");
        Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "Tournament", out.toByteArray());

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            createTournament();

            // Starts the tournament.
            plugin.getServer().getScheduler().runTask(plugin, () -> {
                // Add 1 to tournaments played counter.
                for(CustomPlayer player : plugin.customPlayerManager().getPlayers()) {
                    if(plugin.eventManager().spectators().contains(player.getPlayer())) {
                        continue;
                    }

                    customPlayer.addTournamentPlayed();
                }

                startEvent();
            });
        });
    }

    /**
     * Creates the tournament through challonge.
     */
    private void createTournament() {
        try {
            // Create the tournament
            TournamentQuery.TournamentQueryBuilder builder = TournamentQuery.builder();
            builder.name(hostName + "'s Tournament #" + tournamentNumber)
                    .gameName("Minecraft")
                    .description(plugin.eventManager().kit().getName() + " tournament on ElytraPvP. Join us at play.elytrapvp.net")
                    .holdThirdPlaceMatch(true);

            // Sets the tournament type of the tournament.
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

            // Store the players and their participant ids.
            participants.forEach(participant -> players.put(participant.getId(), participant.getName()));
        }
        catch (DataAccessException exception) {
            tournament = null;
            ChatUtils.chat(host, "&c&lError &8» &cSomething went wrong while creating the tournament! Check console for details.");
            exception.printStackTrace();

            plugin.eventManager().reset();
            Bukkit.setWhitelist(false);
        }
    }

    /**
     * Get the current challonge connection.
     * @return Current challonge object.
     */
    public Challonge getChallonge() {
        return challonge;
    }

    /**
     * Get a player by their tournament id.
     * @param id Tournament id.
     * @return Corresponding player.
     */
    public Player getPlayer(Long id) {
        return Bukkit.getPlayer(players.get(id));
    }

    /**
     * Get a player's tournament id.
     * @param player Player to get id from.
     * @return Corresponding id.
     */
    public Long getPlayerID(Player player) {
        for(Long id : players.keySet()) {
            if(players.get(id).equals(player.getName())) {
                return id;
            }
        }
        return null;
    }

    /**
     * Get the current tournament.
     * @return Tournament object.
     */
    public Tournament getTournament() {
        return tournament;
    }

    /**
     * Starts the event.
     */
    private void startEvent() {
        // Set the status to RUNNING to update the scoreboard.
        plugin.eventManager().eventStatus(EventStatus.RUNNING);

        // Update scoreboard.
        Bukkit.getScheduler().runTask(plugin, ()-> Bukkit.getOnlinePlayers().forEach(player -> new EventScoreboard(plugin, player)));

        // Disables the whitelist.
        Bukkit.setWhitelist(false);

        // Attempts to start the tournament through challonge.
        boolean started = false;
        while(!started) {
            try {
                challonge.startTournament(tournament);
                started = true;
            } catch (DataAccessException e) {
                e.printStackTrace();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        // Displays the start message to all online players.
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
        plugin.getServer().getScheduler().runTask(plugin, () -> Bukkit.getOnlinePlayers().forEach(ItemUtils::giveSpectateItems));

        // Repeatedly loops through the matches, starting any that are waiting to be started.
        taskID = Bukkit.getScheduler().scheduleAsyncRepeatingTask(plugin, ()-> {
            boolean matchesObtained = false;
            while(!matchesObtained) {
                try {
                    // Gets all matches that aren't complete.
                    List<Match> matches = new ArrayList<>();
                    for(Match match : challonge.getMatches(tournament)) {
                        if(match.getState() == MatchState.COMPLETE) {
                            continue;
                        }
                        matches.add(match);
                    }

                    // Ends the event if there are no matches left.
                    if(matches.size() == 0) {
                        stopEvent();
                        return;
                    }

                    // Loops through all matches waiting to be started.
                    for(Match match : matches) {
                        // Makes sure the match hasn't already been started.
                        if(match.getUnderwayAt() != null) {
                            continue;
                        }

                        // Makes sure match has 2 waiting players.
                        if(match.getPlayer1Id() == null || match.getPlayer2Id() == null) {
                            continue;
                        }

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

                        plugin.getServer().getScheduler().runTask(plugin, () -> {
                            // Makes sure the players are not already in a game.
                            if(plugin.gameManager().getGame(player1) != null ||
                                    plugin.gameManager().getGame(player2) != null) {
                                return;
                            }

                            // Make sures there is an open arena
                            if(plugin.arenaManager().getOpenArena(plugin.eventManager().kit()) != null) {
                                Game game = plugin.gameManager().createGame(match);
                                game.addPlayers(player1, player2);
                                game.start();
                            }
                        });
                    }
                    matchesObtained = true;
                }
                catch (DataAccessException e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            System.out.println("Tournament Looping");
        },0, 200);
    }

    /**
     * Stops the current event.
     */
    public void stopEvent() {
        // Cancels the repeating task.
        Bukkit.getScheduler().cancelTask(taskID);

        // Finalizes the tournament
        boolean finished = false;
        while(!finished) {
            try {
                challonge.finalizeTournament(tournament);
                finished = true;
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


        // Loads all participants in the tournament.
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

        // Stores all participant's final rank.
        for(Participant participant : participants) {
            results.put(participant, participant.getFinalRank());
        }

        // Sorts the results to get final rankings.
        Map<Participant, Integer> rankings = MapUtils.sortByValue(results);
        List<Participant> top = new ArrayList<>(rankings.keySet());

        // Assigns medals if the tournament is large enough.
        if(top.size() >= 16) {
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
        }

        // Displays end message to all online players.
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

        // Resets the event manager and teleports everyone back to the spawn.
        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            plugin.eventManager().reset();

            for(Player player : Bukkit.getOnlinePlayers()) {
                player.teleport(LocationUtils.getSpawn(plugin));
                ItemUtils.giveLobbyItems(player);
            }
        }, 200);
    }
}
