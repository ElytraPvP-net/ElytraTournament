package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SpectateCMD extends AbstractCommand {
    private final ElytraTournament plugin;

    public SpectateCMD(ElytraTournament plugin) {
        super("spectate", "", false);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;

        if(plugin.eventManager().spectators().contains(player)) {
            plugin.eventManager().removeSpectator(player);
            ChatUtils.chat(player, "&aYou are no longer spectating the tournament.");
            return;
        }

        plugin.eventManager().addSpectator(player);
        ChatUtils.chat(player, "&aYou are now spectating the tournament.");
    }
}
