package net.steelphoenix.servername.bungee.listener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.steelphoenix.servername.bungee.IBungeeServerNamePlugin;
import net.steelphoenix.servername.bungee.packet.CustomPayloadPacket;
import net.steelphoenix.servername.bungee.packet.PayloadPacketEvent;

public class BrandListener implements Listener {
	private final IBungeeServerNamePlugin plugin;
	public BrandListener(IBungeeServerNamePlugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}
	@EventHandler
	public final void onPayload(PayloadPacketEvent event) {
		CustomPayloadPacket packet = event.getPacket();
		String key = packet.getChannel();

		// Added legacy (MC|Brand) and not found (null) value handling
		if (key == null || (!key.equals("minecraft:brand") && !key.equals("MC|Brand"))) {
			return;
		}

		String oldName = DefinedPacket.readString(packet.getContents());

		// Format: BungeeCord (version) <- <Spigot server name>
		String[] split = oldName.split(" <- ");

		// Get the new name
		String newName = split.length == 2 ? plugin.getColoredConnectedServerName(split[1]) : plugin.getColoredServerName();

		// Write the modified value
		ByteBuf buf = Unpooled.buffer();
		DefinedPacket.writeString(newName, buf);
		packet.setContents(buf);
	}
}
