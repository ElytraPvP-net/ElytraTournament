package net.elytrapvp.elytratournament.guis;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.EventScoreboard;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.gui.CustomGUI;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsGUI extends CustomGUI {

    public SettingsGUI(ElytraTournament plugin, Player player) {
        super(54, "Settings");

        int[] fillers = {0,1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
        for(int i : fillers) {
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
            ItemMeta meta = filler.getItemMeta();
            meta.setDisplayName(" ");
            filler.setItemMeta(meta);
            setItem(i, filler);
        }

        setItem(40, new ItemBuilder(Material.ANVIL).setDisplayName("&a&lKit Editor").build(), (p, a) -> new KitEditorGUI(plugin).open(p));

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        ItemBuilder scoreboard = new ItemBuilder(Material.SIGN).setDisplayName("&a&lShow Scoreboard");
        if(customPlayer.getShowScoreboard()) {
            scoreboard.addLore("&aEnabled");
        }
        else {
            scoreboard.addLore("&cDisabled");
        }
        setItem(22, scoreboard.build(), (p,a) -> {
            customPlayer.setShowScoreboard(!customPlayer.getShowScoreboard());
            new SettingsGUI(plugin, player).open(player);

            if(customPlayer.getShowScoreboard()) {
                new EventScoreboard(plugin, p);
            }
        });
    }
}