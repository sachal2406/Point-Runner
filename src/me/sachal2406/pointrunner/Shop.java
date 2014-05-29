package me.sachal2406.pointrunner;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class Shop implements Listener {
	
    private Inventory inv;
    ArrayList<Player> invmenu = new ArrayList<Player>();
	
    public Shop(Plugin p) {
        inv = Bukkit.getServer().createInventory(null, 36, "§9§l§nShop");
        
        ItemStack leaveitem = new ItemStack(Material.ARROW, 1);
        ItemMeta leaveitemmeta = leaveitem.getItemMeta();
        leaveitemmeta.setDisplayName("§cQuitter");
        leaveitemmeta.addEnchant(Enchantment.DAMAGE_ALL, 1, true);
        leaveitem.setItemMeta(leaveitemmeta);
        inv.setItem(35, leaveitem);
        
        Bukkit.getServer().getPluginManager().registerEvents(this, p);
        
    }
    
    public void show(Player p) {
        p.openInventory(inv);
    }
    
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
      Player player = (Player)event.getWhoClicked();
      ItemStack clicked = event.getCurrentItem();
      Inventory inventory = event.getInventory();
      
      if (inventory.getName().equals(inv.getName())){ 
             if(clicked.getItemMeta().getDisplayName().contains("§cQuitter")) {
              event.setCancelled(true);
              player.closeInventory();
      	}else {
    	  event.setCancelled(true);
      	}
      }
    }
    

}
