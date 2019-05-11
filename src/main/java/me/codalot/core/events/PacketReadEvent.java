package me.codalot.core.events;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PacketReadEvent extends PacketEvent {

    private static final HandlerList HANDLERS_LIST = new HandlerList();

    public PacketReadEvent(Player who, Object packet) {
        super(who, packet);
    }

    @Override
    @SuppressWarnings("all")
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }

    @SuppressWarnings("unused")
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
}
