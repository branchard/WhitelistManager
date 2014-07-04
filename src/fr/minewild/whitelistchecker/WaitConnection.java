package fr.minewild.whitelistchecker;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;

public class WaitConnection extends Thread
{
	
	private ServerSocket	server;
	
	public WaitConnection()
	{
		try
		{
			server = new ServerSocket(8072);
			WhitelistChecker.logMessage(Level.INFO, "Le serveur écoute le port: " + server.getLocalPort());
		}
		catch(BindException e)
		{
			WhitelistChecker.logMessage(Level.SEVERE, "Le port" + server.getLocalPort() + "est déja utilisé");
		}
		catch(IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		try
		{
			while(!server.isClosed())
			{
				Socket client = server.accept();
				WhitelistChecker.logMessage(Level.INFO, "Connection client " + client.getInetAddress().toString().replace("/", ""));
				Thread thrd = new Thread(new ClientThread(client));
				thrd.start();
			}
		}
		catch(IOException e)
		{
			if(!e.getMessage().toLowerCase().contains("socket closed"))
				e.printStackTrace();
		}
	}
	
	public void stopServer() throws IOException
	{
		if(server != null)
			server.close();
	}
}
