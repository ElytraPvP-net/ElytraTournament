package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BlitzKit extends Kit {

    public BlitzKit(ElytraTournament plugin) {
        super(plugin, "Blitz");
        setIconMaterial(Material.STONE_SWORD);
        setGameMode(GameMode.SURVIVAL);
        setKnockback("nospeed");

        ItemStack helmet = new ItemBuilder(Material.CHAINMAIL_HELMET)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.CHAINMAIL_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.STONE_SWORD).setUnbreakable(true).build();
        ItemStack bow = new ItemBuilder(Material.BOW).setUnbreakable(true).build();
        ItemStack axe = new ItemBuilder(Material.IRON_AXE).setUnbreakable(true).build();
        ItemStack blocks1 = new ItemBuilder(Material.WOOD, 32).build();
        ItemStack blocks2 = new ItemBuilder(Material.LEAVES, 32).build();
        ItemStack gapple = new ItemBuilder(Material.GOLDEN_APPLE, 1).build();
        ItemStack carrot = new ItemBuilder(Material.GOLDEN_CARROT, 16).build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 12).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, bow);
        addItem(2, axe);
        addItem(3, blocks1);
        addItem(4, blocks2);
        addItem(7, gapple);
        addItem(8, carrot);
        addItem(35, arrows);
    }
}