package net.visualillusionsent.dexchange.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Logger;

import net.visualillusionsent.dexchange.DEXServer;

public class DEXProperties {
    public static final Logger logger = Logger.getLogger("Minecraft");
    public static final String dirloc = "plugins/config/dExchange/";
    public static String gacc;
    public static boolean logact;
    public static int gmaxs;
    public static DEXServer dexserv;
    
    private final File dir = new File(dirloc);
    private final File readme = new File(dirloc+"README.txt");
    private final File settings = new File(dirloc+"dExSettings.ini");
    private final File messages = new File(dirloc+"Messages.txt");
    private Properties dExSettings = new Properties();
    private String database, username, password, driver;
    private boolean mysql, cmysql;
    private static ArrayList<Integer[]> blacklist = new ArrayList<Integer[]>();
    
    private static DEXDataSource dexds;
    
    public DEXProperties(DEXServer dexserver){
        dexserv = dexserver;
    }
    
    public static DEXDataSource getDataSource(){
        return dexds;
    }
    
    public boolean load(){
        if(!dir.exists()){
            logger.info("[dExchange] First Launch Detected! Creating directory...");
            if(dir.mkdirs()){
                logger.info("[dExchange] Directory created successfully!");
                moveReadMe();
                if(!moveSettings()){
                    return false;
                }
                if(!moveMessages()){
                    return false;
                }
            }
            else{
                logger.severe("[dExchange] Failed to created directory! dExchange will now be terminated...");
                return false;
            }
        }
        
        if(!loadSettings()){
            return false;
        }
        if(!loadMessages()){
            return false;
        }
        if(!dexds.load()){
            return false;
        }
        //Everything loaded!
        return true;
    }
    
    private void moveReadMe(){
        try{
            InputStream in = getClass().getClassLoader().getResourceAsStream("dExchangeREADME.txt");
            FileWriter out = new FileWriter(readme);
            int c;
            while ((c = in.read()) != -1){
                out.write(c);
            }
            in.close();
            out.close();
        } 
        catch (IOException e){ }
    }
    
    private boolean moveSettings(){
        logger.info("[dExchange] Creating settings file...");
        try{
            InputStream in = getClass().getClassLoader().getResourceAsStream("SettingsTemplate.ini");
            FileWriter out = new FileWriter(settings);
            int c;
            while ((c = in.read()) != -1){
                out.write(c);
            }
            in.close();
            out.close();
        } 
        catch (IOException e){
            logger.severe("[dExchange] - Unable to create settings file! dExchange will now be terminated...");
            return false;
        }
        logger.info("[dExchange] Settings file created successfully!");
        return true;
    }
    
    private boolean moveMessages(){
        logger.info("[dExchange] Creating messages file...");
        try{
            InputStream in = getClass().getClassLoader().getResourceAsStream("MessagesTemplate.txt");
            FileWriter out = new FileWriter(messages);
            int c;
            while ((c = in.read()) != -1){
                out.write(c);
            }
            in.close();
            out.close();
        } 
        catch (IOException e){
            logger.severe("[dExchange] - Unable to create messages file! dExchange will now be terminated...");
            return false;
        }
        logger.info("[dExchange] Messages file created successfully!");
        return true;
    }
    
