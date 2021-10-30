package net.ibxnjadev.staffchat.translator;

import net.ibxnjadev.staffchat.helper.Configuration;
import org.bukkit.entity.Player;

public class DefaultTranslatorProvider implements TranslatorProvider {

    private final Configuration configuration;

    public DefaultTranslatorProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String provide(Player player, String message) {
        return configuration.getString(
                        PATH
                ).replace("%player%", player.getName())
                .replace("%message%", message);
    }

}
