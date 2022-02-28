package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.potion.PotionEffectType;

public class EntityRegainHealthListener implements Listener {
    private final ElytraTournament plugin;

    public EntityRegainHealthListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(EntityRegainHealthEvent event) {
        // Exit if not a player.
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();

        // Allow regeneration to work.
        if(player.hasPotionEffect(PotionEffectType.REGENERATION)) {
            return;
        }

        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        if(!plugin.eventManager().kit().naturalRegen()) {
            event.setCancelled(true);
        }
    }
}