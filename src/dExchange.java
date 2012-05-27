import java.util.logging.Logger;

import net.visualillusionsent.dexchange.DEXCommand;
import net.visualillusionsent.dexchange.DEXMisc;
import net.visualillusionsent.dexchange.data.DEXProperties;
import net.visualillusionsent.viutils.VersionCheck;

public class dExchange extends Plugin{
    private Logger logger = Logger.getLogger("Minecraft");
    private DEXListener dExL;
    private DEXProperties dExP;
    private DEXServerBridge dexserv;
    private boolean isInitialized = false;
    
    public void enable(){
        logger.info(this.getName()+" v"+DEXCommand.version+" by DarkDiplomat enabled!");
    }
    
    public void initialize(){
        VersionCheck vc = new VersionCheck(DEXCommand.version, DEXCommand.checkurl);
        if(!vc.isLatest()){
            logger.info("A new version is available! v"+vc.getCurrentVersion());
        }
        logger.info(this.getName()+" v"+DEXCommand.version+" initializing...");
        dexserv = new DEXServerBridge();
        dExP = new DEXProperties(dexserv);
        if(dExP.load()){
            dExL = new DEXListener(new DEXMisc(), dexserv);
            etc.getLoader().addListener(PluginLoader.Hook.BLOCK_BROKEN, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.BLOCK_DESTROYED, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.BLOCK_PLACE, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.BLOCK_RIGHTCLICKED, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.CLOSE_INVENTORY, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.COMMAND, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.OPEN_INVENTORY, dExL, this, PluginListener.Priority.MEDIUM);
            etc.getLoader().addListener(PluginLoader.Hook.SIGN_CHANGE, dExL, this, PluginListener.Priority.MEDIUM);
            isInitialized = true;
        }
        else{
            etc.getLoader().disablePlugin(this.getName());
        }
    }
    
    public void disable(){
        if(isInitialized){
            try{
                etc.getInstance().removeCommand("/dex");
                etc.getInstance().removeCommand("/dex list");
                etc.getInstance().removeCommand("/dex <item>");
                etc.getInstance().removeCommand("/dex ppricechange");
                etc.getInstance().removeCommand("/dex pamountchange");
                etc.getInstance().removeCommand("/dex cisp");
                etc.getInstance().removeCommand("/dex cibp");
                etc.getInstance().removeCommand("/dex additem");
                etc.getInstance().removeCommand("/dex removeitem");
            }
            catch(Exception e){}
        }
        logger.info(this.getName()+" v"+DEXCommand.version+" disabled!");
    }
}
