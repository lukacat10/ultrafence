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
//SKELETON

package me.MaccariTA.plugin7.main;


import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
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
	public void skeleAttack(EntityDamageByEntityEvent e) {
		// XP By shooting
		Entity eTarget = e.getEntity();
		Entity arrow = e.getDamager();
		if (arrow instanceof Arrow)
		{
			Arrow arr = (Arrow) arrow;
			if ((arr.getShooter() instanceof Player) && (eTarget instanceof Player))
			{
				Player damager = (Player) arr.getShooter();
				if (Main.isOk.get(damager.getName())) { //ON / OFF
					if (damager.getLevel() < 75)
					{
					damager.setLevel(damager.getLevel()+25);
					}
					else
					{
						damager.setLevel(100);
					}
				}
			}
		}
	}


	@EventHandler
	public void rightClick(PlayerInteractEvent e) {// Activate Ability
		Player p = e.getPlayer();
		if (Main.isOk.get(p.getName())) { // On/Off
			if (p.getItemInHand().getType().toString().equalsIgnoreCase("bow")){
				if (e.getAction() == (Action.LEFT_CLICK_AIR) && p.getLevel() >= 100) {
					p.sendMessage(ChatColor.GREEN + "" + ChatColor.BOLD + "You have used your explosive arrow skill");
					Location loc = p.getLocation();
					loc.setY(loc.getY()+2.5);
					Arrow arrow = p.getWorld().spawnArrow(loc, p.getLocation().getDirection(), 10F, 1F);
					arrow.setVelocity(p.getLocation().getDirection().multiply(3));
					p.setLevel(p.getLevel()-100);
				}
			}
		}
	}

	@EventHandler
	public void mwHP(PlayerRespawnEvent e) { //TODO The +1 level for second is buggy because if you die multiple times ( for example spam of Herobrine's Ability ) you get multiply times in second
		Player p = e.getPlayer();
		if (Main.isOk.get(p.getName())) { // On/Off
			p.getServer().getScheduler()
			.scheduleSyncDelayedTask(this.main, new Runnable() {

				public void run() { // Spawn with items + 40 hp (Delayed is required in order for this to work)
				    ItemStack helmet = new ItemStack(Material.DIAMOND_HELMET);
					helmet.addEnchantment(Enchantment.PROTECTION_PROJECTILE, 4);
					ItemStack bow = new ItemStack(Material.BOW);
					Potion healpot = new Potion(PotionType.INSTANT_HEAL, 2); //TODO Should be 8 hearts
					Potion speedpot = new Potion(PotionType.SPEED, 2);
					bow.addEnchantment(Enchantment.ARROW_DAMAGE, 4);
					bow.addEnchantment(Enchantment.ARROW_INFINITE, 1);
					p.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, 99999, 4));
					p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 255));
					p.getInventory().setHelmet(helmet);
					p.getInventory().setChestplate(new ItemStack(Material.IRON_CHESTPLATE));
					p.getInventory().setLeggings(new ItemStack(Material.IRON_LEGGINGS));
					p.getInventory().setBoots(new ItemStack(Material.IRON_BOOTS));
					p.getInventory().setItem(0, new ItemStack(Material.IRON_AXE));
					p.getInventory().setItem(1, bow);
					p.getInventory().setItem(9, new ItemStack(Material.ARROW));
					p.getInventory().addItem(healpot.toItemStack(2));
					p.getInventory().addItem(speedpot.toItemStack(2));
				}
			}, 1L);

			p.getServer().getScheduler().scheduleSyncRepeatingTask(this.main, new Runnable() {

				public void run() { // 1xp level per second
					if (p.getLevel()<100)
					{
						p.setLevel(p.getLevel()+1);
					}
				}
			}, 100, 20); // start counting 5 seconds after spawn
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
	public void explosiveArrow(ProjectileHitEvent e) { // arrow explosion effect
		Projectile proj = e.getEntity();
		if ( proj instanceof Arrow)
		{
			Arrow arrow = (Arrow) proj;
			if (!(arrow.getShooter() instanceof Player) && !(arrow.getShooter() instanceof Skeleton))
			{
				arrow.getWorld().playEffect(arrow.getLocation(), Effect.EXPLOSION_HUGE, 150);
			}
		}
	}
	
	@EventHandler
	public void abilityDamage(EntityDamageByEntityEvent e) { // 6 true damage ability
		Entity pl = e.getEntity();
		Entity arrow = e.getDamager();
			if ((pl instanceof Player) && (arrow instanceof Arrow)) {
				Player p = (Player) pl;
				Arrow arr = (Arrow) arrow;
				if (Main.isOk.get(p.getName())) { // ON / OFF
					if (!(arr.getShooter() instanceof Player)){ // TODO INSTANCEOF PLUGIN INSTEAD OF NOT PLAYER
					e.setDamage(0);
					if (p.getHealth() >= 6)
					{
					p.setHealth(p.getHealth()-(double)6);
					}
					else {
					p.setHealth(0);
					}
					}}
			}
		}
	}
