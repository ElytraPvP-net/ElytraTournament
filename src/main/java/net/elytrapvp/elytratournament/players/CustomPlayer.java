package net.elytrapvp.elytratournament.players;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Stores all duels data about a player.
 */
public class CustomPlayer {
    private final ElytraTournament plugin;
    private final UUID uuid;

    private final Map<String, Map<Integer, Integer>> kitEditor = new HashMap<>();

    // Data
    int tournamentsHosted = 0;
    int goldMedals = 0;
    int silverMedals = 0;
    int bronzeMedals = 0;
    int totalMedals = 0;
    int points = 0;
    int tournamentsPlayed = 0;
    String title = "";

    // Settings
    private boolean showScoreboard;

    /**
     * Creates the CustomPlayer object.
     * @param plugin Plugin instance.
     * @param uuid UUID of player.
     */
    public CustomPlayer(ElytraTournament plugin, UUID uuid) {
        this.plugin = plugin;
        this.uuid = uuid;

        // Run everything async to prevent lag.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                for(Kit kit : plugin.kitManager().getKits()) {
                    kitEditor.put(kit.getName(), new HashMap<>());
                }

                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM tournament_statistics WHERE uuid = ?");
                statement.setString(1, uuid.toString());
                ResultSet results = statement.executeQuery();

                if(results.next()) {
                    tournamentsHosted = results.getInt(2);
                    goldMedals = results.getInt(3);
                    silverMedals = results.getInt(4);
                    bronzeMedals = results.getInt(5);
                    totalMedals = results.getInt(6);
                    points = results.getInt(7);
                    tournamentsPlayed = results.getInt(8);
                }
                else {
                    PreparedStatement statement2 = plugin.mySQL().getConnection().prepareStatement("INSERT INTO tournament_statistics (uuid) VALUES (?)");
                    statement2.setString(1, uuid.toString());
                    statement2.executeUpdate();
                }

                PreparedStatement statement3 = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM tournament_kit_editor WHERE uuid = ?");
                statement3.setString(1, uuid.toString());
                ResultSet results3 = statement3.executeQuery();

                while(results3.next()) {
                    kitEditor.get(results3.getString(2)).put(results3.getInt(3), results3.getInt(4));
                }

