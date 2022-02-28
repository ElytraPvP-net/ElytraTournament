package net.elytrapvp.elytratournament.utils.gui;

import net.elytrapvp.elytratournament.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Represebts a simplified way to create
 * interactive GUIs.
 */
public abstract class CustomGUI {
    private static Map<UUID, CustomGUI> inventories = new HashMap<>();
    private static Map<UUID, UUID> openInventories = new HashMap<>();

    private Inventory inventory;
    private UUID uuid;
    private Map<Integer, ClickAction> actions;

    /**
     * Creates a new custom GUI
     * @param size Size of the GUI
     * @param name Name of the GUI
     */
    public CustomGUI(int size, String name) {
        uuid = UUID.randomUUID();
        inventory = Bukkit.createInventory(null, size, ChatUtils.translate(name));
        actions = new HashMap<>();

        inventories.put(uuid, this);
    }

    public static Map<UUID, CustomGUI> getInventories() {
        return inventories;
    }

    public static Map<UUID, UUID> getOpenInventories() {
        return openInventories;
    }

    public void delete() {
        inventories.remove(getUUID());
    }

    public Map<Integer, ClickAction> getActions() {
        return actions;
    }

    public Inventory getInventory() {
        return inventory;
    }

    @Deprecated
    public UUID getUuid() {
        return getUUID();
    }

    public UUID getUUID() {
        return uuid;
    }

    /**
     * Adds an item to the GUI,
     * @param slot slot to add item to.
     * @param item Item to add.
     * @param action Click Action
     */
    public void setItem(int slot, ItemStack item, ClickAction action) {
        inventory.setItem(slot, item);

        if(actions != null) {
            actions.put(slot, action);
        }
    }

    public void setItem(int slot, ItemStack item) {
        setItem(slot, item, null);
    }

    /**
     * Open the GUI for a player.
     * @param player Player to open for
     */
    public void open(Player player) {
        player.openInventory(inventory);
        openInventories.put(player.getUniqueId(), getUuid());
    }

    public void onClose(Player p) {}

    public interface ClickAction {
        void click(Player player, ClickType type);
    }

    public void onClick(InventoryClickEvent event) {
        event.setCancelled(true);
    }

    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
}