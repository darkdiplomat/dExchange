import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
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

public class dExData {
	Logger log = Logger.getLogger("Minecraft");
	String dir = "plugins/config/dExchange/";
	String IF = "plugins/config/dExchange/ItemNameFile.txt";
	String SCF = "plugins/config/dExchange/ChestTrade.txt";
	String SettingFile = "plugins/config/dExchange/dExSettings.ini";
	String Messages = "plugins/config/dExchange/Messages.txt";
	String PSigns = "plugins/config/dExchange/PSigns.txt";
	PropertiesFile Setting;
	String Account = "N/A";
	String BlackList = "";
	dExItems dExI = new dExItems();
	dExChest dExC;
	dExSign dExS;
	HashMap<Chest, dExChest> Chests;
	HashMap<Sign, dExSign> Signs;
	HashMap<Player, Sign> PS;
	ArrayList<String> WholeList;
	HashMap<String, int[]> IDD;
	HashMap<String, Double> BuyPrice;
	HashMap<String, Double> SellPrice;
	FileUtility fu = new FileUtility();
	ArrayList<Integer> BlackListedIDs;
	boolean MySQL, CMySQL, LogAc;
	String DataBase = "jdbc:mysql://localhost:3306/minecraft";
	String UserName = "root";
	String Password = "root";
	String Driver = "com.mysql.jdbc.Driver";
	PropertiesFile NameFix;
	PropertiesFile Mess;
	int MaxSigns = 0;
	PropertiesFile PlayerSign;
	PropertiesFile Items;
	
	String E101 = "You do not have permission to place that!";
	String E102 = "You do not have permission to use that!";
	String E103 = "You did not specify an Item to sell!";
	String E104 = "You did not specify an Amount to sell!";
	String E105 = "Price not found!";
	String E106 = "The was an issue getting the price!";
	String E107 = "The Item is currently not for sell!";
	String E108 = "You entered an invalid amount!";
	String E109 = "Please link you other sign before making a new one!";
	String E110 = "You did not specified a price!";
	String E111 = "You did not fill in Item:Amount fully!";
	String E112 = "The Item could not be found!";
	String E113 = "You entered an invalid Item ID!";
	String E114 = "You entered an invalid Item Damage value!";
	String E115 = "That Item is banned!";
	String E116 = "You entered an invalid price!";
	String E117 = "This sign is missing it's chest!";
	String E118 = "ACCESS DENIED!";
	String E119 = "You can not break a sign you do not own!";
	String E120 = "You can not break a chest you do not own!";
	String E121 = "You do not have enough room in your inventory for the items!";
	String E122 = "You do not have enough money to buy that!";
	String E123 = "You do not have that much to sell!";
	String E124 = "The Global Account does not have the funds for this transaction!";
	String E125 = "There was not enough room in the chest!";
	String E126 = "There was not enough in the chest!";
	String E127 = "The owner does not have enough money to pay!";
	String E128 = "This sign is not accepting that item!";
	String E129 = "You are already set to change a sign's price!";
	String E130 = "You are already set to change a sign's amount!";
	String E131 = "You can not set a P-SHOP amount to '*'!";
	String E132 = "You can not change a sign you do not own!";
	String E133 = "This sign cannot take anymore items!";
	String E134 = "That Item is already listed!";
	
	String M201 = "<lightblue>Global Shop Sign Created!";
	String M202 = "<lightblue>Global Trade Sign Created!";
	String M203 = "<lightblue>You now need to place/left click a chest to complete the link.";
	String M204 = "<lightblue>Link Created!";
	String M205 = "<rose>Link Canceled!";
	String M206 = "<lightblue>for <yellow><A> <M>";
	String M207 = "<lightblue>You have purchased <yellow><A> Stacks<lightblue> of <gold><I>";
	String M208 = "<lightblue>You have purchased <yellow><A><lightblue> of <gold><I>";
	String M209 = "<lightblue>You have sold <yellow><A> Stacks<lightblue> of <gold><I>";
	String M210 = "<lightblue>You have sold <yellow><A><lightblue> of <gold><I>";
	String M211 = "<rose>Link Broken!";
	String M212 = "<lightblue>Your chest is located at <lightgray>X:<white><X> <lightgray>Y:<white><Y> <lightgray>Z:<white><Z> <lightgray>W:<white><W>";
	String M213 = "<lightblue>and has room for <yellow><A><lightblue> more.";
	String M214 = "<lightblue>and has <yellow><A><lightblue> of <gold><I>";
	String M215 = "<gold><X> <lightblue>BuyPrice = <yellow><Y> <lightblue>SellPrice = <yellow><Z>";
	String M216 = "<lightblue>ID = <yellow><X> <lightblue>Damage = <yellow><Y>";
	String M217 = "<lightblue>LeftClick sign to set new price.";
	String M218 = "<lightblue>New price set.";
	String M219 = "<lightblue>LeftClick sign to set new Amount";
	String M220 = "<lightblue>New amount set.";
	String M221 = "<rose>Amount Change Canceled.";
	String M222 = "<rose>Price Change Canceled.";
	String M223 = "<lightblue> Item Added!";
	String M224 = "<rose> Item Removed!";
	String M225 = "<lightblue> Item Buy Price Updated!";
	String M226 = "<lightblue> Item Sell Price Updated!";
	