                PreparedStatement statement4 = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM tournament_settings WHERE uuid = ?");
                statement4.setString(1, uuid.toString());
                ResultSet results4 = statement4.executeQuery();
                if(results4.next()) {
                    showScoreboard = results4.getBoolean(2);

                    if(results.getString(3) == null) {
                        setTitle("");
                    }
                    else {
                        title = results4.getString(3);
                    }
                }
                else {
                    PreparedStatement statement5 = plugin.mySQL().getConnection().prepareStatement("INSERT INTO tournament_settings (uuid) VALUES (?)");
                    statement5.setString(1, uuid.toString());
                    statement5.executeUpdate();

                    showScoreboard = true;
                }
            }
            catch (SQLException exception) {
                ChatUtils.chat(Bukkit.getPlayer(uuid), "&cError &8» &cSomething went wrong loading your data! Please reconnect or your data could be lost.");
                exception.printStackTrace();
            }
        });
    }

    /**
     * Give the player 1 bronze medal.
     */
    public void addBronzeMedal() {
        setBronzeMedals(bronzeMedals + 1);
        setTotalMedals(totalMedals + 1);
        addPoints(2);
    }

    /**
     * Give the player 1 gold medal.
     */
    public void addGoldMedal() {
        setGoldMedals(goldMedals + 1);
        setTotalMedals(totalMedals + 1);
        addPoints(4);
    }

    /**
     * Add points to the player.
     * @param points Points to add.
     */
    public void addPoints(int points) {
        setPoints(this.points + points);
    }

    /**
     * Give the player 1 silver medal.
     */
    public void addSilverMedal() {
        setSilverMedals(silverMedals + 1);
        setTotalMedals(totalMedals + 1);
        addPoints(3);
    }

    /**
     * Add 1 to the tournaments hosted counter.
     */
    public void addTournamentHosted() {
        setTournamentsHosted(tournamentsHosted + 1);
    }

    /**
     * Add 1 to the tournaments played counter.
     */
    public void addTournamentPlayed() {
        setTournamentsPlayed(tournamentsPlayed + 1);
        addPoints(1);
    }

    /**
     * Clear the kit editor of a kit.
     * @param kit Kit to clear.
     */
    private void cleanKitEditor(String kit) {
        try {
            PreparedStatement statement3 = plugin.mySQL().getConnection().prepareStatement("DELETE FROM tournament_kit_editor WHERE uuid = ? AND kit = ?");
            statement3.setString(1, uuid.toString());
            statement3.setString(2, kit);
            statement3.executeUpdate();
        }
        catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Get the number of bronze medals the player has.
     * @return Bronze medals count.
     */
    public int getBronzeMedals() {
        return bronzeMedals;
    }

    /**
     * Get the number of gold medlas the player has.
     * @return Gold medals count.
     */
    public  int getGoldMedals() {
        return goldMedals;
    }

    /**
     * Get the modified kit.
     * @param kit Kit to get modifications of.
     * @return Modifications
     */
    public Map<Integer, Integer> getKitEditor(String kit) {
        return kitEditor.get(kit);
    }

    /**
     * Get if the scoreboard should be shown.
     * @return if the scoreboard should be shown.
     */
    public boolean getShowScoreboard() {
        return showScoreboard;
    }

    /**
     * Get the current number of silver medals.
     * @return Silver medals count.
     */
    public int getSilverMedals() {
        return silverMedals;
    }

    /**
     * Get the player's current title.
     * @return Current title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the number of tournaments hosted.
     * @return Tournaments hosted.
     */
    public int getTournamentsHosted() {
        return tournamentsHosted;
    }

    /**
     * Set if the scoreboard should be shown.
     * @param showScoreboard Whether or not the scoreboard should be shown.
     */
    public void setShowScoreboard(boolean showScoreboard) {
        this.showScoreboard = showScoreboard;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_settings SET showScoreboard = ? WHERE uuid = ?");
                statement.setBoolean(1, showScoreboard);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of bronze medals the player has.
     * @param bronzeMedals New number of bronze medals.
     */
    public void setBronzeMedals(int bronzeMedals) {
        this.bronzeMedals = bronzeMedals;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET bronze = ? WHERE uuid = ?");
                statement.setInt(1, bronzeMedals);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of gold medals the player has.
     * @param goldMedals New number of gold medals.
     */
    public void setGoldMedals(int goldMedals) {
        this.goldMedals = goldMedals;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET gold = ? WHERE uuid = ?");
                statement.setInt(1, goldMedals);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the amount of points this person has.
     * @param points New number of points.
     */
    public void setPoints(int points) {
        this.points = points;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET hosted = ? WHERE uuid = ?");
                statement.setInt(1, points);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of silver medals the player has.
     * @param silverMedals New number of silver medals.
     */
    public void setSilverMedals(int silverMedals) {
        this.silverMedals = silverMedals;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET silver = ? WHERE uuid = ?");
                statement.setInt(1, silverMedals);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the player's current title.
     * @param title New title.
     */
    public void setTitle(String title) {
        this.title = title;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_settings SET title = ? WHERE uuid = ?");
                statement.setString(1, title);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the number of medals the player has.
     * @param totalMedals New number of medals.
     */
    public void setTotalMedals(int totalMedals) {
        this.totalMedals = totalMedals;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET total = ? WHERE uuid = ?");
                statement.setInt(1, totalMedals);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the amount of tournaments this person has hosted.
     * @param tournamentsHosted Number of tournaments hosted.
     */
    public void setTournamentsHosted(int tournamentsHosted) {
        this.tournamentsHosted = tournamentsHosted;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET hosted = ? WHERE uuid = ?");
                statement.setInt(1, tournamentsHosted);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Set the amount of tournaments this person has played in.
     * @param tournamentsPlayed Number of tournaments played in.
     */
    public void setTournamentsPlayed(int tournamentsPlayed) {
        this.tournamentsPlayed = tournamentsPlayed;

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("UPDATE tournament_statistics SET played = ? WHERE uuid = ?");
                statement.setInt(1, tournamentsPlayed);
                statement.setString(2, uuid.toString());
                statement.executeUpdate();
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

    /**
     * Update the kit editor.
     * @param kit Kit to update.
     */
    public void updateKitEditor(String kit) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            cleanKitEditor(kit);

            try {
                Map<Integer, Integer> map = getKitEditor(kit);

                for(int item : map.keySet()) {
                    int slot = map.get(item);

                    PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("INSERT INTO tournament_kit_editor (uuid,kit,item,slot) VALUES (?,?,?,?)");
                    statement.setString(1, uuid.toString());
                    statement.setString(2, kit);
                    statement.setInt(3, item);
                    statement.setInt(4, slot);
                    statement.executeUpdate();
                }
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}