package net.ibxnjadev.staffchat.helper;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class TextComponentBuilder {

    private final TextComponent textComponent;

    public TextComponentBuilder(String component) {
        textComponent = new TextComponent(component);
    }

    public TextComponentBuilder hover(String hover) {
        textComponent.setHoverEvent(
                new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(hover).create()
                )
        );
        return this;
    }

    public TextComponentBuilder clickEvent(ClickEvent.Action action, String value) {
        textComponent.setClickEvent(
                new ClickEvent(action, value)
        );
        return this;
    }

    public TextComponentBuilder color(ChatColor color) {
        textComponent.setColor(color);
        return this;
    }

    public TextComponent build() {
        return textComponent;
    }

    public static TextComponentBuilder builder(String component) {
        return new TextComponentBuilder(component);
    }

}
