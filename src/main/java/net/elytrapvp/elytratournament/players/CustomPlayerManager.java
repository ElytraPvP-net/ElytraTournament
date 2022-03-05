package net.elytrapvp.elytratournament.players;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * Manages all CustomPlayer objects.
 */
public class CustomPlayerManager {
    private final ElytraTournament plugin;
    private Map<UUID, CustomPlayer> players = new HashMap<>();

    public CustomPlayerManager(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    /**
     * Add a player to the CustomPlayer list.
     * This is done when they join the game.
     * @param player Player tp add.
     */
    public void addPlayer(Player player) {
        players.put(player.getUniqueId(), new CustomPlayer(plugin, player.getUniqueId()));
    }

    /**
     * Get the CustomPlayer object of a player.
     * @param player Player to get object of.
     * @return Object of the player.
     */
    public CustomPlayer getPlayer(Player player) {
        if(players.containsKey(player.getUniqueId())) {
            return players.get(player.getUniqueId());
        }

        return null;
    }

    /**
     * Get the custom players on all online players.
     * @return all online custom players.
     */
    public Collection<CustomPlayer> getPlayers() {
        return players.values();
    }

    /**
     * Remove a player from the CustomPlayer list.
     * This is done when they leave the game.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        players.remove(player.getUniqueId());
    }
}