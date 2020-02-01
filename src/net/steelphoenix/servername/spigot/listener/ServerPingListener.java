package net.steelphoenix.servername.spigot.listener;

import org.bukkit.plugin.Plugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;

import net.steelphoenix.servername.IServerNamePlugin;
import net.steelphoenix.servername.spigot.packet.WrapperStatusServerServerInfo;

public class ServerPingListener extends PacketAdapter {
	public ServerPingListener(Plugin plugin) {
		super(plugin, ListenerPriority.NORMAL, PacketType.Status.Server.SERVER_INFO);
	}
	@Override
	public final void onPacketSending(PacketEvent event) {
		WrapperStatusServerServerInfo packet = new WrapperStatusServerServerInfo(event.getPacket());
		WrappedServerPing ping = packet.getJSONResponse();

		// Set the values
		if (((IServerNamePlugin) plugin).isUseUnsupportedProtocol()) {
			ping.setVersionProtocol(999);
		}
		ping.setVersionName(((IServerNamePlugin) plugin).getUncoloredServerName());
	}
	@Override
	public final void onPacketReceiving(PacketEvent event) {
		// Nothing
	}
}