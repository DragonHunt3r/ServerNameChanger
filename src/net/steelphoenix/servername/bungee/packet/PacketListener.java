package net.steelphoenix.servername.bungee.packet;

import java.lang.reflect.Field;

import io.netty.buffer.Unpooled;
import net.md_5.bungee.ServerConnection;
import net.md_5.bungee.UserConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.netty.ChannelWrapper;
import net.md_5.bungee.netty.PipelineUtils;
import net.md_5.bungee.protocol.packet.PluginMessage;
import net.steelphoenix.servername.bungee.packet.PacketEvent.PacketSource;

public class PacketListener implements Listener {
	private static final String IDENTIFIER = "namechanger_packet_intercepter";
	private final Plugin plugin;
	private final Field field;
	public PacketListener(Plugin plugin) throws ReflectiveOperationException {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;

		field = UserConnection.class.getDeclaredField("ch");
		field.setAccessible(true);
	}
	@EventHandler
	public final void onPlayerConnect(PostLoginEvent event) {
		ProxiedPlayer player = event.getPlayer();

		// Fetch the channel
		ChannelWrapper channel;
		try {
			channel = (ChannelWrapper) field.get(player);
		} catch (ReflectiveOperationException exception) {
			throw new RuntimeException("Could not fetch player channel for " + player.getUniqueId(), exception);
		}

		channel.getHandle().pipeline().addBefore(PipelineUtils.BOSS_HANDLER, IDENTIFIER, new ChannelHandler(plugin, true));
	}
	@EventHandler
	public final void onServerConnect(ServerConnectedEvent event) {
		ChannelWrapper channel = ((ServerConnection) event.getServer()).getCh();
		channel.getHandle().pipeline().addBefore(PipelineUtils.BOSS_HANDLER, IDENTIFIER, new ChannelHandler(plugin, false));
	}
	@EventHandler
	public final void onPacket(PacketEvent event) {
		if (!(event.getPacket() instanceof PluginMessage)) {
			return;
		}
		if (event.getSource() != PacketSource.PROXY || event.getTarget() != PacketSource.CLIENT) {
			return;
		}

		PluginMessage packet = (PluginMessage) event.getPacket();

		// Call the event
		CustomPayloadPacket payload = new CustomPayloadPacket(packet.getTag(), Unpooled.wrappedBuffer(packet.getData()));
		plugin.getProxy().getPluginManager().callEvent(new PayloadPacketEvent(payload));

		// Set the new data
		packet.setData(payload.getContents().array());
	}
}