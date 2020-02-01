package net.steelphoenix.servername.bungee;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import net.steelphoenix.servername.bungee.listener.BrandListener;
import net.steelphoenix.servername.bungee.listener.ServerPingListener;
import net.steelphoenix.servername.bungee.packet.PacketListener;

public class ServerNamePlugin extends Plugin implements IBungeeServerNamePlugin {
	private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
	private Configuration config;
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onEnable() {
		// Config
		File dir = getDataFolder();
		if (!dir.exists()) {
			dir.mkdir();
		}
		File cfg = new File(dir, "config.yml");
		if (!cfg.exists()) {
			try (InputStream in = getResourceAsStream("config.yml"); OutputStream out = new FileOutputStream(cfg)) {
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
			} catch (IOException exception) {
				getLogger().log(Level.SEVERE, "Could not save default configuration", exception);
				return;
			}
		}
		try {
			config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(cfg);
		} catch (IOException exception) {
			getLogger().log(Level.SEVERE, "Could not load configuration", exception);
			return;
		}

		PluginManager manager = getProxy().getPluginManager();

		// Packet intercepting
		if (isInjectProxyPipeline()) {
			Listener listener = null;
			try {
				listener = new PacketListener(this);
			} catch (ReflectiveOperationException exception) {
				getLogger().log(Level.SEVERE, "Could not fetch channel field for UserConnection", exception);
			}
			if (listener != null) {
				manager.registerListener(this, listener);
			}
		}


		// Register listeners
		manager.registerListener(this, new ServerPingListener(this));
		manager.registerListener(this, new BrandListener(this));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onDisable() {

		// Unregister listener
		getProxy().getPluginManager().unregisterListeners(this);
	}
	@Override
	public final boolean isUseUnsupportedProtocol() {
		return config.getBoolean("unsupported-protocol");
	}
	@Override
	public final String getServerName() {
		return config.getString("server-name", "BungeeCord");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getColoredServerName() {
		return ChatColor.translateAlternateColorCodes('&', truncate(getServerName(), Short.MAX_VALUE - 2) + "&r");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getUncoloredServerName() {
		return truncate(STRIP_COLOR_PATTERN.matcher(getServerName()).replaceAll(""), Short.MAX_VALUE);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isInjectProxyPipeline() {
		return config.getBoolean("pipeline-injection");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getConnectedServerName() {
		return config.getString("connected-server-name", "BungeeCord (%server%)");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getColoredConnectedServerName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		return ChatColor.translateAlternateColorCodes('&', truncate(getConnectedServerName().replace("%server%", name), Short.MAX_VALUE - 2) + "&r");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getUnColoredConnectedServerName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null");
		}
		return truncate(STRIP_COLOR_PATTERN.matcher(getConnectedServerName().replace("%server%", name)).replaceAll(""), Short.MAX_VALUE);
	}
	/**
	 * Cuts off access string length.
	 * @param string String to truncate if its size exceeds the given maximum.
	 * @param max Maximum allowed String size.
	 * @return The truncated string.
	 */
	private final String truncate(String string, int max) {
		if (string == null) {
			throw new IllegalArgumentException("String cannot be null");
		}
		if (max < 0) {
			throw new IllegalArgumentException("Maximum size cannot be negative");
		}
		return string.length() > max ? string.substring(0, max) : string;
	}
}
