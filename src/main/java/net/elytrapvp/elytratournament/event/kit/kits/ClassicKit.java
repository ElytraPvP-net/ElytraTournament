package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ClassicKit extends Kit {

    public ClassicKit(ElytraTournament plugin) {
        super(plugin, "Classic");
        setIconMaterial(Material.FISHING_ROD);
        setKnockback("rod");
        setRodMultiplier(1.5);

        ItemStack helmet = new ItemBuilder(Material.IRON_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.IRON_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.IRON_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.IRON_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack sword = new ItemBuilder(Material.IRON_SWORD)
                .setUnbreakable(true)
                .build();
        ItemStack fishingRod = new ItemBuilder(Material.FISHING_ROD)
                .build();
        ItemStack bow = new ItemBuilder(Material.BOW)
                .setUnbreakable(true)
                .build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 4).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, fishingRod);
        addItem(2, bow);
        addItem(8, arrows);
    }
}