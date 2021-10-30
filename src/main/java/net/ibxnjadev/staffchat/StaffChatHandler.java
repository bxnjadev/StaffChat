package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.messenger.StaffChatMessage;

public interface StaffChatHandler {

    String CHANNEL_NAME = "staff_chat";

    void write(String sender, String message);

    void executeMessage(StaffChatMessage staffChatMessage);

}
