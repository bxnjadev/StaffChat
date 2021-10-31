package net.ibxnjadev.staffchat.translator;

import me.clip.placeholderapi.PlaceholderAPI;
import net.ibxnjadev.staffchat.helper.Configuration;
import org.bukkit.entity.Player;

public class PlaceholderAPITranslatorProvider implements TranslatorProvider {

    private final Configuration configuration;

    public PlaceholderAPITranslatorProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String provide(Player player, String sender,  String message) {
        return PlaceholderAPI.setPlaceholders(player,
                configuration.getString(
                        PATH
                ).replace(
                        "%player%", sender
                ).replace("%message%", message));
    }

}
