package me.codalot.core.player;

import io.netty.channel.*;
import me.codalot.core.events.PacketReadEvent;
import me.codalot.core.events.PacketWriteEvent;

import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("all")
public class Injector extends ChannelDuplexHandler {

    private CPlayer player;

    Injector(CPlayer player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        PacketReadEvent event = new PacketReadEvent(player.getPlayer(), msg);
//        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        PacketWriteEvent event = new PacketWriteEvent(player.getPlayer(), msg);
//        Bukkit.getPluginManager().callEvent(event);

        if (event.isCancelled())
            return;

        super.write(ctx, msg, promise);
    }

    public void inject() {
        getPipeline().addBefore("packet_handler", player.getUuid().toString(), this);
    }

    public void eject() {
        try {
            getPipeline().remove(this);
        } catch (Exception ignored) {}
    }

    private ChannelPipeline getPipeline() {

        try {
            Object handle = player.getPlayer().getClass().getSuperclass().getDeclaredMethod("getHandle").invoke(player.getPlayer());
            Object connection = handle.getClass().getDeclaredField("playerConnection").get(handle);
            Object network = connection.getClass().getDeclaredField("networkManager").get(connection);
            Channel channel = (Channel) network.getClass().getDeclaredField("channel").get(network);
            return channel.pipeline();
        } catch (NoSuchFieldException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
