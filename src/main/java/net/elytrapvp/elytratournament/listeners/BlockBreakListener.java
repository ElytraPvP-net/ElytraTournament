package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class BlockBreakListener implements Listener {
    private final ElytraTournament plugin;

    public BlockBreakListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority =  EventPriority.HIGHEST)
    public void onEvent(BlockBreakEvent event) {
        if(event.isCancelled()) {
            return;
        }

        Player player = event.getPlayer();

        if(player.getGameMode() == GameMode.CREATIVE) {
            return;
        }

        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            event.setCancelled(true);
            return;
        }

        if(!game.getBlocks().contains(event.getBlock().getLocation())) {
            event.setCancelled(true);
            return;
        }

        if(event.getBlock().getType() == Material.LEAVES || event.getBlock().getType() == Material.WOOL) {
            return;
        }

        // Get the drops from the block and add them to the inventory.
        Collection<ItemStack> drops = event.getBlock().getDrops();
        drops.forEach(drop -> player.getInventory().addItem(drop));

        // Set the block to air to prevent item drops.
        event.setCancelled(true);
        event.getBlock().setType(Material.AIR);
    }
}