package net.elytrapvp.elytratournament;

import net.elytrapvp.elytratournament.commands.AbstractCommand;
import net.elytrapvp.elytratournament.event.EventManager;
import net.elytrapvp.elytratournament.event.arena.ArenaManager;
import net.elytrapvp.elytratournament.event.game.GameManager;
import net.elytrapvp.elytratournament.event.kit.KitManager;
import net.elytrapvp.elytratournament.listeners.*;
import net.elytrapvp.elytratournament.players.CustomPlayerManager;
import net.elytrapvp.elytratournament.runnables.HealingRunnable;
import net.elytrapvp.elytratournament.utils.gui.GUIListeners;
import net.elytrapvp.elytratournament.utils.scoreboard.ScoreboardUpdate;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class ElytraTournament extends JavaPlugin {
    private MySQL mySQL;
    private SettingsManager settingsManager;
    private CustomPlayerManager customPlayerManager;
    private EventManager eventManager;
    private KitManager kitManager;
    private ArenaManager arenaManager;
    private GameManager gameManager;

    @Override
    public void onEnable() {
        // Fix whitelist if it gets stuck.
        Bukkit.setWhitelist(false);

        // Load configuration files.
        settingsManager = new SettingsManager(this);

        // Connect to MySQL.
        mySQL = new MySQL(this);
        mySQL.openConnection();

        // Load kits
        kitManager = new KitManager(this);

        // Load arenas
        arenaManager = new ArenaManager(this);

        // Enable Data Storage
        customPlayerManager = new CustomPlayerManager(this);

        // Setup the event manager.
        eventManager = new EventManager(this);

        // Setup the game manager.
        gameManager = new GameManager(this);

        // Register commands.
        AbstractCommand.registerCommands(this);

        // Register event listeners.
        Bukkit.getPluginManager().registerEvents(new BlockBreakListener(this), this);
        Bukkit.getPluginManager().registerEvents(new BlockIgniteListener(), this);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityRegainHealthListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityShootBowListener(), this);
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageByEntityListener(this), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(this), this);
        Bukkit.getPluginManager().registerEvents(new FoodLevelChangeListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDropItemListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerEggThrowListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerItemConsumeListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileHitListener(this), this);
        Bukkit.getPluginManager().registerEvents(new ProjectileLaunchListener(this), this);

        // Update scoreboard
        new ScoreboardUpdate(this).runTaskTimer(this, 20L, 20L);

        // Enable BungeeCord messaging.
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "Tournament");

        // Change pace of healing.
        new HealingRunnable(this).runTaskTimer(this, 20*8, 20*8);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public ArenaManager arenaManager() {
        return arenaManager;
    }

    public CustomPlayerManager customPlayerManager() {
        return customPlayerManager;
    }

    public EventManager eventManager() {
        return eventManager;
    }

    public KitManager kitManager() {
        return kitManager;
    }

    public MySQL mySQL() {
        return mySQL;
    }

    public SettingsManager settingsManager() {
        return settingsManager;
    }

    public GameManager gameManager() {
        return gameManager;
    }
}
