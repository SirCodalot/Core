package me.codalot.core.player;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import org.bukkit.craftbukkit.v1_14_R1.entity.CraftPlayer;

public class Injector extends ChannelDuplexHandler {

    private CPlayer player;

    Injector(CPlayer player) {
        this.player = player;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        // TODO call event

        super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {

        // TODO call event

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
        return ((CraftPlayer) player.getPlayer()).getHandle().playerConnection.networkManager.channel.pipeline();
    }
}
