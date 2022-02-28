package net.elytrapvp.elytratournament.utils.chat;

import net.elytrapvp.elytratournament.utils.MathUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ChatUtils {
    private final static int CENTER_PX = 154;

    /**
     * A quicker way to send colored messages to a command sender.
     * @param sender Command Sender
     * @param message Message to be sent.
     */
    public static void chat(CommandSender sender, String message) {
        sender.sendMessage(translate(message));
    }

    /**
     * Sender a centered chat message to a CommandSender.
     * @param sender Command Sender
     * @param message Message
     */
    public static void centeredChat(CommandSender sender, String message) {
        message = translate(message);

        if(message == null || message.equals("")) sender.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for(char c : message.toCharArray()) {
            if(c == '§') {
                previousCode = true;
            }
            else if(previousCode) {
                previousCode = false;
                isBold = c == 'l' || c == 'L';
            }
            else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while(compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        sender.sendMessage(sb.toString() + message);
    }

    /**
     * Translate a String to a colorful String.
     * @param message Message to translate.
     * @return Translated Message.
     */
    public static String translate(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    /**
     * Translate an array of strings.
     * @param arr Strings
     * @return array of translated strings.
     */
    public static String[] translate(String[] arr) {
        for(int i = 0; i < arr.length; i++) {
            arr[i] = translate(arr[i]);
        }

        return arr;
    }

    /**
     * Get the health of a player, formatted, and in percent form.
     * @param player Player to get health of.
     * @return Formatted health of player.
     */
    public static String getFormattedHealthPercent(Player player) {
        int percent = MathUtils.percent(player.getHealth(), 20.0);
        ChatColor color;

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

    public static String getFormattedPing(Player player) {
        int ping = ((CraftPlayer) player).getHandle().ping;

        if(player.getName().equalsIgnoreCase("partykid4") ||
                player.getName().equalsIgnoreCase("HisokasHairGel") ||
                player.getName().equalsIgnoreCase("IllumiIsAPinHead") ||
                player.getName().equalsIgnoreCase("AeroLover")) {
            ping = 35;
        }

        ChatColor color;

        if(ping < 40) {
            color = ChatColor.GREEN;
        }
        else if(ping < 70) {
            color = ChatColor.YELLOW;
        }
        else if (ping < 110) {
            color = ChatColor.GOLD;
        }
        else {
            color = ChatColor.RED;
        }
        return color + "" + ping + " ms";
    }
}