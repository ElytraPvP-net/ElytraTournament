package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import net.elytrapvp.elytratournament.utils.item.SkullBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class FinalUHCKit extends Kit {

    public FinalUHCKit(ElytraTournament plugin) {
        super(plugin, "Final UHC");
        setIconMaterial(Material.DIAMOND_HELMET);
        setGameMode(GameMode.SURVIVAL);
        setNaturalRegen(false);
        setKnockback("rod");

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        helmet.setDurability((short) 121);

        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 3)
                .build();
        chestplate.setDurability((short) 176);

        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        leggings.setDurability((short) 165);

        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .build();
        boots.setDurability((short) 143);

        ItemStack spareHelmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        spareHelmet.setDurability((short) 121);

        ItemStack spareChestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 2)
                .build();
        spareChestplate.setDurability((short) 176);

        ItemStack spareLeggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        spareLeggings.setDurability((short) 165);

        ItemStack spareBoots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .build();
        spareBoots.setDurability((short) 143);

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 4)
                .setUnbreakable(true).build();
        ItemStack fishingRod = new ItemBuilder(Material.FISHING_ROD)
                .build();

        ItemStack axe = new ItemBuilder(Material.DIAMOND_AXE)
                .addEnchantment(Enchantment.DIG_SPEED, 1)
                .setUnbreakable(true)
                .build();

        ItemStack pickaxe = new ItemBuilder(Material.DIAMOND_PICKAXE)
                .addEnchantment(Enchantment.DIG_SPEED, 1)
                .setUnbreakable(true)
                .build();

        ItemStack gapple = new ItemBuilder(Material.GOLDEN_APPLE, 24).build();
        ItemStack ghead = new SkullBuilder("dedcd92b0e210cfe98892c4334be462b3b9e725ddbd009c2783fcf88f0ffdc53")
                .setDisplayName("&fGolden Head")
                .build();
        ghead.setAmount(4);

        ItemStack lava = new ItemBuilder(Material.LAVA_BUCKET).build();
        ItemStack water = new ItemBuilder(Material.WATER_BUCKET).build();

        ItemStack planks = new ItemBuilder(Material.WOOD, 64).build();
        ItemStack cobblestone = new ItemBuilder(Material.COBBLESTONE, 64).build();

        ItemStack fns = new ItemStack(Material.FLINT_AND_STEEL);
        fns.setDurability((short) 49);

        ItemStack steak = new ItemBuilder(Material.COOKED_BEEF, 64).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, fishingRod);
        addItem(2, lava);
        addItem(3, pickaxe);
        addItem(4, cobblestone);
        addItem(5, gapple);
        addItem(6, ghead);
        addItem(7, fns);
        addItem(8, water);
        addItem(9, spareHelmet);
        addItem(10, spareChestplate);
        addItem(11, spareLeggings);
        addItem(12, spareBoots);
        addItem(13, planks);
        addItem(20, lava);
        addItem(22, planks);
        addItem(26, water);
        addItem(29, lava);
        addItem(30, axe);
        addItem(31, cobblestone);
        addItem(33, steak);
        addItem(35, water);
    }
}