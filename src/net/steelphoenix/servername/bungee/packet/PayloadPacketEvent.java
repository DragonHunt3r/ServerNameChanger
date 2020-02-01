package net.steelphoenix.servername.bungee.packet;

import net.md_5.bungee.api.plugin.Event;

public class PayloadPacketEvent extends Event {
	private final CustomPayloadPacket packet;
	public PayloadPacketEvent(CustomPayloadPacket packet) {
		if (packet == null) {
			throw new IllegalArgumentException("Packet cannot be null");
		}
		this.packet = packet;
	}
	public final CustomPayloadPacket getPacket() {
		return packet;
	}
}
