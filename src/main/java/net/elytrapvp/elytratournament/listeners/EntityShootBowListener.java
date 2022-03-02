package net.elytrapvp.elytratournament.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class EntityShootBowListener implements Listener {
    @EventHandler
    public void onEvent(EntityShootBowEvent event) {
        // Fixes a visual glitch with arrows by resetting their velocity.
        event.getProjectile().setVelocity(event.getProjectile().getVelocity());
    }
}