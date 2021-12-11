package net.ibxnjadev.staffchat;

import net.ibxnjadev.staffchat.helper.Configuration;
import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import net.ibxnjadev.staffchat.redis.RedisClientWrapper;
import net.ibxnjadev.staffchat.translator.TranslatorProvider;
import net.ibxnjadev.vmessenger.universal.Messenger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

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

        StaffChatMessage staffChatMessage = new StaffChatMessage(
                sender,
                message
        );

        messenger.sendMessage(CHANNEL_NAME, staffChatMessage);
    }

    @Override
    public void executeMessage(StaffChatMessage staffChatMessage) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.hasPermission(PERMISSION_RECEIVE_NAME)) {
                if (hasCanChat(player)) {
                    player.sendMessage(
                            translatorProvider.provide(player, staffChatMessage.getPlayer(), staffChatMessage.getMessage())
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

}
