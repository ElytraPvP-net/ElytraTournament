package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodLevelChangeListener implements Listener {
    private final ElytraTournament plugin;

    public FoodLevelChangeListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(FoodLevelChangeEvent event) {
        // Makes sure we are dealing with a player.
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Game game = plugin.gameManager().getGame(player);

        // Hunger wont lower outside of games
        if(game == null || !plugin.eventManager().kit().hasHunger()) {
            event.setCancelled(true);
        }
    }
}