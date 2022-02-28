package net.elytrapvp.elytratournament.event.game.team;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a group of players
 * working together in a Game.
 */
public class Team {
    private final List<Player> players;
    private final Set<Player> alivePlayers;
    private final Set<Player> deadPlayers = new HashSet<>();

    /**
     * Creates a new team with specific players.
     * @param players Players to add to the team.
     */
    public Team(List<Player> players) {
        this.players = players;
        this.alivePlayers = new HashSet<>(players);
    }

    /**
     * Get all alive players on the team.
     * @return All alive players.
     */
    public Set<Player> getAlivePlayers() {
        return alivePlayers;
    }

    /**
     * Gets all dead players on the team.
     * @return All dead players.
     */
    public Set<Player> getDeadPlayers() {
        return deadPlayers;
    }

    /**
     * Gets all players on the team, alive and dead.
     * @return All players on the team.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Add a player to the dead list,
     * and remove them from the alive list.
     * @param player Player to make dead.
     */
    public void killPlayer(Player player) {
        alivePlayers.remove(player);
        deadPlayers.add(player);
    }

    /**
     * Remove a player from the team.
     * @param player Player to remove.
     */
    public void removePlayer(Player player) {
        getPlayers().remove(player);
        getAlivePlayers().remove(player);
        getDeadPlayers().remove(player);
    }
}