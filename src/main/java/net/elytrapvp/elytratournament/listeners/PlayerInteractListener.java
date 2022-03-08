package net.elytrapvp.elytratournament.listeners;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import net.elytrapvp.elytratournament.guis.SettingsGUI;
import net.elytrapvp.elytratournament.guis.SpectateGUI;
import net.elytrapvp.elytratournament.utils.MathUtils;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

public class PlayerInteractListener implements Listener {
    private final ElytraTournament plugin;
    private final Set<Player> pearlCooldown = new HashSet<>();


    public PlayerInteractListener(ElytraTournament plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Game game = plugin.gameManager().getGame(player);

        // Prevent using items during game countdown.
        if(game != null && game.getGameState() != GameState.RUNNING) {
            event.setCancelled(true);

            // Fixes visual glitch with throwables during countdown.
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), player.getItemInHand());
            return;
        }

        // Checks for the ender pearl cooldown.
        if(plugin.eventManager().activeEvent() != null && event.getItem() != null && event.getItem().getType() == Material.ENDER_PEARL && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            if(pearlCooldown.contains(player)) {
                ChatUtils.chat(player, "&cThat item is currently on cooldown.");
                event.setCancelled(true);
                return;
            }

            if(plugin.eventManager().kit().hasPearlCooldown()) {
                pearlCooldown.add(player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> pearlCooldown.remove(player), 200);
            }
        }

        if(game != null) {
            if (game.getGameState() == GameState.RUNNING) {
                if (plugin.eventManager().kit().getTripleShots() > 0) {
                    if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        if (event.getItem() != null) {
                            if (event.getItem().getType() == Material.BOW) {
                                if (game.getTripleShots(player) > 0) {
                                    Arrow arrow = player.launchProjectile(Arrow.class);
                                    arrow.setVelocity(player.getLocation().getDirection().multiply(1.9));
                                    arrow.setFireTicks(100);

                                    Arrow arrow2 = player.launchProjectile(Arrow.class);
                                    arrow2.setVelocity(MathUtils.rotateVector(arrow.getVelocity().clone(), 0.21816616).multiply(1.1));
                                    arrow2.setFireTicks(100);

                                    Arrow arrow3 = player.launchProjectile(Arrow.class);
                                    arrow3.setVelocity(MathUtils.rotateVector(arrow.getVelocity().clone(), -0.21816616).multiply(1.1));
                                    arrow3.setFireTicks(100);

                                    game.removeTripleShot(player);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Exit if the item is null.
        if(event.getItem() == null)
            return;

        if(event.getItem().getType() == Material.MUSHROOM_SOUP) {
            event.setCancelled(true);
            player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));

            double health = player.getHealth();
            health += 5;

            if(health > player.getMaxHealth()) {
                health = player.getMaxHealth();
            }

            player.setHealth(health);
            player.playSound(player.getLocation(), Sound.DRINK,1 ,1);
            return;
        }

        // Exit if item meta is null.
        if(event.getItem().getItemMeta() == null)
            return;

        String item = ChatColor.stripColor(event.getItem().getItemMeta().getDisplayName());

        if(item == null) {
            return;
        }

        switch (item) {
            case "Settings" -> {
                new SettingsGUI(plugin, player).open(player);
                event.setCancelled(true);
            }

            case "Create Tournament" -> {
                player.chat("/create");
                event.setCancelled(true);
            }

            case "Spectate" -> {
                new SpectateGUI(plugin).open(player);
                event.setCancelled(true);
            }

            case "Double Jump" -> {
                if(game == null) {
                    return;
                }

                if(game.getGameState() != GameState.RUNNING) {
                    return;
                }

                // Exit if game does not have double jumps.
                if(plugin.eventManager().kit().getDoubleJumps() == 0) {
                    return;
                }

                // Prevents players from double jumping too often.
                if(PlayerToggleFlightListener.getDelay().contains(player)) {
                    return;
                }

                // Prevents players from "flying" when having no double jumps left.
                if(game.getDoubleJumps(player) == 0) {
                    event.setCancelled(true);
                    player.setFlying(false);
                    player.setAllowFlight(false);
                    return;
                }

                PlayerToggleFlightListener.getDelay().add(player);
                player.setAllowFlight(false);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    PlayerToggleFlightListener.getDelay().remove(player);
                    player.setAllowFlight(true);
                }, 15);
                game.removeDoubleJump(player);
                player.setFlying(false);

                Vector vector = player.getLocation().getDirection().normalize().multiply(0.5).add(new Vector(0, 0.8, 0));
                player.setVelocity(vector);
            }

            case "Golden Head" -> {
                if(pearlCooldown.contains(player)) {
                    ChatUtils.chat(player, "&cThat item is currently on cooldown.");
                    event.setCancelled(true);
                    return;
                }

                pearlCooldown.add(player);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> pearlCooldown.remove(player), 60);

                if(event.getItem().getAmount() == 1) {
                    player.getInventory().remove(event.getItem());
                }
                else {
                    event.getItem().setAmount(event.getItem().getAmount() - 1);
                }

                // Fix absorption from not resetting.
                player.removePotionEffect(PotionEffectType.ABSORPTION);

                PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 100, 0);
                PotionEffect absorption = new PotionEffect(PotionEffectType.ABSORPTION, 2400, 0);
                PotionEffect regen = new PotionEffect(PotionEffectType.REGENERATION, 100, 2);

                player.addPotionEffect(speed);
                player.addPotionEffect(absorption);
                player.addPotionEffect(regen);

                ChatUtils.chat(player, "&aYou ate a Golden Head!");
                player.playSound(player.getLocation(), Sound.EAT, 1, 1);

                event.setCancelled(true);
            }
        }
    }
}