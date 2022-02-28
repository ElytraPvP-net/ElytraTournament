package net.elytrapvp.elytratournament.utils.item;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;

public class SkullBuilder extends ItemBuilder {
    private String id;
    private Player player;

    /**
     * Create a SkullBuilder
     * @param id Skull Texture
     */
    public SkullBuilder(String id) {
        // Temporarily set the item to a player head.
        super(Material.SKULL_ITEM);
        this.id = id;
    }

    public SkullBuilder(Player player) {
        super(Material.SKULL_ITEM);
        this.id = "null";
        this.player = player;
    }

    @Override
    public ItemStack build() {
        ItemStack item = super.build();
        item.setDurability((short) 3);
        SkullMeta meta = (SkullMeta) item.getItemMeta();

        if(!id.equals("null")) {
            String url = "http://textures.minecraft.net/texture/" + id;

            GameProfile profile = new GameProfile(java.util.UUID.randomUUID(), null);
            byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;

            try {
                profileField = meta.getClass().getDeclaredField("profile");
            }
            catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }

            profileField.setAccessible(true);

            try {
                profileField.set(meta, profile);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        else {
            meta.setOwner(player.getName());
        }

        item.setItemMeta(meta);
        return item;
    }

}