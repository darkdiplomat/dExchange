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
    
    public double getBuyPrice(int id, int damage){
        if(!items.isEmpty()){
            for(DEXItem dexitem : items){
                if(dexitem.equals(id, damage)){
                    return dexitem.getBuyPrice();
                }
            }
        }
        return -1;
    }
    
    public double getBuyPrice(String name){
        if(!items.isEmpty()){
            for(DEXItem dexitem : items){
                if(dexitem.equals(name)){
                    return dexitem.getBuyPrice();
                }
            }
        }
        return -1;
    }
    
    public double getSellPrice(int id, int damage){
        if(!items.isEmpty()){
            for(DEXItem dexitem : items){
                if(dexitem.equals(id, damage)){
                    return dexitem.getSellPrice();
                }
            }
        }
        return -1;
    }
    
    public double getSellPrice(String name){
        if(!items.isEmpty()){
            for(DEXItem dexitem : items){
                if(dexitem.equals(name)){
                    return dexitem.getSellPrice();
                }
            }
        }
        return -1;
    }
    
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
    
    public DEXSign getSign(DEXChest chest){
        for(DEXSign dexsign : signs){
            if(dexsign.getAttachedChests() != null){
                for(DEXChest dexchests : dexsign.getAttachedChests()){
                    if(dexchests.equals(chest)){
                        return dexsign;
                    }
                }
            }
        }
        return null;
    }
    
    public void removeSign(DEXSign sign){
        signs.remove(sign);
    }
    
    public void addSign(DEXSign sign){
        if(signs.contains(sign)){
            signs.remove(sign);
        }
        signs.add(sign);
    }
    
    public boolean isDEXChestOwner(DEXUser user, DEXChest chest){
        if(user.isAdmin()){
            return true;
        }
        String pname = DEXProperties.fixLongName(user.getName(), true);
        if(pname == null){
            return false;
        }
        synchronized(signs){
            for(DEXSign sign : signs){
                if(sign.getBridge() == null){
                    sign.attachBridge(chest.bounceSign(sign));
                }
                if(sign.getBridge() == null){
                    continue;
                }
                if(sign.isAttached(chest)){
                    if(!sign.getText(3).equals(pname)){
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
