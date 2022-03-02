package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {
    private final ElytraTournament plugin;

    public ProjectileHitListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(ProjectileHitEvent event) {
        if(!(event.getEntity() instanceof Arrow)) {
            return;
        }

        if(!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();
        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        if(plugin.eventManager().kit().hasArrowPickup()) {
            return;
        }

        Arrow arrow = (Arrow) event.getEntity();
        arrow.remove();
    }
}