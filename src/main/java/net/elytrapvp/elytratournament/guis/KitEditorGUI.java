package net.elytrapvp.elytratournament.guis;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.gui.CustomGUI;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import net.elytrapvp.elytratournament.utils.item.SkullBuilder;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class KitEditorGUI extends CustomGUI {
    private final ElytraTournament plugin;
    private String kit = "none";

    public KitEditorGUI(ElytraTournament plugin) {
        super(54, "Kit Editor");
        this.plugin = plugin;

        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,48,49,50,51,52,53};
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

        int i = 0;
        for(Kit kit : plugin.kitManager().getKits()) {
            setItem(i + 9, new ItemBuilder(kit.getIconMaterial())
                            .setDisplayName("&a&l" + kit.getName())
                            .addFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .addLore("").addLore("&aClick to edit!").build(),
                    (p, a) -> {
                        p.closeInventory();
                        new KitEditorGUI(plugin, p, kit.getName()).open(p);
                    });
            i++;
        }
    }

    public KitEditorGUI(ElytraTournament plugin, Player player, String kit) {
        super(54, "Kit Editor - " + kit);
        this.plugin = plugin;
        this.kit = kit;
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        ItemStack back = new SkullBuilder("edf5c2f893bd3f89ca40703ded3e42dd0fbdba6f6768c8789afdff1fa78bf6")
                .setDisplayName("&cBack")
                .build();
        setItem(0, back, (p, a) -> {
            p.closeInventory();
            new KitEditorGUI(plugin).open(p);
        });

        ItemStack save = new SkullBuilder("4312ca4632def5ffaf2eb0d9d7cc7b55a50c4e3920d90372aab140781f5dfbc4")
                .setDisplayName("&aSave")
                .build();
        setItem(48, save, (p,a ) -> {
            ItemStack[] items = getInventory().getContents();
            Kit kit1 = plugin.kitManager().getKit(this.kit);
            customPlayer.getKitEditor(kit1.getName()).clear();

            for(int i = 9; i < 45; i++) {
                ItemStack item = items[i];
                if(item == null) {
                    continue;
                }

                if(item.isSimilar(kit1.getItems().get(i))) {
                    continue;
                }

                for(int key : kit1.getItems().keySet()) {
                    if(kit1.getItems().get(key).equals(item)) {
                        customPlayer.getKitEditor(kit1.getName()).put(getReversedMappedSlot(i), key);
                        break;
                    }
                }
            }
            customPlayer.updateKitEditor(kit);
            p.closeInventory();
        });

        ItemStack reset = new SkullBuilder("beb588b21a6f98ad1ff4e085c552dcb050efc9cab427f46048f18fc803475f7")
                .setDisplayName("&cReset")
                .build();
        setItem(50, reset, (p, a) -> {
            customPlayer.getKitEditor(kit).clear();
            customPlayer.updateKitEditor(kit);
            p.closeInventory();
        });

        int[] fillers = {1,2,3,4,5,6,7,8,45,46,47,49,51,52,53};
        for(int i : fillers) {
            ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getData());
            ItemMeta meta = filler.getItemMeta();
            meta.setDisplayName(" ");
            filler.setItemMeta(meta);
            setItem(i, filler);
        }

        Map<Integer, ItemStack> updatedKit = new HashMap<>();
        Set<Integer> slotsUsed = new HashSet<>();

        for(int slot : customPlayer.getKitEditor(kit).keySet()) {
            slotsUsed.add(slot);
            updatedKit.put(slot, plugin.kitManager().getKit(kit).getItems().get(customPlayer.getKitEditor(kit).get(slot)));
        }

        for(int slot : plugin.kitManager().getKit(kit).getItems().keySet()) {
            if(slotsUsed.contains(slot)) {
                continue;
            }

            if(slot > 35) {
                continue;
            }

            ItemStack item =  plugin.kitManager().getKit(kit).getItems().get(slot);

            if(updatedKit.containsValue(item)) {
                continue;
            }

            updatedKit.put(slot, item);
        }

        for(int slot : updatedKit.keySet()) {
            setItem(getMappedSlot(slot), updatedKit.get(slot));
        }
    }

    @Override
    public void onClick(InventoryClickEvent event) {
        if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
            event.setCancelled(true);
            return;
        }

        if(event.getInventory().getName().equalsIgnoreCase("Kit Editor")) {
            event.setCancelled(true);
            return;
        }

        ItemStack item = event.getCurrentItem();

        if(item == null || event.getClickedInventory() == null) {
            event.setCancelled(true);
            return;
        }

        if(event.getClick().isRightClick() && item.getAmount() > 1) {
            event.setCancelled(true);
            return;
        }

        if(item.getType() == Material.STAINED_GLASS_PANE || item.getType() == Material.SKULL_ITEM) {
            if(item.getAmount() > 1) {
                return;
            }
            event.setCancelled(true);
            return;
        }

        if(!event.getClickedInventory().getName().equalsIgnoreCase(getInventory().getName()) || !event.getInventory().getName().equalsIgnoreCase(getInventory().getName())) {
            event.setCancelled(true);
        }
    }

    public void onCloses(Player player) {
        if(this.kit.equals("none")) {
            return;
        }

        ItemStack[] items = getInventory().getContents();
        Kit kit = plugin.kitManager().getKit(this.kit);
        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);
        customPlayer.getKitEditor(kit.getName()).clear();

        for(int i = 9; i < 45; i++) {
            ItemStack item = items[i];
            if(item == null) {
                continue;
            }

            if(item.isSimilar(kit.getItems().get(i))) {
                continue;
            }

            for(int key : kit.getItems().keySet()) {
                if(kit.getItems().get(key).equals(item)) {
                    customPlayer.getKitEditor(kit.getName()).put(getReversedMappedSlot(i), key);
                    break;
                }
            }
        }
    }

    private int getMappedSlot(int slot) {
        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i <= 9; i++) {
            map.put(i, (i + 36));
        }

        for(int i = 9; i <= 35; i++) {
            map.put(i, i);
        }

        return map.get(slot);
    }

    private int getReversedMappedSlot(int slot) {
        Map<Integer, Integer> map = new HashMap<>();

        for(int i = 0; i <= 9; i++) {
            map.put((i + 36), i);
        }

        for(int i = 9; i <= 35; i++) {
            map.put(i, i);
        }

        return map.get(slot);
    }
}