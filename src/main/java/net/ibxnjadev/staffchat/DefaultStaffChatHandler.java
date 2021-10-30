package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.helper.Configuration;
import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import net.ibxnjadev.staffchat.translator.TranslatorProvider;
import net.ibxnjadev.vmesseger.universal.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class DefaultStaffChatHandler implements StaffChatHandler {

    private static final String PERMISSION_RECEIVE_NAME = "staff.chat.receive";

    private final Messenger messenger;
    private final TranslatorProvider translatorProvider;

    private final Configuration configuration;

    public DefaultStaffChatHandler(Messenger messenger, TranslatorProvider translatorProvider, Configuration configuration) {
        this.messenger = messenger;
        this.translatorProvider = translatorProvider;
        this.configuration = configuration;
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

                if (configuration.getBoolean("notification-sound.enable")) {
                    Sound sound = Sound.valueOf(configuration.getString("notification-sound.sound"));

                    player.playSound(
                            player.getLocation(),
                            sound,
                            5L,
                            5L
                    );
                }

            }
        }
    }
}
