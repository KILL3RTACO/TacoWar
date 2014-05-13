package com.kill3rtaco.war.game.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Kit;
import com.kill3rtaco.war.game.kill.PlayerDamage;

public class Player {
	
	private org.bukkit.entity.Player	_player;
	private String						_name, _teamColor;
	private Team						_team;
	private List<PlayerDamage>			_allAttackers;			//not cleared periodically
	private List<PlayerDamage>			_attackers;
	private boolean						_invincible	= false;
	private Kit							_kit;
	
	public Player(org.bukkit.entity.Player player) {
		setPlayer(player);
		clearAttackers();
		new BukkitRunnable() {
			
			@Override
			public void run() {
				//every second, check time 
			}
			
		}.runTaskTimer(TacoWar.plugin, 0L, 20L);
	}
	
	public void setTeam(Team team) {
		_team = team;
		sendMessage("You are now on the " + team + " team");
	}
	
	//necessary because Player object are deleted onPlayerQuit [Bukkit]
	public void setPlayer(org.bukkit.entity.Player player) {
		_player = player;
		_name = _player.getName();
	}
	
	public org.bukkit.entity.Player getBukkitPlayer() {
		return _player;
	}
	
	public String getName() {
		return _name;
	}
	
	public double getHealth() {
		return _player.getHealth();
	}
	
	public boolean isDead() {
		return _player.isDead();
	}
	
	public ItemStack getItemInHand() {
		return _player.getItemInHand();
	}
	
	public String getHealthString() {
		int halfHearts = (int) getHealth();
		if(halfHearts == 0) {
			return "0 hearts";
		} else if(halfHearts == 1) {
			return "half a heart";
		} else {
			int fullHearts = halfHearts / 2;
			boolean half = halfHearts % 2 > 0;
			boolean plural = fullHearts != 1 || half;
			return fullHearts + (half ? " and a half " : " ") + "heart" + (plural ? "s" : "");
		}
	}
	
	public void sendMessage(String message) {
		if(_player != null) {
			TacoWar.plugin.chat.sendPlayerMessageNoHeader(_player, message);
		}
	}
	
	public Team getTeam() {
		return _team;
	}
	
	public void teleport(Location location) {
		_player.teleport(location);
	}
	
	public void addArmor() {
		_player.getInventory().setHelmet(new ItemStack(Material.LEATHER_HELMET));
		_player.getInventory().setChestplate(new ItemStack(Material.LEATHER_CHESTPLATE));
		_player.getInventory().setLeggings(new ItemStack(Material.LEATHER_LEGGINGS));
		_player.getInventory().setBoots(new ItemStack(Material.LEATHER_BOOTS));
	}
	
	public void setKit(Kit kit) {
		if(!kit.isValid()) {
			return;
		}
		_kit = kit;
		PlayerInventory inv = _player.getInventory();
		inv.clear();
		addArmor();
		inv.getHelmet().addUnsafeEnchantments(kit.getHelmetEnchants());
		inv.getChestplate().addUnsafeEnchantments(kit.getChestplateEnchants());
		inv.getLeggings().addUnsafeEnchantments(kit.getLeggingEnchants());
		inv.getBoots().addUnsafeEnchantments(kit.getBootEnchants());
		giveItems();
	}
	
	public void setArmorColor(Color color) {
		for(ItemStack i : _player.getInventory().getArmorContents()) {
			if(i == null || !(i.getItemMeta() instanceof LeatherArmorMeta)) {
				continue;
			}
			LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
			meta.setColor(color);
			i.setItemMeta(meta);
		}
	}
	
	public Kit getKit() {
		return _kit;
	}
	
	public void setTeamColor(String colorPrefix) {
		_teamColor = colorPrefix;
	}
	
	public String getColorfulName() {
		return _teamColor + _name;
	}
	
	public boolean equals(Player player) {
		return _name.equalsIgnoreCase(player.getName());
	}
	
	public void setAdventureMode() {
		_player.setGameMode(GameMode.ADVENTURE);
	}
	
	public void setCanFly(boolean canFly) {
		_player.setAllowFlight(canFly);
	}
	
	public void clearInventory() {
		_player.getInventory().clear();
//		_player.getInventory().setArmorContents(new ItemStack[4]);
	}
	
	public void clearAttackers() {
		_attackers = new ArrayList<PlayerDamage>();
	}
	
	public void addAttacker(Player player, double damage) {
		updateAttacker(player, damage, _attackers); //update damage done in the last __ seconds
		updateAttacker(player, damage, _allAttackers); //update damage done in entire game thus far
	}
	
	private void updateAttacker(Player player, double damage, List<PlayerDamage> list) {
		long time = System.currentTimeMillis();
		boolean found = false;
		for(PlayerDamage d : list) {
			if(d.getAttacker() == player) {
				d.setTime(time);
				d.setDamage(damage);
				found = true;
				break;
			}
		}
		if(!found) { //if the PlayerDamage object does not exist (anymore), add it
			list.add(new PlayerDamage(player, damage, time));
		}
	}
	
	public double totalDamageDoneBy(Player player) {
		return damageDone(player, _allAttackers);
	}
	
	public double damageDoneBy(Player player) {
		return damageDone(player, _attackers);
	}
	
	private double damageDone(Player player, List<PlayerDamage> list) {
		for(PlayerDamage d : list) {
			if(d.getAttacker() == player) {
				return d.getDamage();
			}
		}
		return 0;
	}
	
	public void setInvicible(boolean invincible) {
		_invincible = invincible;
	}
	
	public boolean isInvincible() {
		return _invincible;
	}
	
	public void resetStats() {
		_player.setHealth(_player.getMaxHealth());
		_player.setFoodLevel(20);
	}
	
	public void giveItems() {
		_player.getInventory().setContents(_kit.getItems());
	}
}
