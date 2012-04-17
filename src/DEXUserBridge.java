import net.visualillusionsent.dexchange.DEXItem;
import net.visualillusionsent.dexchange.DEXUser;
import net.visualillusionsent.dexchange.data.DEXProperties;

public class DEXUserBridge implements DEXUser{
    private Player player;
    
    DEXUserBridge(Player player){
        this.player = player;
    }
    
    @Override
    public String getName(){
        return player.getName();
    }
    
    @Override
    public boolean isAdmin(){
        return player.canUseCommand("/dexadmin");
    }
    
    @Override
    public void sendMessage(String message){
        player.sendMessage(message);
    }
    
    @Override
    public void notify(String message){
        player.notify(message);
    }
    
    @Override
    public void charge(double amount){
        etc.getLoader().callCustomHook("dCBalance", new Object[]{ "Player-Charge", player.getName(), amount });
    }
    
    @Override
    public void pay(double amount){
        etc.getLoader().callCustomHook( "dCBalance" , new Object[]{ "Player-Pay", player.getName(), amount });
    }
    
    @Override
    public boolean hasBalance(double amount){
        double balance = (Double) etc.getLoader().callCustomHook( "dCBalance", new Object[]{ "Player-Balance", player.getName() });
        return balance >= amount;
    }

    @Override
    public boolean canCreate(String signtype){
        if(player.canUseCommand("/dexadmin") || player.canUseCommand("/dexall")){
            return true;
        }
        else{
            if(signtype.equalsIgnoreCase("GShop")){
                return player.canUseCommand("/dexpgss");
            }
            else if(signtype.equalsIgnoreCase("GTrade")){
                return player.canUseCommand("/dexpgts");
            }
            else if(signtype.equalsIgnoreCase("PShop")){
                return player.canUseCommand("/dexppss");
            }
            else if(signtype.equalsIgnoreCase("PTrade")){
                return player.canUseCommand("/dexppts");
            }
            else if(signtype.equalsIgnoreCase("SShop")){
                return player.canUseCommand("/dexpsss");
            }
            else if(signtype.equalsIgnoreCase("STrade")){
                return player.canUseCommand("/dexpsts");
            }
                
        }
        return false;
    }
    
    @Override
    public boolean canUse(String type){
        if(player.canUseCommand("/dexadmin") || player.canUseCommand("/dexall")){
            return true;
        }
        else{
            if(type.equalsIgnoreCase("GShop")){
                return player.canUseCommand("/dexugss");
            }
            else if(type.equalsIgnoreCase("GTrade")){
                return player.canUseCommand("/dexugts");
            }
            else if(type.equalsIgnoreCase("PShop")){
                return player.canUseCommand("/dexupss");
            }
            else if(type.equalsIgnoreCase("PTrade")){
                return player.canUseCommand("/dexupts");
            }
            else if(type.equalsIgnoreCase("SShop")){
                return player.canUseCommand("/dexusss");
            }
            else if(type.equalsIgnoreCase("STrade")){
                return player.canUseCommand("/dexusts");
            }
            else if(type.equalsIgnoreCase("buycmd")){
                return player.canUseCommand("/dexbc");
            }
            else if(type.equalsIgnoreCase("sellcmd")){
                return player.canUseCommand("/dexsc");
            }
            else if(type.equalsIgnoreCase("pricechange")){
                return player.canUseCommand("/dexppc");
            }
            else if(type.equalsIgnoreCase("amountchange")){
                return player.canUseCommand("/dexpac");
            }
        }
        return false;
    }
    
    @Override
    public void setInvContents(DEXItem[] dexitems){
        Item[] items = new Item[40];
        
        for(int index = 0; index < 40; index++){
            DEXItem dexitem = dexitems[index];
            items[index] = new Item(dexitem.getId(), dexitem.getAmount(), dexitem.getDamage());
        }
        
        player.getInventory().setContents(items);
    }
    
