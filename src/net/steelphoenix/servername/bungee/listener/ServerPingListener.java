package net.steelphoenix.servername.bungee.listener;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.steelphoenix.servername.IServerNamePlugin;

public class ServerPingListener implements Listener {
	private final IServerNamePlugin plugin;
	public ServerPingListener(IServerNamePlugin plugin) {
		if (plugin == null) {
			throw new IllegalArgumentException("Plugin cannot be null");
		}
		this.plugin = plugin;
	}
	@EventHandler
	public final void onPing(ProxyPingEvent event) {
		ServerPing ping = event.getResponse();
		Protocol protocol = ping.getVersion();

		// Set the values
		if (plugin.isUseUnsupportedProtocol()) {
			protocol.setProtocol(999);
		}
		protocol.setName(plugin.getUncoloredServerName());
	}
}