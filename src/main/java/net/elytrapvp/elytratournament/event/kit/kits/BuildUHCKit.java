package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import net.elytrapvp.elytratournament.utils.item.SkullBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class BuildUHCKit extends Kit {

    public BuildUHCKit(ElytraTournament plugin) {
        super(plugin, "Build UHC");
        setGameMode(GameMode.SURVIVAL);
        setIconMaterial(Material.LAVA_BUCKET);
        setNaturalRegen(false);
        setStrongGapple(true);
        setKnockback("rod");

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                .setUnbreakable(true).build();
        ItemStack fishingRod = new ItemBuilder(Material.FISHING_ROD)
                .build();
        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 3)
                .setUnbreakable(true).build();
        ItemStack axe = new ItemBuilder(Material.DIAMOND_AXE).setUnbreakable(true).build();

        ItemStack gapple = new ItemBuilder(Material.GOLDEN_APPLE, 6).build();
        ItemStack ghead = new SkullBuilder("dedcd92b0e210cfe98892c4334be462b3b9e725ddbd009c2783fcf88f0ffdc53")
                .setDisplayName("&fGolden Head")
                .build();
        ghead.setAmount(3);

        ItemStack lava = new ItemBuilder(Material.LAVA_BUCKET).build();
        ItemStack water = new ItemBuilder(Material.WATER_BUCKET).build();

        ItemStack blocks = new ItemBuilder(Material.WOOD, 64).build();

        ItemStack arrows = new ItemBuilder(Material.ARROW, 24).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, fishingRod);
        addItem(2, bow);
        addItem(3, axe);
        addItem(4, gapple);
        addItem(5, ghead);
        addItem(6, lava);
        addItem(7, water);
        addItem(8, blocks);
        addItem(35, arrows);
        addItem(34, lava);
        addItem(33, water);
        addItem(32, blocks);
    }
}