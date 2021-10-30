package net.ibxnjadev.staffchat.commands;

import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.ibxnjadev.staffchat.StaffChatHandler;
import net.ibxnjadev.staffchat.helper.Configuration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(names = {"staffchat","sc"}, permission = "staff.chat.write")
public class StaffChatCommand implements CommandClass {

    private final StaffChatHandler staffChatHandler;
    private final Configuration configuration;

    public StaffChatCommand(StaffChatHandler staffChatHandler, Configuration configuration) {
        this.staffChatHandler = staffChatHandler;
        this.configuration = configuration;
    }

    @Command(names = "")
    public void main(@Sender CommandSender sender) {
        configuration.getColoredList("help").forEach(sender::sendMessage);
    }

    public void staffChat(@Sender CommandSender sender, @Text String message) {
        staffChatHandler.write(sender.getName(), message);
    }

}
