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
 * 2.add to getISForKit() (TODO: make getISForKit() part of the enum itself!  Done! getISForKitV2()!
 * & fix all errors before doing so because than you know what needs to be updated! 
 * & update editKitSelectorInv());
 * 3.editKitSelectorInv() is capable of working with up to 18 kittypes and staying organized! change when more than 18!;
 * TODO list:
 * 1.(V)the one in line 3
 * 2.the one in setupKitSelectorInv() for finishing the kitselector inventory gui;
 * 3.create a method for the shop inventory gui
 * & make a way to add owned kits for players (a premade method for easy use in the shop!) (IN reloadplayerOwnedKitsMapFromConfig());
 * 4.Finish with the event so inventories will open on click (IN kitinventoryselection());
 * 5.CloseInventoryEvent to check if editKitSelectorInv() has diffrent info from the one saved in config. also check for inventory clicks to only allow moving of items inside editKitSelectorInv() inventory and not between it and other inventories, and also disallow dropping the items out of the inventory!;
 * Permissions: ultrafence.reloadspawninventory, ultrafance.*, ultrafence.all
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
import java.util.UUID;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	private FileConfiguration customConfig = null;
	private File customConfigFile = null;
	
	
	
	//VARIABLES SECTION:::::::::::::::::::::::::::::::::::::::
	ArrayList<ItemStack> spawnInventoryItems = new ArrayList<ItemStack>();
	Map<Player, List<KitType>> playerOwnedKits = new HashMap<Player, List<KitType>>();
	public HashMap<Player, KitType> playerSelectedKit = new HashMap<Player, KitType>();
	Map<Player, Boolean> isPlayerInPvPMode = new HashMap<Player, Boolean>();
	
	
	
	//METHODS:::::::::::::::::::::::::::::::::::::::::::::::::
	// Inventory Editors:
	public Inventory editKitSelectorInv(){
		//TODO: CHECK IF THIS METHOD RETURNS EMPTY INVENTORY! IF IT IS, THE METHOD MUST BE EXECUTED AGAIN TO WORK!
		
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "Kit Selector Editor");
		if((!getCustomConfig().isSet("getinventorykitselector") || getCustomConfig().getConfigurationSection("getinventorykitselector").getValues(false).isEmpty() == true || getCustomConfig().getConfigurationSection("getinventorykitselector").getValues(false) == null) && getCustomConfig().isSet("getinventorykitselector")){
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
		else if(!getCustomConfig().isSet("getinventorykitselector")){
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
					if(kt.typeName.equals(valueme)){
						alkt.put(keyme,kt.getISForKitv2());
					}
				}
			}
			for(Map.Entry<Integer, ItemStack> me : alkt.entrySet()){
				inv.setItem(me.getKey(), me.getValue());
			}
			return inv;
		}

	}
	
	public Inventory editShopInv(){
		Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_RED + "Shop Editor");
		if((!getCustomConfig().isSet("getinventoryshop") || getCustomConfig().getConfigurationSection("getinventoryshop").getValues(false).isEmpty() == true || getCustomConfig().getConfigurationSection("getinventoryshop").getValues(false) == null) && getCustomConfig().isSet("getinventoryshop")){
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
			getCustomConfig().createSection("getinventoryshop.slots", slotANDkittype);
			saveCustomConfig();
			return inv;
		}
		else if(!getCustomConfig().isSet("getinventoryshop")){
			getCustomConfig().createSection("getinventoryshop");
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
			getCustomConfig().createSection("getinventoryshop.slots", slotANDkittype);
			saveCustomConfig();
			return inv;
		}
		else{
			Map<Integer, ItemStack> alkt = new HashMap<Integer, ItemStack>();
			List<String> ls = new ArrayList<String>();
			
			ItemStack isss;
			ItemMeta isssmeta;
			
			
			for(Map.Entry<String, Object> me : getCustomConfig().getConfigurationSection("getinventoryshop.slots").getValues(false).entrySet()){
				int keyme = Integer.parseInt(me.getKey());
				String valueme = (String) me.getValue();
				for(KitType kt : KitType.values()){
					if(kt.typeName.equals(valueme)){
						ls.clear();
						isss = kt.getISForKitv2();
						isssmeta = isss.getItemMeta();
						ls.add(ChatColor.GREEN + "Price: " + ChatColor.GOLD + kt.price);
						isssmeta.setLore(ls);
						isss.setItemMeta(isssmeta);
						alkt.put(keyme,isss);
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
	public Inventory setupKitSelectorInv(Player p){//TODO: FINISH IT!
		if(!getCustomConfig().isSet("getinventorykitselector.slots")){
			editKitSelectorInv();
		}
		reloadplayerOwnedKitsMapFromConfig(new ArrayList<Player>());
		Inventory kitSelectorInv = Bukkit.createInventory(null, 54, ChatColor.AQUA + "Kit Selector");
		String key;
		String value;
		
		for(KitType kt : playerOwnedKits.get(p)){
			for(Map.Entry<String, Object> meso : getCustomConfig().getConfigurationSection("getinventorykitselector.slots").getValues(false).entrySet()){
				key = meso.getKey();
				value = (String) meso.getValue();
				if(kt.typeName.equals(value)){
					kitSelectorInv.setItem(Integer.parseInt(key), kt.getISForKitv2());
				}
//				KitType ktresult;
//				for(KitType ktt : KitType.values()){
//					if(ktt.equals(value)){
////						ktresult = ktt;
//						kitSelectorInv.setItem(Integer.parseInt(key), ktt.getISForKitv2());
//						break;
//					}
//				}
			}
		}
		return kitSelectorInv;
	}
	
	public Inventory setupShopInv(Player p){
		if(!getCustomConfig().isSet("getinventoryshop.slots")){
			editShopInv();
		}
		reloadplayerOwnedKitsMapFromConfig(new ArrayList<Player>());
		Inventory shopInv = Bukkit.createInventory(null, 54, ChatColor.GOLD + "Shop");
		List<String> lss = new ArrayList<String>();
		
		String key;
		String value;
		ItemStack item;
		ItemMeta itemMeta;
		
		for(KitType kt : playerOwnedKits.get(p)){
			for(Map.Entry<String, Object> meso : getCustomConfig().getConfigurationSection("getinventoryshop.slots").getValues(false).entrySet()){
				key = meso.getKey();
				value = (String) meso.getValue();
				if(kt.typeName.equals(value)){
					lss.clear();
					item = kt.getISForKitv2();
					itemMeta = item.getItemMeta();
					lss.add(ChatColor.GREEN + "Price: " + ChatColor.GOLD +kt.price);
					itemMeta.setLore(lss);
					item.setItemMeta(itemMeta);
					shopInv.setItem(Integer.parseInt(key), item);
				}
//				KitType ktresult;
//				for(KitType ktt : KitType.values()){
//					if(ktt.equals(value)){
////						ktresult = ktt;
//						kitSelectorInv.setItem(Integer.parseInt(key), ktt.getISForKitv2());
//						break;
//					}
//				}
			}
		}
		return shopInv;
	}
	
	//Inventory utils:
	public void reloadplayerOwnedKitsMapFromConfig(List<Player> LP){//TODO: make a way to add owned kits for players (a premade method for easy use in the shop!)
		Map<String, Object> MSO = getCustomConfig().getConfigurationSection("playerinfo").getValues(false);
		for(Map.Entry<String, Object> meso : MSO.entrySet()){
			Bukkit.broadcastMessage("uuid: '" + meso.getKey() + "'");
			for(String s : (ArrayList<String>) meso.getValue()){
				Bukkit.broadcastMessage(" - '" + s + "'");
			}
		}
		String puuid;
		if(LP.isEmpty()){
			Bukkit.broadcastMessage("DEBUG 2");
//			for(Map.Entry<String, Object> me : MSO.entrySet()){
//				
//			}
			
			for(Player p : Bukkit.getOnlinePlayers()){
				p.sendMessage(p.getName());
				puuid = p.getUniqueId().toString();
				p.sendMessage(puuid);
				if(MSO.get(puuid) != null){
					p.sendMessage("contains");
					Bukkit.getServer().getPlayer(UUID.fromString(puuid)).sendMessage(Bukkit.getServer().getPlayer(UUID.fromString(puuid)).getName());
					playerOwnedKits.put(Bukkit.getServer().getPlayer(UUID.fromString(puuid)), ListStringToListType((ArrayList<String>)MSO.get(puuid)));
				}
			}
		}
		else{
			Bukkit.broadcastMessage("DEBUG 1");
			for(Player p : LP){
				puuid = p.getUniqueId().toString();
				if(MSO.get(puuid) != null){
					p.sendMessage("contains");
					playerOwnedKits.put(Bukkit.getServer().getPlayer(UUID.fromString(puuid)), ListStringToListType((List<String>)MSO.get(puuid)));
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
	
	public void spawnInventoryItemsInit() {

		if (getCustomConfig().isSet("spawnitems")) {
			Bukkit.broadcastMessage("HIIII");
			ItemStack is;
			ItemMeta ismeta;
			String mevalue;
			String mekey;
			spawnInventoryItems.clear();
			for (Map.Entry<String, Object> me : getCustomConfig()
					.getConfigurationSection("spawnitems").getValues(false)
					.entrySet()) {
				mekey = me.getKey();
				mevalue = (String) me.getValue();
				if (mekey.equalsIgnoreCase("kitselector")) {
					is = new ItemStack(Material.getMaterial(mevalue));
					ismeta = is.getItemMeta();
					ismeta.setDisplayName(ChatColor.AQUA + "Kit Selector");
				} else if (mekey.equalsIgnoreCase("shopselector")) {
					is = new ItemStack(Material.getMaterial(mevalue));
					ismeta = is.getItemMeta();
					ismeta.setDisplayName(ChatColor.GREEN + "Shop");
				} else {
					is = new ItemStack(Material.getMaterial(mevalue));
					ismeta = is.getItemMeta();
					ismeta.setDisplayName(ChatColor.DARK_PURPLE + mekey);
				}
				is.setItemMeta(ismeta);
				spawnInventoryItems.add(is);
//				is = new ItemStack(Material.AIR);
			}
		} else {
			spawnInventoryItems.clear();
			ItemStack is;
			ItemStack iss;
			is = new ItemStack(Material.WRITTEN_BOOK);
			ItemMeta ismeta = is.getItemMeta();
			ismeta.setDisplayName(ChatColor.AQUA + "Kit Selector");
			is.setItemMeta(ismeta);
			iss = new ItemStack(Material.CHEST);
			ismeta = iss.getItemMeta();
			ismeta.setDisplayName(ChatColor.GREEN + "Shop");
			iss.setItemMeta(ismeta);
			spawnInventoryItems.add(is);
			spawnInventoryItems.add(iss);
		}

	}

	public void spawnInventory(Player p) {//This will be executed on player leave pvp mode!:
		Inventory playerInventory = p.getInventory();
		playerInventory.clear();
		for (ItemStack is : spawnInventoryItems) {
			playerInventory.addItem(is);
		}
	}

	// Kit initialization
	enum KitType{
		
		SKELETON("SKELETON",(byte) 0, null, ChatColor.GRAY + "Skeleton Kit", 0), 
		SPIDER("SPIDER", (byte) 3, "MHF_Spider", ChatColor.RED + "Spider Kit", 0), 
		ZOMBIE("ZOMBIE", (byte) 2, null, ChatColor.GREEN + "Zombie Kit", 0), 
		HEROBRINE("HEROBRINE", (byte) 3, "Herobrine", ChatColor.WHITE + "" + ChatColor.BOLD + "Herobrine Kit", 0), 
		ENDERMAN("ENDERMAN", (byte) 3, "MHF_Enderman", ChatColor.DARK_PURPLE + "Enderman Kit", 0), 
		NONE("NONE", (byte) 3, "Steve", ChatColor.WHITE + "Steve", 0);
		
		private final String typeName;
		private final byte typeByte;
		private final String skinName;
		private final String displayName;
		private final double price;
		KitType(String tN, byte tB, String sN, String dN, double p){
			this.typeName = tN;
			this.typeByte = tB;
			this.skinName = sN;
			this.displayName = dN;
			this.price = p;
		}
		
		public ItemStack getISForKitv2(){
			if(typeByte == 3){
				
				SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(
						Material.SKULL_ITEM);
				ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, (byte) 3);
				meta.setOwner(skinName);
				meta.setDisplayName(displayName);
				is.setItemMeta(meta);
				return is;
			}
			else{
				SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(
						Material.SKULL_ITEM);
				ItemStack is = new ItemStack(Material.SKULL_ITEM, 1, typeByte);
				meta.setDisplayName(displayName);
				is.setItemMeta(meta);
				return is;
			}
		}
		
	}
	
/*	public ItemStack getISForKit(KitType kt) { //NOT IN USE FOR NOW
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
*/
	

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
		getServer().getPluginManager().registerEvents(this, this);
		// TODO Auto-generated method stub
		super.onEnable();
	}

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
	}

	//Player leaving pvp:
	public void onPlayerLeavePvPMode(Player p){
		p.teleport(p.getWorld().getSpawnLocation());
		isPlayerInPvPMode.put(p, false);
		spawnInventory(p);
	}
	// EVEMTS::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
	@EventHandler
	public void playerlogsin(PlayerJoinEvent e) {
		onPlayerLeavePvPMode(e.getPlayer());
	}
	@EventHandler
	public void kitinventoryselection(PlayerInteractEvent e){//TODO: Finish with the event so inventories will open on click
		//		if(e.get instanceof Player){

		
		if(e.getItem() != null && e.getItem().getItemMeta().getDisplayName() != null){
			Player p = e.getPlayer();
			if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.AQUA + "Kit Selector")){
				p.openInventory(setupKitSelectorInv(p));
				e.setCancelled(true);
			}
			if(e.getItem().getItemMeta().getDisplayName().equals(ChatColor.GREEN + "Shop")){
				p.openInventory(setupShopInv(p));
				e.setCancelled(true);
			}
			else{
				return;
			}
		}
		//		}
	}

	
	// COMMANDS::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
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
				return true;
			}
			else{
				sender.sendMessage(ChatColor.DARK_RED + "NO PERMISSIONS!");
				return true;
			}
		}
		if(command.getName().equalsIgnoreCase("leavepvpmode")){
			if (sender.isOp()
					|| sender.hasPermission(new Permission(
							"ultrafence.reloadspawninventory"))
					|| sender.hasPermission(new Permission("ultrafence.all"))) {
				if(sender instanceof Player){
					onPlayerLeavePvPMode((Player) sender);
					return true;
				}
			}
			else{
				sender.sendMessage(ChatColor.DARK_RED + "NO PERMISSIONS!");
				return true;
			}
		}
		if(command.getName().equalsIgnoreCase("config")){
			if (sender.isOp()
					|| sender.hasPermission(new Permission(
							"ultrafence.reloadspawninventory"))
					|| sender.hasPermission(new Permission("ultrafence.all"))) {
				if(args.length == 1){
					if(args[0].equalsIgnoreCase("reload")){
						reloadCustomConfig();
						return true;
					}
					else if(args[0].equalsIgnoreCase("default")){
						saveDefaultCustomConfig();
						return true;
					}
					else{
						sender.sendMessage(ChatColor.DARK_RED + "Unknown argument value! Argument number 1!");
					}
				}
				else{
					sender.sendMessage(ChatColor.DARK_RED + "Not enough arguments!");
					return true;
				}
			}
			else{
				sender.sendMessage(ChatColor.DARK_RED + "NO PERMISSIONS!");
				return true;
			}
		}
		if(command.getName().equalsIgnoreCase("getownedkits")){
			if(sender instanceof Player){
				Player p = (Player) sender;
				p.sendMessage("test");
				for(Map.Entry<Player, List<KitType>> meplkt : playerOwnedKits.entrySet()){
					p.sendMessage(meplkt.getKey().getName());
					for(KitType kt : meplkt.getValue()){
						p.sendMessage(" - " + kt.typeName);
					}
				}
			}
		}
		return super.onCommand(sender, command, label, args);
	}
}
