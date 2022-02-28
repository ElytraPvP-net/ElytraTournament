package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.EventStatus;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {
    private final ElytraTournament plugin;

    public PlayerQuitListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        plugin.customPlayerManager().removePlayer(player);
        event.setQuitMessage(ChatUtils.translate("&8[&c-&8] &c" + player.getName()));

        // Cancel tournament if the player is the host.
        if(plugin.eventManager().eventStatus() == EventStatus.WAITING
        && plugin.eventManager().host().equals(player)) {
            player.chat("/cancel");
        }
    }
}