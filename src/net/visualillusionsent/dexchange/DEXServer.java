package net.visualillusionsent.dexchange;

public interface DEXServer {
    
    public DEXSign createSign(Object sign, String[] owners);
    public DEXChest createChest(Object chest);
    public Object getSignObj(int x, int y, int z, int dim, String world);
    public Object getChestObj(int x, int y, int z, int dim, String world);
    public DEXSign setSignPost(int x, int y, int z, int data, int dim, String world);
    public int getChestFaceData(float rot);
}
