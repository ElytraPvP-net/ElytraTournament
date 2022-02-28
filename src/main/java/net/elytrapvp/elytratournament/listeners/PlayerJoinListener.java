package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.EventScoreboard;
import net.elytrapvp.elytratournament.utils.LocationUtils;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.item.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final ElytraTournament plugin;

    public PlayerJoinListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        // Registers the player's data.
        Player player = event.getPlayer();
        plugin.customPlayerManager().addPlayer(player);

        new EventScoreboard(plugin, player);
        player.teleport(LocationUtils.getSpawn(plugin));
        player.setGameMode(GameMode.ADVENTURE);
        event.setJoinMessage(ChatUtils.translate("&8[&a+&8] &a" + player.getName()));

        ItemUtils.giveLobbyItems(player);
    }
}