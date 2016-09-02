package net.visualillusionsent.dexchange.messages;

import net.visualillusionsent.viutils.ChatColor;

public enum ErrorMessages {
    
    E101("You do not have permission to place this sign!"),
    E102("You do not have permission to use this sign!"),
    E103("You do not have permission to use this command!"),
    E104("You did not specify a valid item!"),
    E105("You did not specify a valid amount!"),
    E106("You did not specify a valid price!"),
    E107("Specified item is not allowed to be sold!"),
    E108("You formatted the sign incorrectly!"),
    E109("An error on the sign prevents use..."),
    E110("You do not have enough money..."),
    E111("You do not have enough room..."),
    E112(""),
    E113(""),
    E114(""),
    E115(""),
    E116(""),
    E117(""),
    E118(""),
    E119(""),
    E120(""),
    E121(""),
    E122(""),
    E123(""),
    E124(""),
    E125(""),
    E126(""),
    E127(""),
    E128(""),
    E129(""),
    E130(""),
    E131(""),
    E132(""),
    E133(""),
    E134(""),
    E135(""),
    E136(""),
    E137("");
    
    
    private String message;
    private final String prefix = ChatColor.GREEN+"["+ChatColor.GOLD+"dEx"+ChatColor.GREEN+"]"+ChatColor.ROSE+" ";
    
    private ErrorMessages(String message){
        this.message = message;
    }
    
    final void setMessage(String message){
        this.message = message;
    }
    
    public final String message(){
        return prefix+message;
    }
}
