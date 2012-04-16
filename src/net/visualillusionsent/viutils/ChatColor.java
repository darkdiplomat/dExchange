package net.visualillusionsent.viutils;

public class ChatColor {
    private ChatColor(){}
    private static final String S = "|";
    
    public static final String BLACK = "\u00A70";
    public static final String NAVY = "\u00A71";
    public static final String GREEN = "\u00A72";
    public static final String BLUE = "\u00A73";
    public static final String RED = "\u00A74";
    public static final String PURPLE = "\u00A75";
    public static final String GOLD = "\u00A76";
    public static final String LIGHT_GRAY = "\u00A77";
    public static final String GRAY = "\u00A78";
    public static final String DARK_PURPLE = "\u00A79";
    public static final String LIGHT_GREEN = "\u00A7a";
    public static final String LIGHT_BLUE = "\u00A7b";
    public static final String ROSE = "\u00A7c";
    public static final String LIGHT_PURPLE = "\u00A7d";
    public static final String YELLOW = "\u00A7e";
    public static final String WHITE = "\u00A7f";
    public static final String BOLD = "\u00A7l";
    public static final String STRIKED = "\u00A7m";
    public static final String UNDERLINED = "\u00A7n";
    public static final String ITALIC = "\u00A7o";
    public static final String MARKER = "\u00A7";
    
    public static final String removeFormating(String str){
        return str.replaceAll(BLACK+S+NAVY+S+GREEN+S+BLUE+S+RED+S+
                             PURPLE+S+GOLD+S+LIGHT_GRAY+S+GRAY+S+DARK_PURPLE+S+
                             LIGHT_GREEN+S+LIGHT_BLUE+S+ROSE+S+LIGHT_PURPLE+S+
                             YELLOW+S+WHITE+S+BOLD+S+UNDERLINED+S+STRIKED+S+ITALIC, "");
    }
}
