package net.visualillusionsent.dexchange.signs;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXProperties;

public class DEXTradeSign {
    private static GTrade gtrade = new GTrade();
    private static PShop pshop = new PShop();
    private static STrade strade = new STrade();
    
    public static boolean Create(DEXUser user, DEXSign sign){
        if(sign.getText(0).equalsIgnoreCase("[g-trade]")){
            return gtrade.Create(user, sign);
        }
        else if(sign.getText(0).equalsIgnoreCase("[p-shop]")){
            return pshop.Create(user, sign);
        }
        return false;
    }
    
    public static boolean Use(DEXUser user, DEXSign sign){
        if(sign.getText(0).equals("§2[G-TRADE]")){
            return gtrade.Use(user, sign);
        }
        else if(sign.getText(0).equalsIgnoreCase("§9[P-TRADE]")){
            return pshop.Create(user, sign);
        }
        return false;
    }
    
    public static void UseSTS(DEXUser user, DEXChest chest){
        strade.Use(user, chest);
    }
    
    public static boolean Destroy(DEXUser user, DEXSign sign){
        if(user.isAdmin()){
            DEXProperties.getDataSource().removeSign(sign);
            return true;
        }
        else if(sign.getText(0).equals("§2[G-TRADE]")){
            if(user.canCreate("GTrade")){
                return true;
            }
        }
        else if(sign.getText(0).equals("§9[P-TRADE]")){
            String fix = DEXProperties.fixLongName(user.getName(), true);
            if(user.getName().equals(fix)){
                DEXProperties.getDataSource().removeSign(sign);
                return true;
            }
            else{
                sign.restoreText();
                user.notify("No can destroy!");
            }
        }
        return false;
    }

}
