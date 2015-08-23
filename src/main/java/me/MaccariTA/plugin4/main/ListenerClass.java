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
package me.MaccariTA.plugin4.main;

//TODO Make the heal potions - (2x8 for Enderman and Skeleton, 2x6 for Herobrine and 1x10 for Zombie ), Speed is fine, no need to change.
//TODO Add the kits to the shop
//TODO Economy system
//TODO Prestige Shop - Boolean that changes to true when they buy the prestige rank (if true they spawn with Health_Boost 5 permanent instead of Health_Boost 4), adds 2 hearts.
//TODO Fix Enderman's Teleport (More info in the Enderman class)
//TODO Make Spider's Ability (More info in the Spider Class)
//TODO place Skeleton 1 Level per Second in better location (Go to Skeleton Respawn Event for further information )


import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;


public class ListenerClass implements Listener {
	MainClass main;

	public ListenerClass(MainClass plugin) {
		this.main=plugin;
	}

	@EventHandler
	public void zombieAttack(EntityDamageByEntityEvent e) {
		//XP By hitting
		//try {
		Entity target = e.getEntity();
		Entity player = e.getDamager();
		if (MainClass.isOk.get(player.getName())){ // On/Off
			if ((player instanceof Player) && (target instanceof Player))
			{
				Player p = (Player) player;
				if (p.getLevel()<88){
					p.giveExpLevels(12);
				}
				else {
					p.setLevel(100);
				}
			}
		}
//	} catch (Exception ignored){}
//	}

	@EventHandler
	public void rightClick(PlayerInteractEvent e)
	{//Activate Ability
		Player p = e.getPlayer();
		try {
		if (MainClass.isOk.get(p.getName())){ // On/Off
			if (p.getItemInHand().getType().toString().equalsIgnoreCase("Iron_Sword"))
			{

				if ((e.getAction()==(Action.RIGHT_CLICK_BLOCK) || e.getAction()==(Action.RIGHT_CLICK_AIR)) && p.getLevel()>=100)
				{

					p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have used your heal skill");
					if (p.getHealth()<=34)
					{

						p.setHealth(p.getHealth() + 6);
						p.getWorld().playSound(p.getLocation(), org.bukkit.Sound.NOTE_PIANO, 80, 1);
					}
					else {
						p.setHealth(40);
						p.getWorld().playSound(p.getLocation(), org.bukkit.Sound.NOTE_PIANO, 80, 1);
					}
					p.setLevel(p.getLevel()-100);

				}
			} 
		}
		} catch (Exception ignored){}
	}

	@EventHandler
	public void mwHP(PlayerRespawnEvent e) {
		Player p = e.getPlayer();	
		if (MainClass.isOk.get(p.getName())){ // On/Off 
			p.getServer().getScheduler().scheduleSyncDelayedTask(this.main, new Runnable(){

				public void run() { // Spawn with items + 40 hp (Delayed is required in order for this to work)
					ItemStack chest = new ItemStack(Material.DIAMOND_CHESTPLATE);
					chest.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 2);
					Potion healpot = new Potion(PotionType.INSTANT_HEAL, 2); //TODO Should be 10 hearts
					Potion speedpot = new Potion(PotionType.SPEED, 2);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 4));
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 255));
					p.getInventory().setHelmet(new ItemStack(Material.IRON_HELMET));
					p.getInventory().setChestplate(chest);
					p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
					p.getInventory().setItem(0, new ItemStack(Material.IRON_SWORD));
					p.getInventory().addItem(healpot.toItemStack(1));
					p.getInventory().addItem(speedpot.toItemStack(2));
				}
			}, 1L);
		}
	}

	@EventHandler
	public void Hashmap(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore())
		{ // Adds new players to the Hashmap
			MainClass.isOk.put(p.getName(), false);
		}
	}
	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		 // Makes disconnected players false
			MainClass.isOk.put(p.getName(), false);
			p.getInventory().clear();
			p.setHealth(0);
	}
	
	@EventHandler
	public void noDrops(PlayerDeathEvent e) {
		Player p = (Player) e.getEntity();
		if (MainClass.isOk.get(p.getName())){ 
		e.setDroppedExp(0);
		List<ItemStack> list = e.getDrops();
		list.clear();
		}
		
	}
	@EventHandler
	public void playerAttacked(EntityDamageByEntityEvent e) {
		//35% to get res1
		try {
		Entity player = e.getEntity();
		Entity player2 = e.getDamager();
		if (MainClass.isOk.get(player.getName())){ // On/Off
			if ((player instanceof Player) && (player2 instanceof Player))
			{
				Player p = (Player) player;
				Random rand = new Random();
				int chance = rand.nextInt(100)+1;
				if (chance<=35)
				{
					p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE,20,0));
				}
			}
		}	
	} catch (Exception ignored){}
	}
	
}