	String L301 = "<P1> created <T> at X:<SX> Y:<SY> Z:<SZ> W:<SW>";
	String L302 = "<P1> completed link with chest at X:<CX> Y:<CY> Z: <CZ> W:<CW>";
	String L303 = "<P1> broke link between chest at X:<CX> Y:<CY> Z: <CZ> W:<CW> and sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> by breaking the <T>";
	String L304 = "<P1> used /dex buy to purchase <A> stacks of <I>";
	String L305 = "<P1> used /dex buy to purchase <A> of <I>";
	String L306 = "<P1> used /dex sell to sell <A> stacks of <I>";
	String L307 = "<P1> used /dex sell to sell <A> of <I>";
	String L308 = "<P1> used G-TRADE sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to sell <A> of <I>";
	String L309 = "<P1> used G-SHOP sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to purchase <A> of <I>";
	String L310 = "<P1> used /dex <I> to check prices on <I>";
	String L311 = "<P1> used P-SHOP sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to purchase <A> of <I> from <P2>";
	String L312 = "<P1> used P-TRADE sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> to sell <A> of <I> to <P2>";
	String L313 = "<P1> changed price on <T> sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> from <PR> to <A>";
	String L314 = "<P1> changed amount on <T> sign at X:<SX> Y:<SY> Z:<SZ> W:<SW> from <PR> to <A>";
	String L315 = "<P1> added Name:<T> ID:<I> Damage:<A> BuyPrice:<PR> SellPrice:<P2> to Global Lists";
	String L316 = "<P1> removed Name:<T> from Global Lists";
	String L317 = "<P1> updated Buy Price for Name:<T> to <PR>";
	String L318 = "<P1> updated Sell Price for Name:<T> to <PR>";
	
	
	public dExData(){
		makedirectory();
		Setting = new PropertiesFile(SettingFile);
		NameFix = new PropertiesFile("plugins/config/dExchange/SignNameFixer.txt");
		Mess = new PropertiesFile(Messages);
		PlayerSign = new PropertiesFile(PSigns);
		Items = new PropertiesFile(IF);
		loadsettings();
	}
	
	public void makedirectory(){
		File dExDir = new File(dir);
		File Setting = new File(SettingFile);
		File ItemF = new File(IF);
		File SCFile = new File(SCF);
		File MFile = new File(Messages);
		if(!dExDir.exists()){
			dExDir.mkdirs();
		}
		if(!Setting.exists()){
			try{
				File outputFile = new File(SettingFile);
				InputStream in = getClass().getClassLoader().getResourceAsStream("SettingsTemplate.ini");
				FileWriter out = new FileWriter(outputFile);
				int c;
				while ((c = in.read()) != -1){
					out.write(c);
				}
				in.close();
				out.close();
			} 
			catch (IOException e){
				log.warning("[dExchange] - Unable to create properties file! Using Defaults");
			}
		}
		if(!ItemF.exists()){
			dExI.makeItemsFile(IF);
		}
		if(!SCFile.exists()){
			try {
				SCFile.createNewFile();
			} catch (IOException e) {
				log.warning("[dExchange] - Unable to create ChestTrade File");
			}
		}
		if(!MFile.exists()){
			try {
			    File outputFile = new File(Messages);
				InputStream in = getClass().getClassLoader().getResourceAsStream("MessagesTemplate.txt");
				FileWriter out = new FileWriter(outputFile);
				int c;
				while ((c = in.read()) != -1){
					out.write(c);
				}
				in.close();
				out.close();
			}catch (IOException ioe){
				log.warning("[dExchange] - Unable to load Settings File. Using defaults");
			}
		}
	}
	
	private void loadsettings(){
		Account = LoadStringCheck(Setting, "N/A", "GlobalAccount");
		BlackList = LoadStringCheck(Setting, "7,8,9,10,46", "BlackListedItems");
		MySQL = LoadBooleanCheck(false, "Use-MySQL");
		CMySQL = LoadBooleanCheck(false, "Use-CanaryMySQL");
		LogAc = LoadBooleanCheck(false, "LogActions");
		UserName = LoadStringCheck(Setting, UserName, "UserName");
		Password = LoadStringCheck(Setting, Password, "Password");
		DataBase = LoadStringCheck(Setting, DataBase, "DataBase");
		Driver = LoadStringCheck(Setting, Driver, "SQLDriver");
		MaxSigns = LoadINTCheck(MaxSigns, "MaxSigns");
		
		if((!CMySQL) && (MySQL)){
			try {
				Class.forName(Driver);
			}catch (ClassNotFoundException cnfe) {
				log.severe("[dExchange] - Unable to find driver class: " + Driver);
				log.severe("[dExchange] - Disabling SQL");
				MySQL = false;
			}
		}
		
		BlackListedIDs = new ArrayList<Integer>();
		WholeList = new ArrayList<String>();
		IDD = new HashMap<String, int[]>();
		BuyPrice = new HashMap<String, Double>();
		SellPrice = new HashMap<String, Double>();
		Chests = new HashMap<Chest, dExChest>();
		Signs = new HashMap<Sign, dExSign>();
		PS = new HashMap<Player, Sign>();
		String[] bli = BlackList.split(",");
		for (int i = 0; i < bli.length; i++){
			int blid = -1;
			try{
				blid = Integer.parseInt(bli[i]);
			}catch(NumberFormatException nfe){
				blid = -1;
			}
			if (blid != -1){
				BlackListedIDs.add(blid);
			}
		}
		SCLocs();
		PopulateMaps();
		LoadMessages();
	}
	
	private Connection getSQLConn() throws SQLException{
		Connection conn = null;
		if (CMySQL){
			conn = etc.getSQLConnection();
		}else{
			conn = DriverManager.getConnection(DataBase, UserName, Password);	
		}
		return conn;
	}
	
