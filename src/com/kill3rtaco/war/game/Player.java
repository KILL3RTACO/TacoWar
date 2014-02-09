package com.kill3rtaco.war.game;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.kill3rtaco.war.TacoWar;

public class Player {
	
	private org.bukkit.entity.Player	_player;
	private String						_name;
	private TeamColor					_team;
	
	public Player(org.bukkit.entity.Player player) {
		setPlayer(player);
	}
	
	public void setTeam(TeamColor team) {
		_team = team;
		sendMessage("You are now on the " + team.getColorfulName() + " team");
	}
	
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
			boolean plural = fullHearts > 1 || half;
			return fullHearts + (half ? " and a half " : " ") + "heart" + (plural ? "s" : "");
		}
	}
	
	public void sendMessage(String message) {
		if(_player != null) {
			TacoWar.plugin.chat.sendPlayerMessageNoHeader(_player, message);
		}
	}
	
	public TeamColor getTeam() {
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
		repaintArmor();
	}
	
	public void repaintArmor() {
		for(ItemStack i : _player.getInventory().getArmorContents()) {
			if(i == null || !(i.getItemMeta() instanceof LeatherArmorMeta)) {
				continue;
			}
			LeatherArmorMeta meta = (LeatherArmorMeta) i.getItemMeta();
			meta.setColor(_team.getArmorColor());
			i.setItemMeta(meta);
		}
	}
	
	public void giveItems() {
		PlayerInventory inv = _player.getInventory();
		inv.setHeldItemSlot(0);
		inv.setItemInHand(new ItemStack(Material.IRON_SWORD));
		inv.setHeldItemSlot(1);
		inv.setItemInHand(new ItemStack(Material.BOW));
		inv.setHeldItemSlot(2);
		inv.setItemInHand(new ItemStack(Material.APPLE, 5));
		
		inv.setHeldItemSlot(7);
		inv.setItemInHand(new ItemStack(Material.ARROW, 64));
		inv.setHeldItemSlot(8);
		inv.setItemInHand(new ItemStack(Material.ARROW, 64));
		
		inv.setHeldItemSlot(0);
	}
	
	public String getColorfulName() {
		return _team.getChatColor() + _name;
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
		_player.getInventory().setArmorContents(new ItemStack[4]);
	}
}
