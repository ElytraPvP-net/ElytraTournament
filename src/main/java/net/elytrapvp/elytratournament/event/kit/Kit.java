package net.elytrapvp.elytratournament.event.kit;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.game.Game;
import net.elytrapvp.elytratournament.players.CustomPlayer;
import net.elytrapvp.elytratournament.utils.item.ItemBuilder;
import net.elytrapvp.elytratournament.utils.item.ItemUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
//import pt.foxspigot.jar.knockback.KnockbackModule;

import java.util.*;

/**
 * Stores all information about a kit.
 */
public class Kit {
    private final ElytraTournament plugin;

    // Kit metadata
    private final String name;
    private Material iconMaterial = Material.WOOD_SWORD;

    // Maps
    private final Map<Integer, ItemStack> items = new HashMap<>();
    private final List<PotionEffect> potionEffects = new ArrayList<>();

    // Settings
    private GameMode gameMode = GameMode.ADVENTURE;
    private double maxHealth = 20.0;
    private double rodMultiplier = 1.5;
    private double startingHealth = 20.0;
    private int startingHunger = 20;
    private float startingSaturation = 10;
    private int voidLevel = 51;
    private boolean arrowPickup = false;
    private boolean doDamage = true;
    private boolean hunger = false;
    private boolean naturalRegen = true;
    private boolean pearlCooldown = true;
    private boolean rangedDamage = false;
    private boolean strongGapple = false;
    private boolean takeDamage = true;
    private boolean waterKills = false;
    private String knockback = "default";

    // Abilities
    private int doubleJumps = 0;
    private int repulsors = 0;
    private int tripleShots = 0;

