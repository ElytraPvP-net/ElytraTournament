package net.elytrapvp.elytratournament.guis;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.event.game.GameState;
import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import net.elytrapvp.elytratournament.utils.gui.CustomGUI;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;

public class SpectateGUI extends CustomGUI {

    public SpectateGUI(ElytraTournament plugin) {
        super(54, "Current Matches");

        for(int i = 0; i < plugin.gameManager().getActiveGames().size(); i++) {
            Game game = plugin.gameManager().getActiveGames().get(i);

            ItemBuilder item = new ItemBuilder(plugin.eventManager().kit().getIconMaterial())
                    .setDisplayName("&a" + plugin.eventManager().kit().getName() + " &f(" + game.getArena().getMap().getName() + ")")
                    .addFlag(ItemFlag.HIDE_ATTRIBUTES);

            for(Player p : game.getPlayers()) {
                item.addLore("&7  - " + p.getName());
            }

            setItem(i, item.build(), (p, a) -> {
                p.closeInventory();
                if(game.getGameState() == GameState.WAITING || game.getGameState() == GameState.END) {
                    ChatUtils.chat(p, "&cError &8» &cThat match has ended.");
                    return;
                }

                p.teleport(game.getArena().getSpectateSpawn());
            });
        }
    }
}