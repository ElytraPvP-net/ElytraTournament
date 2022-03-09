package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class SGKit extends Kit {

    public SGKit(ElytraTournament plugin) {
        super(plugin, "SG");
        setIconMaterial(Material.FLINT_AND_STEEL);
        setGameMode(GameMode.SURVIVAL);
        setHunger(true);
        setKnockback("rod");

        setStartingSaturation(10);

        ItemStack helmet = new ItemBuilder(Material.IRON_HELMET)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.STONE_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 1)
                .setUnbreakable(true)
                .build();

        ItemStack rod = new ItemBuilder(Material.FISHING_ROD).build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setUnbreakable(true)
                .build();

        ItemStack arrows = new ItemBuilder(Material.ARROW, 6).build();
        ItemStack steak = new ItemBuilder(Material.COOKED_BEEF, 10).build();
        ItemStack gapple = new ItemBuilder(Material.GOLDEN_APPLE, 1).build();

        ItemStack fns = new ItemStack(Material.FLINT_AND_STEEL);
        fns.setDurability((short) 60);

        ItemStack cobwebs = new ItemBuilder(Material.WEB, 1).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, rod);
        addItem(2, bow);
        addItem(4, arrows);
        addItem(5, gapple);
        addItem(6, steak);
        addItem(7, fns);
        addItem(8, cobwebs);
    }
}