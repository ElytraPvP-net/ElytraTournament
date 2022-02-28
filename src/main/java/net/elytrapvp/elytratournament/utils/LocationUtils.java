package net.elytrapvp.elytratournament.utils;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationUtils {
    /**
     * Get the spawn Location from the Config
     * @return Spawn Location
     */
    public static Location getSpawn(ElytraTournament plugin) {
        String world = plugin.settingsManager().getConfig().getString("Spawn.World");
        double x = plugin.settingsManager().getConfig().getDouble("Spawn.X");
        double y = plugin.settingsManager().getConfig().getDouble("Spawn.Y");
        double z = plugin.settingsManager().getConfig().getDouble("Spawn.Z");
        float pitch = (float) plugin.settingsManager().getConfig().getDouble("Spawn.Pitch");
        float yaw = (float) plugin.settingsManager().getConfig().getDouble("Spawn.Yaw");

        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }
}