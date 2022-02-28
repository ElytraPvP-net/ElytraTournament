package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    private final ElytraTournament plugin;

    public BlockPlaceListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        if(game == null) {
            return;
        }

        if(game.getGameState() == GameState.COUNTDOWN || game.getGameState() == GameState.END) {
            event.setCancelled(true);
            return;
        }

        plugin.eventManager().kit().onBlockPlace(game, event);
        game.addBlock(event.getBlock().getLocation(), Material.AIR);
    }
}