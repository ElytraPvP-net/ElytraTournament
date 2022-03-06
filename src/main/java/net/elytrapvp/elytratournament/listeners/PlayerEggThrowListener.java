package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerEggThrowListener implements Listener {
    private final ElytraTournament plugin;

    public PlayerEggThrowListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEggThrow(PlayerEggThrowEvent event) {
        Player player = event.getPlayer();

        // Prevent egg from spawning a chicken
        event.setHatching(false);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if(plugin.gameManager().getGame(player) != null) {
                player.getInventory().setItem(1, new ItemStack(Material.EGG));
            }
        }, 50);
    }
}
