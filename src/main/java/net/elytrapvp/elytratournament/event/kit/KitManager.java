package net.elytrapvp.elytratournament.event.kit;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.kits.*;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Manages all existing kits.
 */
public class KitManager {
    private final Set<Kit> kits = new LinkedHashSet<>();

    public KitManager(ElytraTournament plugin) {
        kits.add(new ArcherKit(plugin));
        kits.add(new BlitzKit(plugin));
        kits.add(new BowKit(plugin));
        kits.add(new BowSpleefKit(plugin));
        kits.add(new BuildUHCKit(plugin));
        kits.add(new CactusKit(plugin));
        kits.add(new ClassicKit(plugin));
        kits.add(new DiamondKit(plugin));
        kits.add(new FinalUHCKit(plugin));
        kits.add(new IronKit(plugin));
        kits.add(new NoDebuffKit(plugin));
        kits.add(new OPKit(plugin));
        kits.add(new SGKit(plugin));
        kits.add(new SpeedArcherKit(plugin));
        kits.add(new ShortBowKit(plugin));
        kits.add(new SoupKit(plugin));
        kits.add(new SpleefKit(plugin));
        kits.add(new SumoKit(plugin));
    }

    /**
     * Get a kit from its name,
     * @param str Name of the kit.
     * @return Kit from name.
     */
    public Kit getKit(String str) {
        for(Kit kit : getKits()) {
            if(kit.getName().equalsIgnoreCase(str)) {
                return kit;
            }
        }

        return null;
    }

    /**
     * Get all existing kits.
     * @return All kits.
     */
    public Set<Kit> getKits() {
        return kits;
    }

}