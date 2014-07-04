package fr.minewild.whitelistchecker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.logging.Level;

public class ClientThread extends Thread
{
	private Socket			client;
	private PrintWriter		output;
	private BufferedReader	input;
	
	public ClientThread(Socket client)
	{
		this.client = client;
	}
	
	public void run()
	{
		WhitelistChecker.logMessage(Level.INFO, "Connection acceptée");
		try
		{
			client.setSoTimeout(1200);
			output = new PrintWriter(client.getOutputStream(), true);
			input = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
			String inString;
			while((inString = input.readLine()) == null);
			WhitelistChecker.logMessage(Level.INFO, "Packet reçu: " + inString);
			
			String outString = WhitelistChecker.getUUID(inString.toLowerCase()).toLowerCase();
			WhitelistChecker.logMessage(Level.INFO, "Packet envoyé: " + outString);
			output.println(outString);	
		}
		catch(SocketTimeoutException e)
		{
			try
			{
				WhitelistChecker.logMessage(Level.WARNING, "Le client a timeout, le timeout est de " + client.getSoTimeout() + "ms");
			}
			catch(SocketException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch(IOException e)
		{
			//TODO
			e.printStackTrace();
		}
		finally
		{
			try
			{
				client.close();
				WhitelistChecker.logMessage(Level.INFO, "Client fermé");
			}
			catch(IOException e)
			{
				WhitelistChecker.logMessage(Level.WARNING, "Le client n'a put être fermé");
			}
		}
	}
}
