package net.elytrapvp.elytratournament;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * Allows easy access to plugin configuration
 * files. Stores spawn and arena locations.
 */
public class SettingsManager {
    private final ElytraTournament plugin;

    private FileConfiguration config;
    private File configFile;
    private FileConfiguration maps;
    private File mapsFile;

    public SettingsManager(ElytraTournament plugin) {
        this.plugin = plugin;

        config = plugin.getConfig();
        config.options().copyDefaults(true);
        configFile = new File(plugin.getDataFolder(), "config.yml");
        plugin.saveConfig();

        mapsFile = new File(plugin.getDataFolder(), "maps.yml");
        maps = YamlConfiguration.loadConfiguration(mapsFile);

        if(!mapsFile.exists())
            plugin.saveResource("maps.yml", false);
    }

    /**
     * Get the main configuration file.
     * @return Main configuration file.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Get the maps configuration file.
     * @return Maps configuration file.
     */
    public FileConfiguration getMaps() {
        return maps;
    }
}