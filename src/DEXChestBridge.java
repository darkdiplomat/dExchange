import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXSign;

public class DEXChestBridge extends DEXChest{
    private Inventory chestinv;
    
    public DEXChestBridge(Object chestinv) throws IllegalArgumentException{
        super(chestinv);
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

        this.bridge = this;
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
        try{
            if(!wor[dim].isChunkLoaded(x, y, z)){
                wor[dim].loadChunk(x, y, z);
            }
        }catch(Exception e){}
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
    
    public void setBridge(DEXChest bridge){
        this.bridge = this;
    }
    
    public DEXChest getBridge(){
        return this;
    }
    
    @Override
    public Object getChestInv(){
        exists();
        return chestinv;
    }
    
    @Override
    public boolean equals(Object obj){
        return ((DEXChest) obj).hashCode() == hashCode();
    }
    
    public DEXSign bounceSign(DEXSign sign){
        int x = sign.getX(), y = sign.getY(), z = sign.getZ();
        World[] wor = etc.getServer().getWorld(sign.getWorld());
        if(wor == null){
            return null;
        }
        try{
            if(!wor[sign.getDim()].isChunkLoaded(x, y, z)){
                wor[sign.getDim()].loadChunk(x, y, z);
            }
        }catch(Exception e){}
        Block block = etc.getServer().getWorld(sign.getWorld())[sign.getDim()].getBlockAt(x, y, z);
        if(block.blockType.equals(Block.Type.SignPost) || block.blockType.equals(Block.Type.WallSign)){
            ComplexBlock cb = block.getWorld().getComplexBlock(block);
            if(cb != null && (cb instanceof Sign)){
                DEXSign newsign = new DEXSignBridge(cb);
                return newsign;
            }
        }
        return null;
    }
    
    @Override
    public DEXItem[] contents(){
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
    
    @Override
    public void removeItem(DEXItem dexitem){
        if(dexitem.getItem() != null){
            chestinv.removeItem((Item)dexitem.getItem());
        }
        chestinv.update();
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
    public String toString(){
        StringBuilder toRet = new StringBuilder();
        Chest chest = (Chest) chestinv;
        toRet.append(chest.getX());
        toRet.append(",");
        toRet.append(chest.getY());
        toRet.append(",");
        toRet.append(chest.getZ());
        toRet.append(",");
        toRet.append(chest.getWorld().getType().getId());
        toRet.append(",");
        toRet.append(chest.getWorld().getName());
        return toRet.toString();
    }
    
    
}
