package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ShortBowKit extends Kit {
    public ShortBowKit(ElytraTournament plugin) {
        super(plugin, "Short Bow");
        setIconMaterial(Material.IRON_BARDING);
        setNaturalRegen(false);

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

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setUnbreakable(true)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 1).build();


        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, bow);
        addItem(35, arrows);
    }
}