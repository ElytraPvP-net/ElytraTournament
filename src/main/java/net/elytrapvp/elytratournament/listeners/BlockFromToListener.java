package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockFromToListener implements Listener {
    private final ElytraTournament plugin;

    public BlockFromToListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(BlockFromToEvent event) {

        Material type = event.getBlock().getType();
        if (type == Material.WATER || type == Material.STATIONARY_WATER || type == Material.LAVA || type == Material.STATIONARY_LAVA){
            Block b = event.getToBlock();
            if (b.getType() == Material.AIR){
                generatesCobble(type, b);
            }
        }


    }

    private final BlockFace[] faces = new BlockFace[]{
            BlockFace.SELF,
            BlockFace.UP,
            BlockFace.DOWN,
            BlockFace.NORTH,
            BlockFace.EAST,
            BlockFace.SOUTH,
            BlockFace.WEST
    };

    public boolean generatesCobble(Material type, Block b) {
        Material mirrorID1 = (type == Material.WATER || type == Material.STATIONARY_WATER ? Material.LAVA : Material.WATER);
        Material mirrorID2 = (type == Material.WATER || type == Material.STATIONARY_WATER ? Material.STATIONARY_LAVA : Material.STATIONARY_WATER);
        for (BlockFace face : faces) {
            Block r = b.getRelative(face, 1);
            if (r.getType() == mirrorID1 || r.getType() == mirrorID2) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> r.setType(Material.AIR), 200);

                for(BlockFace face2 : BlockFace.values()) {
                    Block s = r.getRelative(face2, 1);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (s.getType() == Material.COBBLESTONE) {
                                // TODO: Check for both water and lava, and if found replace as lava.
                                s.setType(Material.AIR);
                            }
                        }
                    }.runTaskLater(plugin, 200);
                }
                return true;
            }
        }
        return false;
    }
}