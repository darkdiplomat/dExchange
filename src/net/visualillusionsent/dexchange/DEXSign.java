package net.visualillusionsent.dexchange;

/**
 * dExchange Sign interface class
 * 
 * @author darkdiplomat
 */
public interface DEXSign {
    
    public enum Type{
        GSHOP,
        GTRADE,
        PSHOP,
        PTRADE,
        SSHOP,
        STRADE;
    }
    
    public void attachChest(DEXChest chest);
    public void removeChest(DEXChest chest);
    public boolean isAttached(DEXChest chest);
    public DEXChest[] getAttachedChests();
    
    public boolean isOwner(DEXUser user);
    public boolean isOwner(String username);
    public void addOwner(DEXUser user);
    public void addOwner(String username);
    public void removeOwner(DEXUser user);
    public void removeOwner(String username);
    
    public void setText(int index, String text);
    public String getText(int index);
    public void restoreText();
    public String getTypeFromText();
    public DEXSign.Type getType();
    public boolean isShop();
    public boolean isTrade();
    
    public void update();
    public void CancelPlacement();
    
    public void pay(double amount);
    public void charge(double amount);
    public boolean hasBalance(double amount);
    
    public String getWorld();
    public int getX();
    public int getY();
    public int getZ();
    public int getDim();
    
    public boolean equals(Object obj);
    public int hashCode();
    public String toString();
}
