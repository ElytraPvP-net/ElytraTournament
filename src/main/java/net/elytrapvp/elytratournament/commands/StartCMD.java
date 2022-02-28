package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartCMD extends AbstractCommand {
    private final ElytraTournament plugin;

    public StartCMD(ElytraTournament plugin) {
        super("start", "tournament.use", false);

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        // Make sure no other tournaments are currently running.
        if(plugin.eventManager().activeEvent() != null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is already a tournament active!");
            return;
        }

        // Make sure there are enough players to start a tournament.
        if(Bukkit.getOnlinePlayers().size() <= 1) {
            ChatUtils.chat(sender, "&cError &8» &cThere are not enough players to start!");
            return;
        }

        Player player = (Player) sender;

        // Make sure the host is the one running the command.
        if(!player.equals(plugin.eventManager().host())) {
            ChatUtils.chat(sender, "&cError &8» &cOnly the host can run that command!!");
            return;
        }

        Bukkit.setWhitelist(true);
        Bukkit.broadcastMessage(ChatUtils.translate("&a&lTournament &8» &aGenerating Brackets"));
        plugin.eventManager().create();
    }
}