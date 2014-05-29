package me.sachal2406.pointrunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import me.sachal2406.pointrunner.Shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Main extends JavaPlugin implements Listener{
	public final Logger logger = Logger.getLogger("Minecraft");
	public static Main plugin;
	
	private Shop shop;
	
	FileManager settings = FileManager.getInstance();
	
	
	public void onEnable(){
		Bukkit.getPluginManager().registerEvents(this, this);
		shop = new Shop(this);
		settings.setup(this);
		this.logger.info("[PointRunner] Enabled :)");
	}
	
	public void onDisable(){
		this.logger.info("[PointRunner] Disabled :(");
		
	}
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e){
		e.setJoinMessage(null);
		
		Player p = e.getPlayer();
		
		if (!PlayerData.getPlayerData().contains(p.getName() + ".Coins")) {
			PlayerData.getPlayerData().set(p.getName() + ".Coins", Integer.valueOf(0));
			PlayerData.getPlayerData().set(p.getName() + ".Wins", Integer.valueOf(0));
			PlayerData.getPlayerData().set(p.getName() + ".Losses", Integer.valueOf(0));
			PlayerData.save();
		}
		
		// SCOREBOARD PART BEGGINING
		
		ScoreboardManager manager = Bukkit.getScoreboardManager();
	    Scoreboard board = manager.getNewScoreboard();
	    Objective objective = board.registerNewObjective("stats", "dummy");
	    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
	    objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&l-=STATS=-"));
	    	Score score2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "      &2&l&m---")));
	    	score2.setScore(-1);
	    	Score score3 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&6&lJetons:")));
	    	score3.setScore(-2);
	    	
	    	if (PlayerData.getPlayerData().get(p.getName() + ".Coins") == null) {
	    		Score score4 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l0&l")));
	    		score4.setScore(-3);
	    	} else {
	    		Score score4 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l" + PlayerData.getPlayerData().getInt(new StringBuilder(String.valueOf(p.getName())).append(".Coins").toString()) + "&l")));
	    		score4.setScore(-3);
	    		
	    	} 
	    	Score score5 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "     &2&l&m----")));
	    	score5.setScore(-4);
	    	Score score6 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&6&lVictoires:")));
	    	score6.setScore(-5);
	    	
	    	if (PlayerData.getPlayerData().get(p.getName() + ".Wins") == null) {
	    		Score score7 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l0&r")));
	    		score7.setScore(-6);
	    	} else {
	    		Score score7 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l" + PlayerData.getPlayerData().getInt(new StringBuilder(String.valueOf(p.getName())).append(".Wins").toString()) + "&r")));
	    		score7.setScore(-6);
	    	}
	    	
	    	Score score8 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "    &2&l&m-----")));
	    	score8.setScore(-7);
	    	Score score9 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&6&lDéfaites:")));
	    	score9.setScore(-8);
	    	
	    	if (PlayerData.getPlayerData().get(p.getName() + ".Losses") == null) {
	    		Score score10 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l0&k")));
	    		score10.setScore(-9);
	    	} else {
	    		Score score10 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l" + PlayerData.getPlayerData().getInt(new StringBuilder(String.valueOf(p.getName())).append(".Losses").toString()) + "&k")));
	    		score10.setScore(-9);
	    	}
	    p.setScoreboard(board);
	
		
		// SCOREBOARD PART END
		
		
		if(GameData.getGameData().getString("Main-Lobby.") == null){
			Location WSpawn = p.getWorld().getSpawnLocation();
			String prefix = settings.getConfig().getString("Prefix").replace("&", "§");
			
			p.setGameMode(GameMode.ADVENTURE);
			p.setHealth(20);
			p.setFoodLevel(20);
			p.teleport(WSpawn);
			p.sendMessage(prefix + "§cLobby Principal pas encore défini !!!");
			p.sendMessage(prefix + "§3§l/pr setMainLobby §c pour le définir !!!");
		} else {
		
			Double x = GameData.getGameData().getDouble("Main-Lobby.x");
			Double y = GameData.getGameData().getDouble("Main-Lobby.y");
			Double z = GameData.getGameData().getDouble("Main-Lobby.z");
			Float yaw = (float) GameData.getGameData().getDouble("Main-Lobby.yaw");
			Float pitch = (float) GameData.getGameData().getDouble("Main-Lobby.pitch");
			World w = Bukkit.getServer().getWorld(GameData.getGameData().getString("Main-Lobby.world"));
			Location l = new Location(w, x, y, z, yaw, pitch);
			
        	p.teleport(l);
		p.setGameMode(GameMode.ADVENTURE);
		ItemStack shop = new ItemStack(Material.EMERALD, 1);
		ItemMeta shopmeta = shop.getItemMeta();
		shopmeta.setDisplayName("§2§l§nBoutique§7(Bientôt..)");
		p.setHealth(20);
		p.setFoodLevel(20);
		shop.setItemMeta(shopmeta);
		p.getInventory().addItem(shop);
		p.sendMessage("§7§m-]---======================================---[-");
		p.sendMessage("                            §7[§cPointRunner§7]");
		p.sendMessage("                       §cWelcome §3§l" + p.getDisplayName() + " !");
		p.sendMessage("                  §cOn the server §3§l§nTime2Craft");
		p.sendMessage("                             §aHave fun ! :)");
		p.sendMessage("§7§m-]---======================================---[-");
		
		p.setExp(0);
		p.setLevel(0);
		
		}	
	}

	  
	  @EventHandler
	  public void onPlayerDropItem(PlayerDropItemEvent e){
		  e.setCancelled(true);
	  }
	  
	  @EventHandler
	  public void onPlayerQuit(PlayerQuitEvent e){
		  e.setQuitMessage(null);
		  e.getPlayer().getInventory().clear();
		  e.getPlayer().getInventory().setArmorContents(null);
	  }
	  
	  /*
	   *======================================================================================
	   *
	   *                             ---=== COMMANDS PART !!! ===---
	   *
	   *======================================================================================
	   */
	  
	  
	  
      @Override
      public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
              if (!(sender instanceof Player)) {
                      sender.sendMessage(ChatColor.RED + "Only players can do this.");
                      return true;
              }
             
              Player player = (Player) sender;
              Location l = player.getLocation();
              String prefix = settings.getConfig().getString("Prefix").replace("&", "§");
             
              if (cmd.getName().equalsIgnoreCase("pointrunner") || cmd.getName().equalsIgnoreCase("pr")) {
                  if (args.length == 0) {
                        player.sendMessage("§7§m-]---======================================---[-");
                        player.sendMessage("                            §7[§cPointRunner§7]");
                        player.sendMessage("                       §cPlugin by sachal2406");
                        player.sendMessage("                  §cFor the server §3§l§nTime2Craft");
                        player.sendMessage("                             §aHave fun ! :)");
                        player.sendMessage("§7§m-]---======================================---[-");
                        return true;
                  }if(player.isOp()){     
                  if(args[0].equalsIgnoreCase("setmainlobby")){
                                GameData.getGameData().set("Main-Lobby.world", l.getWorld().getName());
                                GameData.getGameData().set("Main-Lobby.x", l.getX());
                                GameData.getGameData().set("Main-Lobby.y", l.getY());
                                GameData.getGameData().set("Main-Lobby.z", l.getZ());
                                GameData.getGameData().set("Main-Lobby.yaw", l.getYaw());
                                GameData.getGameData().set("Main-Lobby.pitch", l.getPitch());
                                sender.sendMessage(prefix + "Main Lobby defined ! :D");
                                GameData.save();
                                return true;
                                
                                
                  } else if(args[0].equalsIgnoreCase("createArena")){
                	  if(args.length == 2){
                	  if(GameData.getGameData().getString("Arenas."+ args[1] + ".Lobby.world") == null){
                	  	GameData.getGameData().set("Arenas."+ args[1] + ".Lobby.world", l.getWorld().getName());
                	  	GameData.getGameData().set("Arenas."+ args[1] + ".Lobby.x", l.getX());
                	  	GameData.getGameData().set("Arenas."+ args[1] + ".Lobby.y", l.getY());
                      	GameData.getGameData().set("Arenas."+ args[1] + ".Lobby.z", l.getZ());
                      	GameData.getGameData().set("Arenas."+ args[1] + ".Lobby.yaw", l.getYaw());
                      	GameData.getGameData().set("Arenas."+ args[1] + ".Lobby.pitch", l.getPitch());
                      	sender.sendMessage(prefix + "Arena " + args[1] + " created ! :D");
                      	GameData.save();
                         return true;
                	  } else {
                		sender.sendMessage(prefix + "§cThe Arena " + args[1] + " already exists !");  
                	  }
                	  } else {
                		  sender.sendMessage(prefix + "§cUsage: §3/pr createArena <arena>");
                	  }
                	  
                	  
                  } else if(args[0].equalsIgnoreCase("delarena")){
                   if(args.length == 2){
                	if(GameData.getGameData().getString("Arenas."+ args[1] + ".Lobby.world") != null){  
                		GameData.getGameData().set("Arenas."+ args[1], null);
                      	sender.sendMessage(prefix + "§cArena " + args[1] + " deleted !");
                      	GameData.save();
                	} else {
                	   sender.sendMessage(prefix + "§cThe Arena " + args[1] + " doesn't exist !");
                	}
                   } else {
                		sender.sendMessage(prefix + "§cUsage: §3/pr delArena <arena>");
                	}
                  } else if(args[0].equalsIgnoreCase("setSpawn")){
                	  if(args.length == 3){
                	  if(GameData.getGameData().getString("Arenas." + args[1] + ".Spawns."+ args[2]) == null){
                		  if(GameData.getGameData().getString("Arenas." + args[1]) != null){
                			if(isInt(args[2])){
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2] + ".world", l.getWorld().getName());
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2] + ".x", l.getX());
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2] + ".y", l.getY());
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2] + ".z", l.getZ());
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2] + ".yaw", l.getYaw());
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2] + ".pitch", l.getPitch());
                        		sender.sendMessage(prefix + "Spawn " + args[2] + " for the arena " + args[1] + " created !");
                        	GameData.save();
                         return true;
                			} else {
                			sender.sendMessage(prefix + "§c" + args[2] + " §aisn't a number !");
                			}
                		  } else {
                      		sender.sendMessage(prefix + "§cThe Arena " + args[1] + " doesn't exist !");  
                      	  }
                	  } else {
                		sender.sendMessage(prefix + "§cThe Spawn " + args[2] + " already exists !");  
                	  }
                	  } else {
                		  sender.sendMessage(prefix + "§cUsage: §3/pr setSpawn <arena> <spawn>");
                	  }
                	  
                	  
                  } else if(args[0].equalsIgnoreCase("delSpawn")){
                	  if(args.length == 3){
                	  if(GameData.getGameData().getString("Arenas." + args[1] + ".Spawns."+ args[2]) != null){
                		  if(GameData.getGameData().getString("Arenas." + args[1]) != null){
                        	GameData.getGameData().set("Arenas." + args[1] + ".Spawns."+ args[2], null);
                        		sender.sendMessage(prefix + "§cSpawn " + args[2] + " for the arena " + args[1] + " deleted !");
                        	GameData.save();
                        		return true;
                		  } else {
                      		sender.sendMessage(prefix + "§cThe Arena " + args[1] + " doesn't exist !");  
                      	  }
                	  } else {
                		sender.sendMessage(prefix + "§cThe Spawn " + args[2] + " doesn't exists !");  
                	  }
                	  } else {
                		  sender.sendMessage(prefix + "§cUsage: §3/pr delSpawn <arena> <spawn>");
                	  }
                	  
                  } else if(args[0].equalsIgnoreCase("giveCoins")){
                      if (args.length == 3) {
                          String target = args[1];
                          int amount = Integer.parseInt(args[2]);
                          Player player1 = Bukkit.getPlayer(target);
                          if (player1.isOnline()) {
                            int om = PlayerData.getPlayerData().getInt(player1.getName() + ".Coins");
                            int nm = om + amount;
                            PlayerData.getPlayerData().set(player1.getName() + ".Coins", Integer.valueOf(nm));
                            PlayerData.save();
                		  
                			ScoreboardManager manager = Bukkit.getScoreboardManager();
                		    Scoreboard board = manager.getNewScoreboard();
                		    Objective objective = board.registerNewObjective("stats", "dummy");
                		    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                		    objective.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&9&l-=STATS=-"));
                		    	Score score2 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "      &2&l&m---")));
                		    	score2.setScore(-1);
                		    	Score score3 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&6&lJetons:")));
                		    	score3.setScore(-2);
                		    	
                		    	if (PlayerData.getPlayerData().get(player1.getName() + ".Coins") == null) {
                		    		Score score4 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l0&l")));
                		    		score4.setScore(-3);
                		    	} else {
                		    		Score score4 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l" + PlayerData.getPlayerData().getInt(new StringBuilder(String.valueOf(player1.getName())).append(".Coins").toString()) + "&l")));
                		    		score4.setScore(-3);
                		    		
                		    	} 
                		    	Score score5 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "     &2&l&m----")));
                		    	score5.setScore(-4);
                		    	Score score6 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&6&lVictoires:")));
                		    	score6.setScore(-5);
                		    	
                		    	if (PlayerData.getPlayerData().get(player1.getName() + ".Wins") == null) {
                		    		Score score7 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l0&r")));
                		    		score7.setScore(-6);
                		    	} else {
                		    		Score score7 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l" + PlayerData.getPlayerData().getInt(new StringBuilder(String.valueOf(player1.getName())).append(".Wins").toString()) + "&r")));
                		    		score7.setScore(-6);
                		    	}
                		    	
                		    	Score score8 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "    &2&l&m-----")));
                		    	score8.setScore(-7);
                		    	Score score9 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&6&lDéfaites:")));
                		    	score9.setScore(-8);
                		    	
                		    	if (PlayerData.getPlayerData().get(player1.getName() + ".Losses") == null) {
                		    		Score score10 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l0&k")));
                		    		score10.setScore(-9);
                		    	} else {
                		    		Score score10 = objective.getScore(Bukkit.getOfflinePlayer(ChatColor.translateAlternateColorCodes('&', "&f&l" + PlayerData.getPlayerData().getInt(new StringBuilder(String.valueOf(player1.getName())).append(".Losses").toString()) + "&k")));
                		    		score10.setScore(-9);
                		    	}
                		    player1.setScoreboard(board);
                		    player.sendMessage(prefix + "§aDon de §3" + amount + " §ajetons à §3" + player1.getName() + " §aeffectué avec succès!");
                	  }
                		  
                	  } else {
                		  sender.sendMessage(prefix + "§cUsage: §3/pr delSpawn <arena> <spawn>");
                	  }
                  }
                  
                  } else {
                	  player.sendMessage(prefix + "§cTu n'es pas autorisé !");
                  }  
              }
              return false;
      }
	  
      @Override
      public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args) {
    	  if (cmd.getName().equalsIgnoreCase("pointrunner") || cmd.getName().equalsIgnoreCase("pr")) {
                      if (args.length == 1) {
                              ArrayList<String> tabcompl = new ArrayList<String>();
                             
                              if (args[0].equals("")) {
 
                                            	  tabcompl.add("createArena");
                                            	  tabcompl.add("delArena");
                                            	  tabcompl.add("setMainLobby");
                                            	  tabcompl.add("setLobby");
                                            	  tabcompl.add("setSpawn");
                                            	  
                              } else if(args[0].startsWith("c")){
                            	  tabcompl.add("createArena");
                            	   
                              }if(args[0].startsWith("dela") || args[0].startsWith("delA")){
                            	  tabcompl.add("delArena");  
                              } else if(args[0].startsWith("delS") || args[0].startsWith("dels")){
                            	  tabcompl.add("delSpawn");
                              } else {
                            	  if(args[0].startsWith("d") || args[0].startsWith("de") || args[0].startsWith("del")){
                            		  tabcompl.add("delArena"); 
                            	  tabcompl.add("delSpawn"); 
                              }  	  
                              } if(args[0].startsWith("setl") || args[0].startsWith("setL")){
                            	  tabcompl.add("setLobby");
                              } else if(args[0].startsWith("setm") || args[0].startsWith("setM")){
                            	  tabcompl.add("setMainLobby");  
                              } else if(args[0].startsWith("sets") || args[0].startsWith("setS")){
                            	  tabcompl.add("setSpawn");
                              } else {
                            	  if(args[0].startsWith("s") || args[0].startsWith("se") || args[0].startsWith("set")){
                            	  tabcompl.add("setLobby");
                            	  tabcompl.add("setMainLobby");
                            	  tabcompl.add("setSpawn");	  
                              }
                              }

                          
                              Collections.sort(tabcompl);
                             
                              return tabcompl;
                      }
                      
              }
             
              return null;
      }
      
      public static boolean isInt(String s) {
    	    try {
    	        Integer.parseInt(s);
    	    } catch (NumberFormatException nfe) {
    	        return false;
    	    }
    	    return true;
    	}
      
      @EventHandler
      public void onPlayerChat(AsyncPlayerChatEvent e){
    	  if(e.getPlayer().isOp()){
    		  e.setFormat("§7[§c§lADMIN§7] §a" + e.getPlayer().getName() + "§7: §f" + e.getMessage());
    	  
    	  } else {
    		  e.setFormat("§7[§6Player§7] §a" + e.getPlayer().getName() + "§7: " + e.getMessage());
    	  }
      }
      
      @EventHandler
      public void onPlayerBreaksBlock(BlockBreakEvent e){
    	  if(!e.getPlayer().isOp()){
    		  e.setCancelled(true);
    	  }
      }
      @EventHandler
      public void onPlayerPlacesBlock(BlockPlaceEvent e){
    	  if(!e.getPlayer().isOp()){
    		  e.setCancelled(true);
    	  }
      }
      
      @EventHandler
      public void onPlayerLoosesHunger(FoodLevelChangeEvent e){
    	  e.setCancelled(true);
      }
      
      @EventHandler
      public void onPlayerInteract(PlayerInteractEvent e) {
      	Player p = e.getPlayer();
              if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK)){
              	if (!(e.getItem().getType() == Material.EMERALD)) return;
	                		shop.show(p);
	                		
	                		
	                	} 
      }
      
  	public void sendMessage(CommandSender sender, String s){
  		String prefix = settings.getConfig().getString("Prefix").replace("&", "§");
  		
		sender.sendMessage(prefix + ChatColor.GRAY + s);
	}
}
