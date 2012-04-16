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

public class GTrade {
    private NumberFormat priceform = new DecimalFormat("0.00");
    private DEXDataSource ds = DEXProperties.getDataSource();
    
    protected boolean Create(DEXUser user, DEXSign sign){
        if(!user.canCreate("GTrade")){
            sign.CancelPlacement();
            user.sendMessage(ErrorMessages.E101.message());
            return true;
        }
        sign.setText(0, "§2[G-TRADE]");
        sign.setText(1, "Ready to");
        sign.setText(2, "take items!");
        sign.setText(3, "");
        
        user.sendMessage(Messages.M202.message());
        //dExD.logAct(301, player.getName(),"", "G-TRADE", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", "", "", "");
        return false;
    }
    
    protected boolean Use(DEXUser user, DEXSign sign){
        if(!user.canUse("GTrade")){
            user.sendMessage(ErrorMessages.E102.message());
            return false;
        }
        
        DEXItem it = user.getItemInHand();
        if (it == null){ user.sendMessage("No Item In Hand"); return true; }
        
        int amount = it.getAmount();
        double price = getPrice(it.getId(), it.getDamage());
        if(price == 0){
            return false;
        }
        else if(price < 0){
            return false;
        }
        price *= amount;
        if(!user.GlobalHasBalance(price)){
            return false;
        }
        String payment = priceform.format(price);
        user.removeItemInHand();
        user.pay(price);
        user.sendMessage("TEST SUCCESS MESSAGE :"+payment);
        //String L1 = dExD.pmessage(210, String.valueOf(amount), name);
        //String L2 = dExD.pmessage(206, priceForm(price), "");
        //player.sendMessage(L1);
        //player.sendMessage(L2);
        //dExD.logAct(308, player.getName(), "", "", String.valueOf(sign.getX()), String.valueOf(sign.getY()), String.valueOf(sign.getZ()), String.valueOf(sign.getWorld().getType().name()), "", "", "", "", name, String.valueOf(amount), "");
        return true;
    }
    
    private double getPrice(int id, int damage){
        return ds.getSellPrice(id, damage);
    }
}
