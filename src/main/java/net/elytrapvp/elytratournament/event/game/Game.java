package net.elytrapvp.elytratournament.event.game;

import at.stefangeyer.challonge.Challonge;
import at.stefangeyer.challonge.exception.DataAccessException;
import at.stefangeyer.challonge.model.Match;
import at.stefangeyer.challonge.model.query.MatchQuery;
import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.Event;
import net.elytrapvp.elytratournament.event.EventScoreboard;
import net.elytrapvp.elytratournament.event.arena.Arena;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.Timer;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.item.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Game {
    private final Map<Location, Material> blocks = new HashMap<>();
    private final Map<Player, Integer> scores = new HashMap<>();

    private final ElytraTournament plugin;
    private final Arena arena;
    private final Match match;
    private final Kit kit;

    private Timer timer;
    private GameState gameState;
    private Player player1;
    private Player player2;

    public Game(ElytraTournament plugin, Arena arena, Match match) {
        this.plugin = plugin;
        this.arena = arena;
        this.match = match;

        plugin.arenaManager().removeArena(arena);
        gameState = GameState.WAITING;
        this.kit = plugin.eventManager().kit();
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Starts a round.
     */
    public void start() {
        timer = new Timer(plugin);

        int spawn = 0;
        for(Player player : getPlayers()) {
            if (spawn >= arena.getSpawns().size()) {
                spawn = 0;
            }

            player.teleport(arena.getSpawns().get(spawn));
            plugin.eventManager().kit().apply(player);
            new GameScoreboard(plugin, player, this);
            spawn++;
        }

        broadcast("&8&m+-----------------------***-----------------------+");
        for(Player player : getPlayers()) {
            ChatUtils.chat(player, "");
            ChatUtils.centeredChat(player, "&a&l" + plugin.eventManager().kit().getName() + " Duel");
            ChatUtils.chat(player, "");

            ChatUtils.centeredChat(player, "&aOpponent:");
            ChatUtils.centeredChat(player, getOpponent(player).getName());

            ChatUtils.chat(player, "");
            ChatUtils.centeredChat(player, "&aScore");
            ChatUtils.centeredChat(player, "&f" + getScore(player) + " &7-&f " + getScore(getOpponent(player)));
        }
        broadcast("&8&m+-----------------------***-----------------------+");

        countdown();
    }

    /**
     * Runs the countdown of a round.
     */
    public void countdown() {
        gameState = GameState.COUNTDOWN;
        BukkitRunnable countdown = new  BukkitRunnable() {
            int counter = 4;
            public void run() {
                counter--;

                if(gameState == GameState.END) {
                    cancel();
                }

                if(counter  != 0) {
                    broadcast("&aStarting in " + counter + "...");
                    for (Player p : getPlayers()) {
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 1);
                    }
                }
                else {
                    for(Player p : getPlayers()) {
                        p.playSound(p.getLocation(), Sound.NOTE_PLING, 1, 2);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                            running();
                        }, 1);
                        cancel();
                    }
                }
            }
        };
        countdown.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Runs when the round is running.
     */
    public void running() {
        if(gameState == GameState.RUNNING) {
            return;
        }

        gameState = GameState.RUNNING;
        timer.start();

        int spawn = 0;
    }

    /**
     * Runs at the end of a round.
     */
    public void end(Player winner, Player loser) {
        if(gameState == GameState.END) {
            return;
        }

        gameState = GameState.END;
        timer.stop();

        addScore(winner);

        broadcast("&8&m+-----------------------***-----------------------+");
        broadcast(" ");
        broadcastCenter("&a&l" + plugin.eventManager().kit().getName() + " Duel &7- &f&l" + timer.toString());
        broadcast(" ");
        broadcastCenter("&aWinner:");
        broadcastCenter(winner.getName() + " &a(" + ChatUtils.getFormattedHealthPercent(winner) + "&a)");
        broadcast(" ");
        for(Player player : getPlayers()) {
            ChatUtils.centeredChat(player, "&aScore");
            ChatUtils.centeredChat(player, "&f" + getScore(player) + " &7-&f " + getScore(getOpponent(player)));
        }
        broadcast("&8&m+-----------------------***-----------------------+");

        // Start again if more score is needed.
        if(getScore(winner) < plugin.eventManager().bestOf().getNeededWins()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this::start, 100);
            return;
        }


        Bukkit.broadcastMessage(ChatUtils.translate("&a&lTournament &8» &f" + winner.getName() + " &ahas defeated &f" + loser.getName() + " &7(&f" + getScore(winner) + " &7-&f " + getScore(loser) + "&7)&a."));

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for(Player player : getPlayers()) {
                player.getInventory().clear();
                player.getInventory().setArmorContents(null);
                player.setAllowFlight(false);
                player.setFlying(false);
                player.setMaxHealth(20.0);
                player.setHealth(20.0);
                player.teleport(arena.getSpectateSpawn());
                player.spigot().setCollidesWithEntities(true);
                ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

                ItemUtils.giveSpectateItems(player);
                new EventScoreboard(plugin, player);

                for(PotionEffect effect : player.getActivePotionEffects()) {
                    player.removePotionEffect(effect.getType());
                }
            }
            plugin.arenaManager().addArena(arena);

            plugin.gameManager().destroyGame(this);
        }, 100);

        Bukkit.getScheduler().runTaskAsynchronously(plugin, ()-> {
            try {
                Event event = plugin.eventManager().activeEvent();
                Player player1 = event.getPlayer(match.getPlayer1Id());

                MatchQuery.MatchQueryBuilder builder;

                if(winner.equals(player1)) {
                    builder = MatchQuery.builder()
                            .winnerId(event.getPlayerID(winner))
                            .scoresCsv(getScore(winner) + "-" + getScore(loser));
                }
                else {
                    builder = MatchQuery.builder()
                            .winnerId(event.getPlayerID(winner))
                            .scoresCsv(getScore(loser) + "-" + getScore(winner));
                }

                Challonge challonge = plugin.eventManager().activeEvent().getChallonge();

                challonge.updateMatch(match, builder.build());
            } catch (DataAccessException exception) {
                exception.printStackTrace();
            }
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            for(Location location : blocks.keySet()) {
                location.getWorld().getBlockAt(location).setType(blocks.get(location));

                if(!location.getChunk().isLoaded()) {
                    location.getChunk().load();
                    Bukkit.getScheduler().runTaskLater(plugin, () -> location.getChunk().unload(), 100);
                }
            }

            //plugin.eventManager().activeEvent().nextRound();
        }, 120);
    }

    // -----------------------------------------------------------------------------------------------------------------

    /**
     * Add a block to the game cache.
     * Used for arena resetting.
     * @param location Location of the block.
     * @param material Material of the block.
     */
    public void addBlock(Location location, Material material) {
        blocks.put(location, material);
    }

    /**
     * Add players to the game.
     * @param player1 First player to add.
     * @param player2 Second player to add.
     */
    public void addPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        scores.put(player1, 0);
        scores.put(player2, 0);
    }

    /**
     * Add 1 point to the player.
     * @param player Player to add score to.
     */
    public void addScore(Player player) {
        setScore(player, getScore(player) + 1);
    }

    /**
     * Broadcast a message to the arena.
     * @param message Message to broadcast.
     */
    public void broadcast(String message) {
        for(Player player : getPlayers()) {
            ChatUtils.chat(player, message);
        }
    }

    /**
     * Broadcast a centered message to the arena.
     * @param message Message to broadcast.
     */
    public void broadcastCenter(String message) {
        for(Player player : getPlayers()) {
            ChatUtils.centeredChat(player, message);
        }
    }

    /**
     * Get the arena being used in the game.
     * @return Arena being used.
     */
    public Arena getArena() {
        return arena;
    }

    /**
     * Get all blocks that have been placed by players.
     * @return All blocks placed by players.
     */
    public Collection<Location> getBlocks() {
        return blocks.keySet();
    }

    /**
     * Get the state of the game.
     * @return Current game state.
     */
    public GameState getGameState() {
        return gameState;
    }

    /**
     * Get the players in the game.
     * @return Players in the game.
     */
    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        return players;
    }

    /**
     * Get a player's score.
     * @param player Player to get score of.
     * @return The score.
     */
    public int getScore(Player player) {
        return scores.get(player);
    }

    /**
     * Get the Game's timer.
     * @return current timer.
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * Get the opponent of a player.
     * @param player Player to get opponent of.
     * @return Opponent.
     */
    public Player getOpponent(Player player) {
        if(player.equals(player1)) {
            return player2;
        }
        else if(player.equals(player2)) {
            return player1;
        }

        return null;
    }

    /**
     * Runs when a played disconnects.
     * @param player Player who disconnected.
     */
    public void playerDisconnect(Player player) {
        broadcast("&a" + player.getName() + " disconnected.");
        player.getLocation().getWorld().strikeLightning(player.getLocation());

        setScore(getOpponent(player), plugin.eventManager().bestOf().getNeededWins() - 1);
        end(getOpponent(player), player);
    }

    /**
     * Runs when a player is killed.
     * @param player Player who was killed.
     */
    public void playerKilled(Player player) {
        player.getLocation().getWorld().strikeLightning(player.getLocation());
        player.teleport(arena.getSpectateSpawn());
        broadcast("&a" + player.getName() + " has died!");
        new EventScoreboard(plugin, player).addPlayer(player);

        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setMaxHealth(20.0);
        player.setHealth(20.0);
        player.teleport(arena.getSpectateSpawn());
        player.spigot().setCollidesWithEntities(true);
        ((CraftPlayer) player).getHandle().getDataWatcher().watch(9, (byte) 0);

        // Prevents stuff from breaking if the game is already over.
        if(gameState == GameState.END) {
            return;
        }

        end(getOpponent(player), player);
    }

    /**
     * Set the score of a player.
     * @param player Player to set score of.
     * @param score new score.
     */
    public void setScore(Player player, int score) {
        scores.put(player, score);
    }
}