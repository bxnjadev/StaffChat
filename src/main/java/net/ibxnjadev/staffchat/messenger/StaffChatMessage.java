package net.ibxnjadev.staffchat.messenger;

import java.beans.ConstructorProperties;

public class StaffChatMessage {

    private final String player;
    private final String message;

    @ConstructorProperties({
            "player", "message"
    })
    public StaffChatMessage(String player, String message) {
        this.player = player;
        this.message = message;
    }

    public String getPlayer() {
        return player;
    }

    public String getMessage() {
        return message;
    }

}
