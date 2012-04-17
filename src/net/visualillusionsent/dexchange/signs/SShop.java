package net.visualillusionsent.dexchange.signs;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXDataSource;
import net.visualillusionsent.dexchange.data.DEXProperties;
import net.visualillusionsent.dexchange.messages.ErrorMessages;
import net.visualillusionsent.dexchange.messages.Messages;

public class SShop {
    private NumberFormat priceform = new DecimalFormat("0.00");
    private DEXDataSource ds = DEXProperties.getDataSource();
    
    protected boolean Create(DEXUser user, DEXSign sign){
        if(!user.canCreate("SShop")){
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
        
        sign.setText(0, "§7[S-SHOP]");
        sign.setText(3, priceform.format((price*amount)));
        
        user.sendMessage(Messages.M205.message());
        user.sendMessage("TEST SUCCESS MESSAGE [SSHOP-1] (CLICK/PLACE CHEST)");
        ds.addSign(sign);
        //dExD.logAct(301, player.getName(), "", "G-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", "", "", "");
        return false;
    }
    
    protected boolean Use(DEXUser user, DEXSign sign){
        if(!user.canUse("SShop")){
            user.sendMessage(ErrorMessages.E102.message());
            return false;
        }
        DEXItem item = getItem(sign.getText(1));
        if(item == null){
            user.sendMessage(ErrorMessages.E109.message());
            return false;
        }
        double price = item.getBuyPrice();
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
        
        if (!user.hasRoom(item.getId(), item.getDamage(), amount)){
            user.sendMessage(ErrorMessages.E111.message());
            return false;
        }
        int contains = 0;
        for(DEXChest chest : sign.getAttachedChests()){
            if(chest.exists()){
                contains += chest.getAmountOf(item.getId(), item.getDamage());
            }
            else{
                sign.removeChest(chest);
            }
        }
        if(contains == 0){
            user.notify("Items Not Found...");
            return true;
        }
        int checkamount = 0;
        for(DEXChest chest : sign.getAttachedChests()){
            checkamount = chest.removeItem(item.getId(), item.getDamage(), checkamount);
            if(checkamount == 0){
                break;
            }
        }
        user.addItems(item.getId(), item.getDamage(), amount);
        user.charge(price);
        user.payGlobal(price);
        //user.paySign(DEXProperties.getUserNameFromFix(sign.getText(3)), price);
        user.sendMessage("Purchased!");
        return false;
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