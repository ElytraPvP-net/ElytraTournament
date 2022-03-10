package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerBucketEmptyListener implements Listener {
    private final ElytraTournament plugin;

    public PlayerBucketEmptyListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        if(event.getBucket() != null && event.getBucket() == Material.WATER_BUCKET) {
            generatesCobble(Material.STATIONARY_WATER, event.getBlockClicked().getRelative(event.getBlockFace()));
        }

        if(game == null) {
            return;
        }

        Location location = event.getBlockClicked().getRelative(event.getBlockFace()).getLocation();
        game.addBlock(location, Material.AIR);
    }

    private final BlockFace[] faces = new BlockFace[]{
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    public void generatesCobble(Material type, Block b) {
        Material mirrorID1 = (type == Material.WATER || type == Material.STATIONARY_WATER ? Material.LAVA : Material.WATER);
        Material mirrorID2 = (type == Material.WATER || type == Material.STATIONARY_WATER ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER);
        for (BlockFace face : faces) {
            Block r = b.getRelative(face, 1);

            if (r.getType() == mirrorID1 || r.getType() == mirrorID2) {
                if(r.getData() == 0) {
                    continue;
                }

                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    r.setType(Material.AIR);
                }, 200);

                for(BlockFace face2 : BlockFace.values()) {
                    Block s = r.getRelative(face2, 1);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(s.getType() == Material.COBBLESTONE) {
                                // TODO: Check for both water and lava, and if found replace as lava.
                                s.setType(Material.AIR);
                            }
                        }
                    }.runTaskLater(plugin, 200);
                }
            }
        }
    }
}