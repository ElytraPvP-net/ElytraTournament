package net.elytrapvp.elytratournament.commands;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.BestOf;
import net.elytrapvp.elytratournament.event.EventScoreboard;
import net.elytrapvp.elytratournament.event.EventStatus;
import net.elytrapvp.elytratournament.event.EventType;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.gui.CustomGUI;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import net.elytrapvp.elytratournament.utils.item.SkullBuilder;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CreateCMD extends AbstractCommand {
    private final ElytraTournament plugin;

    public CreateCMD(ElytraTournament plugin) {
        super("create", "tournament.use", false);
        this.plugin = plugin;
    }

    public void execute(CommandSender sender, String[] args) {

        // Make sure no other tournaments are currently running.
        if(plugin.eventManager().activeEvent() != null) {
            ChatUtils.chat(sender, "&c&lError &8» &cThere is already a tournament active!");
            return;
        }

        Player player = (Player) sender;
        plugin.eventManager().host(player);
        new SetKitGUI().open(player);

        // Update scoreboard
        Bukkit.getOnlinePlayers().forEach(p -> new EventScoreboard(plugin, p));
    }

    private class SetKitGUI extends CustomGUI {
        public SetKitGUI() {
            super(54, "Select A Kit");

            int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
            for(int i : fillers) {
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
                ItemMeta meta = filler.getItemMeta();
                meta.setDisplayName(" ");
                filler.setItemMeta(meta);
                setItem(i, filler);
            }

            int i = 0;
            for(Kit kit : plugin.kitManager().getKits()) {
                setItem(i + 9, new ItemBuilder(kit.getIconMaterial())
                                .setDisplayName("&a&l" + kit.getName())
                                .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                                .addLore("").addLore("&aClick to Select!").build(),
                        (p, a) -> {
                            plugin.eventManager().kit(kit);
                            new SetBracketGUI().open(p);
                        });
                i++;
            }
        }
    }

    private class SetBracketGUI extends CustomGUI {
        public SetBracketGUI() {
            super(54, "Select Bracket");

            int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
            for(int i : fillers) {
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
                ItemMeta meta = filler.getItemMeta();
                meta.setDisplayName(" ");
                filler.setItemMeta(meta);
                setItem(i, filler);
            }

            ItemStack singleElim = new SkullBuilder("6d65ce83f1aa5b6e84f9b233595140d5b6beceb62b6d0c67d1a1d83625ffd")
                    .setDisplayName("&aSingle Elimination")
                    .addLore("").addLore("&aClick to Select!").build();
            setItem(20, singleElim, (p,a) -> {
                plugin.eventManager().eventType(EventType.SINGLE_ELIMINATION);
                new SetBestOfGUI().open(p);
            });

            ItemStack doubleElim = new SkullBuilder("dd54d1f8fbf91b1e7f55f1bdb25e2e33baf6f46ad8afbe08ffe757d3075e3")
                    .setDisplayName("&aDouble Elimination")
                    .addLore("").addLore("&aClick to Select!").build();
            setItem(24, doubleElim, (p,a) -> {
                plugin.eventManager().eventType(EventType.DOUBLE_ELIMINATION);
                new SetBestOfGUI().open(p);
            });
        }
    }

    private class SetBestOfGUI extends CustomGUI {
        public SetBestOfGUI() {
            super(54, "Select Best Of");

            int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
            for(int i : fillers) {
                ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
                ItemMeta meta = filler.getItemMeta();
                meta.setDisplayName(" ");
                filler.setItemMeta(meta);
                setItem(i, filler);

                ItemStack bo1 = new SkullBuilder("6d65ce83f1aa5b6e84f9b233595140d5b6beceb62b6d0c67d1a1d83625ffd")
                        .setDisplayName("&a&lBest of 1")
                        .addLore("").addLore("&aClick to Select!").build();
                setItem(19, bo1, (p,a) -> {
                    plugin.eventManager().bestOf(BestOf.ONE);
                    plugin.eventManager().eventStatus(EventStatus.WAITING);
                    p.closeInventory();
                });

                ItemStack bo3 = new SkullBuilder("21e4ea59b54cc99416bc9f624548ddac2a38eea6a2dbf6e4ccd83cec7ac969")
                        .setDisplayName("&a&lBest of 3")
                        .addLore("").addLore("&aClick to Select!").build();
                setItem(21, bo3, (p,a) -> {
                    plugin.eventManager().bestOf(BestOf.THREE);
                    plugin.eventManager().eventStatus(EventStatus.WAITING);
                    p.closeInventory();
                });

                ItemStack bo5 = new SkullBuilder("84c8c3710da2559a291adc39629e9ccea31ca9d3d3586bfea6e6e06124b3c")
                        .setDisplayName("&a&lBest of 5")
                        .addLore("").addLore("&aClick to Select!").build();
                setItem(23, bo5, (p,a) -> {
                    plugin.eventManager().bestOf(BestOf.FIVE);
                    plugin.eventManager().eventStatus(EventStatus.WAITING);
                    p.closeInventory();
                });

                ItemStack bo7 = new SkullBuilder("24bde79f84fc5f3f1fbc5bc01071066bd20cd263a1654d64d60d84248ba9cd")
                        .setDisplayName("&a&lBest of 7")
                        .addLore("").addLore("&aClick to Select!").build();
                setItem(25, bo7, (p,a) -> {
                    plugin.eventManager().bestOf(BestOf.SEVEN);
                    plugin.eventManager().eventStatus(EventStatus.WAITING);
                    p.closeInventory();
                });
            }
        }
    }
}