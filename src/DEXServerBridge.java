import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXServer;
import net.visualillusionsent.dexchange.DEXSign;

public class DEXServerBridge implements DEXServer {
    
    @Override
    public DEXSign createSign(Object sign, String[] owners) {
        return new DEXSignBridge(sign, owners);
    }

    @Override
    public DEXChest createChest(Object chest) {
        return new DEXChestBridge(chest);
    }

    @Override
    public Object getSignObj(int x, int y, int z, int dim, String world) {
        World[] wor = etc.getServer().getWorld(world);
        if(wor != null){
            wor[dim].loadChunk(x, y, z);
            Block block = wor[dim].getBlockAt(x, y, z);
            if(block.blockType.equals(Block.Type.SignPost) || block.blockType.equals(Block.Type.WallSign)){
                Sign sign = (Sign)wor[dim].getComplexBlock(block);
                if(sign.getText(0).matches("")){
                    return sign;
                }
            }
        }
        return null;
    }

    @Override
    public Object getChestObj(int x, int y, int z, int dim, String world) {
        World[] wor = etc.getServer().getWorld(world);
        if(wor != null){
            wor[dim].loadChunk(x, y, z);
            Block block = wor[dim].getBlockAt(x, y, z);
            if(block.blockType.equals(Block.Type.Chest)){
                ComplexBlock cb = wor[dim].getComplexBlock(block);
                return cb;
            }
        }
        return null;
    }
    
    @Override
    public DEXSign setSignPost(int x, int y, int z, int data, int dim, String world) {
        Block block = new Block();
        block.setType(63);
        block.setX(x);
        block.setY(y);
        block.setZ(z);
        block.setData(data);
        etc.getServer().getWorld(world)[dim].setBlock(block);
        Sign sign = (Sign)etc.getServer().getWorld(world)[dim].getComplexBlock(block);
        if(sign != null){
            return new DEXSignBridge(sign, null);
        }
        return null;
    }
    
    public int getChestFaceData(float rot){
        if((rot >= 0 && rot < 45) || (rot >= 315 && rot < 361)){
            return 0x3;
        }
        else if(rot >= 45 && rot < 115){
            return 0x5;
        }
        else if(rot >= 115 && rot < 225){
            return 0x2;
        }
        else if(rot >= 225 && rot < 315){
            return 0x4;
        }
        else{
            return 0x2;
        }
    }
}
