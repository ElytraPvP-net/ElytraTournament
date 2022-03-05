package net.elytrapvp.elytratournament.commands;

import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.EventScoreboard;
import net.elytrapvp.elytratournament.event.EventStatus;
import net.elytrapvp.elytratournament.event.EventType;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CancelCMD extends AbstractCommand {
    private ElytraTournament plugin;

    public CancelCMD(ElytraTournament plugin) {
        super("cancel", "tournament.use", true);
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        switch (plugin.eventManager().eventStatus()) {
            case NONE -> ChatUtils.chat(sender, "&c&lError &8» &cThere is no tournament to cancel!");
            case RUNNING -> ChatUtils.chat(sender, "&c&lError &8» &cThe tournament is already running!");
            case WAITING -> {
                plugin.eventManager().eventStatus(EventStatus.NONE);
                plugin.eventManager().eventType(EventType.NONE);
                plugin.eventManager().host(null);
                plugin.eventManager().kit(null);
                Bukkit.broadcastMessage(ChatUtils.translate("&a&lTournament &8» &aThe tournament has been canceled."));

                // Update Scoreboard
                Bukkit.getOnlinePlayers().forEach(player -> new EventScoreboard(plugin, player));

                // Update bungeecord
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("cancel");
                Iterables.getFirst(Bukkit.getOnlinePlayers(), null).sendPluginMessage(plugin, "Tournament", out.toByteArray());
            }
        }
    }
}