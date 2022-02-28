package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.utils.MathUtils;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByEntityListener implements Listener {
    private final ElytraTournament plugin;

    public EntityDamageByEntityListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEvent(EntityDamageByEntityEvent event) {
        // Exit if not a player.
        if(!(event.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Game game = plugin.gameManager().getGame(player);

        // Exit if player is not in a game.
        if(game == null) {
            return;
        }

        // Checks if the kit should take damage
        if(!plugin.eventManager().kit().hasTakeDamage()) {
            event.setCancelled(true);
            return;
        }

        // Check if players should do damage.
        if(!plugin.eventManager().kit().hasDoDamage()) {
            event.setDamage(0);
            return;
        }

        // Makes sure the damager is an arrow before continuing.
        if(!(event.getDamager() instanceof Arrow)) {
            return;
        }

        Arrow a = (Arrow) event.getDamager();

        // Makes sure a player shot the arrow.
        if(!(a.getShooter() instanceof Player)) {
            return;
        }

        Player shooter = (Player) a.getShooter();

        // Applies ranged damage if enabled.
        if(plugin.eventManager().kit().hasRangedDamage()) {
            rangedDamage(event, player, shooter);
        }

        ChatUtils.chat(shooter, "&f" + player.getName() + " &ahas " + getHealthPercent((player.getHealth() - event.getFinalDamage()))  + " &aremaining.");

        if(event.getFinalDamage() >= player.getHealth()) {
            event.setCancelled(true);

            Bukkit.getScheduler().runTaskLater(plugin, () -> game.playerKilled(player), 1);
        }
    }

    /**
     * Get the health of a player based in percent and formatted.
     * @param damage Health.
     * @return formatted health.
     */
    private String getHealthPercent(Double damage) {
        int percent = MathUtils.percent(damage, 20.0);
        ChatColor color;

        if(percent < 0) {
            percent = 0;
        }

        if(percent == 0) {
            color = ChatColor.DARK_RED;
        }
        if(percent < 25) {
            color = ChatColor.RED;
        }
        else if(percent < 50) {
            color = ChatColor.GOLD;
        }
        else if(percent < 75) {
            color = ChatColor.YELLOW;
        }
        else {
            color = ChatColor.GREEN;
        }

        return "" + color + percent + "%";
    }

    /**
     * Applies ranged damage to the arrow.
     * @param event EntityDamageByEntityEvent
     * @param player Player getting hit.
     * @param shooter Shooter of the arrow.
     */
    private void rangedDamage(EntityDamageByEntityEvent event, Player player, Player shooter) {
        Location shooterloc = shooter.getLocation();
        double dis = player.getLocation().distance(shooterloc);
        if (dis > 40.0D) {
            event.setDamage(7.0D);
        } else if (dis > 30.0D && dis <= 40.0D) {
            event.setDamage(6.0D);
        } else if (dis > 20.0D && dis <= 30.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(5.8D);
            } else {
                event.setDamage(4.0D);
            }
        } else if (dis > 17.0D && dis <= 20.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(5.0D);
            } else if (event.getDamage() <= 6.0D && event.getDamage() >= 2.0D) {
                event.setDamage(3.0D);
            } else if (event.getDamage() < 2.0D) {
                event.setDamage(1.0D);
            }
        } else if (dis > 14.0D && dis <= 17.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(4.0D);
            } else if (event.getDamage() <= 6.0D && event.getDamage() >= 2.0D) {
                event.setDamage(2.0D);
            } else if (event.getDamage() < 2.0D) {
                event.setDamage(0.6D);
            }
        } else if (dis > 8.0D && dis <= 14.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(3.0D);
            } else if (event.getDamage() <= 6.0D && event.getDamage() >= 2.0D) {
                event.setDamage(1.5D);
            } else if (event.getDamage() < 2.0D) {
                event.setDamage(0.5D);
            }
        } else if (dis > 4.0D && dis <= 8.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(2.0D);
            } else if (event.getDamage() <= 6.0D && event.getDamage() >= 2.0D) {
                event.setDamage(1.0D);
            } else if (event.getDamage() < 2.0D) {
                event.setDamage(0.2D);
            }
        } else if (dis > 2.0D && dis <= 4.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(1.0D);
            } else if (event.getDamage() <= 6.0D && event.getDamage() >= 2.0D) {
                event.setDamage(0.4D);
            } else if (event.getDamage() < 2.0D) {
                event.setDamage(0.1D);
            }
        } else if (dis >= 0.0D && dis <= 2.0D) {
            if (event.getDamage() > 6.0D) {
                event.setDamage(0.4D);
            } else if (event.getDamage() <= 6.0D && event.getDamage() >= 2.0D) {
                event.setDamage(0.2D);
            } else if (event.getDamage() < 2.0D) {
                event.setDamage(0.1D);
            }
        }
    }
}