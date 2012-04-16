package net.visualillusionsent.dexchange.messages;

import net.visualillusionsent.viutils.ChatColor;

public enum Messages {
    
    M201(ChatColor.LIGHT_BLUE+"Global Shop Sign Created!"),
    M202(ChatColor.LIGHT_BLUE+"Global Trade Sign Created!"),
    M203(ChatColor.LIGHT_BLUE+"Player Shop Sign Created!"),
    M204(ChatColor.LIGHT_BLUE+"Player Trade Sign Created!"),
    M205(ChatColor.LIGHT_BLUE+"Server Shop Sign Created!"),
    M206(ChatColor.LIGHT_BLUE+"Server Trade Chest Created!"),
    M207(ChatColor.LIGHT_BLUE+"Left Click/Place a chest to complete the link."),
    M208(ChatColor.LIGHT_BLUE+"Link Created!"),
    M209(ChatColor.ROSE+"Link canceled!"),
    M210(ChatColor.LIGHT_BLUE+"Global price was updated. Click sign again if you accept the new price."),
    M211(""),
    M212(""),
    M213(""),
    M214(""),
    M215(""),
    M216(""),
    M217(""),
    M218(""),
    M219(""),
    M220(""),
    M221(""),
    M222(""),
    M223(""),
    M224(""),
    M225(""),
    M226(""),
    M227(""),
    M228(""),
    M229(""),
    M230(""),
    M231(""),
    M232(""),
    M233(""),
    M234(""),
    M235(""),
    M236(""),
    M237("");
    
    
    private String message;
    private final String prefix = ChatColor.GREEN+"["+ChatColor.GOLD+"dEx"+ChatColor.GREEN+"] ";
    
    private Messages(String message){
        this.message = formatMessage(message);
    }
    
    private String formatMessage(String message){
        message = message.replace("<black>", ChatColor.BLACK);
        message = message.replace("<navy>", ChatColor.NAVY);
        message = message.replace("<green>", ChatColor.GREEN);
        message = message.replace("<blue>", ChatColor.BLUE);
        message = message.replace("<red>", ChatColor.RED);
        message = message.replace("<purple>", ChatColor.PURPLE);
        message = message.replace("<gold>", ChatColor.GOLD);
        message = message.replace("<lightgray>", ChatColor.LIGHT_GRAY);
        message = message.replace("<gray>", ChatColor.GRAY);
        message = message.replace("<darkpurple>", ChatColor.DARK_PURPLE);
        message = message.replace("<lightgreen>", ChatColor.LIGHT_GREEN);
        message = message.replace("<lightblue>", ChatColor.LIGHT_BLUE);
        message = message.replace("<rose>", ChatColor.ROSE);
        message = message.replace("<lightpurple>", ChatColor.LIGHT_PURPLE);
        message = message.replace("<yellow>", ChatColor.YELLOW);
        message = message.replace("<white>", ChatColor.WHITE);
        message = message.replace("<bold>", ChatColor.BOLD);
        message = message.replace("<striked>", ChatColor.STRIKED);
        message = message.replace("<underlined>", ChatColor.UNDERLINED);
        message = message.replace("<italic>", ChatColor.ITALIC);
        return message;
    }
    
    final void setMessage(String message){
        this.message = message;
    }
    
    public final String message(){
        return prefix+message;
    }
}