    private boolean loadSettings(){
        logger.info("[dExchange] Loading settings...");
        try{
            FileInputStream stream = new FileInputStream(settings);
            dExSettings.load(stream);
            stream.close();
        }
        catch(IOException ex){
            logger.info("[dExchange] Failed to load settings! dExchange will now be terminated...");
            return false;
        }
        
        gacc = parseString("N/A", "GlobalAccount");
        logact = parseBoolean(false, "LogActions");
        gmaxs = parseInteger(-1, "MaxSigns");
        
        String blacked = parseString("7,8,9,10,46", "BlackListedItems");
        if(blacked.length() > 0){
            String[] blackened = blacked.split(",");
            for(String black : blackened){
                Integer[] blackitem = new Integer[2];
                try{
                    if(black.contains(":")){
                        String[] blackeneditem = black.split(":");
                        blackitem[0] = Integer.valueOf(blackeneditem[0]);
                        blackitem[1] = Integer.valueOf(blackeneditem[1]);
                    }
                    else{
                        blackitem[0] = Integer.valueOf(black);
                        blackitem[1] = 0;
                    }
                }
                catch (NumberFormatException nfe){
                    logger.warning("[dExchange] Failure reading BlackListedItem: "+blackened+" (Are you sure you formatted it correctly?)");
                    continue;
                }
                catch (ArrayIndexOutOfBoundsException aioobe){
                    logger.warning("[dExchange] Failure reading BlackListedItem: "+blackened+" (Are you sure you formatted it correctly?)");
                    continue;
                }
                blacklist.add(blackitem);
            }
        }

        mysql = parseBoolean(false, "Use-MySQL");
        cmysql = parseBoolean(false, "Use-CanaryMySQL");
        
        if(mysql){
            username = parseString(username, "UserName");
            password = parseString(password, "Password");
            database = parseString(database, "DataBase");
            driver = parseString(driver, "SQLDriver");
            mysql = verifyMySQL();
        }
        
        if(mysql){
            dexds = new DEXMySQL();
        }
        else{
            dexds = new DEXFlatFile();
        }
        //The first person to comment in the video about this line will get there name somewhere in the code of dExchange :3 TODO!
        return true;
    }
    
    private String parseString(String defaultvalue, String property){
        String value;
        if(dExSettings.containsKey(property)){
            value = dExSettings.getProperty(property).trim();
            if(value.equals("")){
                logger.warning("[dExchange] Bad Value at "+property+" Using default of "+String.valueOf(defaultvalue));
                value = defaultvalue;
            }
        }
        else{
            logger.warning("[dExchange] Value for: "+property+" not found! Using default of "+String.valueOf(defaultvalue));
            value = defaultvalue;
        }
        return value;
    }
    
    private boolean parseBoolean(boolean defaultvalue, String property){
        boolean value;
        if(dExSettings.containsKey(property)){
            value = Boolean.valueOf(dExSettings.getProperty(property).trim());
        }else{
            logger.warning("[dExchange] Value: "+property+" not found! Using default of "+String.valueOf(defaultvalue));
            value = defaultvalue;
        }
        return value;
    }
    
    private int parseInteger(int defaultvalue, String property){
        int value;
        if(dExSettings.containsKey(property)){
            try{
                value = Integer.valueOf(dExSettings.getProperty(property).trim());
            }catch(NumberFormatException NFE){
                logger.warning("[dExchange] Bad Value at "+property+" Using default of "+String.valueOf(defaultvalue));
                value = defaultvalue;
            }
        }
        else{
            logger.warning("[dExchange] Value: "+property+" not found! Using default of "+String.valueOf(defaultvalue));
            value = defaultvalue;
        }
        return value;
    }
    
    private boolean verifyMySQL(){
        logger.info("[dExchange] Checking MySQL Settings...");
        FileInputStream stream = null;
        Properties csql = new Properties();
        if(cmysql){
            try {
                stream = new FileInputStream("mysql.properties");
                csql.load(stream);
                database = csql.getProperty("db");
                username = csql.getProperty("user");
                password = csql.getProperty("pass");
                driver = csql.getProperty("driver");
                stream.close();
            } 
            catch (IOException ex) {
                logger.warning("[dExchange] Unable to load Canary's MySQL Settings...");
                logger.warning("[dExchange] Disabling MySQL...");
                return false;
            }
        }
        try {
            Class.forName(driver);
        } 
        catch (ClassNotFoundException cnfe) {
            logger.warning("[dExchange] Unable to find driver class: " + driver);
            logger.warning("[dExchange] Disabling MySQL...");
            return false;
        }
        return true;
    }
    
    private boolean loadMessages(){
        
        return true;
    }
    
    public static boolean isBlackListed(int id, int damage){
        for(Integer[] blacklisted : blacklist){
            if(blacklisted[0] == id && blacklisted[1] == damage){
                return true;
            }
        }
        return false;
    }
}
