import net.visualillusionsent.dexchange.DEXItem;


public class DEXItemBridge extends DEXItem{
    protected Item item;

    public DEXItemBridge(Item item){
        super(item != null ? item.getItemId() : 0, item != null ? item.getAmount() : 0, item != null ? item.getDamage() : 0);
        this.item = item;
    }
    
    @Override
    public Object getItem(){
        return item;
    }
}
