package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {
    private final ElytraTournament plugin;

    public PlayerMoveListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        if(plugin.eventManager().kit().waterKills()) {
            Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
            Block block2 = player.getLocation().getBlock();

            if(block.getType() == Material.STATIONARY_WATER || block2.getType() == Material.STATIONARY_WATER) {

                if(game.getGameState() == GameState.COUNTDOWN) {
                    player.teleport(game.getArena().getSpawns().get(0));
                    return;
                }

                if(game.getGameState() == GameState.END) {
                    return;
                }

                game.playerKilled(player);
                return;
            }
        }

        if(player.getLocation().getY() < plugin.eventManager().kit().getVoidLevel()) {
            if(game.getGameState() == GameState.COUNTDOWN) {
                player.teleport(game.getArena().getSpawns().get(0));
                return;
            }

            game.playerKilled(player);
            player.teleport(game.getArena().getSpawns().get(0));
            player.setAllowFlight(true);
            player.setFlying(true);
        }
    }
}