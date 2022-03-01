package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import net.elytrapvp.elytratournament.guis.SettingsGUI;
import net.elytrapvp.elytratournament.guis.SpectateGUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    private ElytraTournament plugin;

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