	public void LoadMessages(){
		E101 = LoadStringCheck(Mess, E101, "101-PlaceNoPermission");
		E102 = LoadStringCheck(Mess, E102,"102-UseNoPermission");
		E103 = LoadStringCheck(Mess, E103,"103-ItemNotSpecified");
		E104 = LoadStringCheck(Mess, E104,"104-AmountNotSpecified");
		E105 = LoadStringCheck(Mess, E105,"105-PriceNotFound");
		E106 = LoadStringCheck(Mess, E106,"106-PriceIssues");
		E107 = LoadStringCheck(Mess, E107,"107-NotForSell");
		E108 = LoadStringCheck(Mess, E108,"108-AmountInvalid");
		E109 = LoadStringCheck(Mess, E109,"109-LinkInProgress");
		E110 = LoadStringCheck(Mess, E110,"110-PriceNotSpecified");
		E111 = LoadStringCheck(Mess, E111,"111-SignNotCorrect");
		E112 = LoadStringCheck(Mess, E112,"112-ItemNotFound");
		E113 = LoadStringCheck(Mess, E113,"113-InvalidItemID");
		E114 = LoadStringCheck(Mess, E114,"114-InvalidItemDamage");
		E115 = LoadStringCheck(Mess, E115,"115-ItemBanned");
		E116 = LoadStringCheck(Mess, E116,"116-InvalidPrice");
		E117 = LoadStringCheck(Mess, E117,"117-MissingChest");
		E118 = LoadStringCheck(Mess, E118,"118-OpenChestDenied");
		E119 = LoadStringCheck(Mess, E119,"119-BreakSignDenied");
		E120 = LoadStringCheck(Mess, E120,"120-BreakChestDenied");
		E121 = LoadStringCheck(Mess, E121,"121-InventoryPlayerFull");
	    E122 = LoadStringCheck(Mess, E122,"122-NotEnoughMoney");
	    E123 = LoadStringCheck(Mess, E123,"123-InventoryPlayerNone");
	    E124 = LoadStringCheck(Mess, E124,"124-GlobalNoMoney");
	    E125 = LoadStringCheck(Mess, E125,"125-InventoryChestFull");
	    E126 = LoadStringCheck(Mess, E126,"126-InventoryChestNone");
	    E127 = LoadStringCheck(Mess, E127,"127-PlayerOtherNoMoney");
	    E128 = LoadStringCheck(Mess, E128,"128-PTRADEIncorrectItem");
	    E129 = LoadStringCheck(Mess, E129,"129-SetPriceEngaged");
	    E130 = LoadStringCheck(Mess, E130,"130-SetAmountEngage");
	    E131 = LoadStringCheck(Mess, E131,"131-CannotSetPSHOPAmount");
	    E132 = LoadStringCheck(Mess, E132,"132-CannotChangeSignNotOwned");
	    E133 = LoadStringCheck(Mess, E133,"133-SignNotTakingItems");
	    E134 = LoadStringCheck(Mess, E134,"134-ItemAlreadyListed");
		
		M201 = LoadStringCheck(Mess, M201,"201-GSHOPCreate");
		M202 = LoadStringCheck(Mess, M202,"202-GTRADECreate");
		M203 = LoadStringCheck(Mess, M203,"203-PSignLink");
		M204 = LoadStringCheck(Mess, M204,"204-LinkCreate");
		M205 = LoadStringCheck(Mess, M205,"205-LinkCancel");
		M206 = LoadStringCheck(Mess, M206,"206-GBuySell");
		M207 = LoadStringCheck(Mess, M207,"207-BuySTLine1");
		M208 = LoadStringCheck(Mess, M208,"208-BuyLine1");
		M209 = LoadStringCheck(Mess, M209,"209-SellSTLine1");
		M210 = LoadStringCheck(Mess, M210,"210-SellLine1");
		M211 = LoadStringCheck(Mess, M211,"211-PSignLinkBroken");
		M212 = LoadStringCheck(Mess, M212,"212-ChestLoc");
		M213 = LoadStringCheck(Mess, M213,"213-ChestRoom");
		M214 = LoadStringCheck(Mess, M214,"214-ChestAmount");
		M215 = LoadStringCheck(Mess, M215,"215-PriceCheckL1");
		M216 = LoadStringCheck(Mess, M216,"216-PriceCheckL2");
		M217 = LoadStringCheck(Mess, M217,"217-SetSignPrice1");
		M218 = LoadStringCheck(Mess, M218,"218-SetSignPrice2");
	    M219 = LoadStringCheck(Mess, M219,"219-SetSignAmount1");
	    M220 = LoadStringCheck(Mess, M220,"220-SetSignAmount2");
	    M221 = LoadStringCheck(Mess, M221,"221-SignAmountChangeCancel");
	    M222 = LoadStringCheck(Mess, M222,"222-SignPriceChangeCancel");
	    M223 = LoadStringCheck(Mess, M223,"223-ItemAddedToList");
	    M224 = LoadStringCheck(Mess, M224,"224-ItemRemovedFromList");
	    M225 = LoadStringCheck(Mess, M225,"225-ItemBuyPriceUpdate");
	    M226 = LoadStringCheck(Mess, M226,"226-ItemSellPriceUpdate");
		
		L301 = LoadStringCheck(Mess, L301,"301-SignCreate");
		L302 = LoadStringCheck(Mess, L302,"302-SignLinked");
		L303 = LoadStringCheck(Mess, L303,"303-LinkBroken");
		L304 = LoadStringCheck(Mess, L304,"304-DEXBUYS");
		L305 = LoadStringCheck(Mess, L305,"305-DEXBUY");
		L306 = LoadStringCheck(Mess, L306,"306-DEXSELLS");
		L307 = LoadStringCheck(Mess, L307,"307-DEXSELL");
		L308 = LoadStringCheck(Mess, L308,"308-GTUsed");
		L309 = LoadStringCheck(Mess, L309,"309-GSUsed");
		L310 = LoadStringCheck(Mess, L310,"310-PriceCheck");
		L311 = LoadStringCheck(Mess, L311,"311-PSUsed");
		L312 = LoadStringCheck(Mess, L312,"312-PTUsed");
		L313 = LoadStringCheck(Mess, L313,"313-PSignPriceUpdate");
	    L314 = LoadStringCheck(Mess, L314,"314-PSignAmountUpdate");
		L315 = LoadStringCheck(Mess, L315,"315-ItemAdded");
	    L316 = LoadStringCheck(Mess, L316,"316-ItemRemoved");
	    L317 = LoadStringCheck(Mess, L317,"317-BPriceUpdate");
	    L318 = LoadStringCheck(Mess, L318,"318-SPriceUpdate");
	    
	}
	
	private String LoadStringCheck(PropertiesFile props, String defaultvalue, String Property){
		String value;
		if(props.containsKey(Property)){
			value = props.getString(Property);
		}else{
			log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
			value = defaultvalue;
		}
		return value;
	}
	
	private boolean LoadBooleanCheck(boolean defaultvalue, String Property){
		boolean value;
		if(Setting.containsKey(Property)){
			value = Setting.getBoolean(Property);
		}else{
			log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
			value = defaultvalue;
		}
		return value;
	}
	
