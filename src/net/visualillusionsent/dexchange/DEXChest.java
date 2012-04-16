package net.visualillusionsent.dexchange;

public class DEXChest {
    protected int x, y, z, dim;
    protected String world;
    protected DEXChest bridge;
    
    public DEXChest(int x, int y, int z, int dim, String world){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.world = world;
    }
    
    public DEXChest (Object chestinv)throws IllegalArgumentException{}
    
    public boolean exists(){
        return bridge.exists();
    }
    
    public void setBridge(DEXChest bridge){
        this.bridge = bridge;
    }
    
    public DEXChest getBridge(){
        return bridge;
    }
    
    public Object getChestInv(){
        return bridge.getChestInv();
    }
    
    public boolean equals(Object obj){
        return bridge.equals(obj);
    }
    
    public int getAmountOf(int id, int damage){
        return bridge.getAmountOf(id, damage);
    }
    
    public boolean hasEnough(int id, int damage, int amount){
        return bridge.hasEnough(id, damage, amount);
    }
    
    public int getX(){
        return x;
    }
    
    public int getY(){
        return y;
    }
    
    public int getZ(){
        return z;
    }
    
    public int getDim(){
        return dim;
    }
    
    public String getWorld(){
        return world;
    }
    
    public DEXItem[] contents(){
        return bridge.contents();
    }
    
    public void removeItem(DEXItem item){
        bridge.removeItem(item);
    }
    
    public int removeItem(int id, int damage, int amount){
        return bridge.removeItem(id, damage, amount);
    }
    
    public int hashCode(){
        return toString().hashCode();
    }
    
    public DEXSign bounceSign(DEXSign sign){
        return bridge.bounceSign(sign);
    }
    
    public String toString(){
        StringBuilder toRet = new StringBuilder();
        toRet.append(x);
        toRet.append(",");
        toRet.append(y);
        toRet.append(",");
        toRet.append(z);
        toRet.append(",");
        toRet.append(dim);
        toRet.append(",");
        toRet.append(world);
        return toRet.toString();
    }
}
