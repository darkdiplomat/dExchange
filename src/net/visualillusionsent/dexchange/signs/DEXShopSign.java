package net.visualillusionsent.dexchange.signs;

import net.visualillusionsent.dexchange.DEXMisc;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXProperties;

public class DEXShopSign {
    private static GShop gshop = new GShop();
    private static PShop pshop = new PShop();
    private static SShop sshop = new SShop();
    
    public static boolean Create(DEXUser user, DEXSign sign){
        if(sign.getText(0).equalsIgnoreCase("[g-shop]")){
            return gshop.Create(user, sign);
        }
        else if(sign.getText(0).equalsIgnoreCase("[p-shop]")){
            return pshop.Create(user, sign);
        }
        else if(sign.getText(0).equalsIgnoreCase("[s-shop]")){
            return sshop.Create(user, sign);
        }
        return false;
    }
    
    public static boolean Use(DEXUser user, DEXSign sign){
        if(sign.getText(0).equals("§1[G-SHOP]")){
            return gshop.Use(user, sign);
        }
        else if(sign.getText(0).equalsIgnoreCase("§5[P-SHOP]")){
            DEXSign sign2 = DEXProperties.getDataSource().getSign(sign);
            if(sign2 != null){
                if(sign2.getBridge() == null){
                    sign2.attachBridge(sign);
                }
                return pshop.Use(user, sign2);
            }
            else{
                user.sendMessage("DERP");
            }
        }
        else if(sign.getText(0).equalsIgnoreCase("§7[S-SHOP]")){
            DEXSign sign2 = DEXProperties.getDataSource().getSign(sign);
            if(sign2 != null){
                if(sign2.getBridge() == null){
                    sign2.attachBridge(sign);
                }
                return sshop.Use(user, sign2);
            }
            else{
                user.sendMessage("DERP");
            }
        }
        return false;
    }
    
    public static boolean Destroy(DEXUser user, DEXSign sign){
        if(user.isAdmin()){
            DEXProperties.getDataSource().removeSign(sign);
            String pname = DEXProperties.getUserNameFromFix(sign.getText(3));
            if(pname != null && DEXMisc.instance.isMakingPShop(pname)){
                DEXMisc.instance.removeMakingPShop(pname);
            }
            return true;
        }
        else if(sign.getText(0).equals("§1[G-SHOP]")){
            if(user.canCreate("GShop")){
                return true;
            }
        }
        else if(sign.getText(0).equals("§5[P-SHOP")){
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
