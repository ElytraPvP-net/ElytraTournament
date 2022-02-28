package net.elytrapvp.elytratournament;

import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private ElytraTournament plugin;
    private Connection connection;
    private final String host;
    private final String database;
    private final String username;
    private final String password;
    private final int port;

    public MySQL(ElytraTournament plugin) {
        this.plugin = plugin;
        host = plugin.settingsManager().getConfig().getString("MySQL.host");
        database = plugin.settingsManager().getConfig().getString("MySQL.database");
        username = plugin.settingsManager().getConfig().getString("MySQL.username");
        password = plugin.settingsManager().getConfig().getString("MySQL.password");
        port = plugin.settingsManager().getConfig().getInt("MySQL.port");
    }

    /**
     * Close a connection.
     */
    public void closeConnection() {
        if(isConnected()) {
            try {
                connection.close();
            }
            catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get the connection.
     * @return Connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Get if plugin is connected to the database.
     * @return Connected
     */
    private boolean isConnected() {
        return (connection != null);
    }

    /**
     * Open a MySQL Connection
     */
    public void openConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                return;
            }

            synchronized(ElytraTournament.class) {
                if (connection != null && !connection.isClosed()) {
                    return;
                }
                Class.forName("com.mysql.jdbc.Driver");
                connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false&characterEncoding=utf8", username, password);
            }

            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
                try {
                    connection.isValid(0);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            },0,20*60*7);
        }
        catch(SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}