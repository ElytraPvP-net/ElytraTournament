package net.elytrapvp.elytratournament.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public class EntityRegainHealthListener implements Listener {

    @EventHandler
    public void onEvent(EntityRegainHealthEvent event) {
        if(event.getRegainReason() == EntityRegainHealthEvent.RegainReason.SATIATED) {
            event.setCancelled(true);
            return;
        }
    }
}