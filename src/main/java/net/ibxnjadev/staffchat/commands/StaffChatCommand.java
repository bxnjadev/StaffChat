package net.ibxnjadev.staffchat.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.Messenger;

public class StaffChatCommand implements CommandClass {

    private final Messenger messenger;

    public StaffChatCommand(Messenger messenger) {
        this.messenger = messenger;
    }

    @Command(names = "")
    public void main(@Sender Player player) {

    }

    public void staffChat(@Sender Player player, @Text String message) {

    }

}
