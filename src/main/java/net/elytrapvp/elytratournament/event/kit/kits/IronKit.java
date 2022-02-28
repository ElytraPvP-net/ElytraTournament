package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class IronKit extends Kit {

    public IronKit(ElytraTournament plugin) {
        super(plugin, "Iron");
        setIconMaterial(Material.IRON_SWORD);
        setKnockback("nospeed");

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

        ItemStack sword = new ItemBuilder(Material.IRON_SWORD)
                .setUnbreakable(true)
                .build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .setUnbreakable(true)
                .build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 12).build();

        ItemStack gapple = new ItemBuilder(Material.GOLDEN_APPLE, 1).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, bow);
        addItem(2, gapple);
        addItem(8, arrows);
    }
}