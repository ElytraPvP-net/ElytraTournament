package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CactusKit extends Kit {
    public CactusKit(ElytraTournament plugin) {
        super(plugin, "Cactus");
        setIconMaterial(Material.CACTUS);
        setGameMode(GameMode.SURVIVAL);
        setMaxHealth(1);
        setStartingHealth(1);

        addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200000, 100, true));

        setNaturalRegen(false);
        setVoidLevel(50);
        spawnOnStart(true);

        ItemStack cactus = new ItemBuilder(Material.CACTUS, 16).build();
        ItemStack egg = new ItemStack(Material.EGG);

        addItem(0, cactus);
        addItem(1, egg);
    }
}