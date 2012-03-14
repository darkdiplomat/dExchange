import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Logger;

public class dExchange extends Plugin {

	public final String name = "dExchange";
	public final String version = "1.6";
	public String currver = version;
	public final String author = "Darkdiplomat";
	public final Logger log = Logger.getLogger("Minecraft");
	
	protected dExData dExD = new dExData(this);
	protected dExActions dExA = new dExActions(this);
	protected dExListener dExL = new dExListener(this);

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
			log.info("[dExchange] - There is an update available! Current = " + currver);
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
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(new URL("http://visualillusionsent.net/cmod_plugins/versions.php?plugin="+name).openStream()));
            String inputLine;
            if ((inputLine = in.readLine()) != null) {
                currver = inputLine;
            }
            in.close();
            return Float.valueOf(version.replace("_", "")) >= Float.valueOf(currver.replace("_", ""));
        } 
        catch (Exception E) {
        }
        return true;
    }
}

/*******************************************************************************\
* dExchange v1.x                                                                *
* Copyright (C) 2011-2012 Visual Illusions Entertainment                        *
* @author darkdiplomat <darkdiplomat@visualillusionsent.net>                    *
*                                                                               *
* This file is part of dExchange.                                               *                       
*                                                                               *
* This program is free software: you can redistribute it and/or modify          *
* it under the terms of the GNU General Public License as published by          *
* the Free Software Foundation, either version 3 of the License, or             *
* (at your option) any later version.                                           *
*                                                                               *
* This program is distributed in the hope that it will be useful,               *
* but WITHOUT ANY WARRANTY; without even the implied warranty of                *
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                 *
* GNU General Public License for more details.                                  *
*                                                                               *
* You should have received a copy of the GNU General Public License             *
* along with this program.  If not, see http://www.gnu.org/licenses/gpl.html.   *
\*******************************************************************************/
