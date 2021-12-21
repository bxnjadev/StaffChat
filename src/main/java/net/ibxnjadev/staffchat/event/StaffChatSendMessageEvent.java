package net.ibxnjadev.staffchat.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class StaffChatSendMessageEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private String sender;
    private String message;

    public StaffChatSendMessageEvent(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
