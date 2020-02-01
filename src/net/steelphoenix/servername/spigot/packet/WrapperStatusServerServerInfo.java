package net.steelphoenix.servername.spigot.packet;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedServerPing;

public class WrapperStatusServerServerInfo extends AbstractPacket {
	public static final PacketType TYPE = PacketType.Status.Server.SERVER_INFO;
	public WrapperStatusServerServerInfo() {
		super(new PacketContainer(TYPE), TYPE);
		handle.getModifier().writeDefaults();
	}
	public WrapperStatusServerServerInfo(PacketContainer packet) {
		super(packet, TYPE);
	}
	public final WrappedServerPing getJSONResponse() {
		return handle.getServerPings().readSafely(0);
	}
	public final void setJSONResponse(WrappedServerPing value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		handle.getServerPings().write(0, value);
	}
}
