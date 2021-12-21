package net.ibxnjadev.staffchat.commands;

import net.ibxnjadev.staffchat.StaffChatHandler;
import net.ibxnjadev.staffchat.helper.Configuration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class StaffChatCommand implements CommandExecutor {

    private static final String PERMISSION_COMMAND = "staffchat.use";
    private static final String PERMISSION_RELOAD_COMMAND = "staffchat.reload";
    private static final String PERMISSION_VISIBILITY_COMMAND = "staffchat.visibility";

    private final StaffChatHandler staffChatHandler;
    private final Configuration configuration;
    private final Configuration messages;

    public StaffChatCommand(StaffChatHandler staffChatHandler, Configuration configuration, Configuration messages) {
        this.staffChatHandler = staffChatHandler;
        this.configuration = configuration;
        this.messages = messages;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (hasPermission(sender, PERMISSION_COMMAND)) {

            if (args.length == 0) {

                if (sender instanceof Player) {
                    staffChatHandler.pinUpChat((Player) sender);
                    return true;
                }

                sender.sendMessage(messages.getString("help"));
                return true;
            }

            if (args.length == 1) {
                switch (args[0].toLowerCase(Locale.ROOT)) {
                    case "visibility":
                        if (sender instanceof Player) {
                            if (hasPermission(sender, PERMISSION_VISIBILITY_COMMAND)) {
                                staffChatHandler.executeVisibilityChat((Player) sender);
                            }
                            break;
                        }
                    case "reload":
                        if (hasPermission(sender, PERMISSION_RELOAD_COMMAND)) {
                            configuration.reload();
                            sender.sendMessage(messages.getString("reload"));
                        }
                        break;
                }

                StringBuilder stringBuilder = new StringBuilder();

                for (String s : args) {
                    stringBuilder.append(s).append(" ");
                }

                staffChatHandler.write(sender.getName(), stringBuilder.toString());
            }
        }
        return true;
    }

    public boolean hasPermission(CommandSender sender, String permission) {
        if (sender.hasPermission(permission)) {
            return true;
        }
        sender.sendMessage(messages.getString("no-permission"));
        return false;
    }

}
