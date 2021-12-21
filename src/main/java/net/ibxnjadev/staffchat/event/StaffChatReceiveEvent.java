package net.ibxnjadev.staffchat.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;

public class StaffChatReceiveEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final Collection<Player> players;
    private final String sender;
    private final String message;

    public StaffChatReceiveEvent(Collection<Player> players, String sender, String message) {
        this.players = players;
        this.sender = sender;
        this.message = message;
    }

    public void addPlayers(Player... playersArray) {
        players.addAll(Arrays.asList(playersArray));
    }

    public void removePlayersIf(Predicate<Player> filter) {
        players.removeIf(filter);
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
