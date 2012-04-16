package net.visualillusionsent.dexchange.signs;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXDataSource;
import net.visualillusionsent.dexchange.data.DEXProperties;
import net.visualillusionsent.dexchange.messages.ErrorMessages;
import net.visualillusionsent.dexchange.messages.Messages;

public class GShop{
    private NumberFormat priceform = new DecimalFormat("0.00");
    private DEXDataSource ds = DEXProperties.getDataSource();
    
    protected boolean Create(DEXUser user, DEXSign sign){
        if(!user.canCreate("GShop")){
            sign.CancelPlacement();
            user.sendMessage(ErrorMessages.E101.message());
            return true;
        }
        
        if(sign.getText(1).equals("") || sign.getText(2).equals("")){
            sign.CancelPlacement();
            user.sendMessage(ErrorMessages.E108.message());
            return true;
        }
        
        int amount = parseAmount(sign.getText(2));
        if(amount == -1){
            user.sendMessage(ErrorMessages.E105.message());
            sign.CancelPlacement();
            return true;
        }
        
        DEXItem item = getItem(sign.getText(1));
        if(item == null){
            sign.CancelPlacement();
            user.sendMessage(ErrorMessages.E104.message());
            return true;
        }
        double price = item.getBuyPrice();
        if(price <= 0){
            sign.CancelPlacement();
            user.sendMessage(ErrorMessages.E107.message());
            return true;
        }
        
        if(item.getName().length() < 16){
            sign.setText(1, item.getName());
        }
        else{
            sign.setText(1, sign.getText(1).toUpperCase());
        }
        
        sign.setText(0, "§1[G-SHOP]");
        sign.setText(3, priceform.format((price*amount)));
        
        user.sendMessage(Messages.M201.message());
        //dExD.logAct(301, player.getName(), "", "G-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", "", "", "");
        return false;
    }
    
    protected boolean Use(DEXUser user, DEXSign sign){
        if(!user.canUse("GShop")){
            user.sendMessage(ErrorMessages.E102.message());
            return false;
        }
        double price = getPrice(sign.getText(1));
        int amount = parseAmount(sign.getText(2));
        
        if(price <= 0){
            user.sendMessage(ErrorMessages.E107.message());
            return true;
        }
        if(amount == -1){
            user.sendMessage(ErrorMessages.E109.message());
            return false;
        }
        price *= amount;
        if(!sign.getText(3).equals(priceform.format(price))){
            sign.setText(3, priceform.format(price));
            user.sendMessage(Messages.M210.message());
            return false;
        }
        if(!user.hasBalance(price)){
            user.sendMessage(ErrorMessages.E110.message());
            return false;
        }
        DEXItem item = getItem(sign.getText(1));
        if(item == null){
            user.sendMessage(ErrorMessages.E109.message());
            return false;
        }
        if (!user.hasRoom(item.getId(), item.getDamage(), amount)){
            user.sendMessage(ErrorMessages.E111.message());
            return false;
        }
        user.payGlobal(price);
        user.addItems(item.getId(), item.getDamage(), amount);
        user.charge(price);
        //String L1 = dExD.pmessage(208, String.valueOf(a), Item.toUpperCase());
        //String L2 = dExD.pmessage(206, priceform.format(price), "");
        user.sendMessage("TEST SUCCESS MESSAGE [GSHOP-2]");
        //player.sendMessage(L2);
        //dExD.logAct(309, player.getName(), "", "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", Item, String.valueOf(a), "");
        return true;
    }
    
    private double getPrice(String line){
        if(!line.matches("\\d{1,4}") && !line.matches("(\\d{1,4}):\\d{1,5}")){
            return ds.getBuyPrice(line.toUpperCase());
        }
        else if (line.contains(":")){
            String[] iddamage = line.split(":");
            int id = 0, damage = 0;
            try{
                id = Integer.parseInt(iddamage[0]);
                damage = Integer.parseInt(iddamage[1]);
                return ds.getBuyPrice(id, damage);
            }
            catch(NumberFormatException nfe){
                return -2;
            }
        }
        else{
            int id = 0;
            try{
                id = Integer.parseInt(line);
                return ds.getBuyPrice(id, 0);
            }
            catch(NumberFormatException nfe){
                return -2;
            }
        }
    }
    
    private DEXItem getItem(String line){
        if(!line.matches("\\d{1,4}") && !line.matches("(\\d{1,4}):\\d{1,5}")){
            return ds.getItem(line.toUpperCase());
        }
        else if (line.contains(":")){
            String[] iddamage = line.split(":");
            int id = 0, damage = 0;
            try{
                id = Integer.parseInt(iddamage[0]);
                damage = Integer.parseInt(iddamage[1]);
                return ds.getItem(id, damage);
            }
            catch(NumberFormatException nfe){
                return null;
            }
        }
        else{
            int id = 0;
            try{
                id = Integer.parseInt(line);
                return ds.getItem(id, 0);
            }
            catch(NumberFormatException nfe){
                return null;
            }
        }
    }
    
    private int parseAmount(String line){
        try{
            return Integer.parseInt(line);
        }
        catch(NumberFormatException nfe){
            return -1;
        }
    }
}
