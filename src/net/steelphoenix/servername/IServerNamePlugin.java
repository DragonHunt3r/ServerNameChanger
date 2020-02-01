package net.steelphoenix.servername;

public interface IServerNamePlugin {
	public boolean isUseUnsupportedProtocol();
	/**
	 * Get the raw server name as defined in the configuration.
	 * @return The configured name.
	 */
	public String getServerName();
	/**
	 * Get the server name colored with a reset added at the end and of max size 256.
	 * @return The colored name.
	 */
	public String getColoredServerName();
	/**
	 * Get the server name stripped of colors of max size 256.
	 * @return The uncolored name.
	 */
	public String getUncoloredServerName();
}
