package net.steelphoenix.servername.bungee;

import net.steelphoenix.servername.IServerNamePlugin;

public interface IBungeeServerNamePlugin extends IServerNamePlugin {
	/**
	 * If the plugin injects itself in the connection pipelines to intercept packets.
	 * May cause performance issues.
	 * @return true if it should inject itself.
	 */
	public boolean isInjectProxyPipeline();
	/**
	 * Get the server name if the player is connected to a backend Spigot server
	 * %server% will be replaced with the server name received from the Spigot server
	 * @return The server name.
	 */
	public String getConnectedServerName();
	public String getColoredConnectedServerName(String name);
	public String getUnColoredConnectedServerName(String name);
}