package net.elytrapvp.elytratournament.event;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.scoreboard.CustomScoreboard;
import net.elytrapvp.elytratournament.utils.scoreboard.ScoreHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class EventScoreboard extends CustomScoreboard {
    private final ElytraTournament plugin;

    public EventScoreboard(ElytraTournament plugin, Player player) {
        super(player);
        this.plugin = plugin;
        update(player);
    }

    public void update(Player player) {
        ScoreHelper helper;
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        switch (plugin.eventManager().eventStatus()) {
            case NONE -> {
                helper.setTitle("&a&lTournament &c(Alpha)");
                helper.setSlot(12, "&7&m------------------");
                helper.setSlot(11, "&fWaiting for a host.");
                helper.setSlot(10, "");
                helper.setSlot(9, "&aPlayers: &f" + Bukkit.getOnlinePlayers().size());
                helper.setSlot(8, "");
                helper.setSlot(7, "&aMedals:");
                helper.setSlot(6, "  &6Gold: &f" + customPlayer.getGoldMedals());
                helper.setSlot(5, "  &fSilver: &f" + customPlayer.getSilverMedals());
                helper.setSlot(4, "  &cBronze: &f" + customPlayer.getBronzeMedals());
                helper.setSlot(3, "");
                helper.setSlot(2, "&7&m------------------");
                helper.setSlot(1, "&aplay.elytrapvp.net");
            }
            case WAITING -> {
                helper.setTitle("&a&lTournament &c(Alpha)");
                helper.setSlot(15, "&7&m------------------");
                helper.setSlot(14, "&aHost: &f" + plugin.eventManager().host().getName());
                helper.setSlot(13, "&aKit: &f" + plugin.eventManager().kit().getName());
                helper.setSlot(12, "&aBracket: &f" + plugin.eventManager().eventType());
                helper.setSlot(11, "&aTeam: &f1v1 &7(" + plugin.eventManager().bestOf().toString() + "&7)");
                helper.setSlot(10, "");
                helper.setSlot(9, "&aPlayers: &f" + Bukkit.getOnlinePlayers().size());
                helper.setSlot(8, "");
                helper.setSlot(7, "&aMedals:");
                helper.setSlot(6, "  &6Gold: &f" + customPlayer.getGoldMedals());
                helper.setSlot(5, "  &fSilver: &f" + customPlayer.getSilverMedals());
                helper.setSlot(4, "  &cBronze: &f" + customPlayer.getBronzeMedals());
                helper.setSlot(3, "");
                helper.setSlot(2, "&7&m------------------");
                helper.setSlot(1, "&aplay.elytrapvp.net");
            }
            case RUNNING -> {
                helper.setTitle("&a&lTournament &c(Alpha)");
                helper.setSlot(10, "&7&m------------------");
                helper.setSlot(9, "&aHost: &f" + plugin.eventManager().host().getName());
                helper.setSlot(8, "&aKit: &f" + plugin.eventManager().kit().getName());
                helper.setSlot(7, "&aBracket: &f" + plugin.eventManager().eventType());
                helper.setSlot(6, "&aTeam: &f1v1 &7(" + plugin.eventManager().bestOf().toString() + "&7)");
                helper.setSlot(5, "");
                helper.setSlot(4, "&aFighting: &f" + (plugin.gameManager().getActiveGames().size() * 2));
                helper.setSlot(3, "");
                helper.setSlot(2, "&7&m------------------");
                helper.setSlot(1, "&aplay.elytrapvp.net");
            }
        }
    }
}