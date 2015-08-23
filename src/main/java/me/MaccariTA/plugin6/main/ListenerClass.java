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
//ENDERMAN

package me.MaccariTA.plugin6.main;


import java.util.List;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
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
	Main main;

	public ListenerClass(Main plugin) {
		this.main = plugin;
	}

	@EventHandler
	public void endyAttack(EntityDamageByEntityEvent e) {
		// XP By hitting
		try {
			Entity target = e.getEntity();
			Entity player = e.getDamager();
			if (Main.isOk.get(player.getName())) { // On/Off
				if ((player instanceof Player) && (target instanceof Player)) {
					Player p = (Player) player;
					if (p.getLevel() < 80) {
						p.giveExpLevels(20);
					} else {
						p.setLevel(100);
					}
				}
			}
		} catch (Exception ignored) {
		}
	}

	@EventHandler
	public void rightClick(PlayerInteractEvent e) {// Activate Ability
		int count = 0;
		Player p = e.getPlayer();
		if (Main.isOk.get(p.getName())) { // On/Off
			if (p.getItemInHand().getType().toString()
					.equalsIgnoreCase("iron_sword")) {
				if ((e.getAction() == (Action.RIGHT_CLICK_BLOCK) || e
						.getAction() == (Action.RIGHT_CLICK_AIR))
						&& p.getLevel() >= 100) {
					for (Player player : p.getServer().getOnlinePlayers())
					{
						if (p.getLocation().distance(player.getLocation()) <= 25)
						{
							if (p.getName() != player.getName())
							{
							if (p.canSee(player)) // Change to compicated shit to understand if you're looking at him
							{
								count++;
								p.teleport(player);
								p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 120, 2));
							}
						}						
					}
					}

					if (count != 0) {
						p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD
								+ "You have used your teleport skill");
						p.setLevel(p.getLevel() - 100);
					} else {
						p.sendMessage(ChatColor.RED + "" + ChatColor.BOLD
								+ "No targets within range");
					}
				}
			}
		}
	}	



	@EventHandler
	public void mwHP(PlayerRespawnEvent e) {
		Player p = e.getPlayer();
		if (Main.isOk.get(p.getName())) { // On/Off
			p.getServer().getScheduler()
			.scheduleSyncDelayedTask(this.main, new Runnable() {

				public void run() { // Spawn with items + 40 hp (Delayed is required in order for this to work)
				ItemStack boots = new ItemStack(
							Material.DIAMOND_BOOTS);
					Potion healpot = new Potion(PotionType.INSTANT_HEAL, 2); //TODO Should be 8 hearts
					Potion speedpot = new Potion(PotionType.SPEED, 2);
					boots.addEnchantment(Enchantment.PROTECTION_FALL, 4);
					p.addPotionEffect(new PotionEffect(
							PotionEffectType.HEALTH_BOOST, 99999, 4));
					p.addPotionEffect(new PotionEffect(
							PotionEffectType.REGENERATION, 20, 255));
					p.getInventory().setHelmet(
							new ItemStack(Material.IRON_HELMET));
					p.getInventory().setChestplate(
							new ItemStack(Material.IRON_CHESTPLATE));
					p.getInventory().setLeggings(
							new ItemStack(Material.IRON_LEGGINGS));
					p.getInventory().setBoots(boots);
					p.getInventory().setItem(0,
							new ItemStack(Material.IRON_SWORD));
					p.getInventory().addItem(healpot.toItemStack(2));
					p.getInventory().addItem(speedpot.toItemStack(2));
				}
			}, 1L);
		}
	}

	@EventHandler
	public void Hashmap(PlayerLoginEvent e) {
		Player p = e.getPlayer();
		if (!p.hasPlayedBefore()) { // Adds new players to the Hashmap
			Main.isOk.put(p.getName(), false);
		}
	}

	@EventHandler
	public void onDisconnect(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		// Makes disconnected players false on the Hasmap + clears the players
		// inventory before so they wont drop the items
		Main.isOk.put(p.getName(), false);
		p.getInventory().clear(); // doesnt work
		p.setHealth(0);
	}

	@EventHandler
	public void noDrops(PlayerDeathEvent e) { // Players will not drop items or
		// EXP
		Player p = (Player) e.getEntity();
		if (Main.isOk.get(p.getName())) {
			if (p instanceof Player) {
				e.setDroppedExp(0);
				List<ItemStack> list = e.getDrops();
				list.clear();
			}
		}
	}
	@EventHandler
	public void antiKB(EntityDamageByEntityEvent e) { // Players get Anti KB
		Entity damage = e.getDamager();
		Entity pla = e.getEntity();
	
			if ((pla instanceof Player) &&  ((damage instanceof Player) || (damage instanceof Arrow))) {
				Player p = (Player) pla;
				if (Main.isOk.get(p.getName())) { // ON / OFF
				Random rand = new Random();
				int chance = rand.nextInt(100)+1;
				if (chance <= 90)
				{
					e.setCancelled(true);
					p.setHealth(p.getHealth()-e.getFinalDamage());
				}
				}
			}
		}
	}
	


