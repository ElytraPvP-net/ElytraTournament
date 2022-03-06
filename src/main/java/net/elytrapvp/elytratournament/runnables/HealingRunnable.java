package net.elytrapvp.elytratournament.runnables;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class HealingRunnable extends BukkitRunnable {
    private final ElytraTournament plugin;

    public HealingRunnable(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            // Allow regeneration to work.
            if(player.hasPotionEffect(PotionEffectType.REGENERATION)) {
                continue;
            }

            //
            if(!plugin.eventManager().kit().naturalRegen()) {
                continue;
            }

            double health = player.getHealth() + 1;

            if(health > 20) {
                health = 20;
            }

            player.setHealth(health);
        }
    }
}
