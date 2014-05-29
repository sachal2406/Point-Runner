package me.sachal2406.pointrunner;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private static FileConfiguration config = null;
    private static File configFile = null;
    
    static String[] broadcastTimes = { "1", "2", "3", "4", "5", "10", "15", "20", "30", "60", "120" };
    
    public static void load() {
        config = getConfig();
        
        config.options().header(
        		"############################################################\n" +
        		"# +------------------------------------------------------+ #\n" +
        		"# |              PointRunner Configuration               | #\n" +
        		"# +------------------------------------------------------+ #\n" +
        		"############################################################");
        
        config.addDefault("Prefix", "&7[&cPointRunner&7]: &a");
        config.addDefault("minplayers", 12);
        config.addDefault("maxplayers", 24);
        getConfig().options().copyDefaults(true);
        save();
    }
    
    public static void reload() {
        if (configFile == null) {
            configFile = new File("plugins/PointRunner/config.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    public static FileConfiguration getConfig() {
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
