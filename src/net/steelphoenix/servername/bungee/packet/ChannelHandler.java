package net.steelphoenix.servername.bungee.packet;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Plugin;
import net.steelphoenix.servername.bungee.packet.PacketEvent.PacketSource;

public class ChannelHandler extends ChannelDuplexHandler {
	private final Plugin plugin;
	private final boolean playerChannel;
	public ChannelHandler(Plugin plugin, boolean playerChannel) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
		this.playerChannel = playerChannel;
	}
	@Override
	public final void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// Call the event
		Cancellable event = plugin.getProxy().getPluginManager().callEvent(new PacketEvent(playerChannel ? PacketSource.CLIENT : PacketSource.SERVER, PacketSource.PROXY, msg));

		// Pass read to next handler
		if (!event.isCancelled()) {
			super.channelRead(ctx, msg);
		}
	}
	@Override
	public final void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		// Call the event
		Cancellable event = plugin.getProxy().getPluginManager().callEvent(new PacketEvent(PacketSource.PROXY, playerChannel ? PacketSource.CLIENT : PacketSource.SERVER, msg));

		// Pass write to next handler
		if (!event.isCancelled()) {
			super.write(ctx, msg, promise);
		}
	}
}