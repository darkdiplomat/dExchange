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

public class dExData {
    protected dExchange dEx;
	private final String dir = "plugins/config/dExchange/";
	private final String IF = "plugins/config/dExchange/ItemNameFile.txt";
	private final String SCF = "plugins/config/dExchange/ChestTrade.txt";
	private final String SettingFile = "plugins/config/dExchange/dExSettings.ini";
	private final String Messages = "plugins/config/dExchange/Messages.txt";
	private final String PSigns = "plugins/config/dExchange/PSigns.txt";
	private PropertiesFile Setting;
	private String Account = "N/A";
	private String BlackList = "";
	private dExItems dExI = new dExItems();
	private dExChest dExC;
	private dExSign dExS;
	private HashMap<Chest, dExChest> Chests;
	private HashMap<Sign, dExSign> Signs;
	private HashMap<Player, Sign> PS;
	private ArrayList<String> WholeList;
	protected HashMap<String, int[]> IDD;
	private HashMap<String, Double> BuyPrice;
	private HashMap<String, Double> SellPrice;
	private FileUtility fu = new FileUtility();
	private ArrayList<Integer> BlackListedIDs;
	protected boolean MySQL, CMySQL, LogAc;
	private String DataBase = "jdbc:mysql://localhost:3306/minecraft";
	private String UserName = "root";
	private String Password = "root";
	private String Driver = "com.mysql.jdbc.Driver";
	private PropertiesFile NameFix;
	private PropertiesFile Mess;
	private int MaxSigns = 0;
	private PropertiesFile PlayerSign;
	//private PropertiesFile Items;
	private dExMessages dExM;
	
