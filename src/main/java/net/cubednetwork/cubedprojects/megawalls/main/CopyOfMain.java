/*
 * Copyright 2015 lukacat10 & MaccariTA

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
/*How to add kits?
 * 1.add them to enum;
 * 2.add to getISForKit() (TODO: make getISForKit() part of the enum itself! 
 * & fix all errors before doing so because than you know what needs to be updated! 
 * & update editKitSelectorInv());
 * 3.editKitSelectorInv() is capable of working with up to 18 kittypes and staying organized! change when more than 18!;
 * TODO list:
 * 1.the one in line 2
 * 2.the one in line 134 for finishing the kitselector inventory gui;
 * 3.create a method for the shop inventory gui
 * & make a way to add owned kits for players (a premade method for easy use in the shop!) (LINE 140);
 * 4.Finish with the event so inventories will open on click (371);
 * 5.CloseInventoryEvent to check if editKitSelectorInv() has diffrent info from the one saved in config. also check for inventory clicks to only allow moving of items inside editKitSelectorInv() inventory and not between it and other inventories, and also disallow dropping the items out of the inventory!;
 */
package net.cubednetwork.cubedprojects.megawalls.main;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class CopyOfMain extends JavaPlugin implements Listener {
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;

	Map<Player, Boolean> isPlayerInPvPMode = new HashMap<Player, Boolean>();
	// Inventory Editors:
	public Inventory editKitSelectorInv(){
		//TODO: CHECK IF THIS METHOD RETURNS EMPTY INVENTORY! IF IT IS, THE METHOD MUST BE EXECUTED AGAIN TO WORK!
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "Kit Selector Editor");
		if((getCustomConfig().getString("getinventorykitselector").isEmpty() || getCustomConfig().getString("getinventorykitselector") == null) && getCustomConfig().contains("getinventorykitselector")){
			int[] intarray = {4,5,6,13,14,15,22,23,24,31,32,33,40,41,42,49,50,51};
			List<Integer> slotintlist = new ArrayList<Integer>();
			Map<String, String> slotANDkittype = new HashMap<String, String>();
			for(int i : intarray){
				slotintlist.add(i);
			}
			int ii = 0;
			
			for(KitType kit : KitType.values()){
				if(!(ii > (slotintlist.size() - 1))){
					slotANDkittype.put("" + slotintlist.get(ii), kit.typeName);
//					inv.setItem(slotintlist.get(ii), getISForKit(kit));
					ii = ii + 1;
				}
				else if(ii - (slotintlist.size() - 1) == 1){
					ii =slotintlist.get(slotintlist.size() - 1) + 1;
//					inv.setItem(ii, getISForKit(kit));
					ii = ii + 1;
				}
				else{
					slotANDkittype.put("" + ii, kit.typeName);
//					inv.setItem(ii, getISForKit(kit));
					ii = ii + 1;
				}
			}
			getCustomConfig().createSection("getinventorykitselector.slots", slotANDkittype);
			saveCustomConfig();
			return inv;
		}
		else if(!getCustomConfig().contains("getinventorykitselector")){
			getCustomConfig().createSection("getinventorykitselector");
			int[] intarray = {4,5,6,13,14,15,22,23,24,31,32,33,40,41,42,49,50,51};
			List<Integer> slotintlist = new ArrayList<Integer>();
			Map<String, String> slotANDkittype = new HashMap<String, String>();
			for(int i : intarray){
				slotintlist.add(i);
			}
			int ii = 0;
			
			for(KitType kit : KitType.values()){
				if(!(ii > (slotintlist.size() - 1))){
					slotANDkittype.put("" + slotintlist.get(ii), kit.typeName);
//					inv.setItem(slotintlist.get(ii), getISForKit(kit));
					ii = ii + 1;
				}
				else if(ii - (slotintlist.size() - 1) == 1){
					ii =slotintlist.get(slotintlist.size() - 1) + 1;
//					inv.setItem(ii, getISForKit(kit));
					ii = ii + 1;
				}
				else{
					slotANDkittype.put("" + ii, kit.typeName);
//					inv.setItem(ii, getISForKit(kit));
					ii = ii + 1;
				}
			}
			getCustomConfig().createSection("getinventorykitselector.slots", slotANDkittype);
			saveCustomConfig();
			return inv;
		}
		else{
			Map<Integer, ItemStack> alkt = new HashMap<Integer, ItemStack>();
			for(Map.Entry<String, Object> me : getCustomConfig().getConfigurationSection("getinventorykitselector.slots").getValues(false).entrySet()){
				int keyme = Integer.parseInt(me.getKey());
				String valueme = (String) me.getValue();
				for(KitType kt : KitType.values()){
					if(kt.typeName.equals(me)){
						alkt.put(keyme,getISForKit(kt));
					}
				}
			}
			for(Map.Entry<Integer, ItemStack> me : alkt.entrySet()){
				inv.setItem(me.getKey(), me.getValue());
			}
			return inv;
		}

	}
	// Inventorys (KitSelector + Shop) initialization
