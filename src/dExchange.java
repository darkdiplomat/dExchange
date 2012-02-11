import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Logger;

/**
* dExchange v1.x
* Copyright (C) 2011 Visual Illusions Entertainment
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>
*
* This file is part of dExchange.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with dExchange.  If not, see http://www.gnu.org/licenses/gpl.html
*/

public class dExchange extends Plugin {

	String name = "dExchange";
	String version = "1.5.1";
	String CurrVer = "1.5.1";
	String author = "Darkdiplomat";
	static Logger log = Logger.getLogger("Minecraft");
	
	dExData dExD = new dExData();
	dExActions dExA = new dExActions(this);
	dExListener dExL = new dExListener(this);

	public void initialize() {
		etc.getLoader().addListener( PluginLoader.Hook.COMMAND, dExL, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_RIGHTCLICKED, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_PLACE, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_BROKEN, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_DESTROYED, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.SIGN_CHANGE, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.CLOSE_INVENTORY, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.OPEN_INVENTORY, dExL, this, PluginListener.Priority.LOW);
	}
	
	public synchronized void disable() {
		etc.getInstance().removeCommand("/dex");
		etc.getInstance().removeCommand("/dex list");
		etc.getInstance().removeCommand("/dex <item>");
		etc.getInstance().removeCommand("/dex ppricechange");
		etc.getInstance().removeCommand("/dex pamountchange");
		etc.getInstance().removeCommand("/dex cisp");
		etc.getInstance().removeCommand("/dex cibp");
		etc.getInstance().removeCommand("/dex additem");
		etc.getInstance().removeCommand("/dex removeitem");
		log.info(name + " version " + version + " is disabled!");
	}
	
	public void enable() {
		if(!isLatest()){
			log.info("[dExchange] - There is an update available! Current = " + CurrVer);
		}
		etc.getInstance().addCommand("/dex", "<buy/sell> <item> <amount> - buy or sell items");
		etc.getInstance().addCommand("/dex list", " - list prices and items");
		etc.getInstance().addCommand("/dex <item>","- display info on the item");
		etc.getInstance().addCommand("/dex ppricechange","(ppc) - change price on P-SIGNS");
		etc.getInstance().addCommand("/dex pamountchange","(pac) - change amount on P-SIGNS");
		etc.getInstance().addCommand("/dex cisp","<Name> <Price> - change Item's Global Sell Price");
		etc.getInstance().addCommand("/dex cibp","<Name> <Price> - change Item's Global Buy Price");
		etc.getInstance().addCommand("/dex additem"," <Name> <ID> <Damage> <BuyPrice> <SellPrice> - Adds Item to List");
		etc.getInstance().addCommand("/dex removeitem"," <Name> - Removes Item from Global Lists");
		log.info( name + " version " + version + " by " + author + " is enabled!" );
	}
	
	public boolean isLatest(){
		String address = "http://www.visualillusionsent.net/cmod_plugins/Versions.html";
		URL url = null;
		try {
			url = new URL(address);
		} catch (MalformedURLException e) {
			return true;
		}
		String[] Vpre = new String[1]; 
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("dExchange=")){
					Vpre = inputLine.split("=");
					CurrVer = Vpre[1].replace("</p>", "");
				}
			}
			in.close();
		} catch (IOException e) {
			return true;
		}
		return (version.equals(CurrVer));
	}
}
