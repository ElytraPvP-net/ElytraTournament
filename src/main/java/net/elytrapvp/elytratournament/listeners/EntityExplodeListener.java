package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class EntityExplodeListener implements Listener {
    private final ElytraTournament plugin;

    public EntityExplodeListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        e.setCancelled(true);

        if(!(e.getEntity() instanceof TNTPrimed)) {
            return;
        }

        TNTPrimed tnt = (TNTPrimed) e.getEntity();

        if(tnt.getSource() == null) {
            tnt.getSourceLoc().getWorld().getBlockAt(tnt.getSourceLoc()).setType(Material.TNT);
            return;
        }

        if(!tnt.getSource().isValid()) {
            return;
        }

        if(!(tnt.getSource() instanceof Player)) {
            return;
        }

        Player shooter = (Player) tnt.getSource();

        if(plugin.gameManager().getGame(shooter) == null) {
            return;
        }

        Game game = plugin.gameManager().getGame(shooter);

        if(tnt.getSourceLoc() == null) {
            return;
        }

        game.addBlock(tnt.getSourceLoc(), Material.TNT);

        tnt.remove();
    }

}