package net.elytrapvp.elytratournament.event.kit.kits;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import org.bukkit.Material;

public class SumoKit extends Kit {

    public SumoKit(ElytraTournament plugin) {
        super(plugin, "Sumo");
        setKnockback("nospeed");

        setIconMaterial(Material.SLIME_BALL);

        setDoDamage(false);
        setWaterKills(true);
        spawnOnStart(true);
    }
}