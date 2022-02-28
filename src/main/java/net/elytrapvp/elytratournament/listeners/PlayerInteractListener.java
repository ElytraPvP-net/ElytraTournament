package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.guis.SettingsGUI;
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

        Player player = event.getPlayer();
        switch (item) {
            case "Settings" -> new SettingsGUI(plugin, player).open(player);
            case "Create Tournament" -> player.chat("/create");
        }
    }
}