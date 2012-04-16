package net.visualillusionsent.dexchange;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import net.visualillusionsent.dexchange.data.DEXProperties;

public class DEXMisc {
    public static DEXMisc instance;
    public HashMap<String, DEXSign> makePShop = new HashMap<String, DEXSign>();
    private static ArrayList<DEXUser> makeSTrade = new ArrayList<DEXUser>();
    private static ArrayList<DEXChest> usingSTrade = new ArrayList<DEXChest>();
    private Timer resetSTrade = new Timer();
    
    public DEXMisc(){
        instance = this;
    }
    
    public boolean isMakingPShop(DEXUser user){
        return makePShop.containsKey(user.getName());
    }
    
    public boolean isMakingPShop(String name){
        return makePShop.containsKey(name);
    }
    
    public void addMakingPShop(DEXUser user, DEXSign sign){
        makePShop.put(user.getName(), sign);
    }
    
    public void removeMakingPShop(DEXUser user){
        makePShop.remove(user.getName());
    }
    
    public void removeMakingPShop(String name){
        makePShop.remove(name);
    }
    
    public void handleMakingPShop(DEXUser user, DEXChest chest, boolean placed){
        DEXSign sign = makePShop.get(user.getName());
        if(DEXProperties.getDataSource().isDEXChestOwner(user, chest) || placed){
            sign.attachChest(chest);
            makePShop.remove(user.getName());
            DEXProperties.getDataSource().addSign(sign);
            user.sendMessage("SUCCESS");
            DEXProperties.getDataSource().save();
        }
        else{
            
        }
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
            sign = user.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), (0 | 0x8));
        }
        else if(rot >= 45 && rot < 115){
            sign = user.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), (0 | 0x4));
        }
        else if(rot >= 115 && rot < 225){
            sign = user.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), (0 | 0x0));
        }
        else if(rot >= 225 && rot < 315){
            sign = user.setSignPost(chest.getX(), chest.getY()+1, chest.getZ(), (0 | 0xC));
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
            DEXSign sign = DEXProperties.getDataSource().getSign(chest);
            sign.removeAttachedChest(chest);
            return false;
        }
        return true;
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
