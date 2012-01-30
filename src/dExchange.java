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
	String version = "1.4.1";
	String author = "Darkdiplomat";
	static Logger log = Logger.getLogger("Minecraft");
	
	static dExData dExD = new dExData();
	static dExActions dExA = new dExActions();
	static dExListener dExL = new dExListener();

	public void initialize() {
		etc.getLoader().addListener( PluginLoader.Hook.COMMAND, dExL, this, PluginListener.Priority.MEDIUM);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_RIGHTCLICKED, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_PLACE, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_BROKEN, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.BLOCK_DESTROYED, dExL, this, PluginListener.Priority.LOW);
		etc.getLoader().addListener( PluginLoader.Hook.SIGN_CHANGE, dExL, this, PluginListener.Priority.LOW);
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
}
