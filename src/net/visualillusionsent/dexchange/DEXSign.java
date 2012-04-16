package net.visualillusionsent.dexchange;

import java.util.ArrayList;

/**
 * dExchange Sign handling class
 * 
 * @author darkdiplomat
 */
public class DEXSign {
    protected int x, y, z, dim;
    protected String world;
    protected ArrayList<DEXChest> attachedchest = new ArrayList<DEXChest>();
    protected DEXSign bridge; //DEXSignBridge Object
    
    public DEXSign(int x, int y, int z, int dim, String world){
        this.x = x;
        this.y = y;
        this.z = z;
        this.dim = dim;
        this.world = world;
    }
    
    public DEXSign(Object obj){ }
    
    public String getText(int index) {
        return bridge.getText(index);
    }
    
    public void setText(int index, String text) {
        bridge.setText(index, text);
    }
    
    public void update() { 
        bridge.update();
    }
    
    public void attachBridge(DEXSign bridge){
        this.bridge = bridge;
    }
    
    public DEXSign getBridge(){
        return bridge;
    }
    
    public DEXChest bounceChest(DEXChest chest){
        return bridge.bounceChest(chest);
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
    
    public void CancelPlacement() {
        bridge.CancelPlacement();
    }
    
    public DEXChest[] getAttachedChests() {
        DEXChest[] attached = new DEXChest[attachedchest.size()];
        for(int index = 0; index < attachedchest.size(); index++){
            attached[index] = attachedchest.get(index);
        }
        return attached;
    }
    
    public Object getSign() {
        return null;
    }
    
    public boolean equals(Object obj) {
        if(obj instanceof DEXSign){
            return ((DEXSign)obj).hashCode() == hashCode();
        }
        return false;
    }
    
    public DEXSign getSign(DEXChest chest) {
        if(attachedchest.contains(chest)){
            return this;
        }
        return null;
    }
    
    public void attachChest(DEXChest chest) {
        if(!attachedchest.contains(chest)){
            attachedchest.add(chest);
        }
    }
    
    public boolean isAttached(DEXChest chest) {
        return attachedchest.contains(chest);
    }
    
    public void removeAttachedChest(DEXChest chest){
        attachedchest.remove(chest);
    }
    
    public int hashCode() {
        String hash = x+y+z+dim+world;
        return hash.hashCode();
    }
    
    public void restoreText(){
         bridge.restoreText();
    }
    
    public String toString() {
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
        toRet.append(":");
        if(!attachedchest.isEmpty()){
            for(DEXChest chest : attachedchest){
                toRet.append(chest.toString());
                toRet.append(";");
            }
        }
        return toRet.toString();
    }
}
