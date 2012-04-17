package net.visualillusionsent.dexchange;

public interface DEXChest {
    
    public int getAmountOf(int id, int damage);
    public boolean hasEnough(int id, int damage, int amount);
    public int removeItem(int id, int damage, int amount);
    public DEXItem[] getContents();
    
    public boolean exists();
    public boolean equals(Object obj);
    
    public int getX();
    public int getY();
    public int getZ();
    public int getDim();
    public String getWorld();
    
    public int hashCode();
    public String toString();
}