//	public void setupKitSelectorInv(){//TODO: FINISH IT!
//		kitSelectorInv = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Kit Selector");
//		kitSelectorInv.setItem(4, arg1);
//	}
	
	public void reloadplayerOwnedKitsMapFromConfig(List<Player> LP){//TODO: make a way to add owned kits for players (a premade method for easy use in the shop!)
		Map<String, Object> MSO = getCustomConfig().getConfigurationSection("playerinfo").getValues(false);
		String puuid;
		if(LP.isEmpty()){
//			for(Map.Entry<String, Object> me : MSO.entrySet()){
//				
//			}
			
			for(Player p : Bukkit.getOnlinePlayers()){
				puuid = p.getUniqueId().toString();
				if(MSO.containsKey(puuid)){
					playerOwnedKits.put(Bukkit.getServer().getPlayer(puuid), ListStringToListType((List<String>)MSO.get(puuid)));
				}
			}
		}
		else{
			for(Player p : LP){
				puuid = p.getUniqueId().toString();
				if(MSO.containsKey(puuid)){
					playerOwnedKits.put(Bukkit.getServer().getPlayer(puuid), ListStringToListType((List<String>)MSO.get(puuid)));
				}
			}
		}
		
	}
	
	public List<KitType> ListStringToListType(List<String> LS){
		List<KitType> LKT = new ArrayList<KitType>();
		for(String s : LS){
			for(KitType kt : KitType.values()){
				if(kt.typeName.equalsIgnoreCase(s)){
					LKT.add(kt);
				}
			}
		}
		return LKT;
	}
	
	Map<Player, List<KitType>> playerOwnedKits = new HashMap<Player, List<KitType>>();
	Inventory kitSelectorInv;
	ArrayList<ItemStack> spawnInventoryItems = new ArrayList<ItemStack>();

	public void spawnInventoryItemsInit() {

		if (getCustomConfig().contains("spawnitems")) {
			ItemStack is = new ItemStack(Material.AIR);
			ItemMeta ismeta = is.getItemMeta();
			String mevalue;
			String mekey;
			for (Map.Entry<String, Object> me : getCustomConfig()
					.getConfigurationSection("spawnitems").getValues(false)
					.entrySet()) {
				mekey = me.getKey();
				mevalue = (String) me.getValue();
				if (mekey.equalsIgnoreCase("kitselector")) {
					// is.setType(Material.getMaterial(mevalue));
					ismeta = is.getItemMeta();
					ismeta.setDisplayName(ChatColor.AQUA + "Kit Selector");
				} else if (mekey.equalsIgnoreCase("shopselector")) {
					// is.setType(Material.getMaterial(mevalue));
					ismeta = is.getItemMeta();
					ismeta.setDisplayName(ChatColor.GREEN + "Shop");
				} else {
					// is.setType(Material.getMaterial(mevalue));
					ismeta = is.getItemMeta();
					ismeta.setDisplayName(ChatColor.DARK_PURPLE + mekey);
				}
				is.setItemMeta(ismeta);
				is.setType(Material.getMaterial(mevalue));
				spawnInventoryItems.add(is);
				is = new ItemStack(Material.AIR);
			}
		} else {
			ItemStack[] is = {};
			is[0] = new ItemStack(Material.WRITTEN_BOOK);
			ItemMeta ismeta = is[0].getItemMeta();
			ismeta.setDisplayName(ChatColor.AQUA + "Kit Selector");
			is[0].setItemMeta(ismeta);
			is[1] = new ItemStack(Material.CHEST);
			ismeta = is[1].getItemMeta();
			ismeta.setDisplayName(ChatColor.GREEN + "Shop");
			is[1].setItemMeta(ismeta);
			spawnInventoryItems.add(is[0]);
			spawnInventoryItems.add(is[1]);
		}

	}

	public void spawnInventory(Player p) {
		Inventory playerInventory = p.getInventory();
		playerInventory.clear();
		for (ItemStack is : spawnInventoryItems) {
			playerInventory.addItem(is);
		}
		
	}

	// Kit initialization
	enum KitType{
		
		SKELETON("SKELETON",(byte) 0, null), 
		SPIDER("SPIDER", (byte) 3, "MHL_Spider"), 
		ZOMBIE("ZOMBIE", (byte) 2, null), 
		HEROBRINE("HEROBRINE", (byte) 3, "Herobrine"), 
		ENDERMAN("ENDERMAN", (byte) 3, "MHL_Enderman"), 
		NONE("NONE", (byte) 3, "Steve");
		
		private final String typeName;
		private final byte typeByte;
		private final String skinName;
		KitType(String tN, byte tB, String sN){
			this.typeName = tN;
			this.typeByte = tB;
			this.skinName = sN;
		}
		
	}

	public ItemStack getISForKit(KitType kt) {
		SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(
				Material.SKULL_ITEM);
		ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);

		switch (kt) {
		case SKELETON:
			is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 0);
			break;
		case SPIDER:
			// is = new ItemStack(Material.SKULL_ITEM,1,(byte) 3);
			meta.setOwner("MHF_Spider");
			is.setItemMeta(meta);
			break;
		case ZOMBIE:
			is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 2);
			break;
		case HEROBRINE:
			meta.setOwner("Herobrine");
			is.setItemMeta(meta);
			break;
		case ENDERMAN:
			meta.setOwner("MHF_Enderman");
			is.setItemMeta(meta);
			break;
		case NONE:
			break;
		default:
			break;
		}
		return is;
	}

	public HashMap<Player, KitType> playerSelectedKit = new HashMap<Player, KitType>();

	// Config initialization
	public void reloadCustomConfig() {
		if (customConfigFile == null) {
			customConfigFile = new File(getDataFolder(), "megaconfig.yml");
		}
		customConfig = YamlConfiguration.loadConfiguration(customConfigFile);
		Reader defConfigStream;
		try {
			defConfigStream = new InputStreamReader(
					this.getResource("megaconfig.yml"), "UTF8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Bukkit.broadcastMessage(ChatColor.DARK_RED
					+ "ERROR AT LINE 61! UNSUPPORTEDENCODING!");
			return;
		}
		if (defConfigStream != null) {
			YamlConfiguration defConfig = YamlConfiguration
					.loadConfiguration(defConfigStream);
			customConfig.setDefaults(defConfig);
		}
	}

	public FileConfiguration getCustomConfig() {
		if (customConfig == null) {
			reloadCustomConfig();
		}
		return customConfig;
	}

	public void saveCustomConfig() {
		if (customConfig == null || customConfigFile == null) {
			return;
		}
		try {
			getCustomConfig().save(customConfigFile);
		} catch (IOException ex) {
			getLogger().log(Level.SEVERE,
					"Could not save config to " + customConfigFile, ex);
		}
	}

	public void saveDefaultCustomConfig() {
		if (customConfigFile == null) {
			customConfigFile = new File(getDataFolder(), "megaconfig.yml");
		}
		if (!customConfigFile.exists()) {
			this.saveResource("megaconfig.yml", false);
		}
	}

	// Server methods:
	@Override
	public void onEnable() {
		reloadCustomConfig();
		spawnInventoryItemsInit();
		reloadplayerOwnedKitsMapFromConfig(new ArrayList<Player>());
		// TODO Auto-generated method stub
		super.onEnable();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	// Basic Events:
	@EventHandler
	public void playerlogsin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		p.teleport(p.getWorld().getSpawnLocation());
		isPlayerInPvPMode.put(p, false);
		spawnInventory(p);
	}
	
	public void kitinventoryselection(InventoryClickEvent e){//TODO: Finish with the event so inventories will open on click
		if(e.getWhoClicked() instanceof Player){
			Player p = (Player) e.getWhoClicked();
			if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Kit Selector")){
				
			}
			if(e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Shop")){
				
			}
		}
	}
	// Commands:
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		// TODO Auto-generated method stub
		if (command.getName().equalsIgnoreCase("reloadspawninventory")) {
			if (sender.isOp()
					|| sender.hasPermission(new Permission(
							"ultrafence.reloadspawninventory"))
					|| sender.hasPermission(new Permission("ultrafence.all"))) {
				spawnInventoryItemsInit();
			}
		}
		return super.onCommand(sender, command, label, args);
	}
}
