package net.elytrapvp.elytratournament.event.game;

import at.stefangeyer.challonge.model.Match;
import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages all active games.
 */
public class GameManager {
    private final ElytraTournament plugin;
    private final List<Game> activeGames = new ArrayList<>();

    public GameManager(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates a new game with a specific match.
     * @param match Match to use.
     * @return Game that was created.
     */
    public Game createGame(Match match) {
        Game game = new Game(plugin, plugin.arenaManager().getOpenArena(plugin.eventManager().kit()), match);
        getActiveGames().add(game);
        return game;
    }

    /**
     * Destroys an existing game object.
     * @param game Game to destroy.
     */
    public void destroyGame(Game game) {
        getActiveGames().remove(game);
    }

    /**
     * Get all currently active games.
     * @return All active games.
     */
    public List<Game> getActiveGames() {
        return activeGames;
    }

    /**
     * Get the gane a player is currently in.
     * Returns null if no game found.
     * @param player Player to get game of.
     * @return Game player is currently in.
     */
    public Game getGame(Player player) {
        for(Game game : getActiveGames()) {
            if(game.getPlayers().contains(player)) {
                return game;
            }
        }
        return null;
    }
}