	public dExData(dExchange dEx){
	    this.dEx = dEx;
		makedirectory();
		Setting = new PropertiesFile(SettingFile);
		NameFix = new PropertiesFile("plugins/config/dExchange/SignNameFixer.txt");
		Mess = new PropertiesFile(Messages);
		PlayerSign = new PropertiesFile(PSigns);
		//Items = new PropertiesFile(IF);
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
				dEx.log.warning("[dExchange] - Unable to create properties file! Using Defaults");
			}
		}
		if(!ItemF.exists()){
			dExI.makeItemsFile(IF);
		}
		if(!SCFile.exists()){
			try {
				SCFile.createNewFile();
			} catch (IOException e) {
				dEx.log.warning("[dExchange] - Unable to create ChestTrade File");
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
				dEx.log.warning("[dExchange] - Unable to load Settings File. Using defaults");
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
				dEx.log.severe("[dExchange] - Unable to find driver class: " + Driver);
				dEx.log.severe("[dExchange] - Disabling SQL");
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
		dExM = new dExMessages(this, Mess);
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
	
	private String LoadStringCheck(PropertiesFile props, String defaultvalue, String Property){
		String value;
		if(props.containsKey(Property)){
			value = props.getString(Property);
		}else{
		    dEx.log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
			value = defaultvalue;
		}
		return value;
	}
	
	private boolean LoadBooleanCheck(boolean defaultvalue, String Property){
		boolean value;
		if(Setting.containsKey(Property)){
			value = Setting.getBoolean(Property);
		}else{
		    dEx.log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
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
			    dEx.log.warning("[dExchange] Bad Value at "+Property+" Using default of "+String.valueOf(defaultvalue));
				value = defaultvalue;
			}
		}
		else{
		    dEx.log.warning("[dExchange] - Value: "+Property+" not found! Using default of "+String.valueOf(defaultvalue));
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					PreparedStatement ps = conn.prepareStatement("SELECT * FROM dExchange");
					ResultSet rs = ps.executeQuery();
					while (rs.next()){
						try{
							String name = rs.getString("Name");
							int[] IDDA = new int[]{rs.getInt("ItemID"), rs.getInt("Damage")};
							double buy = rs.getDouble("BuyPrice");
							double sell =  rs.getDouble("SellPrice");
							String get = name+"="+IDDA[0]+":"+IDDA[1]+","+buy+","+sell;
							IDD.put(name, IDDA);
							BuyPrice.put(name, buy);
							SellPrice.put(name, sell);
							WholeList.add(get);
						} catch (NumberFormatException NFE){
						    dEx.log.severe("[dExchange] - Unable to get data from dExchange!");
							continue;
						}
					}
				} catch (SQLException ex) {
				    dEx.log.severe("[dExchange] - Unable to get data from dExchange!");
				}finally{
					try{
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
					    dEx.log.severe("[dExchange] - Could not close connection to SQL");
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
						    dEx.log.severe("[dExchange] - There was an issue with ID for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
						    dEx.log.severe("[dExchange] - There was an issue with ID for ItemName:" + name);
							continue;
						}
						try{
							damage = Integer.parseInt(IDsplit[1]);
						}catch(NumberFormatException nfe){
						    dEx.log.severe("[dExchange] - There was an issue with Damage for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
						    dEx.log.severe("[dExchange] - There was an issue with Damage for ItemName:" + name);
							continue;
						}
						try{
							buy = Double.parseDouble(idpricesplit[1]);
						}catch(NumberFormatException nfe){
						    dEx.log.severe("[dExchange] - There was an issue with BuyPrice for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
						    dEx.log.severe("[dExchange] - There was an issue with BuyPrice for ItemName:" + name);
							continue;
						}
						try{
							sell = Double.parseDouble(idpricesplit[2]);
						}catch(NumberFormatException nfe){
						    dEx.log.severe("[dExchange] - There was an issue with SellPrice for ItemName:" + name);
							continue;
						}catch(IndexOutOfBoundsException IOOBE){
						    dEx.log.severe("[dExchange] - There was an issue with SellPrice for ItemName:" + name);
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
			    dEx.log.severe("[dExchange] - Unable to load in Items File");
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
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
	    		    dEx.log.severe("[dExchange] - Unable to get data from dExchange!");
	    		}finally{
	    			try{
	    				if (conn != null){
	    					conn.close();
	    				}
	    			}catch (SQLException sqle) {
	    			    dEx.log.severe("[dExchange] - Could not close connection to SQL");
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
			    dEx.log.severe("[dExchange] - Unable to load ChestTrade File");
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
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
	    		    dEx.log.severe("[dExhange] - Unable to add data to dExchangeChests!");
	    		}finally{
	    			try{
	    				if (conn != null){
	    					conn.close();
	    				}
	    			}catch (SQLException sqle) {
	    			    dEx.log.severe("[dExchange] - Could not close connection to SQL");
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
			    dEx.log.severe("[dExchange] - Unable to Add TradeLink!");
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
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
	    		    dEx.log.severe("[dExchange] - Unable to delete data from dExchangeChests!");
	    		}finally{
	    			try{
	    				if (conn != null){
	    					conn.close();
	    				}
	    			}catch (SQLException sqle) {
	    			    dEx.log.severe("[dExchange] - Could not close connection to SQL");
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
				if(!key.getWorld().getChunk(key.getX(), key.getY(), key.getZ()).isLoaded()){
					key.getWorld().loadChunk(key.getX(), key.getY(), key.getZ());
				}
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					ps = conn.prepareStatement("UPDATE dExchange SET BuyPrice = ? WHERE Name = ? LIMIT 1");
					ps.setDouble(1, bp);
					ps.setString(2, IName);
					ps.executeUpdate();
				} catch (SQLException ex) {
				    dEx.log.severe("[dExchange] - Unable to delete Item from dExchange!");
				}finally{
					try{
						if (ps != null){
							ps.close();
						}
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
					    dEx.log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			PropertiesFile ITF = new PropertiesFile(IF);
			ITF.setString(IName, idd[0]+","+priceForm(bp)+","+priceForm(sp));
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					PreparedStatement ps = conn.prepareStatement("UPDATE dExchange SET SellPrice = ? WHERE Name = ? LIMIT 1");
					ps.setDouble(1, sp);
					ps.setString(2, IName);
					ps.executeUpdate();
				} catch (SQLException ex) {
				    dEx.log.severe("[dExchange] - Unable to delete Item from dExchange!");
				}finally{
					try{
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
					    dEx.log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			PropertiesFile ITF = new PropertiesFile(IF);
			ITF.setString(IName, idd[0]+","+priceForm(bp)+","+priceForm(sp));
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
				conn = null;
			}
			if(conn != null){
				try{
					PreparedStatement ps = conn.prepareStatement("DELETE FROM dExchange WHERE Name = ?");
					ps.setString(1, IName);
					ps.executeUpdate();
				} catch (SQLException ex) {
				    dEx.log.severe("[dExchange] - Unable to delete Item from dExchange!");
				}finally{
					try{
						if (conn != null){
							conn.close();
						}
					}catch (SQLException sqle) {
					    dEx.log.severe("[dExchange] - Could not close connection to SQL");
					}
				}
			}
		}
		else{
			PropertiesFile ITF = new PropertiesFile(IF);
			ITF.removeKey(IName);
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
			    dEx.log.severe("[dExchange] - Unable to set MySQL Connection");
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
	    		    dEx.log.severe("[dExhange] - Unable to add Item to Item Lists!");
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
			    dEx.log.severe("[dExhange] - Unable to add Item to Item Lists!");
			}
		}
		IDD.put(IName, new int[]{id, dam});
		BuyPrice.put(IName, bp);
		dEx.log.info(String.valueOf(BuyPrice.get(IName)));
		SellPrice.put(IName, sp);
		WholeList.add(Line);
	}
	
	public void logAct(int Code, String P1, String P2, String T, String SX, String SY, String SZ, String SW, String CX, String CY, String CZ, String CW, String I, String A, String PR){
		dExM.logAct(Code, P1, P2, T, SX, SY, SZ, SW, CX, CY, CZ, CW, I, A, PR);
	}
	
	public boolean ErrorMessage(Player player, int code){
		return dExM.ErrorMessage(player, code);
	}
	
	public String pmessage(int code, String A, String I){
		return dExM.pmessage(code, A, I);
	}
	
	public String SpecM(int code, String X, String Y, String Z, String W){
		return dExM.SpecM(code, X, Y, Z, W);
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
