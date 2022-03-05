package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;

public class ProjectileLaunchListener implements Listener {
    private final ElytraTournament plugin;

    public ProjectileLaunchListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onLaunch(ProjectileLaunchEvent event) {
        if (event.getEntityType().equals(EntityType.FISHING_HOOK)) {
            event.getEntity().setVelocity(event.getEntity().getVelocity().multiply(1.4D));
        }

        if(event.getEntity() instanceof Egg) {
            if(!(event.getEntity().getShooter() instanceof Player)) {
                return;
            }

            Player shooter = (Player) event.getEntity().getShooter();

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if(plugin.gameManager().getGame(shooter) != null) {
                    shooter.getInventory().setItem(1, new ItemStack(Material.EGG));
                }
            }, 50);
            return;
        }


        if(event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();

            if(!(arrow.getShooter() instanceof Player)) {
                return;
            }

            Player shooter = (Player) arrow.getShooter();
            Game game = plugin.gameManager().getGame(shooter);

            if(game == null) {
                return;
            }

            if(game.getGameState() == GameState.RUNNING) {
                return;
            }

            event.setCancelled(true);
        }
    }
}