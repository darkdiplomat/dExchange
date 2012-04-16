import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXSign;

/**
 * dExchange sign bridge class
 * used to handle normal Sign methods from within the packages
 * 
 * @author darkdiplomat
 */
public class DEXSignBridge extends DEXSign {
    private Sign sign;
    
    public DEXSignBridge(Object obj){
        super(obj);
        this.sign = (Sign) obj;
        this.x = sign.getX();
        this.y = sign.getY();
        this.z = sign. getZ();
        this.dim = sign.getWorld().getType().toIndex();
        this.world = sign.getWorld().getName();
    }
    
    public DEXChest bounceChest(DEXChest chest){
        int x = chest.getX(), y = chest.getY(), z = chest.getZ();
        World[] wor = etc.getServer().getWorld(chest.getWorld());
        if(wor == null){
            return null;
        }
        try{
            if(!wor[chest.getDim()].isChunkLoaded(x, y, z)){
                wor[chest.getDim()].loadChunk(x, y, z);
            }
        }catch(Exception e){}
        Block block = etc.getServer().getWorld(chest.getWorld())[chest.getDim()].getBlockAt(x, y, z);
        if(block.blockType.equals(Block.Type.Chest)){
            ComplexBlock cb = block.getWorld().getComplexBlock(block);
            if(cb != null && (cb instanceof DoubleChest || cb instanceof Chest)){
                DEXChest newchest = new DEXChestBridge(cb);
                return newchest;
            }
        }
        return null;
    }
    
    public DEXSign getBridge(){
        return this;
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
    public void update(){
        sign.update();
    }

    @Override
    public void CancelPlacement() {
        sign.getWorld().dropItem(x, y, z, 323);
        sign.getWorld().setBlockAt(0, x, y, z);
    }
    
    @Override
    public void restoreText(){
        sign.setText(0, sign.getText(0));
        sign.setText(1, sign.getText(1));
        sign.setText(2, sign.getText(2));
        sign.setText(3, sign.getText(3));
        sign.update();
    }
    
    @Override
    public boolean equals(Object obj){
        if(obj instanceof DEXSign){
            return ((DEXSign) obj).hashCode() == hashCode();
        }
        return false;
    }
    
    public int hashCode() {
        String hash = x+y+z+dim+world;
        return hash.hashCode();
    }
}
