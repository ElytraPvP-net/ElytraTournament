package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedArcherKit extends Kit {

    public SpeedArcherKit(ElytraTournament plugin) {
        super(plugin, "Speed Archer");
        setIconMaterial(Material.FEATHER);
        setRangedDamage(true);
        setNaturalRegen(false);

        ItemStack helmet = new ItemBuilder(Material.LEATHER_HELMET)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.LEATHER_CHESTPLATE)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.LEATHER_LEGGINGS)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.LEATHER_BOOTS)
                .setUnbreakable(true)
                .build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_INFINITE, 1)
                .addEnchantment(Enchantment.ARROW_KNOCKBACK, 2)
                .setUnbreakable(true)
                .build();
        ItemStack arrows = new ItemBuilder(Material.ARROW, 1).build();

        ItemStack pearl = new ItemBuilder(Material.ENDER_PEARL).build();

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, bow);
        addItem(1, pearl);
        addItem(35, arrows);

        addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 999999, 0));
    }

}