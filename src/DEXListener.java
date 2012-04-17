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
    private DEXServerBridge dexserv;
    private PluginLoader loader;
    private Plugin logblock, realms, cuboids, cuboids2, chestlock, lwc, chastity;
    
    public DEXListener(DEXMisc misc, DEXServerBridge dexserv){
        this.misc = misc;
        this.dexserv = dexserv;
        this.loader = etc.getLoader();
    }
    
    protected void setPlugins(){
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
            DEXSign dexsign = new DEXSignBridge(sign, null);
            if(dexsign.isShop() || dexsign.isTrade()){
                DEXUser user = new DEXUserBridge(player);
                if(dexsign.isShop()){
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
            if(misc.isConnectingPShop(user)){
                DEXChest chest = new DEXChestBridge(block.getWorld().getComplexBlock(block));
                misc.handlePShopConnections(user, chest, false);
                return true;
            }
        }
        return false;
    }
    
    public boolean onBlockPlace(Player player, Block bp, Block bc, Item item){
        if(bp.blockType.equals(Block.Type.Chest)){
            if(!isProtectedArea(player, bc)){
                DEXUser user = new DEXUserBridge(player);
                if(misc.isConnectingPShop(user)){
                    //TODO Scan for other chests
                    player.getWorld().setBlock(bp);
                    bp.setData(DEXProperties.dexserv.getChestFaceData(player.getRotation()));
                    bp.update();
                    //logBlock(player.getName(), bp);
                    int am = player.getItemStackInHand().getAmount() - 1;
                    player.getItemStackInHand().setAmount(am);
                    DEXChest chest = new DEXChestBridge(bp.getWorld().getComplexBlock(bp));
                    misc.handlePShopConnections(user, chest, true);
                    return true;
                }
                else if(misc.isMakingSTrade(user)){
                    if(!dexserv.scanForChestsNear(player, bp)){
                        player.getWorld().setBlock(bp);
                        bp.setData(dexserv.getChestFaceData(player.getRotation()));
                        bp.update();
                        //logBlock(player.getName(), bp);
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
                else if(dexserv.scanForSTradeChestsNear(player, bp)){
                    player.notify("Cannot make into DoubleChest!");
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean onBlockRightClick(Player player, Block block, Item item){
        if(block.blockType.equals(Block.Type.SignPost) || block.blockType.equals(Block.Type.WallSign)){
            Sign sign = (Sign)block.getWorld().getComplexBlock(block);
            DEXSign dexsign = new DEXSignBridge(sign, null);
            if(dexsign.isShop() || dexsign.isTrade()){
                DEXUser user = new DEXUserBridge(player);
                if(dexsign.isShop()){
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
        DEXSign dexsign = new DEXSignBridge(sign, null);
        if(dexsign.isShop() || dexsign.isTrade()){
            DEXUser user = new DEXUserBridge(player);
            try{
                if(dexsign.isShop()){
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
