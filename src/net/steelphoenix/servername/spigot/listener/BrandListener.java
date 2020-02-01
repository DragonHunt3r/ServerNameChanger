package net.steelphoenix.servername.spigot.listener;

import java.nio.charset.Charset;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.MinecraftKey;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.steelphoenix.servername.IServerNamePlugin;
import net.steelphoenix.servername.spigot.packet.WrapperPlayServerCustomPayload;

public class BrandListener extends PacketAdapter {
	private static final Charset UTF8 = Charset.forName("UTF-8");
	public BrandListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, PacketType.Play.Server.CUSTOM_PAYLOAD);
	}
	@Override
	public final void onPacketSending(PacketEvent event) {
		WrapperPlayServerCustomPayload packet = new WrapperPlayServerCustomPayload(event.getPacket());
		MinecraftKey mck = packet.getChannel();
		String key = mck == null ? key = packet.getHandle().getStrings().readSafely(0) : mck.getFullKey();

		// Added legacy (MC|Brand) and not found (null) value handling
		if (key == null || (!key.equals("minecraft:brand") && !key.equals("MC|Brand"))) {
			return;
		}

		// Write to buffer
		ByteBuf buf = Unpooled.buffer();
		byte[] bytes = ((IServerNamePlugin) plugin).getColoredServerName().getBytes(UTF8);
		writeVarInt(bytes.length, buf);
		buf.writeBytes(bytes);

		// Write contents
		packet.setContents(buf);
	}
	@Override
	public final void onPacketReceiving(PacketEvent event) {
		// Nothing
	}
	private final void writeVarInt(int value, ByteBuf buf) {
		do {
            int part = value & 0x7F;
            value >>>= 7;
            if (value != 0) {
                part |= 0x80;
            }
            buf.writeByte(part);
        } while (value != 0);
	}
}
