package net.ibxnjadev.staffchat;

import me.fixeddev.commandflow.CommandManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilderImpl;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.fixeddev.commandflow.command.Command;
import net.ibxnjadev.staffchat.commands.StaffChatCommand;
import net.ibxnjadev.staffchat.helper.Configuration;
import net.ibxnjadev.staffchat.messenger.StaffChatMessage;
import net.ibxnjadev.staffchat.messenger.StaffChatMessageInterceptor;
import net.ibxnjadev.staffchat.redis.RedisClientWrapper;
import net.ibxnjadev.staffchat.translator.DefaultTranslatorProvider;
import net.ibxnjadev.staffchat.translator.PlaceholderAPITranslatorProvider;
import net.ibxnjadev.staffchat.translator.TranslatorProvider;
import net.ibxnjadev.vmesseger.universal.Messenger;
import net.ibxnjadev.vmesseger.universal.serialize.ObjectJacksonAdapter;
import net.ibxnjadev.vmessenger.redis.RedisMessenger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.List;

public final class StaffChat extends JavaPlugin {

    private static final String CHANNEL_NAME = "staff_chat_channel";
    private JedisPool jedisPool = null;

    @Override
    public void onEnable() {

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

        Messenger messenger = new RedisMessenger(CHANNEL_NAME, redisClientWrapper.getClient(), new ObjectJacksonAdapter());
        StaffChatHandler staffChatHandler = new DefaultStaffChatHandler(messenger, translatorProvider, configuration);

        messenger.intercept(StaffChatHandler.CHANNEL_NAME, StaffChatMessage.class, new StaffChatMessageInterceptor(staffChatHandler));

        PartInjector partInjector = PartInjector.create();
        partInjector.install(new BukkitModule());
        partInjector.install(new DefaultsModule());

        AnnotatedCommandTreeBuilder annotatedCommandTreeBuilder = new AnnotatedCommandTreeBuilderImpl(partInjector);
        CommandManager commandManager = new BukkitCommandManager(getName());

        List<Command> commands = new ArrayList<>(annotatedCommandTreeBuilder.fromClass(new StaffChatCommand(staffChatHandler, messages)));
        commandManager.registerCommands(commands);

    }

    @Override
    public void onDisable() {
        jedisPool.close();
    }
}
