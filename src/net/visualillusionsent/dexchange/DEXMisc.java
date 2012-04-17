package net.visualillusionsent.dexchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.visualillusionsent.dexchange.data.DEXProperties;

public class DEXMisc {
    public static DEXMisc instance;
    public HashMap<String, DEXSign> connectPShop = new HashMap<String, DEXSign>();
    public HashMap<String, DEXSign> connectSShop = new HashMap<String, DEXSign>();
    private ArrayList<DEXUser> makeSTrade = new ArrayList<DEXUser>();
    private ArrayList<DEXChest> usingSTrade = new ArrayList<DEXChest>();
    private Timer resetSTrade = new Timer();
    
    public DEXMisc(){
        instance = this;
    }
    
    public boolean isConnectingPShop(DEXUser user){
        return connectPShop.containsKey(user.getName());
    }
    
    public boolean isConnectingPShop(String name){
        return connectPShop.containsKey(name);
    }
    
    public void addConnectingPShop(DEXUser user, DEXSign sign){
        connectPShop.put(user.getName(), sign);
    }
    
    public void removeConnectingPShop(DEXUser user){
        connectPShop.remove(user.getName());
    }
    
    public void removeConnectingPShop(String name){
        connectPShop.remove(name);
    }
    
    public void cancelConnectingPShop(DEXSign sign){
        if(connectPShop.containsValue(sign)){
            for(String username : connectPShop.keySet()){
                if(connectPShop.get(username).equals(sign)){
                    connectPShop.remove(username);
                    break;
                }
            }
        }
    }
    
    public void handlePShopConnections(DEXUser user, DEXChest chest, boolean placed){
        DEXSign sign = connectPShop.get(user.getName());
        if(DEXProperties.getDataSource().isDEXChestOwner(user, chest) || placed){
            sign.attachChest(chest);
            connectPShop.remove(user.getName());
            DEXProperties.getDataSource().addSign(sign);
            user.sendMessage("SUCCESS");
            DEXProperties.getDataSource().save();
        }
        else{
            user.notify("NOT CHEST OWNER!");
        }
    }
    
    public void handleSShopConnections(DEXUser user, DEXChest chest, boolean placed){
        DEXSign sign = connectSShop.get(user.getName());
        if(DEXProperties.getDataSource().isDEXChestOwner(user, chest) || placed){
            sign.attachChest(chest);
            connectSShop.remove(user.getName());
            DEXProperties.getDataSource().addSign(sign);
            user.sendMessage("SUCCESS");
            DEXProperties.getDataSource().save();
        }
        user.notify("NOT CHEST OWNER!");
    }
    
    public boolean isMakingSTrade(DEXUser user){
        return makeSTrade.contains(user);
    }
    
    public void addMakingSTrade(DEXUser user){
        makeSTrade.add(user);
    }
    
    public void removeMakingSTrade(DEXUser user){
        makeSTrade.remove(user);
    }
    
    public void handleMakingSTrade(DEXUser user, DEXChest chest){
        float rot = user.getRotation();
        if(rot < 0){ rot *= -1; } //Fix negative rotation
        DEXSign sign = null;
        if((rot >= 0 && rot < 45) || (rot >= 315 && rot < 361)){
            sign = DEXProperties.dexserv.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), 0x8, user.getDim(), user.getWorld());
        }
        else if(rot >= 45 && rot < 115){
            sign = DEXProperties.dexserv.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), 0x4, user.getDim(), user.getWorld());
        }
        else if(rot >= 115 && rot < 225){
            sign = DEXProperties.dexserv.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), 0x0, user.getDim(), user.getWorld());
        }
        else if(rot >= 225 && rot < 315){
            sign = DEXProperties.dexserv.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), 0xC, user.getDim(), user.getWorld());
        }
        if(sign != null){
            sign.setText(0, "§6[S-TRADE]");
            sign.setText(1, "§2Ready To");
            sign.setText(2, "§2Accept");
            sign.setText(3, "~~~~~~~~");
            sign.update();
            removeMakingSTrade(user);
            //dExD.logAct(301, player.getName(),"", "S-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", "", "", "");
        }
        else{
            user.notify("An error occurred");
        }
    }
    
    public void addSTSInUse(DEXChest chest){
        usingSTrade.add(chest);
        resetSTrade.schedule(new resetSTS(chest), 300000L); //Remove chest if taking longer than 5mins to use
    }
    
    public void removeSTSInUse(DEXChest chest){
        usingSTrade.remove(chest);
    }
    
    public boolean isSTSInUse(DEXChest chest){
        return usingSTrade.contains(chest);
    }
    
    public boolean destroyDEXChest(DEXUser user, DEXChest chest){
        if(DEXProperties.getDataSource().isDEXChestOwner(user, chest)){
            DEXProperties.getDataSource().removeChest(chest);
            return false;
        }
        return true;
    }
    
    public boolean isTool(int id){
        switch (id){
            case 256:
            case 258:
            case 269:
            case 270:
            case 271:
            case 273:
            case 274:
            case 275:
            case 277:
            case 278:
            case 279:
            case 284:
            case 285:
            case 286:
            case 290:
            case 291:
            case 292:
            case 293:
            case 294: 
                return true;
            default : 
                return false;
        }
    }
    
    private class resetSTS extends TimerTask{
        DEXChest chest;
        
        public resetSTS(DEXChest chest){
            this.chest = chest;
        }
        
        public void run(){
            if(usingSTrade.contains(chest)){
                usingSTrade.remove(chest);
            }
        }
    }
}
