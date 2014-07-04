package fr.minewild.whitelistchecker;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class WhitelistChecker extends JavaPlugin
{
	static private Logger	log	= Logger.getLogger("WhitelistChecker");
	private WaitConnection	serverThread;
	
	public void onEnable()
	{
		serverThread = new WaitConnection();
		serverThread.start();
		logMessage(Level.INFO, "Running !");
	}
	
	public void onDisable()
	{
		try
		{
			if(serverThread != null)
				serverThread.stopServer();
		}
		catch(final Exception e)
		{
			logMessage(Level.SEVERE, "Impossible de stopper le socket");
		}
	}
	
	static public void logMessage(Level type, String message)
	{
		switch(type.toString().toLowerCase())
		{
			case "info":
				log.info(Constants.PLUGIN_LOG_PREFIX + " " + message);
			break;
			case "warning":
				log.warning(Constants.PLUGIN_LOG_PREFIX + " " + message);
			break;
			case "severe":
				log.severe(Constants.PLUGIN_LOG_PREFIX + " " + message);
			break;
		}
	}
	
	static public void sendMessage(CommandSender sender, String message)
	{
		sender.sendMessage(Constants.PLUGIN_LOG_PREFIX + " " + message);
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String args[])
	{
		try
		{
			if(label.equalsIgnoreCase("wc"))
			{
				if(args.length == 1)
				{
					String uuid = getUUID(args[0]);
					if(uuid != null)
						sendMessage(sender, args[0] + ": " + uuid);
					else
						sendMessage(sender, "le joueur " + args[0] + " n'existe pas");
				}
				else
					sendMessage(sender, "il faut un argument");
			}
		}
		catch(Exception e)
		{
			sendMessage(sender, "Exception: " + e.getStackTrace().toString() + ": " + e.toString());
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	static public String getUUID(String playerName)
	{
		OfflinePlayer player;
		player = Bukkit.getOfflinePlayer(playerName);
		if(player != null)
		{
			if(player.isWhitelisted())
				return player.getUniqueId().toString().replace("-", "");
			else
				return "null";
		}
		else
			return "null";
	}
}
