package net.visualillusionsent.dexchange.signs;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXMisc;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXProperties;

public class STrade {
    
    protected void Use(DEXUser user, DEXChest chest){
        DEXMisc.instance.removeSTSInUse(chest);
        double payout = 0;
        boolean notempty = false, notall = false;
        StringBuilder sold = new StringBuilder();
        for(DEXItem item : chest.getContents()){
            if(item != null){
                notempty = true;
                int id = item.getId(), amount = item.getAmount(), damage = item.getDamage();
                DEXItem itemcheck = DEXProperties.getDataSource().getItem(id, damage);
                if (itemcheck == null){
                    notall = true;
                    user.giveItem(item);
                    chest.removeItem(id, damage, amount);
                    continue;
                }
                double price = itemcheck.getSellPrice();
                if(price <= 0){
                    notall = true;
                    user.giveItem(item);
                    chest.removeItem(id, damage, amount);
                    continue;
                }
                user.sendMessage(""+item.getAmount());
                price *= amount;
                payout += price;
                chest.removeItem(id, damage, amount);
                sold.append(itemcheck.getName()+"("+amount+") ");
            }
        }
        if(notempty){
            user.pay(payout);
            user.sendMessage("Sold for: "+payout);
            if(notall){
                user.sendMessage("Not all sold");
            }
        }
    }
}
