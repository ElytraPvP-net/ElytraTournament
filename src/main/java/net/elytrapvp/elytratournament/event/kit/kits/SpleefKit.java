package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class SpleefKit extends Kit {

    public SpleefKit(ElytraTournament plugin) {
        super(plugin, "Spleef");

        setIconMaterial(Material.SNOW_BALL);
        setGameMode(GameMode.SURVIVAL);
        setTakeDamage(false);
        setVoidLevel(50);

        ItemStack shovel = new ItemBuilder(Material.DIAMOND_SPADE)
                .addEnchantment(Enchantment.DIG_SPEED, 5)
                .setUnbreakable(true)
                .build();
        addItem(0, shovel);
    }

    @Override
    public void onBlockBreak(Game game, BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.SNOW_BLOCK) {
            return;
        }

        if(new Random().nextInt(4) == 0) {
            event.getPlayer().getInventory().addItem(new ItemStack(Material.SNOW_BALL));
        }

        game.addBlock(event.getBlock().getLocation(), Material.SNOW_BLOCK);
        event.getBlock().setType(Material.AIR);
        event.setCancelled(true);
    }
}
