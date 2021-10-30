package net.ibxnjadev.staffchat.translator;

import org.bukkit.entity.Player;

public interface TranslatorProvider {

    String PATH = "format";

    String provide(Player player, String message);

}
