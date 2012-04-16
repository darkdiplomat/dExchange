import net.visualillusionsent.dexchange.DEXChest;
import net.visualillusionsent.dexchange.DEXCommand;
import net.visualillusionsent.dexchange.DEXMisc;
import net.visualillusionsent.dexchange.DEXSign;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXProperties;
import net.visualillusionsent.dexchange.signs.DEXShopSign;
import net.visualillusionsent.dexchange.signs.DEXTradeSign;

public class DEXListener extends PluginListener{
    private DEXMisc misc;
    private PluginLoader loader;
    private Plugin logblock, realms, cuboids, cuboids2, chestlock, lwc, chastity;
    
    public DEXListener(DEXMisc misc){
        this.misc = misc;
        this.loader = etc.getLoader();
        setPlugins();
    }
    
    private void setPlugins(){
        logblock = loader.getPlugin("LogBlock");
        realms = loader.getPlugin("Realms");
        cuboids = loader.getPlugin("CuboidsPlugin");
        cuboids2 = loader.getPlugin("Cuboids2");
        lwc = loader.getPlugin("LWC");
        chastity = loader.getPlugin("ChastityChest");
    }
    
    public boolean onBlockBreak(Player player, Block block){
        if(block.blockType.equals(Block.Type.SignPost) || block.blockType.equals(Block.Type.WallSign)){
            Sign sign = (Sign)block.getWorld().getComplexBlock(block);
            String type = sign.getText(0);
            if(isShop(type) || isTrade(type)){
                DEXUser user = new DEXUserBridge(player);
                DEXSign dexsign = new DEXSignBridge(sign);
                
                if(isShop(type)){
                    return !DEXShopSign.Destroy(user, dexsign);
                }
                else{
                    return !DEXTradeSign.Destroy(user, dexsign);
                }
            }
        }
        else if(block.blockType.equals(Block.Type.Chest)){
            ComplexBlock cb = block.getWorld().getComplexBlock(block);
            DEXChest dexchest = new DEXChestBridge(cb);
            DEXUser user = new DEXUserBridge(player);
            return misc.destroyDEXChest(user, dexchest);
        }
        return false;
    }
    
    public boolean onBlockDestroy(Player player, Block block){
        if(block.blockType.equals(Block.Type.Chest)){
            DEXUser user = new DEXUserBridge(player);
            if(misc.isMakingPShop(user)){
                DEXChest chest = new DEXChestBridge(block.getWorld().getComplexBlock(block));
                misc.handleMakingPShop(user, chest, false);
                return true;
            }
        }
        return false;
    }
    