	private int LoadINTCheck(int defaultvalue, String Property){
		int value;
		if(Setting.containsKey(Property)){
			try{
				value = Setting.getInt(Property);
			}catch(NumberFormatException NFE){
				log.warning("[dExchange] Bad Value at "+Property+" Using default of "+String.valueOf(defaultvalue));
				value = defaultvalue;
			}
		}
		else{
			log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
			value = defaultvalue;
		}
		return value;
	}
	
	private void PopulateMaps(){
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					PreparedStatement ps = conn.prepareStatement("SELECT * FROM dExchange");
					ResultSet rs = ps.executeQuery();
					while (rs.next()){
						String name = rs.getString("Name");
						int[] IDDA = new int[]{rs.getInt("ItemID"), rs.getInt("Damage")};
						double buy = rs.getDouble("BuyPrice");
						double sell =  rs.getDouble("SellPrice");
						String get = name+"="+IDDA[0]+":"+IDDA[1]+","+buy+","+sell;
						IDD.put(name, IDDA);
						BuyPrice.put(name, buy);
						SellPrice.put(name, sell);
						WholeList.add(get);
					}
				} catch (SQLException ex) {
					log.severe("[dExchange] - Unable to get data from dExchange!");
				}finally{
					try{
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
						log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			Scanner scanner = null;
			try {
				scanner = new Scanner(new FileInputStream(IF));
				while (scanner.hasNextLine()){
					String get = scanner.nextLine();
					if (!get.contains("#") && !get.equals("")){
						String[] getname = get.split("=");
						String[] idpricesplit = getname[1].split(",");
						String[] IDsplit = new String[2];
						int ID = 0, damage = 0;
						double buy = 0, sell = 0;
						String name = getname[0];
						if (idpricesplit[0].contains(":")){
							IDsplit = idpricesplit[0].split(":");
						}
						else{
							IDsplit[0] = idpricesplit[0];
							IDsplit[1] = "0";
						}
						try{
							ID = Integer.parseInt(IDsplit[0]);
						}catch(NumberFormatException nfe){
							log.severe("[dExchange] - There was an issue with ID for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
							log.severe("[dExchange] - There was an issue with ID for ItemName:" + name);
							continue;
						}
						try{
							damage = Integer.parseInt(IDsplit[1]);
						}catch(NumberFormatException nfe){
							log.severe("[dExchange] - There was an issue with Damage for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
							log.severe("[dExchange] - There was an issue with Damage for ItemName:" + name);
							continue;
						}
						try{
							buy = Double.parseDouble(idpricesplit[1]);
						}catch(NumberFormatException nfe){
							log.severe("[dExchange] - There was an issue with BuyPrice for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
							log.severe("[dExchange] - There was an issue with BuyPrice for ItemName:" + name);
							continue;
						}
						try{
							sell = Double.parseDouble(idpricesplit[2]);
						}catch(NumberFormatException nfe){
							log.severe("[dExchange] - There was an issue with SellPrice for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
							log.severe("[dExchange] - There was an issue with SellPrice for ItemName:" + name);
							continue;
						}
						int[] IDDA = new int[]{ID, damage};
						IDD.put(name, IDDA);
						BuyPrice.put(name, buy);
						SellPrice.put(name, sell);
						WholeList.add(name+"="+IDDA[0]+":"+IDDA[1]+","+buy+","+sell);
					}
				}
			}catch (IOException e) {
				log.severe("[dExchange] - Unable to load in Items File");
			}finally{
				if (scanner != null){
					scanner.close();
				}
			}
		}
		
	}
	
	private void SCLocs(){
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
	    		try{
	    			PreparedStatement ps = conn.prepareStatement("SELECT * FROM dExchangeChests");
	    			ResultSet rs = ps.executeQuery();
	    			while (rs.next()){
	    				Sign sign;
	    				Chest chest;
	    				int sw = rs.getInt("SW");
						int sx = rs.getInt("SX");
						int sy = rs.getInt("SY");
						int sz = rs.getInt("SZ");
						int cw = rs.getInt("CW");
						int cx = rs.getInt("CX");
						int cy = rs.getInt("CY");
						int cz = rs.getInt("CZ");
						etc.getServer().getWorld(sw).loadChunk(sx, sy, sz);
						Block block = etc.getServer().getWorld(sw).getBlockAt(sx, sy, sz);
						if ((block.getType() == 63) || (block.getType() == 68)){
							sign = (Sign)etc.getServer().getWorld(sw).getComplexBlock(block);
							etc.getServer().getWorld(cw).loadChunk(cx, cy, cz);
							block = etc.getServer().getWorld(cw).getBlockAt(cx, cy, cz);
							if(block.getType() == 54){
								chest = (Chest) etc.getServer().getWorld(cw).getOnlyComplexBlock(block);
								String pname = sign.getText(3);
								if(checkNameFix(pname)){
									pname = NameFix(pname);
								}
								if(Chests.containsKey(chest)){
									dExC = Chests.get(chest);
									dExC.addSign(sign);
									Chests.put(chest, dExC);
								}
								else{
									dExC = new dExChest(chest, pname);
									dExC.addSign(sign);
									Chests.put(chest, dExC);
								}
								if(Signs.containsKey(sign)){
									dExS = Signs.get(sign);
									dExS.addChest(chest);
									Signs.put(sign, dExS);
								}
								else{
									dExS = new dExSign(sign, pname);
									dExS.addChest(chest);
									Signs.put(sign, dExS);
								}
							}
						}
	    			}
	    		} catch (SQLException ex) {
	    			log.severe("[dExchange] - Unable to get data from dExchange!");
	    		}finally{
	    			try{
	    				if (conn != null){
	    					conn.close();
	    				}
	    			}catch (SQLException sqle) {
	    				log.severe("[dExchange] - Could not close connection to SQL");
	    			}
	    		}
			}
		}
		else{
			try {
				BufferedReader in = new BufferedReader(new FileReader(SCF));
				String line;
				while ((line = in.readLine()) != null){
					if (!line.contains("#") && !line.equals("")){
						String[] Loc = line.split(":");
						String[] SignLoc = Loc[0].split(",");
						String[] ChestLoc = Loc[1].split(",");
						Sign sign;
						Chest chest;
						int sw = Integer.valueOf(SignLoc[0]);
						int sx = Integer.valueOf(SignLoc[1]);
						int sy = Integer.valueOf(SignLoc[2]);
						int sz = Integer.valueOf(SignLoc[3]);
						int cw = Integer.valueOf(ChestLoc[0]);
						int cx = Integer.valueOf(ChestLoc[1]);
						int cy = Integer.valueOf(ChestLoc[2]);
						int cz = Integer.valueOf(ChestLoc[3]);
						etc.getServer().getWorld(sw).loadChunk(sx, sy, sz);
						Block block = etc.getServer().getWorld(sw).getBlockAt(sx, sy, sz);
						if ((block.getType() == 63) || (block.getType() == 68)){
		    				sign = (Sign)etc.getServer().getWorld(sw).getComplexBlock(block);
		    				etc.getServer().getWorld(cw).loadChunk(cx, cy, cz);
		    				block = etc.getServer().getWorld(cw).getBlockAt(cx, cy, cz);
		    				if(block.getType() == 54){
		    					chest = (Chest) etc.getServer().getWorld(cw).getOnlyComplexBlock(block);
		    					String pname = sign.getText(3);
								if(checkNameFix(pname)){
									pname = NameFix(pname);
								}
								if(Chests.containsKey(chest)){
									dExC = Chests.get(chest);
									dExC.addSign(sign);
									Chests.put(chest, dExC);
								}
								else{
									dExC = new dExChest(chest, pname);
									dExC.addSign(sign);
									Chests.put(chest, dExC);
								}
								if(Signs.containsKey(sign)){
									dExS = Signs.get(sign);
									dExS.addChest(chest);
									Signs.put(sign, dExS);
								}
								else{
									dExS = new dExSign(sign, pname);
									dExS.addChest(chest);
									Signs.put(sign, dExS);
								}
		    				}
		    			}
		    		}
		    	}
				in.close();
			} catch (IOException e) {
				log.severe("[dExchange] - Unable to load ChestTrade File");
			}
		}
	}
	
	public ArrayList<String> ListLine(){
		return WholeList;
	}
	
	public boolean canmakemore(Player player){
		if(MaxSigns < 1){ return true; }
		int curra = PlayerSign.getInt(player.getName());
		if(curra+1 >= MaxSigns){
			return false;
		}
		return true;
	}
	
	public void addPlayerSignTotal(String name){
		if(PlayerSign.containsKey(name)){
			int i = PlayerSign.getInt(name)+1;
			PlayerSign.setInt(name, i);
		}
		else{
			PlayerSign.setInt(name, 1);
		}
	}
	
	public void removePlayerSignTotal(String name){
		int i = PlayerSign.getInt(name)-1;
		PlayerSign.setInt(name, i);
	}
	
	public double globalaccountbalance(){
		double balance = -2;
		if(!Account.equals("N/A")){
			balance = (Double)etc.getLoader().callCustomHook("dCBalance", new Object[]{"Joint-Balance-NC", Account});
		}
		return balance;
	}
	
	public void payglobalaccount(double amount){
		etc.getLoader().callCustomHook("dCBalance", new Object[]{"Joint-Deposit-NC", Account, amount});
	}
	
	public void chargeglobalaccount(double amount){
		etc.getLoader().callCustomHook("dCBalance", new Object[]{"Joint-Withdraw-NC", Account, amount});
	}
	
	public String getMN(){
		return (String)etc.getLoader().callCustomHook("dCBalance", new Object[]{"MoneyName"});
	}
	
	public double getItemSellPrice(String ItemName){
		double price = -1;
		if (SellPrice.containsKey(ItemName)){
			price = SellPrice.get(ItemName);
			if(price < 0.01){
				price = -2;
			}
		}
		return price;
	}
	
	public int[] getItemId(String ItemName){
		int[] ID = new int[]{-1, 0};
		if(IDD.containsKey(ItemName)){
			ID = IDD.get(ItemName);
		}
		return ID;
	}
	
	public double getItemBuyPrice(String ItemName){
		double price = -1;
		if(BuyPrice.containsKey(ItemName)){
			price = BuyPrice.get(ItemName);
			if(price < 0.01){
				price = -2;
			}
		}
		return price;
	}
	
	public String reverseItemLookUp(int ID, int damage){
		int[] idd = new int[]{ID, damage};
		Iterator<String> keyIterator = IDD.keySet().iterator();
		String Name = null;
		while(keyIterator.hasNext()){
		  String Namecheck = keyIterator.next();
		  int[] check = IDD.get(Namecheck);
		  if(check[0] == idd[0] && check[1] == idd[1]){
			  Name = Namecheck;
			  break;
		  }
		}
		return Name;
	}
	
	public void openlink(Player player, Sign sign){
		PS.put(player, sign);
	}
	
	public boolean checklink(Player player){
		return PS.containsKey(player);
	}
	
	public void cancellink(Player player){
		PS.remove(player);
	}
	
	public void closelink(Player player, Chest chest){
		Sign sign = PS.get(player);
		String pname = sign.getText(3);
		if(checkNameFix(pname)){
			pname = NameFix(pname);
		}
		PS.remove(player);
		if(Chests.containsKey(chest)){
			dExC = Chests.get(chest);
			dExC.addSign(sign);
			Chests.put(chest, dExC);
		}
		else{
			dExC = new dExChest(chest, pname);
			dExC.addSign(sign);
			Chests.put(chest, dExC);
		}
		if(Signs.containsKey(sign)){
			dExS = Signs.get(sign);
			dExS.addChest(chest);
			Signs.put(sign, dExS);
		}
		else{
			dExS = new dExSign(sign, pname);
			dExS.addChest(chest);
			Signs.put(sign, dExS);
		}
		addlinktofile(chest, sign);
	}
	
	public boolean CheckSign(Sign sign){
		return Signs.containsKey(sign);
	}
	
	public boolean isSignOwner(Sign sign, String name){
		return Signs.get(sign).isOwner(name);
	}
	
	public boolean CheckChest(Chest chest){
		return Chests.containsKey(chest);
	}
	
	public boolean isChestOwner(Chest chest, String name){
		return Chests.get(chest).isOwner(name);
	}
	
	public void addlinktofile(Chest chest, Sign sign){
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
	    		try{
	    			PreparedStatement ps = conn.prepareStatement("INSERT INTO dExchangeChests (SW,SX,SY,SZ,CW,CX,CY,CZ) VALUES(?,?,?,?,?,?,?,?)");
	    			ps.setInt(1, sign.getWorld().getType().getId());
	    			ps.setInt(2, sign.getX());
	    			ps.setInt(3, sign.getY());
	    			ps.setInt(4, sign.getZ());
	    			ps.setInt(5, chest.getWorld().getType().getId());
	    			ps.setInt(6, chest.getX());
	    			ps.setInt(7, chest.getY());
	    			ps.setInt(8, chest.getZ());
	    			ps.executeUpdate();
	    		} catch (SQLException ex) {
	    			log.severe("[dExhange] - Unable to add data to dExchangeChests!");
	    		}finally{
	    			try{
	    				if (conn != null){
	    					conn.close();
	    				}
	    			}catch (SQLException sqle) {
	    				log.severe("[dExchange] - Could not close connection to SQL");
	    			}
	    		}
			}
		}
		else{
			int sw = sign.getWorld().getType().getId();
			int sx = sign.getX();
			int sy = sign.getY();
			int sz = sign.getZ();
			int cw = chest.getWorld().getType().getId();
			int cx = chest.getX();
			int cy = chest.getY();
			int cz = chest.getZ();
			String loc = sw+","+sx+","+sy+","+sz+":"+cw+","+cx+","+cy+","+cz;
			try {
				FileWriter fw = new FileWriter(SCF,true);
				fw.write(loc+System.getProperty("line.separator"));
				fw.close();  
			} catch (IOException e) {
				log.severe("[dExchange] - Unable to Add TradeLink!");
			}
		}
	}
	
