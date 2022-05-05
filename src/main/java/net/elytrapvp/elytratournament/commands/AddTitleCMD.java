package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

public class AddTitleCMD extends AbstractCommand {
    private final ElytraTournament plugin;

    public AddTitleCMD(ElytraTournament plugin) {
        super("addtitle", "tournament.addprefix", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            ChatUtils.chat(sender, "&cUsage &8» &c/addtitle [player] [title]");
        }

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                String target;
                String username;

                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM player_info WHERE username = ?");
                statement.setString(1, args[0]);
                ResultSet results = statement.executeQuery();

                if(results.next()) {
                    target = results.getString(1);
                    username = results.getString(2);
                }
                else {
                    ChatUtils.chat(sender, "&cError &8» &cThat player has not played.");
                    return;
                }

                String title = StringUtils.join(Arrays.copyOfRange(args, 1, args.length));

                PreparedStatement statement2 = plugin.mySQL().getConnection().prepareStatement("INSERT INTO tournament_titles (uuid,title) VALUES (?,?)");
                statement2.setString(1, target);
                statement2.setString(2, title);
                statement2.executeUpdate();

                ChatUtils.chat(sender, "&aAdded title &f" + title + " &ato &f" + username + "&a.");
            }
            catch (SQLException exception) {
                ChatUtils.chat(sender, "&cError &8» &cSomething went wrong while adding title. Tell partykid4.");
                exception.printStackTrace();
            }
        });
    }
}