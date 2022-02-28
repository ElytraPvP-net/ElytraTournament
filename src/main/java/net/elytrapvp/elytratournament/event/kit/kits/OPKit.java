package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class OPKit extends Kit {

    public OPKit(ElytraTournament plugin) {
        super(plugin, "OP");
        setRodMultiplier(1.5);
        setStrongGapple(true);
        setIconMaterial(Material.DIAMOND_CHESTPLATE);

        setGameMode(GameMode.SURVIVAL);

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
                .setUnbreakable(true)
                .build();
        ItemStack leggings = new ItemBuilder(Material.DIAMOND_LEGGINGS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .build();
        ItemStack boots = new ItemBuilder(Material.DIAMOND_BOOTS)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 3)
                .setUnbreakable(true)
                .build();

        ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
                .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                .setUnbreakable(true)
                .build();

        ItemStack rod = new ItemBuilder(Material.FISHING_ROD).build();

        ItemStack bow = new ItemBuilder(Material.BOW)
                .addEnchantment(Enchantment.ARROW_DAMAGE, 4)
                .build();

        ItemStack goldenApples = new ItemBuilder(Material.GOLDEN_APPLE, 6).build();

        Potion speedPot = new Potion(PotionType.SPEED);
        speedPot.setSplash(true);
        speedPot.setLevel(2);
        ItemStack speed = speedPot.toItemStack(1);

        Potion regenPot = new Potion(PotionType.REGEN);
        regenPot.setSplash(true);
        regenPot.setLevel(1);
        ItemStack regen = regenPot.toItemStack(1);

        ItemStack arrows = new ItemBuilder(Material.ARROW, 20).build();

        ItemStack fns = new ItemStack(Material.FLINT_AND_STEEL);
        fns.setDurability((short) 60);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, rod);
        addItem(2, bow);
        addItem(3, fns);
        addItem(4, goldenApples);
        addItem(5, speed);
        addItem(6, regen);
        addItem(8, arrows);
        addItem(9, speed);
        addItem(10, regen);
    }
}