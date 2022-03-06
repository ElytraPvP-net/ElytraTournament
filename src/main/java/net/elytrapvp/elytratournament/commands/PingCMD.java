package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCMD extends AbstractCommand {

    public PingCMD() {
        super("ping", "", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(args.length == 0 || !sender.hasPermission("ping.other")) {
            ChatUtils.chat(player, "&aYour ping is " + ChatUtils.getFormattedPing(player) + "&a.");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if(target == null) {
            ChatUtils.chat(sender, "&cError &8» &cThat player is not online.");
            return;
        }

        ChatUtils.chat(sender, "&a" + target.getName() + "\'s ping is " + ChatUtils.getFormattedPing(target) + "&a.");
    }
}