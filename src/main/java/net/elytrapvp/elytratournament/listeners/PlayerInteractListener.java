package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import net.elytrapvp.elytratournament.guis.SettingsGUI;
import net.elytrapvp.elytratournament.guis.SpectateGUI;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.HashSet;
import java.util.Set;

public class PlayerInteractListener implements Listener {
    private ElytraTournament plugin;
    private final Set<Player> pearlCooldown = new HashSet<>();


    public PlayerInteractListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        // Prevent using items during game countdown.
        if(game != null && game.getGameState() != GameState.RUNNING) {
            event.setCancelled(true);

            // Fixes visual glitch with throwables during countdown.
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), player.getItemInHand());
            return;
        }

        // Checks for the ender pearl cooldown.
        if(plugin.eventManager().activeEvent() != null && event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if(pearlCooldown.contains(player)) {
                ChatUtils.chat(player, "&cThat item is currently on cooldown.");
                event.setCancelled(true);
                return;
            }

            if(plugin.eventManager().kit().hasPearlCooldown()) {
                pearlCooldown.add(player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> pearlCooldown.remove(player), 200);
            }
        }

        // Exit if the item is null.
        if(event.getItem() == null)
            return;

        // Exit if item meta is null.
        if(event.getItem().getItemMeta() == null)
            return;

        String item = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());

        if(item == null) {
            return;
        }

        switch (item) {
            case "Settings" -> {
                new SettingsGUI(plugin, player).open(player);
                event.setCancelled(true);
            }

            case "Create Tournament" -> {
                player.chat("/create");
                event.setCancelled(true);
            }

            case "Spectate" -> {
                new SpectateGUI(plugin).open(player);
                event.setCancelled(true);
            }
        }
    }
}