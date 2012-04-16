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
        if(misc.isMakingPShop(user)){
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
            user.notify("Price not specified!");
            return true;
        }
        String[] check = parseItemData(sign.getText(1));
        if(check.equals("BLACKLISTED")){
            sign.CancelPlacement();
            return true;
        }
        else if(check.equals("UNKNOWNITEM")){
            sign.CancelPlacement();
            user.notify("ITEM ERROR");
            return true;
        }
        else if(check.equals("NUMBERERROR")){
            sign.CancelPlacement();
            user.notify("NUMBERERROR");
            return true;
        }
        else if(check.equals("FORMATERROR")){
            sign.CancelPlacement();
            user.notify("FORMATERROR");
            return true;
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
        String pname = DEXProperties.fixLongName(user.getName(), false);
        if(pname == null){
            sign.CancelPlacement();
            return true;
        }
        if(check[0].matches("\\w*") && !check[0].matches("\\d*")){
            sign.setText(1, check[0]+":"+check[1]);
        }
        
        sign.setText(0, "§5[P-SHOP]");
        sign.setText(2, priceform.format(price));
        sign.setText(3, pname);
        misc.addMakingPShop(user, sign);
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
        String pname = DEXProperties.fixLongName(user.getName(), true);
        if(sign.getText(3).equals(pname)){
            String[] check = parseItemData(sign.getText(1));
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
                if(chest.getBridge() == null){
                    DEXChest chest2 = sign.bounceChest(chest);
                    chest.setBridge(chest2.getBridge());
                }
                if(chest.getBridge() != null && chest.exists()){
                    contains += chest.getAmountOf(id, damage);
                    numofchests++;
                }
                else{
                    sign.removeAttachedChest(chest);
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
                if(chest.getBridge() == null){
                    DEXChest chest2 = sign.bounceChest(chest);
                    chest.setBridge(chest2.getBridge());
                }
                if(chest.getBridge() != null && chest.exists()){
                    contains += chest.getAmountOf(id, damage);
                }
                else{
                    sign.removeAttachedChest(chest);
                }
            }
            if(contains == 0){
                user.notify("Items Not Found...");
                return true;
            }
            for(DEXChest chest : sign.getAttachedChests()){
                if(chest.getBridge() == null){
                    DEXChest chest2 = sign.bounceChest(chest);
                    chest.setBridge(chest2.getBridge());
                }
                if(chest.getBridge() != null && chest.exists()){
                    checkamount = chest.removeItem(id, damage, checkamount);
                }
                else{
                    sign.removeAttachedChest(chest);
                }
                if(checkamount == 0){
                    break;
                }
            }
            user.addItems(id, damage, amount);
            user.charge(price);
            user.paySign(DEXProperties.getUserNameFromFix(sign.getText(3)), price);
            user.sendMessage("Purchased!");
        }
        return false;
    }
    
    private String[] parseItemData(String line){
        String[] data = line.toUpperCase().split(":");
        try{
            if(line.matches("(\\w*):\\d*") && !line.matches("(\\d*):\\d*")){
                DEXItem item = ds.getItem(data[0].toUpperCase());
                if (item != null){
                    if(!DEXProperties.isBlackListed(item.getId(), item.getDamage())){
                        data[0] = data[0].toUpperCase();
                        Integer.parseInt(data[1]);
                    }
                    else{
                        data[0] = "BLACKLISTED";
                    }
                }
                else{
                    data[0] = "UNKNOWNITEM";
                }
            }
            else if (!line.contains(":")){
                data[0] = "FORMATERROR";
                return data;
            }
            else{
                int id = 0, damage = 0, amount = 0;
                if(line.matches("(\\d*):\\d*")){
                    id = Integer.parseInt(data[0]);
                    amount = Integer.parseInt(data[1]);
                    if(DEXProperties.isBlackListed(id, damage)){
                        data[0] = "BLACKLISTED";
                    }
                    DEXItem item = ds.getItem(id, damage);
                    String lengthcheck = item.getName()+":"+amount;
                    if(lengthcheck.length() < 16){
                        data[0] = item.getName();
                    }
                }
                else{
                    id = Integer.parseInt(data[0]);
                    damage = Integer.parseInt(data[1]);
                    Integer.parseInt(data[2]);
                    if(DEXProperties.isBlackListed(id, damage)){
                        data[0] = "BLACKLISTED";
                    }
                    DEXItem item = ds.getItem(id, damage);
                    String lengthcheck = item.getName()+":"+amount;
                    if(lengthcheck.length() < 16){
                        data = new String[2];
                        data[0] = item.getName();
                        data[1] = String.valueOf(amount);
                    }
                }
            }
        }
        catch(NumberFormatException nfe){
            data[0] = "NUMBERERROR";
        }
        return data;
    }
}
