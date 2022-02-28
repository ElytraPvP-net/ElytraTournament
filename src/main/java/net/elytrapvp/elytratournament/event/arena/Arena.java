package net.elytrapvp.elytratournament.event.arena;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an arena that a game is held in.
 */
public class Arena {
    private final Map map;
    private final List<Location> spawns = new ArrayList<>();
    private Location spectateSpawn;

    /**
     * Creates an arena object.
     * @param plugin ElytraDuels instance.
     * @param map Map the arena uses.
     * @param id Id of the arena.
     */
    public Arena(ElytraTournament plugin, Map map, String id) {
        this.map = map;

        // Setting these two variables makes it easier to get information.
        FileConfiguration maps = plugin.settingsManager().getMaps();
        String path = "Maps." + getMap().getId() + ".arenas." + id + ".";

        // Loops through all spawns for this arena in maps.yml
        ConfigurationSection section = maps.getConfigurationSection(path + "spawns");
        for(String spawn : section.getKeys(false)) {
            String world = maps.getString(path + "spawns." + spawn + ".World");
            double x = maps.getDouble(path + "spawns." +  spawn + ".X");
            double y = maps.getDouble(path + "spawns." +  spawn + ".Y");
            double z = maps.getDouble(path + "spawns." +  spawn + ".Z");
            float pitch = (float) maps.getDouble(path + "spawns." +  spawn + ".Pitch");
            float yaw = (float) maps.getDouble(path + "spawns." +  spawn + ".Yaw");

            spawns.add(new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch));
        }

        ConfigurationSection section2 = maps.getConfigurationSection(path + "spectate");
        if(section2 == null) {
            spectateSpawn = spawns.get(0);
        }
        else {
            String world = maps.getString(path + "spectate" + ".World");
            double x = maps.getDouble(path + "spectate" + ".X");
            double y = maps.getDouble(path + "spectate" + ".Y");
            double z = maps.getDouble(path + "spectate" + ".Z");
            float pitch = (float) maps.getDouble(path + "spectate" + ".Pitch");
            float yaw = (float) maps.getDouble(path + "spectate" + ".Yaw");
            spectateSpawn = new Location(Bukkit.getWorld(world), x, y ,z, yaw, pitch);
        }
    }

    /**
     * Get the spectator spawn.
     * @return Spectator spawn location.
     */
    public Location getSpectateSpawn() {
        return spectateSpawn;
    }

    /**
     * Get the map that this arena uses.
     * @return Map the arena uses.
     */
    public Map getMap() {
        return map;
    }

    /**
     * Get all spawns of the arena.
     * @return All spawns of the arena.
     */
    public List<Location> getSpawns() {
        return spawns;
    }
}