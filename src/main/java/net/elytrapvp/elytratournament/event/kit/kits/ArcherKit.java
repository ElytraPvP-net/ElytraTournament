package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class ArcherKit extends Kit {

    public ArcherKit(ElytraTournament plugin) {
        super(plugin, "Archer");
        setIconMaterial(Material.ARROW);

        setNaturalRegen(false);

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1)
                .setUnbreakable(true)
                .build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .setUnbreakable(true)
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
