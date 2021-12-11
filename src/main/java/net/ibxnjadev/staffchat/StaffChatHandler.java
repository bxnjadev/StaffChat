package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import org.bukkit.entity.Player;

public interface StaffChatHandler {

    String CHANNEL_NAME = "staff_chat";

    void write(String sender, String message);

    void executeMessage(StaffChatMessage staffChatMessage);

    void pinUpChat(Player player);

    boolean hasPinUpChat(Player player);

    void executeVisibilityChat(Player player);

    boolean hasCanChat(Player player);

}
