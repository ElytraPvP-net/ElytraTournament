package net.elytrapvp.elytratournament.event.game;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.scoreboard.CustomScoreboard;
import net.elytrapvp.elytratournament.utils.scoreboard.ScoreHelper;
import org.bukkit.entity.Player;


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

        if(plugin.eventManager().kit().hasAbilities()) {
            helper.setTitle("&a&lTournament &b(Beta)");
            helper.setSlot(13, "&7&m------------------");
            helper.setSlot(12, "&aTime: &f" + game.getTimer().toString());
            helper.setSlot(11, "");
            helper.setSlot(10, "&aLadder");
            helper.setSlot(9, "  &f" + plugin.eventManager().kit().getName());
            helper.setSlot(8, " ");
            helper.setSlot(7, "&aAbilities");
            helper.setSlot(6, "  &aDouble Jump: &f" + game.getDoubleJumps(player) + "/" + plugin.eventManager().kit().getDoubleJumps());
            helper.setSlot(5, "  &aTriple Shot: &f" + game.getTripleShots(player) + "/" + plugin.eventManager().kit().getTripleShots());
            helper.setSlot(4, "  &aRepulsor: &f" + game.getRepulsors(player) + "/" + plugin.eventManager().kit().getRepulsors());
            //helper.setSlot(4, "  &aRepulsor: &c0&f/&c0");
            helper.setSlot(3, "");
            helper.setSlot(2, "&7&m------------------");
            helper.setSlot(1, "&aplay.elytrapvp.net");
        }
        else {
            helper.setTitle("&a&lTournament &b(Beta)");
            helper.setSlot(15, "&7&m------------------");
            helper.setSlot(14, "&aTime: &f" + game.getTimer().toString());
            helper.setSlot(13, "");
            helper.setSlot(12, "&aLadder");
            helper.setSlot(11, "  &f" + plugin.eventManager().kit().getName());
            helper.setSlot(10, " ");
            helper.setSlot(9, "&aOpponent");

            Player opponent = game.getOpponent(player);
            helper.setSlot(8, "  &f" + opponent.getName());
            helper.setSlot(7, "  " + ChatUtils.getFormattedHealthPercent(opponent) + " &7- " + ChatUtils.getFormattedPing(opponent));
            helper.setSlot(6, "");
            helper.setSlot(5, "&aScore:");
            helper.setSlot(4, "  &f" + game.getScore(player) + " &7- &f" + game.getScore(game.getOpponent(player)));
            helper.setSlot(3, "");
            helper.setSlot(2, "&7&m------------------");
            helper.setSlot(1, "&aplay.elytrapvp.net");
        }
    }
}