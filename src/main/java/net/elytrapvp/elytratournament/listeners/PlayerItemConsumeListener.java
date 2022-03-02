package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PlayerItemConsumeListener implements Listener {
    private ElytraTournament plugin;

    public PlayerItemConsumeListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerItemConsumeEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        if(event.getItem().getType() == Material.GOLDEN_APPLE) {
            if(!plugin.eventManager().kit().hasStrongGapple()) {
                return;
            }

            PotionEffect effect = new PotionEffect(PotionEffectType.REGENERATION, 100, 2);
            event.getPlayer().addPotionEffect(effect);
        }
    }
}