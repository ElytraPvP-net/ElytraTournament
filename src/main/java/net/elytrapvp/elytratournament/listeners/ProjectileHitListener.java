package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.util.Vector;

public class ProjectileHitListener implements Listener {
    private final ElytraTournament plugin;

    public ProjectileHitListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(ProjectileHitEvent event) {

        if(!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();
        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        // Runs code for Arrows
        if(event.getEntity() instanceof Arrow) {
            if(plugin.eventManager().kit().hasArrowPickup()) {
                return;
            }

            Arrow arrow = (Arrow) event.getEntity();
            arrow.remove();
            return;
        }

        if(event.getEntity() instanceof Snowball) {
            // Stole this off Spigot, not perfect but gets the job done.
            Location loc = event.getEntity().getLocation();
            Vector vec = event.getEntity().getVelocity();
            Location loc2 = new Location(loc.getWorld(), loc.getX()+vec.getX(), loc.getY()+vec.getY(), loc.getZ()+vec.getZ());
            System.out.println(loc2.getBlock().getTypeId());
            if (loc2.getBlock().getType() == Material.SNOW_BLOCK) {
                game.addBlock(loc2, Material.SNOW_BLOCK);
                loc2.getBlock().setType(Material.AIR);
            }
        }
    }
}