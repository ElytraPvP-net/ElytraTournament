package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionType;

public class NoDebuffKit extends Kit {

    public NoDebuffKit(ElytraTournament plugin) {
        super(plugin, "No Debuff");
        setIconMaterial(Material.POTION);
        setHunger(true);

        ItemStack helmet = new ItemBuilder(Material.DIAMOND_HELMET)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
                .setUnbreakable(true)
                .build();
        ItemStack chestplate = new ItemBuilder(Material.DIAMOND_CHESTPLATE)
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2)
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
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .addEnchantment(Enchantment.DAMAGE_ALL, 3)
                .setUnbreakable(true)
                .build();
        ItemStack pearls = new ItemBuilder(Material.ENDER_PEARL, 16).build();

        ItemStack steak = new ItemBuilder(Material.COOKED_BEEF, 64).build();

        Potion speedPot = new Potion(PotionType.SPEED);
        speedPot.setLevel(2);
        ItemStack speed = speedPot.toItemStack(1);

        Potion fireResPot = new Potion(PotionType.FIRE_RESISTANCE);
        fireResPot.setHasExtendedDuration(true);
        ItemStack fireRes = fireResPot.toItemStack(1);

        Potion healingPot = new Potion(PotionType.INSTANT_HEAL);
        healingPot.setSplash(true);
        healingPot.setLevel(2);
        ItemStack healing = healingPot.toItemStack(1);

        addItem(39, helmet);
        addItem(38, chestplate);
        addItem(37, leggings);
        addItem(36, boots);

        addItem(0, sword);
        addItem(1, pearls);
        addItem(2, speed);
        addItem(3, fireRes);
        addItem(8, steak);
        addItem(17, speed);
        addItem(26, speed);
        addItem(35, speed);

        int[] healingSlots = {4,5,6,7,9,10,11,12,13,14,15,16,18,19,20,21,22,23,24,25,27,28,29,30,31,32,33,34,35};
        for(int i : healingSlots) {
            addItem(i, healing);
        }
    }
}