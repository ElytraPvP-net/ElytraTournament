package net.elytrapvp.elytratournament.event.game;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.scoreboard.CustomScoreboard;
import net.elytrapvp.elytratournament.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;

import java.util.List;

public class GameScoreboard extends CustomScoreboard {
    private final Game game;
    private final ElytraTournament plugin;

    public GameScoreboard(ElytraTournament plugin, Player player, Game game) {
        super(player);
        this.plugin = plugin;
        this.game = game;
        update(player);
    }

    public void update(Player player) {
        ScoreHelper helper;

        if(ScoreHelper.hasScore(player)) {
            helper = ScoreHelper.getByPlayer(player);
        }
        else {
            helper = ScoreHelper.createScore(player);
        }

        helper.setTitle("&a&lTournament &c(Alpha)");
        helper.setSlot(14, "&7&m------------------");
        helper.setSlot(13, "&aTime: &f" + game.getTimer().toString());
        helper.setSlot(12, "");
        helper.setSlot(11, "&aLadder");
        helper.setSlot(10, "  &f" + plugin.eventManager().kit().getName());
        helper.setSlot(9, " ");
        helper.setSlot(8, "&aOpponent");

        Player opponent = game.getOpponent(player);
        helper.setSlot(5, "  &f" + opponent.getName());
        helper.setSlot(4, "  " + ChatUtils.getFormattedHealthPercent(opponent) + " &7- " + ChatUtils.getFormattedPing(opponent));
        helper.setSlot(3, "");
        helper.setSlot(2, "&7&m------------------");
        helper.setSlot(1, "&aplay.elytrapvp.net");
    }
}