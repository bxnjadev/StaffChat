package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.event.StaffChatReceiveEvent;
import net.ibxnjadev.staffchat.event.StaffChatSendMessageEvent;
import net.ibxnjadev.staffchat.helper.Configuration;
import net.ibxnjadev.staffchat.helper.TextComponentBuilder;
import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import net.ibxnjadev.staffchat.redis.RedisClientWrapper;
import net.ibxnjadev.staffchat.translator.TranslatorProvider;
import net.ibxnjadev.vmessenger.universal.Messenger;
import net.md_5.bungee.api.chat.ClickEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public class DefaultStaffChatHandler implements StaffChatHandler {

    private static final String PERMISSION_RECEIVE_NAME = "staff.chat.receive";
    private static final String PIN_UP_STAFF_CHAT_PREFIX = "PIN_UP_STAFF_CHAT:";
    private static final String VISIBILITY_CHAT_PREFIX = "VISIBILITY_CHAT:";

    private final Messenger messenger;
    private final RedisClientWrapper redisClientWrapper;
    private final JedisPool jedisPool;
    private final TranslatorProvider translatorProvider;

    private final Configuration messages;
    private final Configuration configuration;

    public DefaultStaffChatHandler(Messenger messenger,
                                   RedisClientWrapper redisClientWrapper,
                                   TranslatorProvider translatorProvider,
                                   Configuration messages,
                                   Configuration configuration) {
        this.messenger = messenger;
        this.redisClientWrapper = redisClientWrapper;
        this.jedisPool = redisClientWrapper.getClient();
        this.translatorProvider = translatorProvider;
        this.messages = messages;
        this.configuration = configuration;
    }

    @Override
    public void write(String sender, String message) {

        StaffChatSendMessageEvent event = new StaffChatSendMessageEvent(sender, message);
        Bukkit.getPluginManager().callEvent(event);

        StaffChatMessage staffChatMessage = new StaffChatMessage(event.getSender(), event.getMessage());

        messenger.sendMessage(staffChatMessage);
    }

    @Override
    public void executeMessage(StaffChatMessage staffChatMessage) {

        StaffChatReceiveEvent staffChatReceiveEvent =
                new StaffChatReceiveEvent(getPlayersWithPermission(), staffChatMessage.getPlayer(), staffChatMessage.getMessage());

        Bukkit.getPluginManager().callEvent(staffChatReceiveEvent);

        for (Player player : staffChatReceiveEvent.getPlayers()) {
            if (hasCanChat(player)) {

                player.spigot().sendMessage(
                        TextComponentBuilder
                                .builder(
                                        translatorProvider.provide(player, staffChatMessage.getPlayer(), staffChatMessage.getMessage())
                                )
                                .hover(messages.getString("format-hover"))
                                .clickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/sc ")
                                .build()
                );

                if (messages.getBoolean("notification-sound.enable")) {
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

    @Override
    public void pinUpChat(Player player) {

        String uuidString = player.getUniqueId().toString();

        redisClientWrapper.execute(jedis -> {
            if (!jedis.exists(PIN_UP_STAFF_CHAT_PREFIX + uuidString)) {
                jedis.set(PIN_UP_STAFF_CHAT_PREFIX + uuidString, "true");
                player.sendMessage(messages.getString("pinned"));
            } else {
                jedis.del(PIN_UP_STAFF_CHAT_PREFIX + uuidString);
                player.sendMessage(messages.getString("un-pinned"));
            }
        });

    }

    @Override
    public boolean hasPinUpChat(Player player) {
        String uuidString = player.getUniqueId().toString();

        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(PIN_UP_STAFF_CHAT_PREFIX + uuidString);
        }

    }

    @Override
    public void executeVisibilityChat(Player player) {
        String uuidString = player.getUniqueId().toString();

        redisClientWrapper.execute(jedis -> {
            if (jedis.exists(VISIBILITY_CHAT_PREFIX + uuidString)) {
                jedis.del(VISIBILITY_CHAT_PREFIX + uuidString);
                player.sendMessage(configuration.getString("visibility-disable"));
            } else {
                jedis.set(VISIBILITY_CHAT_PREFIX + uuidString, "true");
                player.sendMessage(configuration.getString("visibility-disable"));
            }

        });
    }

    @Override
    public boolean hasCanChat(Player player) {
        String uuidString = player.getUniqueId().toString();

        try (Jedis jedis = jedisPool.getResource()) {
            return !jedis.exists(VISIBILITY_CHAT_PREFIX + uuidString);
        }

    }

    private List<Player> getPlayersWithPermission() {
        List<Player> players = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PERMISSION_RECEIVE_NAME)) {
                players.add(player);
            }
        }

        return players;
    }

}