    @Override
    public DEXItem[] getInvContents(){
        DEXItem[] dexitems = new DEXItem[40];
        Item[] items = player.getInventory().getContents();
        
        for(int index = 0; index < 40; index++){
            dexitems[index] = new DEXItemBridge(items[index]);
        }
        
        return dexitems;
    }

    @Override
    public boolean hasRoom(int id, int damage, int amount){
        int room = 0;
        for (int i = 0; i < 36; i++){
            Item item = player.getInventory().getItemFromSlot(i);
            if (item != null){
                if (item.getItemId() == id){
                    if (item.getDamage() == damage){
                        int am = 64 - item.getAmount();
                        if (am > 0){
                            room += am;
                        }
                    }
                }
            }
            else{
                room += 64;
            }
        }
        return room >= amount;
    }

    @Override
    public void addItems(int id, int damage, int amount) {
        Inventory inv = player.getInventory();
        for (int slot = 0; slot < 36 && amount > 0; slot++){
            Item item = inv.getItemFromSlot(slot);
            if (item != null){
                if (item.getItemId() == id && item.getDamage() == damage){
                    int iam = item.getAmount();
                    if(iam < 64){
                        if (amount > 64){
                            item.setAmount(64);
                            amount -= (64 - iam);
                        }
                        else if((iam+amount) > 64){
                            item.setAmount(64);
                            amount -= (64-iam);
                        }
                        else{
                            item.setAmount(iam+amount);
                            amount = 0;
                            break;
                        }
                    }
                }
            }
        }
        
        while(amount > 0){
            if (amount > 64){
                inv.setSlot(id, 64, damage, inv.getEmptySlot());
                amount -= 64;
            }
            else{
                inv.setSlot(id, amount, damage, inv.getEmptySlot());
                amount = 0;
            }
        }
        inv.update();
    }
    
    @Override
    public void giveItem(DEXItem item){
        player.giveItem((Item)item.getItem());
    }
    
    @Override
    public boolean GlobalHasBalance(double amount){
        if(DEXProperties.gacc.equals("N/A")){
            return true;
        }
        double balance = (Double)etc.getLoader().callCustomHook("dCBalance", new Object[]{ "Joint-Balance-NC", DEXProperties.gacc });
        
        return balance > amount;
    }
    
    @Override
    public void payGlobal(double amount){
        if(!DEXProperties.gacc.equals("N/A")){
            etc.getLoader().callCustomHook("dCBalance", new Object[]{ "Joint-Deposit-NC", DEXProperties.gacc, amount });
        }
    }
    
    @Override
    public void chargeGlobal(double amount){
        if(!DEXProperties.gacc.equals("N/A")){
            etc.getLoader().callCustomHook("dCBalance", new Object[]{ "Joint-Withdraw-NC", DEXProperties.gacc, amount });
        }
    }

    @Override
    public DEXItem getItemInHand() {
        Item item = player.getItemStackInHand();
        if(item != null){
            return new DEXItem(item.getItemId(), item.getAmount(), item.getDamage());
        }
        return null;
    }
    
    @Override
    public void removeItemInHand(){
        player.getItemStackInHand().setAmount(0);
    }
    
    @Override
    public boolean equals(Object obj){
        if(!(obj instanceof DEXUser)){
            return false;
        }
        DEXUser other = (DEXUser) obj;
        return hashCode() == other.hashCode();
    }
    
    @Override
    public int hashCode(){
        return getName().hashCode();
    }
    
    public Object getPlayer(){
        return player;
    }
    
    @Override
    public float getRotation(){
        return player.getRotation();
    }
    
    public double getX(){
        return player.getX();
    }
    
    public double getY(){
        return player.getY();
    }
    
    public double getZ(){
        return player.getZ();
    }
    
    public int getDim(){
        return player.getWorld().getType().toIndex();
    }
    
    public String getWorld(){
        return player.getWorld().getName();
    }
}
