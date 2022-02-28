package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class DiamondKit extends Kit {

    public DiamondKit(ElytraTournament plugin) {
        super(plugin, "Diamond");
        setIconMaterial(Material.DIAMOND_SWORD);
        setKnockback("nospeed");

        setHunger(true);

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_PROJECTILE, 1)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
                .setUnbreakable(true)
                .build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setUnbreakable(true)
                .build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 32).build();

        ItemStack steak = new ItemBuilder(Material.COOKED_BEEF, 64).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, bow);
        addItem(8, steak);
        addItem(7, arrows);
    }
}