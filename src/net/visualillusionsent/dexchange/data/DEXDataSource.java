package net.visualillusionsent.dexchange.data;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;

public abstract class DEXDataSource {

    protected final Logger logger = Logger.getLogger("Minecraft");
    protected ArrayList<DEXItem> items = new ArrayList<DEXItem>();
    protected ArrayList<DEXSign> signs = new ArrayList<DEXSign>();
    
    abstract boolean load();
    public abstract void save();
    
    public DEXItem getItem(int id, int damage){
        if(!items.isEmpty()){
            for(DEXItem dexitem : items){
                if(dexitem.equals(id, damage)){
                    return dexitem;
                }
            }
        }
        return null;
    }
    
    public DEXItem getItem(String name){
        if(!items.isEmpty()){
            for(DEXItem dexitem : items){
                if(dexitem.equals(name)){
                    return dexitem;
                }
            }
        }
        return null;
    }
    
    public DEXSign getSign(DEXSign sign){
        if(signs.contains(sign)){
            return signs.get(signs.indexOf(sign));
        }
        return null;
    }
    
    public void removeSign(DEXSign sign){
        signs.remove(sign);
    }
    
    public void addSign(DEXSign sign){
        signs.add(sign);
    }
    
    public void removeChest(DEXChest chest){
        for(DEXSign sign : signs){
            if(sign.isAttached(chest)){
                sign.removeChest(chest);
            }
        }
    }
    
    public boolean isDEXChestOwner(DEXUser user, DEXChest chest){
        if(user.isAdmin()){
            return true;
        }
        boolean isConnected = false;
        synchronized(signs){
            for(DEXSign sign : signs){
                if(sign.isAttached(chest)){
                    isConnected = true;
                    if(sign.isOwner(user.getName())){
                        return true;
                    }
                }
            }
        }
        if(!isConnected){
            return false;
        }
        return true;
    }
}
