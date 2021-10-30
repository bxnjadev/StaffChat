package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import org.bukkit.entity.Player;

public interface StaffChatHandler {

    String CHANNEL_NAME = "staff_chat";

    void write(Player sender, String message);

    void executeMessage(StaffChatMessage staffChatMessage);

}
