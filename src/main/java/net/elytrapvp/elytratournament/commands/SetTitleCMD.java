package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetTitleCMD extends AbstractCommand {
    private final ElytraTournament plugin;

    public SetTitleCMD(ElytraTournament plugin) {
        super("settitle", "tournament.settitle", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            ChatUtils.chat(sender, "&cUsage &8» &c/settitle [title]");
            return;
        }

        Player player = (Player) sender;
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        String title = ChatUtils.translate(StringUtils.join(args, " "));
        customPlayer.setTitle(title);

        ChatUtils.chat(player, "&aYour title has been set to &f" + title + "&a.");
    }
}
