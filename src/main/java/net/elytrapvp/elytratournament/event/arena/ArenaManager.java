package net.elytrapvp.elytratournament.event.arena;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.*;

/**
 * Manages all existing arenas.
 */
public class ArenaManager {
    private final Set<Arena> openArenas = new HashSet<>();

    public ArenaManager(ElytraTournament plugin) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            FileConfiguration maps = plugin.settingsManager().getMaps();
            ConfigurationSection section = maps.getConfigurationSection("Maps");

            for(String map : section.getKeys(false)) {
                new Map(plugin, map);
            }
        }, 1);

    }

    /**
     * Add an arena to the queue.
     * @param arena Arena to add.
     */
    public void addArena(Arena arena) {
        getOpenArenas().add(arena);
    }

    /**
     * Get a random open arena for a specific kit.
     * @param kit Kit to get arena for.
     * @return Random open arena.
     */
    public Arena getOpenArena(Kit kit) {
        List<Arena> open = new ArrayList<>();

        for(Arena arena : getOpenArenas()) {
            if(arena.getMap().getKits().contains(kit)) {
                open.add(arena);
            }
        }

        if(open.size() == 0) {
            return null;
        }

        Random random = new Random();
        return open.get(random.nextInt(open.size()));
    }

    /**
     * Get all arenas that are currently open.
     * @return All currently open arenas.
     */
    public Set<Arena> getOpenArenas() {
        return openArenas;
    }

    /**
     * Remove an arena from the queue.
     * @param arena Arena to remove.
     */
    public void removeArena(Arena arena) {
        getOpenArenas().remove(arena);
    }
}