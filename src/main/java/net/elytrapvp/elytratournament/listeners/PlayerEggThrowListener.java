package net.elytrapvp.elytratournament.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;

public class PlayerEggThrowListener implements Listener {

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent event) {
        // Prevent egg from spawning a chicken
        event.setHatching(false);
    }
}
