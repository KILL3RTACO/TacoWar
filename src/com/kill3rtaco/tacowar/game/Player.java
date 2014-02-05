package com.kill3rtaco.tacowar.game;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import com.kill3rtaco.tacowar.TacoWar;

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
		ItemStack[] items = new ItemStack[36];
		items[18] = new ItemStack(Material.ARROW, 64);
		items[19] = new ItemStack(Material.ARROW, 64); //first two columns in third row
		
		//hotbar
		items[27] = new ItemStack(Material.STONE_SWORD); //1
		items[28] = new ItemStack(Material.BOW); //2
		items[29] = new ItemStack(Material.APPLE, 5); //3
	}
	
	public void giveItems(ItemStack[] items) {
		Inventory inv = _player.getInventory();
		for(int i = 0; i < items.length; i++) {
			if(i > inv.getSize() - 1) {
				break;
			}
			ItemStack item = items[i];
			if(item == null || item.getType() == Material.AIR) {
				continue;
			}
			inv.setItem(i, item);
		}
	}
	
	public String getColorfulName() {
		return _team.getChatColor() + _name;
	}
}
