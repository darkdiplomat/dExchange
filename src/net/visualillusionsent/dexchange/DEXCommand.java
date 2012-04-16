package net.visualillusionsent.dexchange;

import java.util.logging.Logger;

import net.visualillusionsent.dexchange.messages.ErrorMessages;
import net.visualillusionsent.viutils.Updater;
import net.visualillusionsent.viutils.VersionCheck;

public class DEXCommand {
    public static final String version = "2.0", 
                               name = "dExchange", 
                               downurl = "http://dl.canarymod.net/plugins/get.php?c=e&id=12",
                               checkurl = "http://visualillusionsent.net/cmod_plugins/versions.php?plugin="+name,
                               jarloc = "plugins/dExchange.jar";
    private static final Logger logger = Logger.getLogger("Minecraft");
    private static final VersionCheck vc = new VersionCheck(version, checkurl);
    private static final Updater update = new Updater(downurl, jarloc, name, logger);
    
    public static boolean execute(DEXUser user, String[] args){
        if(args[0].equals("update")){
            Update(user, args);
            return true;
        }
        else if(args[0].equalsIgnoreCase("/dExchange")){
            Check(user);
            return true;
        }
        else if(args[0].equalsIgnoreCase("sts")){
            STS(user);
            return true;
        }
        return false;   
    }
    
    private static void Update(DEXUser user, String[] args){
        boolean forceupdate = false;
        if(args.length > 1){
            if(args[1].equals("force")){
                forceupdate = true;
            }
        }
        if(vc.isLatest() && !forceupdate){
            user.notify("No Update Availible");
            return;
        }
        
        user.notify(update.performUpdate());
    }
    
    private static void Check(DEXUser user){
        user.sendMessage("§2"+name+" v§6"+version+"§2 By: §aDarkDiplomat");
        if(user.isAdmin() && !vc.isLatest()){
            user.notify("There is an update availible! v"+vc.getCurrentVersion());
        }
    }
    
    private static void STS(DEXUser user){
        if(!user.canCreate("STrade")){
            user.sendMessage(ErrorMessages.E101.message());
        }
        if(!DEXMisc.instance.isMakingSTrade(user)){
            DEXMisc.instance.addMakingSTrade(user);
        }
        //String mess = dExD.pmessage(228, "", "");
        user.sendMessage("STS TEST");
    }
}
