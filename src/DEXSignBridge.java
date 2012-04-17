import java.util.ArrayList;

import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;

/**
 * dExchange sign bridge class
 * used to handle normal Sign methods from within the packages
 * 
 * @author darkdiplomat
 */
public class DEXSignBridge implements DEXSign {
    private Sign sign;
    private int x, y, z, dim;
    private String world;
    private ArrayList<DEXChest> attachedchests = new ArrayList<DEXChest>();
    private ArrayList<String> signowners = new ArrayList<String>();
    private Type type;
    
    public DEXSignBridge(Object obj, String[] owners){
        this.sign = (Sign) obj;
        this.x = sign.getX();
        this.y = sign.getY();
        this.z = sign. getZ();
        this.dim = sign.getWorld().getType().toIndex();
        this.world = sign.getWorld().getName();
        if(owners != null){
            for(String owner : owners){
                if(!owner.equals("null")){
                    signowners.add(owner);
                }
            }
        }
    }
    
    @Override
    public void attachChest(DEXChest chest){
        attachedchests.add(chest);
    }
    
    @Override
    public void removeChest(DEXChest chest){
        attachedchests.remove(chest);
    }
    
    public boolean isAttached(DEXChest chest){
        return attachedchests.contains(chest);
    }
    
    @Override
    public DEXChest[] getAttachedChests() {
        if(!attachedchests.isEmpty()){
            DEXChest[] attached = new DEXChest[attachedchests.size()];
            for(int index = 0; index < attachedchests.size(); index++){
                attached[index] = attachedchests.get(index);
            }
            return attached;
        }
        return null;
    }
    
    @Override
    public boolean isOwner(DEXUser user) {
        return signowners.contains(user.getName());
    }

    @Override
    public boolean isOwner(String username) {
        return signowners.contains(username);
    }

    @Override
    public void addOwner(DEXUser user) {
        signowners.add(user.getName());
    }

    @Override
    public void addOwner(String username) {
        signowners.add(username);
    }

    @Override
    public void removeOwner(DEXUser user) {
        signowners.remove(user.getName());
    }

    @Override
    public void removeOwner(String username) {
        signowners.remove(username);
    }
    
    @Override
    public void setText(int index, String text){
        sign.setText(index, text);
    }
    
    @Override
    public String getText(int index) {
        return sign.getText(index);
    }
    
    @Override
    public void restoreText(){
        setText(0, sign.getText(0));
        setText(1, sign.getText(1));
        setText(2, sign.getText(2));
        setText(3, sign.getText(3));
        update();
    }
    
    @Override
    public String getTypeFromText() {
        return sign.getText(0).replaceAll("\u00A7[\\d]", "").replace("[", "").replace("]", "").toUpperCase();
    }
    
    @Override
    public Type getType(){
        if(type == null){
            type = DEXSign.Type.valueOf(getTypeFromText());
        }
        return type;
    }
    
    @Override
    public boolean isShop(){
        return type.equals(Type.GSHOP) || type.equals(Type.PSHOP) || type.equals(Type.SSHOP);
    }
    
    @Override
    public boolean isTrade(){
        return type.equals(Type.GTRADE) || type.equals(Type.PTRADE) || type.equals(Type.STRADE);
    }
    
    @Override
    public void update(){
        sign.update();
    }

    @Override
    public void CancelPlacement() {
        sign.getWorld().dropItem(x, y, z, 323);
        sign.getWorld().setBlockAt(0, x, y, z);
    }
    
    @Override
    public String getWorld() {
        return world;
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public int getDim() {
        return dim;
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof DEXSign){
            return ((DEXSign) obj).hashCode() == hashCode();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        String hashget = "dExchange:DarkDiplomat:DEXSign:"+x+":"+y+":"+z+":"+":"+dim+":"+world;
        return hashget.hashCode();
    }
    
    @Override
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
        if(!signowners.isEmpty()){
            for(String owner : signowners){
                toRet.append(owner);
                toRet.append(",");
            }
        }
        else{
            toRet.append("null");
        }
        toRet.append(":");
        if(!attachedchests.isEmpty()){
            for(DEXChest chest : attachedchests){
                toRet.append(chest.toString());
                toRet.append(";");
            }
        }
        return toRet.toString();
    }
}
