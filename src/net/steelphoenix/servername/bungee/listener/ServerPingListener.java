package net.steelphoenix.servername.bungee.listener;

import java.util.function.Supplier;
import java.util.logging.Level;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.ServerPing.Protocol;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.steelphoenix.servername.IServerNamePlugin;

public class ServerPingListener implements Listener {
	private final IServerNamePlugin plugin;
	private final Supplier<Protocol> fallback = new FallbackProtocolSupplier();
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

		// If protocol is null
		if (protocol == null) {

			// Log the null protocol
			((Plugin) plugin).getLogger().log(Level.WARNING, "Server ping did not include protocol version, attempting to inject fallback protocol");

			// Use the fallback protocol supplier
			protocol = fallback.get();
			ping.setVersion(protocol);
		}

		// Set the values
		if (plugin.isUseUnsupportedProtocol()) {
			protocol.setProtocol(999);
		}
		protocol.setName(plugin.getUncoloredServerName());
	}
	private class FallbackProtocolSupplier implements Supplier<Protocol> {
		private static final String NAME = "BungeeCord 1.8.x-1.15.x";
		private static final int VERSION = 480;
		@Override
		public final Protocol get() {
			return new Protocol(NAME, VERSION);
		}
	}
}