import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;

public class DEXChestBridge implements DEXChest{
    private Inventory chestinv;
    private int x, y, z, dim;
    private String world;
    
    public DEXChestBridge(Object chestinv){
        if(!(chestinv instanceof Chest)){
            if(!(chestinv instanceof DoubleChest)){
                throw new IllegalArgumentException("Inventory was not an instance of Chest/DoubleChest");
            }
        }
        this.chestinv = (Inventory) chestinv;
        
        if(chestinv instanceof Chest){
            Chest chest = (Chest) this.chestinv;
            this.x = chest.getX();
            this.y = chest.getY();
            this.z = chest.getZ();
            this.dim = chest.getWorld().getType().toIndex();
            this.world = chest.getWorld().getName();
        }
        else{
            DoubleChest chest = (DoubleChest) this.chestinv;
            this.x = chest.getX();
            this.y = chest.getY();
            this.z = chest.getZ();
            this.dim = chest.getWorld().getType().toIndex();
            this.world = chest.getWorld().getName();
        }
    }
    
    @Override
    public int getAmountOf(int id, int damage){
        int iam = 0;
        if(exists()){
            for (int i = 0; i < chestinv.getContentsSize(); i++){
                Item item = chestinv.getItemFromSlot(i);
                if (item != null){
                    if (item.getItemId() == id){
                        if (item.getDamage() == damage){
                            iam += item.getAmount();
                        }
                    }
                }
            }
        }
        return iam;
    }
    
    @Override
    public boolean hasEnough(int id, int damage, int amount){
        int iam = getAmountOf(id, damage);
        return iam >= amount;
    }

    @Override
    public boolean exists() {
        World[] wor = etc.getServer().getWorld(world);
        if(wor == null){
            return false;
        }
        wor[dim].loadChunk(x, y, z);
        Block block = etc.getServer().getWorld(world)[dim].getBlockAt(x, y, z);
        if(block.blockType.equals(Block.Type.Chest)){
            ComplexBlock cb = block.getWorld().getComplexBlock(block);
            if(cb != null && (cb instanceof DoubleChest || cb instanceof Chest)){
                this.chestinv = (Inventory) cb;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public DEXItem[] getContents(){
        if(chestinv != null){
            DEXItem[] dexitems = new DEXItemBridge[chestinv.getContentsSize()];
            int index = 0;
            for(Item item : chestinv.getContents()){
                dexitems[index] = (item != null ? new DEXItemBridge(item) : null);
                index++;
            }
            return dexitems;
        }
        return new DEXItem[1];
    }
    
    public int removeItem(int id, int damage, int amount){
        for (int i = 0; i < chestinv.getContentsSize(); i++){
            if (amount > 0){
                Item item = chestinv.getItemFromSlot(i);
                if(item != null){
                    int ia = item.getAmount();
                    if(item.getItemId() == id){
                        if(item.getDamage() == damage){
                            if(ia <= amount){
                                chestinv.removeItem(i);
                                chestinv.update();
                                amount -= ia;
                            }
                            else{
                                item.setAmount((ia-amount));
                                chestinv.update();
                                amount -= ia;
                            }
                        }
                    }
                }
            }
        }
        return amount;
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
    public String getWorld() {
        return world;
    }
    
    @Override
    public boolean equals(Object obj){
        return ((DEXChest) obj).hashCode() == hashCode();
    }
    
    @Override
    public int hashCode(){
        String hashget = "dExchange:DarkDiplomat:DEXChest:"+x+":"+y+":"+z+":"+":"+dim+":"+world;
        return hashget.hashCode();
    }
    
    @Override
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
