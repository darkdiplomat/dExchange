package net.visualillusionsent.dexchange.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXSign;

public class DEXFlatFile extends DEXDataSource{
    private final File ItemsFile = new File(DEXProperties.dirloc+"ItemNameFile.txt");
    private Properties Items = new Properties();
    private final File SCFile = new File(DEXProperties.dirloc+"ChestTrade.txt");
    
    public DEXFlatFile(){}
    
    boolean load(){
        if(!loadItems()){
            return false;
        }
        if(!loadSignsChests()){
            return false;
        }
        return true;
    }
    
    private boolean loadItems(){
        logger.info("[dExchange] Attempting to load ItemNameFile.txt...");
        if(!ItemsFile.exists()){
            logger.info("[dExchange] ItemNameFile.txt not found! Creating...");
            try{
                InputStream in = getClass().getClassLoader().getResourceAsStream("ItemNameFile.txt");
                FileWriter out = new FileWriter(ItemsFile);
                int c;
                while ((c = in.read()) != -1){
                    out.write(c);
                }
                in.close();
                out.close();
            } 
            catch (IOException e){
                logger.info("[dExchange] Failed to create ItemNameFile.txt... dExchange will now be terminated...");
                return false;
            }
            logger.info("[dExchange] ItemNameFile.txt created successfully!");
        }
        synchronized(items){
            try {
                FileInputStream stream = new FileInputStream(ItemsFile);
                Items.load(stream);
            } catch (IOException e) {
                logger.info("[dExchange] Failed to load ItemNameFile.txt... dExchange will now be terminatedd...");
                return false;
            }
            for(Object key : Items.keySet()){
                String itemname = (String) key;
                String[] info = Items.getProperty(itemname).split(",");
                int id = -1, damage = 0;
                double buyprice = -1, sellprice = -1;
                try{
                    if(info[0].contains(":")){
                        id = Integer.valueOf(info[0].split(":")[0].trim());
                        damage = Integer.valueOf(info[0].split(":")[1].trim());
                    }
                    else{
                        id = Integer.valueOf(info[0].trim());
                    }
                    buyprice = Double.valueOf(info[1].trim());
                    sellprice = Double.valueOf(info[2].trim());
                }
                catch(NumberFormatException nfe){
                    logger.warning("[dExchange] Failure reading line for Item: "+itemname+" (Are you sure you formatted it correctly?)");
                    continue;
                }
                catch(ArrayIndexOutOfBoundsException aioobe){
                    logger.warning("[dExchange] Failure reading line for Item: "+itemname+" (Are you sure you formatted it correctly?)");
                    continue;
                }
                
                if(id > 0 && damage > -1){
                    DEXItem dexitem = new DEXItem(id, damage, buyprice, sellprice, itemname.toUpperCase());
                    if(!items.contains(dexitem)){
                        items.add(dexitem);
                    }
                }
            }
        }
        logger.info("[dExchange] ItemNameFile.txt loaded successfully!");
        return true;
    }
    
    private boolean loadSignsChests(){ //TODO Messages
        try{
            if(!SCFile.exists()){
                SCFile.createNewFile();
            }
            else{
                BufferedReader in = new BufferedReader(new FileReader(SCFile));
                String line;
                while((line = in.readLine()) != null){
                    String[] sc = line.split(":");
                    String[] siloc = sc[0].split(",");
                    Object sign = DEXProperties.dexserv.getSignObj(Integer.valueOf(siloc[0]), Integer.valueOf(siloc[1]), Integer.valueOf(siloc[2]), Integer.valueOf(siloc[3]), siloc[4]);
                    if(sign != null){
                        DEXSign dexsign = DEXProperties.dexserv.createSign(sign, new String[0]);
                        String[] chlocs = sc[1].split(";");
                        for(String chloc : chlocs){
                            String[] loc = chloc.split(",");
                            Object chest = DEXProperties.dexserv.getChestObj(Integer.valueOf(loc[0]), Integer.valueOf(loc[1]), Integer.valueOf(loc[2]), Integer.valueOf(loc[3]), loc[4]);
                            if(chest != null){
                                DEXChest dexchest = DEXProperties.dexserv.createChest(chest);
                                dexsign.attachChest(dexchest);
                            }
                        }
                        signs.add(dexsign);
                    }
                }
                in.close();
            }
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

    @Override
    public void save() {
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(SCFile));
            for(DEXSign sign : signs){
                out.write(sign.toString()); out.newLine();
            }
            out.close();
        }
        catch(IOException ioe){
            
        }
    }
}
