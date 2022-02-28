package net.elytrapvp.elytratournament.utils;

import net.elytrapvp.elytratournament.ElytraTournament;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Creates a timer that counts up.
 * Used in queue and in match.
 */
public class Timer {
    private final ElytraTournament plugin;
    private int seconds;
    private int minutes;
    private final BukkitRunnable task;

    /**
     * Create a timer.
     */
    public Timer(ElytraTournament plugin) {
        this.plugin = plugin;
        seconds = 0;
        minutes = 0;

        task = new BukkitRunnable() {
            @Override
            public void run() {
                seconds++;

                if(seconds == 60) {
                    seconds = 0;
                    minutes ++;
                }
            }
        };
    }

    /**
     * Reset the timer.
     */
    public void reset() {
        seconds = 0;
        minutes = 0;
    }

    /**
     * Start the timer.
     */
    public void start() {
        task.runTaskTimer(plugin, 0, 20);
    }

    /**
     * Stop the timer.
     */
    public void stop() {
        task.cancel();
    }

    /**
     * Converts the timer into a String
     * @return String version of timer.
     */
    public String toString() {
        String minute = "";
        if(minutes < 10) {
            minute += "0";
        }
        minute += "" + minutes;

        String second = "";

        if(seconds < 10) {
            second += "0";
        }
        second += "" + seconds;

        return minute + ":" + second;
    }
}