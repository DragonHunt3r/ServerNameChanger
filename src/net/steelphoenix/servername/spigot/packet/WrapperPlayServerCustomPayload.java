package net.steelphoenix.servername.spigot.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.MinecraftKey;

import io.netty.buffer.ByteBuf;

public class WrapperPlayServerCustomPayload extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Play.Server.CUSTOM_PAYLOAD;
	public WrapperPlayServerCustomPayload() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}
	public WrapperPlayServerCustomPayload(PacketContainer packet) {
		super(packet, TYPE);
	}
	public final MinecraftKey getChannel() {
		return handle.getMinecraftKeys().readSafely(0);
	}
	public final void setChannel(MinecraftKey value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		handle.getMinecraftKeys().writeSafely(0, value);
	}
	public final ByteBuf getContents() {
		return (ByteBuf) handle.getModifier().withType(ByteBuf.class).readSafely(0);
	}
	public final void setContents(ByteBuf value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		if (MinecraftReflection.is(MinecraftReflection.getPacketDataSerializerClass(), value)) {
			handle.getModifier().withType(ByteBuf.class).writeSafely(0, value);
			return;
		}
		Object serializer = MinecraftReflection.getPacketDataSerializer(value);
		handle.getModifier().withType(ByteBuf.class).write(0, serializer);
	}
}