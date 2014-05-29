package me.sachal2406.pointrunner;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerData {

    private static FileConfiguration config = null;
    private static File configFile = null;
    
    public static void load() {
        config = getPlayerData();
        
        config.options().header(
        		"############################################################\n" +
        		"# +------------------------------------------------------+ #\n" +
        		"# |                PointRunner Player Data               | #\n" +
        		"# +------------------------------------------------------+ #\n" +
        		"############################################################");
        
        getPlayerData().options().copyDefaults(true);
        save();
    }
    
    public static void reload() {
        if (configFile == null) {
            configFile = new File("plugins/PointRunner/PlayerData.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public static FileConfiguration getPlayerData() {
        if (config == null) {
            reload();
        }
        return config;
    }
    
    public static void save() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            config.save(configFile);
        } catch (IOException ex) {
            Logger.getLogger(JavaPlugin.class.getName()).log(Level.SEVERE, "Could not save configFile to " + configFile, ex);
        }
    }
    
}