    /**
     * Create a kit.
     * @param name Name of the kit.
     */
    public Kit(ElytraTournament plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    /**
     * Add an item to the kit.
     * @param slot Slot item is in.
     * @param item Item to add.
     */
    public void addItem(int slot, ItemStack item) {
        ItemBuilder builder = new ItemBuilder(item.clone()).addLore(ItemUtils.convertToInvisibleString(slot + ""));
        items.put(slot, builder.build());
    }

    /**
     * Add a potion effect to the kit.
     * @param effect Potion effect to add.
     */
    public void addPotionEffect(PotionEffect effect) {
        potionEffects.add(effect);
    }

    /**
     * Apply a kit to a player.
     * @param player Player to apply kit to.
     */
    public void apply(Player player) {
        // Clear inventory.
        player.getInventory().clear();
        player.getInventory().setHelmet(null);
        player.getInventory().setChestplate(null);
        player.getInventory().setLeggings(null);
        player.getInventory().setBoots(null);

        // Clear potion effects.
        player.getActivePotionEffects().forEach(effect -> player.removePotionEffect(effect.getType()));

        CustomPlayer customPlayer = plugin.customPlayerManager().getPlayer(player);

        Map<Integer, ItemStack> updatedKit = new HashMap<>();
        Set<Integer> slotsUsed = new HashSet<>();

        for(int slot : customPlayer.getKitEditor(name).keySet()) {
            slotsUsed.add(slot);
            updatedKit.put(slot, items.get(customPlayer.getKitEditor(name).get(slot)));
        }

        for(int slot : items.keySet()) {
            if(slotsUsed.contains(slot)) {
                continue;
            }

            ItemStack item =  items.get(slot);

            if(updatedKit.containsValue(item)) {
                continue;
            }

            updatedKit.put(slot, item);
        }

        // Give items
        for(int i : updatedKit.keySet()) {
            ItemStack item = updatedKit.get(i).clone();

            if(item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();
                List<String> lore = new ArrayList<>(meta.getLore());
                if(lore.size() > 0) {
                    lore.remove(lore.size() - 1);
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
            }

            player.getInventory().setItem(i, item);
        }

        // Set game mode/health/hunger/saturation.
        player.setGameMode(gameMode);
        player.setMaxHealth(maxHealth);

        if(startingHealth > maxHealth) {
            startingHealth = maxHealth;
        }

        player.setHealth(startingHealth);
        player.setFoodLevel(startingHunger);
        player.setSaturation(startingSaturation);

        // Apply potion effects to the kit.
        for(PotionEffect effect : potionEffects) {
            player.addPotionEffect(effect);
        }

        //((CraftPlayer) player).getHandle().setKnockback(KnockbackModule.getByName(knockback));
    }

    /**
     * Get the number of double jumps the kit has.
     * @return Number of double jumps.
     */
    public int getDoubleJumps() {
        return doubleJumps;
    }

    /**
     * Get the kit's game mode.
     * @return Game mode of the kit.
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Get the material for the kit icon.
     * @return
     */
    public Material getIconMaterial() {
        return iconMaterial;
    }

    /**
     * Get the items in the kit.
     * @return Items in the kit.
     */
    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    /**
     * Set the knockback profile of the kit.
     * @param knockback Knockback profile name.
     */
    public void setKnockback(String knockback) {
        this.knockback = knockback;
    }

    /**
     * Get the max health.
     * @return Max health.
     */
    public double getMaxHealth() {
        return maxHealth;
    }

    /**
     * Get the name of the kit.
     * @return Name of the kit.
     */
    public String getName() {
        return name;
    }

    /**
     * Get the number of repulsors used in the kit.
     * @return
     */
    public int getRepulsors() {
        return repulsors;
    }

    /**
     * Get the rod multiplier of the kit.
     * @return Rod multiplier of the kit.
     */
    public double getRodMultiplier() {
        return rodMultiplier;
    }

    /**
     * Get the starting health.
     * @return Starting health.
     */
    public double getStartingHealth() {
        return startingHealth;
    }

    /**
     * Get the starting hunger.
     * @return Starting hunger.
     */
    public int getStartingHunger() {
        return startingHunger;
    }

    /**
     * Get the starting saturation.
     * @return Starting saturation.
     */
    public float getStartingSaturation() {
        return startingSaturation;
    }

    /**
     * Get the number of triple shots the kit has.
     * @return Number of triple shots.
     */
    public int getTripleShots() {
        return tripleShots;
    }

    /**
     * Get the void level of the kit.
     * @return Void level of the kit.
     */
    public int getVoidLevel() {
        return voidLevel;
    }

    /**
     * Get whether the kit has any abilities.
     * @return Whether or not the kit has abilities.
     */
    public boolean hasAbilities() {
        return doubleJumps > 0 || repulsors > 0 || tripleShots > 0;
    }

    /**
     * Get whether the kit has arrow pickup enable.
     * @return Wehther or not players can pick up arrows.
     */
    public boolean hasArrowPickup() {
        return arrowPickup;
    }

    /**
     * Check if players should do damage.
     * @return If players should do damage.
     */
    public boolean hasDoDamage() {
        return doDamage;
    }

    /**
     * Get if the kit should have hunger.
     * @return Whether or not the kit has hunger.
     */
    public boolean hasHunger() {
        return hunger;
    }

    /**
     * Get if the kit should have pearl cooldown.
     * @return Whether the kit has pearl cooldown.
     */
    public boolean hasPearlCooldown() {
        return pearlCooldown;
    }

    /**
     * Get if the kit will have ranged ranged.
     * @return Whether or not the kit as ranged damage.
     */
    public boolean hasRangedDamage() {
        return rangedDamage;
    }

    /**
     * Get if the kit should have strong golden apples.
     * @return Whether or not it has strong golden apples.
     */
    public boolean hasStrongGapple() {
        return strongGapple;
    }

    /**
     * Get if players should take damage.
     * @return Whether players should take damage.
     */
    public boolean hasTakeDamage() {
        return takeDamage;
    }

    /**
     * Get if the kit should have natural regen.
     * @return Whether or not the kit has natural regen.
     */
    public boolean naturalRegen() {
        return naturalRegen;
    }

    /**
     * Called when a block is placed in game.
     * @param game Current Game
     * @param event BlockPlaceEvent
     */
    public void onBlockPlace(Game game, BlockPlaceEvent event) {}

    /**
     * Set if players should be able to pickup arrows.
     * @param arrowPickup Whether or not arrows can be picked up.
     */
    public void setArrowPickup(boolean arrowPickup) {
        this.arrowPickup = arrowPickup;
    }

    /**
     * Set whether or not players should do damage to others.
     * @param doDamage If players should do damage.
     */
    public void setDoDamage(boolean doDamage) {
        this.doDamage = doDamage;
    }

    /**
     * Set the number of double jumps
     * @param doubleJumps
     */
    public void setDoubleJumps(int doubleJumps) {
        this.doubleJumps = doubleJumps;
    }

    /**
     * Set the kit's game mode.
     * @param gameMode Game mode of the kit.
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Set it the kit should have hunger.
     * @param hunger Whether or not the kit has hunger.
     */
    public void setHunger(boolean hunger) {
        this.hunger = hunger;
    }

    /**
     * Set the icon material of the kit.
     * @param iconMaterial Icon material
     */
    public void setIconMaterial(Material iconMaterial) {
        this.iconMaterial = iconMaterial;
    }

    /**
     * Set the kit's maximum health.
     * @param maxHealth Maximum health of the kit.
     */
    public void setMaxHealth(double maxHealth) {
        this.maxHealth = maxHealth;
    }

    /**
     * Set if the kit should have natural regen.
     * @param naturalRegen Whether or not the kit has natural regen.
     */
    public void setNaturalRegen(boolean naturalRegen) {
        this.naturalRegen = naturalRegen;
    }

    /**
     * Set the rod multiplier of the kit.
     * @param rodMultiplier The rod multiplier of the kit.
     */
    public void setRodMultiplier(double rodMultiplier) {
        this.rodMultiplier = rodMultiplier;
    }

    /**
     * Set if the kit should have ranged damage.
     * @param rangedDamage Whether or not the kit has ranged damage.
     */
    public void setRangedDamage(boolean rangedDamage) {
        this.rangedDamage = rangedDamage;
    }

    /**
     * Set the number of repulsors
     * @param repulsors
     */
    public void setRepulsors(int repulsors) {
        this.repulsors = repulsors;
    }

    /**
     * Set if the kit should have pearl cooldown.
     * @param pearlCooldown If the kit has pearl cooldown.
     */
    public void setPearlCooldown(boolean pearlCooldown) {
        this.pearlCooldown = pearlCooldown;
    }

    /**
     * Set the kit's starting health.
     * @param startingHealth The starting health of the kit.
     */
    public void setStartingHealth(double startingHealth) {
        this.startingHealth = startingHealth;
    }

    /**
     * Set the kit's starting hunger.
     * @param startingHunger Starting hunger of the kit.
     */
    public void setStartingHunger(int startingHunger) {
        this.startingHunger = startingHunger;
    }

    /**
     * Set the kit's starting saturation.
     * @param startingSaturation Set the starting saturation of the kit.
     */
    public void setStartingSaturation(float startingSaturation) {
        this.startingSaturation = startingSaturation;
    }

    /**
     * Set it Golden Apples should be stronger.
     * @param strongGapple Whether or not golden apples are stronger.
     */
    public void setStrongGapple(boolean strongGapple) {
        this.strongGapple = strongGapple;
    }

    /**
     * Set if players should take damage.
     * @param takeDamage Whether players should take damage.
     */
    public void setTakeDamage(boolean takeDamage) {
        this.takeDamage = takeDamage;
    }

    /**
     * Set the number of triple shots
     * @param tripleShots
     */
    public void setTripleShots(int tripleShots) {
        this.tripleShots = tripleShots;
    }

    /**
     * Set the void level of the kit.
     * @param voidLevel Void level of the kit.
     */
    public void setVoidLevel(int voidLevel) {
        this.voidLevel = voidLevel;
    }

    /**
     * Set if water should kill players.
     * @param waterKills Whether or not water kills players.
     */
    public void setWaterKills(boolean waterKills) {
        this.waterKills = waterKills;
    }

    /**
     * Get if the kit should kill the player
     *  when they touch water.
     * @return Wether or not water kills the player.
     */
    public boolean waterKills() {
        return waterKills;
    }
}