package net.steelphoenix.servername.spigot;

import java.io.File;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import net.steelphoenix.servername.IServerNamePlugin;
import net.steelphoenix.servername.spigot.listener.BrandListener;
import net.steelphoenix.servername.spigot.listener.ServerPingListener;

public class ServerNamePlugin extends JavaPlugin implements IServerNamePlugin {
	private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)&[0-9A-FK-OR]");
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
		saveDefaultConfig();
		reloadConfig();

		// Register listeners
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener(new ServerPingListener(this));
		manager.addPacketListener(new BrandListener(this));
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void onDisable() {
		// Unregister listeners
		ProtocolLibrary.getProtocolManager().removePacketListeners(this);
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean isUseUnsupportedProtocol() {
		return getConfig().getBoolean("unsupported-protocol");
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getServerName() {
		return getConfig().getString("server-name", "Spigot");
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
