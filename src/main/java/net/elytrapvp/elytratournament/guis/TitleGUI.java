package net.elytrapvp.elytratournament.guis;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.gui.CustomGUI;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import net.elytrapvp.elytratournament.utils.item.SkullBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TitleGUI extends CustomGUI {

    public TitleGUI(ElytraTournament plugin, Player player) {
        super(54, "Titles");

        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,50,51,52,53};
        for(int i : fillers) {
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
            ItemMeta meta = filler.getItemMeta();
            meta.setDisplayName(" ");
            filler.setItemMeta(meta);
            setItem(i, filler);
        }

        ItemStack back = new SkullBuilder("edf5c2f893bd3f89ca40703ded3e42dd0fbdba6f6768c8789afdff1fa78bf6")
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> {
            p.closeInventory();
            new SettingsGUI(plugin, p).open(p);
        });

        plugin.getServer().getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

                PreparedStatement statement = plugin.mySQL().getConnection().prepareStatement("SELECT * FROM tournament_titles WHERE uuid = ? LIMIT 36");
                statement.setString(1, player.getUniqueId().toString());
                ResultSet results = statement.executeQuery();

                int i = 0;
                while(results.next()) {
                    String title = results.getString(3);

                    setItem(i + 9, new ItemBuilder(Material.NAME_TAG)
                            .setDisplayName(title)
                            .addLore("")
                            .addLore("&aClick to select!")
                            .build(),
                            (p, a) -> {
                        p.closeInventory();
                        customPlayer.setTitle(title);
                        ChatUtils.chat(p, "&aYour title has been set to &f" + title + "&a.");
                    });

                    i++;
                }

                setItem(49, new ItemBuilder(Material.BARRIER).setDisplayName("&cReset").build(), (p,a) -> {
                    customPlayer.setTitle("");
                    ChatUtils.chat(p, "&aYour title has been reset.");
                });
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }

}
