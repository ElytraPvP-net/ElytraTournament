package net.elytrapvp.elytratournament.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItemListener implements Listener {
    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if(event.getItemDrop().getItemStack().getType() == Material.GLASS_BOTTLE || event.getItemDrop().getItemStack().getType() == Material.BOWL) {
            event.getItemDrop().remove();
            return;
        }

        event.setCancelled(true);
    }
}