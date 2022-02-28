package net.elytrapvp.elytratournament.utils.scoreboard;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * Represents a scoreborad that is easy
 * to create an customize.
 */
public abstract class CustomScoreboard {
    private static final HashMap<UUID, CustomScoreboard> players = new HashMap<>();

    public CustomScoreboard(Player p) {
        ScoreHelper.removeScore(p);
        players.put(p.getUniqueId(), this);
    }

    public static HashMap<UUID, CustomScoreboard> getPlayers() {
        return players;
    }

    public void addPlayer(Player p) {
        getPlayers().put(p.getUniqueId(), this);
    }

    public abstract void update(Player p);
}