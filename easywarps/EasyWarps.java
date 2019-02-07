package im.craig.easywarps;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * A plugin by Cragie
 *
 */
public class EasyWarps extends JavaPlugin {
    MyConfigManager manager; //Creates a config manager
    MyConfig homesConfig; //initialises homesConfig
    MyConfig warpsConfig; //initialises warpsConfig
    
    HashMap<String, Location> playerHomes = new HashMap<String, Location>();
    HashMap<String, Location> playerWarps = new HashMap<String, Location>();
    
    @Override
    public void onEnable() {
        manager = new MyConfigManager(this);
        homesConfig = manager.getNewConfig("home.yml", new String[] { //Creates a file "home.yml" if doesnt exist.
            "This is the player home destinations.",
            "These probably shouldnt be changed manually."});
        warpsConfig = manager.getNewConfig("warps.yml", new String[]{ //Creates a file "warps.yml" if doesnt exist.
            "This is a list of warps.",
            "do not edit these values manually."});
    }
    
    @Override
    public void onDisable() {
        // TODO Insert logic to be performed when the plugin is disabled
        //getLogger().info("onDisable has been invoked!");
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        String easyWarps = ChatColor.DARK_BLUE + "[" + ChatColor.GOLD + "EasyWarps" + ChatColor.DARK_BLUE + "]" + ChatColor.WHITE + ": ";
        if(sender instanceof Player)
        {
            Player player = (Player) sender;
            
            if(label.equalsIgnoreCase("sethome"))
            {
                playerHomes.put(player.getName(), player.getLocation());
                homesConfig.set("Homes." + player.getName(), player.getLocation()); //Creates homes.user.(location)
                homesConfig.saveConfig(); //overwrites the homesConfig.yml in its current state
                player.sendMessage(easyWarps + ChatColor.GOLD + "Home set!");
            }
            if(label.equalsIgnoreCase("home"))
            {
                if(homesConfig.get("Homes." + player.getName()) == null)
                {
                    player.sendMessage(easyWarps + ChatColor.RED + "You do not have a set home.");
                    player.sendMessage(easyWarps + ChatColor.RED + "Use /sethome to set a home.");
                    return true;
                }
                else{
                    Location homeLocation = (Location) homesConfig.get("Homes."+player.getName());
                    player.teleport(homeLocation);
                    player.sendMessage(easyWarps + ChatColor.GOLD + "Welcome home!");
                }
                
                
            }
            
            if(label.equalsIgnoreCase("setwarp"))
            {
                if(args.length == 1)
                {
                    playerWarps.put(args[0], player.getLocation());
                    warpsConfig.set("Warps." + args[0], player.getLocation());
                    warpsConfig.saveConfig();
                    player.sendMessage(easyWarps + ChatColor.GOLD + "The warp " + args[0] + " has been created!");
                }
                else{
                    player.sendMessage(easyWarps + ChatColor.RED + "You need to name the warp!");
                    player.sendMessage(easyWarps + ChatColor.RED + "Usage: /setwarp <warp name>");
                }
            }
            
            if(label.equalsIgnoreCase("warp"))
            {
                if(args.length == 0)
                {
                    player.sendMessage(easyWarps + ChatColor.RED + "You need to type the warp name!");
                    player.sendMessage(easyWarps + ChatColor.RED + "Usage: /warp <warp name>");
                    return true;
                }
                if(args.length == 1)
                {
                    if(warpsConfig.get("Warps." + args[0]) == null)
                    {
                        player.sendMessage(easyWarps + ChatColor.RED + "This warp does not exist.");
                        player.sendMessage(easyWarps + ChatColor.RED + "Try /warps for a list of warps.");
                        return true;
                    }
                    else{
                        Location warpLocation = (Location) warpsConfig.get("Warps."+args[0]); //get location from warpsConfig.yml
                        player.teleport(warpLocation);
                        player.sendMessage(easyWarps + ChatColor.GOLD + "You have warped to " + args[0] + "!");
                        return true;
                    }
                }
                
                if(args.length > 1)
                {
                    player.sendMessage(easyWarps + ChatColor.RED + "Warp names can only contain one word");
                }
                
                else{
                    player.sendMessage(easyWarps + ChatColor.RED + "Something wasnt right there.");
                }
            }
            
            if (label.equalsIgnoreCase("warps")) {
                if (args.length == 0) {
                    player.sendMessage(easyWarps + ChatColor.GOLD +  "List of warps: " + ChatColor.GREEN + warpsConfig.getConfigurationSection("Warps").getKeys(false) + ChatColor.WHITE);
                    return true;
                }
                if (args.length > 0) {
                    player.sendMessage(easyWarps + ChatColor.RED + "command takes 1 argument");
                    return true;
                }
                if (getConfig().getString("warps." + args[0]) == null || getConfig().getString("warps." + args[0]).isEmpty()) {
                    player.sendMessage(easyWarps + ChatColor.RED + "There are no warps set.");
                }
                return true;
            }
            
            if(label.equalsIgnoreCase("delwarp"))
            {
                if(args.length == 1)
                {
                    warpsConfig.getConfigurationSection("Warps").set(args[0], null);
                    warpsConfig.saveConfig();
                    player.sendMessage(easyWarps + ChatColor.GOLD + "The warp " + args[0] + " has been deleted.");
                    
                }
                else{
                    player.sendMessage(easyWarps + ChatColor.RED + "You need to type the warp name!");
                    player.sendMessage(easyWarps + ChatColor.RED + "Usage: /warp <warp name>");
                }
            }
        }
        return true;
    }
    
}

