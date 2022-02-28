package net.elytrapvp.elytratournament.utils.scoreboard;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * A repeating task to update all active
 * Custom Scorebords.
 */
public class ScoreboardUpdate extends BukkitRunnable {
    private final ElytraTournament plugin;

    public ScoreboardUpdate(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()) {
            CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(p);

            if(!customPlayer.getShowScoreboard()) {
                p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                return;
            }

            if(ScoreHelper.hasScore(p)) {
                CustomScoreboard.getPlayers().get(p.getUniqueId()).update(p);
            }
        }
    }
}