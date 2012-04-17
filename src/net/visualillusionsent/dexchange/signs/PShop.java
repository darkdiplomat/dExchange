package net.visualillusionsent.dexchange.signs;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXMisc;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXDataSource;
import net.visualillusionsent.dexchange.data.DEXProperties;
import net.visualillusionsent.dexchange.messages.ErrorMessages;

public class PShop{
    private NumberFormat priceform = new DecimalFormat("0.00");
    private DEXDataSource ds = DEXProperties.getDataSource();
    private DEXMisc misc = DEXMisc.instance;
    
    protected boolean Create(DEXUser user, DEXSign sign){
        if(!user.canCreate("PShop")){
            sign.CancelPlacement();
            user.notify(ErrorMessages.E101.message());
            return true;
        }
        if(misc.isConnectingPShop(user)){
            sign.CancelPlacement();
            user.notify("Already making sign");
            return true;
        }
        if(sign.getText(1).equals("")){
            sign.CancelPlacement();
            user.notify("Item/Amount not specified!");
            return true;
        }
        if(sign.getText(2).equals("")){
            sign.CancelPlacement();
            user.notify("Amount not specified!");
            return true;
        }
        if(sign.getText(3).equals("")){
            sign.CancelPlacement();
            user.notify("Price not specified!");
            return true;
        }
        String itemdata = parseItemData(sign.getText(1));
        if(itemdata.equals("ERROR")){
            sign.CancelPlacement();
            user.notify("SIGN ERROR!");
            return true;
        }
        else if(itemdata.equals("BLACKLISTED")){
            sign.CancelPlacement();
            user.notify("BLACKLISTED ITEM!");
            return true;
        }
        else if(itemdata.matches("[A-Z]*")){
            sign.setText(1, itemdata);
        }
        double price = 0;
        try{
            price = Double.parseDouble(sign.getText(2).replace(",", "."));
        }
        catch (NumberFormatException nfe){
            sign.CancelPlacement();
            return true;
        }
        if(price < 0.01){
            sign.CancelPlacement();
            return true;
        }
        sign.setText(0, "�5[P-SHOP]");
        sign.setText(3, priceform.format(price));
        misc.addConnectingPShop(user, sign);
        user.sendMessage("TEST SUCCESS MESSAGE [PSHOP-1] (CLICK/PLACE CHEST)");
        ds.addSign(sign);
        //dExD.logAct(301, player.getName(),"", "P-SHOP", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", "", "", "");
        //dExD.addPlayerSignTotal(player.getName());
        return false;
    }
    
    protected boolean Use(DEXUser user, DEXSign sign){
        if(!user.canUse("PShop")){
            user.sendMessage(ErrorMessages.E102.message());
            return true;
        }
        String pname = "";//DEXProperties.fixLongName(user.getName(), true);
        if(sign.getText(3).equals(pname)){
            String itemdata = parseItemData(sign.getText(1));
            int id = 0, damage = 0, contains = 0, numofchests = 0;
            if(check[0].equals("BLACKLISTED")){
                user.sendMessage(ErrorMessages.E107.message());
                return true;
            }
            else if(check[0].matches("UNKNOWNITEM|NUMBERERROR|FORMATERROR")){
                user.sendMessage(ErrorMessages.E109.message());
                return true;
            }
            if(sign.getText(1).matches("(\\w*):\\d*") && !sign.getText(1).matches("(\\d*):\\d*")){
                DEXItem item = ds.getItem(check[0]);
                id = item.getId();
                damage = item.getDamage();
            }
            else{
                id = Integer.valueOf(check[0]);
                if(check.length > 2){
                    damage = Integer.valueOf(check[1]);
                }
            }
            for(DEXChest chest : sign.getAttachedChests()){
                if(chest.exists()){
                    contains += chest.getAmountOf(id, damage);
                    numofchests++;
                }
                else{
                    sign.removeChest(chest);
                }
            }
            user.sendMessage(String.valueOf(contains) +" "+String.valueOf(numofchests));
        }
        else{
            String[] check = parseItemData(sign.getText(1));
            if(check[0].equals("BLACKLISTED")){
                user.sendMessage(ErrorMessages.E107.message());
                return true;
            }
            else if(check[0].matches("UNKNOWNITEM|NUMBERERROR|FORMATERROR")){
                user.sendMessage(ErrorMessages.E109.message());
                return true;
            }
            int id = 0, damage = 0, amount = 0, contains = 0;
            DEXItem dexitem;
            if(sign.getText(1).matches("(\\w*):\\d*") && !sign.getText(1).matches("(\\d*):\\d*")){
                dexitem = ds.getItem(check[0]);
                id = dexitem.getId();
                damage = dexitem.getDamage();
                amount = Integer.valueOf(check[1]);
            }
            else{
                id = Integer.valueOf(check[0]);
                if(check.length > 2){
                    damage = Integer.valueOf(check[1]);
                }
                amount = Integer.valueOf(check[2]);
            }
            if (!user.hasRoom(id, damage, amount)){
                user.sendMessage(ErrorMessages.E111.message());
                return false;
            }
            double price = Double.valueOf(sign.getText(2));
            if(!user.hasBalance(price)){
                user.sendMessage(ErrorMessages.E110.message());
                return false;
            }
            int checkamount = amount;
            for(DEXChest chest : sign.getAttachedChests()){
                if(chest.exists()){
                    contains += chest.getAmountOf(id, damage);
                }
                else{
                    sign.removeChest(chest);
                }
            }
            if(contains == 0){
                user.notify("Items Not Found...");
                return true;
            }
            for(DEXChest chest : sign.getAttachedChests()){
                checkamount = chest.removeItem(id, damage, checkamount);
                if(checkamount == 0){
                    break;
                }
            }
            user.addItems(id, damage, amount);
            user.charge(price);
            //user.paySign(DEXProperties.getUserNameFromFix(sign.getText(3)), price);
            user.sendMessage("Purchased!");
        }
        return false;
    }
    
    private String parseItemData(String line){
        int id = 0, damage = 0;
        if(!line.matches("\\d{1,4}") && !line.matches("(\\d{1,4}):\\d{1,5}")){
            line = line.toUpperCase();
            DEXItem dexitem = ds.getItem(line);
            if(dexitem == null){
                return "ERROR";
            }
        }
        else if (line.contains(":")){
            String[] iddamage = line.split(":");
            try{
                id = Integer.parseInt(iddamage[0]);
                damage = Integer.parseInt(iddamage[1]);
            }
            catch(NumberFormatException nfe){
                return "ERROR";
            }
        }
        else{
            try{
                id = Integer.parseInt(line);
            }
            catch(NumberFormatException nfe){
                return "ERROR";
            }
        }
        if(DEXProperties.isBlackListed(id, damage)){
            return "BLACKLISTED";
        }
        DEXItem dexitem = ds.getItem(id, damage);
        if(dexitem != null){
            if(dexitem.getName().length() < 16){
                return dexitem.getName();
            }
        }
        return line;
    }
}
