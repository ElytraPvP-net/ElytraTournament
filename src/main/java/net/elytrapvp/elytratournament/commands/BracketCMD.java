package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.command.CommandSender;

public class BracketCMD extends AbstractCommand {
    private final ElytraTournament plugin;

    public BracketCMD(ElytraTournament plugin) {
        super("bracket", "", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(plugin.eventManager().activeEvent() == null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is no tournament active right now!");
            return;
        }

        ChatUtils.chat(sender, "&a&lBracket &8» &fhttps://challonge.com/" + plugin.eventManager().activeEvent().getTournament().getUrl());
    }
}