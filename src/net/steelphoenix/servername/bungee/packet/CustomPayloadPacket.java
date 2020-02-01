package net.steelphoenix.servername.bungee.packet;

import io.netty.buffer.ByteBuf;

public class CustomPayloadPacket {
	private String channel;
	private ByteBuf buf;
	public CustomPayloadPacket(String channel, ByteBuf buf) {
		if (channel == null) {
			throw new IllegalArgumentException("Channel cannot be null");
		}
		if (buf == null) {
			throw new IllegalArgumentException("Contents cannot be null");
		}
		this.channel = channel;
		this.buf = buf;
	}
	public final String getChannel() {
		return channel;
	}
	public final void setChannel(String value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		this.channel = value;
	}
	public final ByteBuf getContents() {
		return buf;
	}
	public final void setContents(ByteBuf value) {
		if (value == null) {
			throw new IllegalArgumentException("Value cannot be null");
		}
		this.buf = value;
	}
}
