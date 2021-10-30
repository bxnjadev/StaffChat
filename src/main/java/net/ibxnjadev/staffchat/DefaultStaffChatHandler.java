package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import net.ibxnjadev.staffchat.translator.TranslatorProvider;
import net.ibxnjadev.vmesseger.universal.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class DefaultStaffChatHandler implements StaffChatHandler {

    private static final String PERMISSION_RECEIVE_NAME = "staff.chat.receive";

    private final Messenger messenger;
    private final TranslatorProvider translatorProvider;

    public DefaultStaffChatHandler(Messenger messenger, TranslatorProvider translatorProvider) {
        this.messenger = messenger;
        this.translatorProvider = translatorProvider;
    }

    @Override
    public void write(Player sender, String message) {

        StaffChatMessage staffChatMessage = new StaffChatMessage(
                sender.toString(),
                message
        );

        messenger.sendMessage(CHANNEL_NAME, staffChatMessage);
        executeMessage(staffChatMessage);
    }

    @Override
    public void executeMessage(StaffChatMessage staffChatMessage) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PERMISSION_RECEIVE_NAME)) {
                player.sendMessage(
                        translatorProvider.provide(player, staffChatMessage.getMessage())
                );
            }
        }
    }
}
