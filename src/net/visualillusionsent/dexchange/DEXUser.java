package net.visualillusionsent.dexchange;

/**
 * An interface to CanaryMod Player
 * 
 * @author darkdiplomat
 */
public interface DEXUser {
    
    public String getName();
    public boolean isAdmin();
    public void sendMessage(String message);
    public void notify(String message);
    public void charge(double amount);
    public void pay(double amount);
    public boolean hasBalance(double amount);
    public boolean canCreate(String signtype);
    public boolean canUse(String type);
    public void setInvContents(DEXItem[] dexitems);
    public DEXItem[] getInvContents();
    public boolean hasRoom(int id, int damage, int amount);
    public void addItems(int id, int damage, int amount);
    public boolean GlobalHasBalance(double amount);
    public void payGlobal(double amount);
    public void chargeGlobal(double amount);
    public DEXItem getItemInHand();
    public void removeItemInHand();
    public boolean equals(Object obj);
    public Object getPlayer();
    public int hashCode();
    public float getRotation();
    public void giveItem(DEXItem item);
    public double getX();
    public double getY();
    public double getZ();
    public int getDim();
    public String getWorld();
}