	public boolean blacklisteditem(int ID){
		if(BlackListedIDs.contains(ID)){
			return true;
		}
		return false;
	}
	
	public void removeline(Sign sign, Chest chest, boolean chestbroke){
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
	    		try{
	    			PreparedStatement ps = conn.prepareStatement("DELETE FROM dExchangeChests WHERE SW = ? and SX = ? and SY = ? and SZ = ? and CW = ? and CX = ? and CY = ? and CZ = ?");
	    			ps.setInt(1, sign.getWorld().getType().getId());
	    			ps.setInt(2, sign.getX());
	    			ps.setInt(3, sign.getY());
	    			ps.setInt(4, sign.getZ());
	    			ps.setInt(5, chest.getWorld().getType().getId());
	    			ps.setInt(6, chest.getX());
	    			ps.setInt(7, chest.getY());
	    			ps.setInt(8, chest.getZ());
	    			ps.executeUpdate();
	    		} catch (SQLException ex) {
	    			log.severe("[dExchange] - Unable to delete data from dExchangeChests!");
	    		}finally{
	    			try{
	    				if (conn != null){
	    					conn.close();
	    				}
	    			}catch (SQLException sqle) {
	    				log.severe("[dExchange] - Could not close connection to SQL");
	    			}
	    		}
			}
		}
		else{
			int sw = sign.getBlock().getWorld().getType().getId();
			int sx = sign.getX();
			int sy = sign.getY();
			int sz = sign.getZ();
			int cw = chest.getWorld().getType().getId();
			int cx = chest.getX();
			int cy = chest.getY();
			int cz = chest.getZ();
			String loc = sw+","+sx+","+sy+","+sz+":"+cw+","+cx+","+cy+","+cz;
			fu.removeLineFromFile(SCF, loc);
		}
		if(Chests.containsKey(chest)){
			dExC = Chests.get(chest);
			dExC.removeSign(sign);
			if((dExC.emptyarray()) || (chestbroke)){
				Chests.remove(chest);
			}
		}
		if(Signs.containsKey(sign)){
			dExS = Signs.get(sign);
			dExS.removeChest(chest);
			if(dExC.emptyarray() || (!chestbroke)){
				Signs.remove(sign);
			}
		}
		checkfurther(sign, chest, chestbroke);
	}
	
	public void checkfurther(Sign sign, Chest chest, boolean chestbroke){
		boolean linked = false;
		if(chestbroke){
			for(Sign key : Signs.keySet()){
				if(Signs.get(key).containsChest(chest)){
					sign = key;
					linked = true;
				}
			}
		}
		else{
			for(Chest key : Chests.keySet()){
				if(Chests.get(key).containsSign(sign)){
					chest = key;
					linked = true;
				}
			}
		}
		if(linked){
			removeline(sign, chest, chestbroke);
		}
	}
	
	public Chest getChest(Sign sign){
		for(Chest key : Chests.keySet()){
			if(Chests.get(key).containsSign(sign)){
				return key;
			}
		}
		return null;
	}
	
	public Sign getSign(Chest chest){
		for(Sign key : Signs.keySet()){
			if(Signs.get(key).containsChest(chest)){
				return key;
			}
		}
		return null;
	}
	
	public Sign getOpenLinkSign(Player player){
		return PS.get(player);
	}
	
	public String NameFix(String Name){
		return NameFix.getString(Name);
	}
	
	public boolean checkNameFix(String name){
		return NameFix.containsKey(name);
	}
	
	public void setNameFix(String Name, String Fix){
		NameFix.setString(Fix, Name);
	}
	
	public void updateItemBuyPrice(String IName, double bp){
		String OldLine = "";
		String NewLine = "";
		int[] idd = IDD.get(IName);
		double sp = SellPrice.get(IName);
		double obp = BuyPrice.get(IName);
		if(idd[1] == 0){
			OldLine = IName+"="+idd[0]+","+priceForm(obp)+","+priceForm(sp);
			NewLine = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(sp);
		}
		else{
			OldLine = IName+"="+idd[0]+","+priceForm(obp)+","+priceForm(sp);
			NewLine = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(sp);
		}
		if(MySQL){
			Connection conn = null;
			PreparedStatement ps = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					ps = conn.prepareStatement("UPDATE dExchange SET BuyPrice = ? WHERE Name = ? LIMIT 1");
					ps.setDouble(1, bp);
					ps.setString(2, IName);
					ps.executeUpdate();
				} catch (SQLException ex) {
					log.severe("[dExchange] - Unable to delete Item from dExchange!");
				}finally{
					try{
						if (ps != null){
							ps.close();
						}
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
						log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			//REFACTOR
			fu.removeLineFromFile(IF, OldLine);
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(IF, true));
				out.newLine();
				out.write(NewLine);
				out.close();
			} catch (IOException e) {
				log.severe("[dExhange] - Unable to add Item to Item Lists!");
			}
		}
		BuyPrice.put(IName, bp);
		WholeList.remove(OldLine);
		WholeList.add(NewLine);
	}
	
	public void updateItemSellPrice(String IName, double sp){
		String OldLine = "";
		String NewLine = "";
		int[] idd = IDD.get(IName);
		double osp = SellPrice.get(IName);
		double bp = BuyPrice.get(IName);
		if(idd[1] == 0){
			OldLine = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(osp);
			NewLine = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(sp);
		}
		else{
			OldLine = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(osp);
			NewLine = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(sp);
		}
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					PreparedStatement ps = conn.prepareStatement("UPDATE dExchange SET SellPrice = ? WHERE Name = ? LIMIT 1");
					ps.setDouble(1, sp);
					ps.setString(2, IName);
					ps.executeUpdate();
				} catch (SQLException ex) {
					log.severe("[dExchange] - Unable to delete Item from dExchange!");
				}finally{
					try{
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
						log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			fu.removeLineFromFile(IF, OldLine);
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(IF,true));
				out.newLine();
				out.write(NewLine);
				out.close();
			} catch (IOException e) {
				log.severe("[dExhange] - Unable to add Item to Item Lists!");
			}
		}
		SellPrice.put(IName, sp);
		WholeList.remove(OldLine);
		WholeList.add(NewLine);
	}
	
	public void removeItem(String IName){
		int[] idd = IDD.get(IName);
		double bp = BuyPrice.get(IName);
		double sp = SellPrice.get(IName);
		String Line = "";
		if(idd[1] == 0){
			Line = IName+"="+idd[0]+","+priceForm(bp)+","+priceForm(sp);
		}
		else{
			Line = IName+"="+idd[0]+":"+idd[1]+","+priceForm(bp)+","+priceForm(sp);
		}
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					PreparedStatement ps = conn.prepareStatement("DELETE FROM dExchange WHERE Name = ?");
					ps.setString(1, IName);
					ps.executeUpdate();
				} catch (SQLException ex) {
					log.severe("[dExchange] - Unable to delete Item from dExchange!");
				}finally{
					try{
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
						log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			fu.removeLineFromFile(IF, Line);
		}
		IDD.remove(IName);
		BuyPrice.remove(IName);
		SellPrice.remove(IName);
		WholeList.remove(Line);
	}
	
	public void addItemToList(String IName, int id, int dam, double bp, double sp){
		String Line = "";
		if(dam == 0){
			Line = IName+"="+id+","+priceForm(bp)+","+priceForm(sp);
		}
		else{
			Line = IName+"="+id+":"+dam+","+priceForm(bp)+","+priceForm(sp);
		}
		if(MySQL){
			Connection conn = null;
			try{
				conn = getSQLConn();
			}catch(SQLException SQLE){
				log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
	    		try{
	    			PreparedStatement ps = conn.prepareStatement("INSERT INTO dExchange (Name,ID,Damage,BuyPrice,SellPrice) VALUES(?,?,?,?,?)");
	    			ps.setString(1, IName);
	    			ps.setInt(2, id);
	    			ps.setInt(3, dam);
	    			ps.setDouble(4, bp);
	    			ps.setDouble(5, sp);
	    			ps.executeUpdate();
	    			conn.close();
	    		} catch (SQLException ex) {
	    			log.severe("[dExhange] - Unable to add Item to Item Lists!");
	    		}
			}
		}
		else{
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(IF,true));
				out.newLine();
				out.write(Line);
				out.close();
			} catch (IOException e) {
				log.severe("[dExhange] - Unable to add Item to Item Lists!");
			}
		}
		IDD.put(IName, new int[]{id, dam});
		BuyPrice.put(IName, bp);
		log.info(String.valueOf(BuyPrice.get(IName)));
		SellPrice.put(IName, sp);
		WholeList.add(Line);
	}
	
	public void logAct(int Code, String P1, String P2, String T, String SX, String SY, String SZ, String SW, String CX, String CY, String CZ, String CW, String I, String A, String PR){
		if(LogAc){
			String m = "";
			switch(Code){
			case 301: m = "[dEx]" + L301.replace("<P1>", P1).replace("<T>", T).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW); break;
			case 302: m = "[dEx]" + L302.replace("<P1>", P1).replace("<T>", T).replace("<CX>", CX).replace("<CY>", CY).replace("<CZ>", CZ).replace("<CW>", CW); break;
			case 303: m = "[dEx]" + L303.replace("<P1>", P1).replace("<T>", T).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<CX>", CX).replace("<CY>", CY).replace("<CZ>", CZ).replace("<CW>", CW); break;
			case 304: m = "[dEx]" + L304.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 305: m = "[dEx]" + L305.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 306: m = "[dEx]" + L306.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 307: m = "[dEx]" + L307.replace("<P1>", P1).replace("<A>", A).replace("<I>", I); break;
			case 308: m = "[dEx]" + L308.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I); break;
			case 309: m = "[dEx]" + L309.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I); break;
			case 310: m = "[dEx]" + L310.replace("<P1>", P1).replace("<I>", I); break;
			case 311: m = "[dEx]" + L311.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I).replace("<P2>", P2); break;
			case 312: m = "[dEx]" + L312.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<I>", I).replace("<P2>", P2); break;
			case 313: m = "[dEx]" + L313.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<T>", T).replace("<PR>", PR); break;
			case 314: m = "[dEx]" + L314.replace("<P1>", P1).replace("<SX>", SX).replace("<SY>", SY).replace("<SZ>", SZ).replace("<SW>", SW).replace("<A>", A).replace("<T>", T).replace("<PR>", PR); break;
			case 315: m = "[dEx]" + L315.replace("<P1>", P1).replace("<T>", T).replace("<I>", I).replace("<A>", A).replace("<PR>", PR).replace("<P2>", P2); break;
			case 316: m = "[dEx]" + L316.replace("<P1>", P1).replace("<T>", T); break;
			case 317: m = "[dEx]" + L317.replace("<P1>", P1).replace("<T>", T).replace("<PR>", PR); break;
			case 318: m = "[dEx]" + L318.replace("<P1>", P1).replace("<T>", T).replace("<PR>", PR); break;
			}
			etc.getLoader().callCustomHook("dCBalance", new Object[]{"log", m});
		}
	}
	
	public boolean ErrorMessage(Player player, int code){
		switch(code){
		case 101: player.notify(E101); return true;
		case 102: player.notify(E102); return true;
		case 103: player.notify(E103); return true;
		case 104: player.notify(E104); return true;
		case 105: player.notify(E105); return true;
		case 106: player.notify(E106); return true;
		case 107: player.notify(E107); return true;
		case 108: player.notify(E108); return true;
		case 109: player.notify(E109); return true;
		case 110: player.notify(E110); return true;
		case 111: player.notify(E111); return true;
		case 112: player.notify(E112); return true;
		case 113: player.notify(E113); return true;
		case 114: player.notify(E114); return true;
		case 115: player.notify(E115); return true;
		case 116: player.notify(E116); return true;
		case 117: player.notify(E117); return true;
		case 118: player.notify(E118); return true;
		case 119: player.notify(E119); return true;
		case 120: player.notify(E120); return true;
		case 121: player.notify(E121); return true;
		case 122: player.notify(E122); return true;
		case 123: player.notify(E123); return true;
		case 124: player.notify(E124); return true;
		case 125: player.notify(E125); return true;
		case 126: player.notify(E126); return true;
		case 127: player.notify(E127); return true;
		case 128: player.notify(E128); return true;
		case 129: player.notify(E129); return true;
		case 130: player.notify(E130); return true;
		case 131: player.notify(E131); return true;
		case 132: player.notify(E132); return true;
		case 133: player.notify(E133); return true;
		case 134: player.notify(E134); return true;
		default: return true;
		}
	}
	
	public String pmessage(int code, String A, String I){
		String M = null;
		switch (code){
		case 201: M = Colorize(M201); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 202: M = Colorize(M202); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 203: M = Colorize(M203); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 204: M = Colorize(M204); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 205: M = Colorize(M205); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 206: M = Colorize(M206); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 207: M = Colorize(M207); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 208: M = Colorize(M208); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 209: M = Colorize(M209); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 210: M = Colorize(M210); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 211: M = Colorize(M211); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 213: M = Colorize(M213); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 214: M = Colorize(M214); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 217: M = Colorize(M217); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 218: M = Colorize(M218); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 219: M = Colorize(M219); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 220: M = Colorize(M220); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 221: M = Colorize(M221); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 222: M = Colorize(M222); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 223: M = Colorize(M223); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 224: M = Colorize(M224); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 225: M = Colorize(M225); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		case 226: M = Colorize(M226); M = "§2[§6dEx§2]" + M.replace("<A>", A).replace("<I>", I).replace("<M>", getMN()); return M;
		default: String Missing = "MISSING MESSAGE M:"+code; return Missing;
		}
	}
	
	public String SpecM(int code, String X, String Y, String Z, String W){
		String SM = null;
		switch (code){
			case 212: SM = Colorize(M212); SM = "§2[§6dEx§2]" + SM.replace("<X>", String.valueOf(X)).replace("<Y>", String.valueOf(Y)).replace("<Z>", String.valueOf(Z)).replace("<W>", String.valueOf(W)); return SM;
			case 215: SM = Colorize(M215); SM = "§2[§6dEx§2]" + SM.replace("<X>", String.valueOf(X)).replace("<Y>", String.valueOf(Y)).replace("<Z>", String.valueOf(Z)).replace("<W>", String.valueOf(W)); return SM;
			case 216: SM = Colorize(M216); SM = "§2[§6dEx§2]" + SM.replace("<X>", String.valueOf(X)).replace("<Y>", String.valueOf(Y)).replace("<Z>", String.valueOf(Z)).replace("<W>", String.valueOf(W)); return SM;
		}
		return SM;
	}
	
	private String Colorize(String Message){
		Message = Message.replace("<black>", Colors.Black);
		Message = Message.replace("<blue>", Colors.Blue);
		Message = Message.replace("<darkpurple>", Colors.DarkPurple);
		Message = Message.replace("<gold>", Colors.Gold);
		Message = Message.replace("<gray>", Colors.Gray);
		Message = Message.replace("<green>", Colors.Green);
		Message = Message.replace("<lightblue>", Colors.LightBlue);
		Message = Message.replace("<lightgray>", Colors.LightGray);
		Message = Message.replace("<lightgreen>", Colors.LightGreen);
		Message = Message.replace("<lightpurple>", Colors.LightPurple);
		Message = Message.replace("<navy>", Colors.Navy);
		Message = Message.replace("<purple>", Colors.Purple);
		Message = Message.replace("<red>", Colors.Red);
		Message = Message.replace("<rose>", Colors.Rose);
		Message = Message.replace("<white>", Colors.White);
		Message = Message.replace("<yellow>", Colors.Yellow);
		return Message;
	}
	
	public String priceForm(double price){
		String newprice = String.valueOf(price);
		String[] form = newprice.split("\\.");
		if(form[1].length() == 1){
			newprice += "0";
		}
		else{
			newprice = form[0] + "." + form[1].substring(0, 2);
		}
		return newprice;
	}
	
}
