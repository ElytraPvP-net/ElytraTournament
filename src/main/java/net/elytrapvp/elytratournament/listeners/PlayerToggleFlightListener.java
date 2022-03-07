package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class PlayerToggleFlightListener implements Listener {
    private final ElytraTournament plugin;
    private static final Set<Player> delay = new HashSet<>();

    public PlayerToggleFlightListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        if(game.getGameState() != GameState.RUNNING) {
            return;
        }

        // Exit if game does not have double jumps.
        if(plugin.eventManager().kit().getDoubleJumps() == 0) {
            return;
        }

        // Prevents players from double jumping too often.
        if(delay.contains(player)) {
            return;
        }

        // Prevents players from "flying" when having no double jumps left.
        if(game.getDoubleJumps(player) == 0) {
            event.setCancelled(true);
            player.setFlying(false);
            player.setAllowFlight(false);
            return;
        }

        delay.add(player);
        player.setAllowFlight(false);
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            delay.remove(player);
            player.setAllowFlight(true);
        }, 15);
        game.removeDoubleJump(player);
        player.setFlying(false);

        Vector vector = player.getLocation().getDirection().normalize().multiply(0.5).add(new Vector(0, 0.8, 0));
        player.setVelocity(vector);

        if(game.getDoubleJumps(player) == 0) {
            player.setFlying(false);
            player.setAllowFlight(false);
        }

        event.setCancelled(true);
    }

    public static Set<Player> getDelay() {
        return delay;
    }
}