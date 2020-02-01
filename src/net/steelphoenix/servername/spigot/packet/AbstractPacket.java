package net.steelphoenix.servername.spigot.packet;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class AbstractPacket {
	private final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
	protected final PacketContainer handle;
	protected AbstractPacket(PacketContainer handle, PacketType type) {
		if (handle == null) {
			throw new IllegalArgumentException("Handle cannot be null");
		}
		if (handle.getType() != type) {
			throw new IllegalArgumentException(handle.getHandle() + " is not a packet of type " + type);
		}
		this.handle = handle;
	}
	public PacketContainer getHandle() {
		return handle;
	}
	public final void sendPacket(Player receiver) throws InvocationTargetException {
		if (receiver == null) {
			throw new IllegalArgumentException("Player cannot be null");
		}
		manager.sendServerPacket(receiver, handle);
	}
}
