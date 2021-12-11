package net.ibxnjadev.staffchat.listener;

import net.ibxnjadev.staffchat.StaffChatHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatListener implements Listener {

    private final StaffChatHandler staffChatHandler;

    public PlayerChatListener(StaffChatHandler staffChatHandler) {
        this.staffChatHandler = staffChatHandler;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if (staffChatHandler.hasPinUpChat(player)) {
            event.setCancelled(true);
            staffChatHandler.write(player.getName(),  event.getMessage());
        }

    }

}
