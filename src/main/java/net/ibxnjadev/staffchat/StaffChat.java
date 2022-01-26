package net.ibxnjadev.staffchat;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.ibxnjadev.staffchat.commands.StaffChatCommand;
import net.ibxnjadev.staffchat.helper.Configuration;
import net.ibxnjadev.staffchat.listener.PlayerChatListener;
import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import net.ibxnjadev.staffchat.messenger.StaffChatMessageInterceptor;
import net.ibxnjadev.staffchat.redis.RedisClientWrapper;
import net.ibxnjadev.staffchat.translator.DefaultTranslatorProvider;
import net.ibxnjadev.staffchat.translator.PlaceholderAPITranslatorProvider;
import net.ibxnjadev.staffchat.translator.TranslatorProvider;
import net.ibxnjadev.vmessenger.redis.RedisMessenger;
import net.ibxnjadev.vmessenger.universal.Messenger;
import net.ibxnjadev.vmessenger.universal.serialize.ObjectJacksonAdapter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

public final class StaffChat extends JavaPlugin {

    private static final String CHANNEL_NAME = "staff_chat_channel";
    private JedisPool jedisPool = null;

    @Override
    public void onEnable() {

        ObjectMapper mapper = new ObjectMapper();

        Configuration messages = new Configuration(this, "messages");
        Configuration configuration = new Configuration(this, "config");

        RedisClientWrapper redisClientWrapper = new RedisClientWrapper(
                configuration.getString("redis.host"),
                configuration.getString("redis.password"),
                configuration.getInt("redis.port")
        );

        redisClientWrapper.establishConnection();
        jedisPool = redisClientWrapper.getClient();

        TranslatorProvider translatorProvider = new DefaultTranslatorProvider(messages);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            translatorProvider = new PlaceholderAPITranslatorProvider(messages);
        }

        Messenger messenger = new RedisMessenger(CHANNEL_NAME, redisClientWrapper.getClient(), redisClientWrapper.getJedis(), new ObjectJacksonAdapter(), mapper);
        StaffChatHandler staffChatHandler = new DefaultStaffChatHandler(messenger, redisClientWrapper, translatorProvider, messages, configuration);

        messenger.intercept(StaffChatHandler.CHANNEL_NAME, StaffChatMessage.class, new StaffChatMessageInterceptor(staffChatHandler));
        getCommand("staffchat").setExecutor(new StaffChatCommand(staffChatHandler, configuration, messages));
        Bukkit.getPluginManager().registerEvents(new PlayerChatListener(staffChatHandler), this);

    }

    @Override
    public void onDisable() {
        jedisPool.close();
    }
}
