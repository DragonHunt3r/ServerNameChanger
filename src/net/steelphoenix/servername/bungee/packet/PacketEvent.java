package net.steelphoenix.servername.bungee.packet;

import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class PacketEvent extends Event implements Cancellable {
	public enum PacketSource {
		CLIENT,
		PROXY,
		SERVER;
	}
	private final PacketSource from;
	private final PacketSource to;
	private final Object packet;
	private boolean cancel = false;
	public PacketEvent(PacketSource from, PacketSource to, Object packet) {
		if (from == null) {
			throw new IllegalArgumentException("Source cannot be null");
		}
		if (to == null) {
			throw new IllegalArgumentException("Target cannot be null");
		}
		if (packet == null) {
			throw new IllegalArgumentException("Packet cannot be null");
		}
		this.from = from;
		this.to = to;
		this.packet = packet;
	}
	public final PacketSource getSource() {
		return from;
	}
	public final PacketSource getTarget() {
		return to;
	}
	public final Object getPacket() {
		return packet;
	}
	@Override
	public final boolean isCancelled() {
		return cancel;
	}
	@Override
	public final void setCancelled(boolean cancel) {
		this.cancel = cancel;
	}
}