    public boolean onBlockPlace(Player player, Block bp, Block bc, Item item){
        if(bp.blockType.equals(Block.Type.Chest)){
            if(!isProtectedArea(player, bc)){
                DEXUser user = new DEXUserBridge(player);
                if(misc.isMakingPShop(user)){
                    //TODO Scan for other chests
                    bp.getWorld().setBlock(bp);
                    logBlock(player.getName(), bp);
                    int am = player.getItemStackInHand().getAmount() - 1;
                    player.getItemStackInHand().setAmount(am);
                    DEXChest chest = new DEXChestBridge(bp.getWorld().getComplexBlock(bp));
                    misc.handleMakingPShop(user, chest, true);
                    return true;
                }
                else if(misc.isMakingSTrade(user)){
                    if(!scanForChestsNear(player, bp)){
                        bp.getWorld().setBlock(bp);
                        logBlock(player.getName(), bp);
                        int am = player.getItemStackInHand().getAmount() - 1;
                        player.getItemStackInHand().setAmount(am);
                        DEXChest chest = new DEXChestBridge(bp.getWorld().getComplexBlock(bp));
                        misc.handleMakingSTrade(user, chest);
                        return true;
                    }
                    else{
                        player.notify("Cannot make into DoubleChest!");
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean onBlockRightClick(Player player, Block block, Item item){
        if(block.blockType.equals(Block.Type.SignPost) || block.blockType.equals(Block.Type.WallSign)){
            Sign sign = (Sign)block.getWorld().getComplexBlock(block);
            String type = sign.getText(0);
            if(isShop(type) || isTrade(type)){
                DEXUser user = new DEXUserBridge(player);
                DEXSign dexsign = new DEXSignBridge(sign);
                if(isShop(type)){
                    return DEXShopSign.Use(user, dexsign);
                }
                else{
                    return DEXTradeSign.Use(user, dexsign);
                }
            }
        }
        return false;
    }
    
    public void onCloseInventory(HookParametersCloseInventory closeInventory){
        Inventory inv = closeInventory.getInventory();
        if(inv instanceof Chest){
            Chest chest = (Chest) inv;
            Sign sign = (Sign) chest.getWorld().getComplexBlock(chest.getX(), chest.getY()+1, chest.getZ());
            if(sign != null && sign.getText(0).equals("§6[S-TRADE]")){
                DEXUser user = new DEXUserBridge(closeInventory.getPlayer());
                DEXChest dexchest = new DEXChestBridge(inv);
                DEXTradeSign.UseSTS(user, dexchest);
            }
        }
    }
    
    public boolean onCommand(Player player, String[] args){
        if(args[0].equals("/dex") && args.length > 1){
            DEXUser user = new DEXUserBridge(player);
            String[] newargs = etc.combineSplit(1, args, " ").split(" ");
            return DEXCommand.execute(user, newargs);
        }
        else if(args[0].equalsIgnoreCase("/dExchange")){
            DEXUser user = new DEXUserBridge(player);
            return DEXCommand.execute(user, args);
        }
        return false;
    }
    
    
    public boolean onOpenInventory(HookParametersOpenInventory openInventory){
        Inventory inv = openInventory.getInventory();
        if(inv instanceof Chest){
            Chest chest = (Chest) inv;
            Sign sign = (Sign) chest.getWorld().getComplexBlock(chest.getX(), chest.getY()+1, chest.getZ());
            if(sign != null && sign.getText(0).equals("§6[S-TRADE]")){
                DEXChest dexchest = new DEXChestBridge(inv);
                if(misc.isSTSInUse(dexchest)){
                    return true;
                }
                misc.addSTSInUse(dexchest);
            }
            else if(!DEXProperties.getDataSource().isDEXChestOwner(new DEXUserBridge(openInventory.getPlayer()), new DEXChestBridge(inv))){
                openInventory.getPlayer().notify("ACCESS DENIED!");
                return true;
            }
        }
        else if (inv instanceof DoubleChest){
            if(!DEXProperties.getDataSource().isDEXChestOwner(new DEXUserBridge(openInventory.getPlayer()), new DEXChestBridge(inv))){
                openInventory.getPlayer().notify("ACCESS DENIED!");
                return true;
            }
        }
        return false;
    }
    
    public boolean onSignChange(Player player, Sign sign){
        String type = sign.getText(0);
        if(isMakingShop(type) || isMakingTrade(type)){
            DEXUser user = new DEXUserBridge(player);
            DEXSign dexsign = new DEXSignBridge(sign);
            try{
                if(isMakingShop(type)){
                    return DEXShopSign.Create(user, dexsign);
                }
                else{
                    return DEXTradeSign.Create(user, dexsign);
                }
            }
            catch(Exception e){
                player.sendMessage("EXCEPTION!");
                e.printStackTrace();
                dexsign.CancelPlacement();
                return true;
            }
        }
        return false;
    }
    
    private boolean isMakingShop(String type){
        return type.matches("\\[[GgPpSs]\\-[Ss][Hh][Oo][Pp]\\]");
    }
    
    private boolean isMakingTrade(String type){
        return type.matches("\\[[GgPp]\\-[Tt][Rr][Aa][Dd][Ee]\\]");
    }
    
    private boolean isShop(String type){
        return type.matches("\u00A7[1|5|7]\\[[G|P|S]\\-SHOP\\]");
    }
    
    private boolean isTrade(String type){
        return type.matches("\u00A7[1|9]\\[[G|S]\\-TRADE\\]");
    }
    
    private boolean scanForChestsNear(Player player, Block block){
        int bx = block.getX(), bz = block.getZ();
        Block block2 = player.getWorld().getBlockAt(bx-1, block.getY(), bz);
        if(!(block2.blockType.equals(Block.Type.Chest))){
            block2 = player.getWorld().getBlockAt(bx+1, block.getY(), bz);
        }
        if(!(block2.blockType.equals(Block.Type.Chest))){
            block2 = player.getWorld().getBlockAt(bx, block.getY(), bz-1);
        }
        if(!(block2.blockType.equals(Block.Type.Chest))){
            block2 = player.getWorld().getBlockAt(bx, block.getY(), bz+1);
        }
        if(block2.blockType.equals(Block.Type.Chest)){
            return true;
        }
            
        return false;
    }
    
    private void logBlock(String name, Block block){
        if(logblock != null && logblock.isEnabled()){
            loader.callCustomHook("LogBlockAPI", new Object[] {name, null, block});
        }
    }
    
    @SuppressWarnings("unused")//TODO
    private boolean isProtected(Player player, Block block){
        boolean protect = false, isSet = false;
        if( chastity != null && chastity.isEnabled() ){
            try{
                protect = !(Boolean)loader.callCustomHook("ChastityChest-Check", new Object[]{player, block});
                isSet = true;
            }catch(Exception E){ //API Failed/Non-Existent
                protect = false;
                isSet = false;
            }
        }
        else if(lwc != null && lwc.isEnabled()){
            try{
                protect = !(Boolean)loader.callCustomHook("LWC-AccessCheck", new Object[]{player, block});
                isSet = true;
            }catch(Exception E){ //API Failed/Non-Existent
                protect = false;
                isSet = false;
            }
        }
        else if(chestlock != null && chestlock.isEnabled()){
            try{
                protect = !(Boolean)loader.callCustomHook("ChestLock-API", new Object[] {player, block, "IS_OWNER"});
                isSet = true;
            }catch(Exception E){ //API Failed/Non-Existent
                protect = false;
                isSet = false;
            }
        }
        
        if(!isSet){
            if( realms != null && realms.isEnabled()){
                try{
                    protect = !(Boolean)loader.callCustomHook("Realms-PermissionCheck", new Object[]{"INTERACT", player, block});
                }catch(Exception E){ //API Failed/Non-Existent
                    protect = false;
                }
            }
            else if(cuboids2 != null && cuboids2.isEnabled()){
                try{
                    protect = (Boolean)loader.callCustomHook("CuboidsAPI", new Object[]{"PLAYER_ALLOWED", player, block});
                }catch(Exception E){ //API Failed/Non-Existent
                    protect = false;
                }
            }
            else if(cuboids != null && cuboids.isEnabled()){
                //MEH
            }
        }
        return protect;
    }
    
    private boolean isProtectedArea(Player player, Block block){
        boolean protect = false;
        if(realms != null && realms.isEnabled()){
            try{
                protect = !(Boolean)loader.callCustomHook("Realms-PermissionCheck", new Object[]{"CREATE", player, block});
            }catch(Exception E){ //API Failed/Non-Existent
                protect = false;
            }
        }
        else if(cuboids2 != null && cuboids2.isEnabled()){
            try{
                protect = (Boolean)loader.callCustomHook("CuboidsAPI", new Object[]{"PLAYER_ALLOWED", player, block});
            }catch(Exception E){ //API Failed/Non-Existent
                protect = false;
            }
        }
        else if(cuboids != null && cuboids.isEnabled()){
            //MEH
        }
        return protect;
    }
}
