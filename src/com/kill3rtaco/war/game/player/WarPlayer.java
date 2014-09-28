package com.kill3rtaco.war.game.player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.kill.PlayerDamage;

public class WarPlayer {
	
	private String				_name;
	private WarTeam				_team;
	private WarKit				_kit;
	private boolean				_invincible	= false;
	private List<PlayerDamage>	_damagers	= new ArrayList<PlayerDamage>();
	private int					_kills, _deaths, _assists;
	
	public WarPlayer(String name) {
		_name = name;
		_kills = _deaths = _assists = 0;
	}
	
	public void setKit(WarKit kit) {
		clearInventory();
		_kit = kit.cloneObject();
		giveKit();
	}
	
	public void addKill() {
		_kills++;
	}
	
	public void addDeath() {
		_deaths++;
	}
	
	public void addAssist() {
		_assists++;
	}
	
	public int getKills() {
		return _kills;
	}
	
	public int getDeaths() {
		return _deaths;
	}
	
	public int getAssists() {
		return _assists;
	}
	
	public double getKDA() {
		return (_kills + _assists) / (_deaths == 0 ? 1 : _deaths);
	}
	
	public double getKD() {
		return _kills / (_deaths == 0 ? 1 : _deaths);
	}
	
	public String getKDR() {
		return new DecimalFormat("#.##").format(getKD());
	}
	
	public String getKDAString(String numColor, String slashColor) {
		return numColor + _kills + slashColor + "/" + _deaths + slashColor + "/" + _assists;
	}
	
	public String getName() {
		return _name;
	}
	
	public WarKit getKit() {
		return _kit;
	}
	
	public void setInvicible(boolean invincible) {
		_invincible = invincible;
	}
	
	public boolean isInvincible() {
		return _invincible;
	}
	
	public void giveArmor() {
		Player p = getBukkitPlayer();
		if (p == null)
			return;
		PlayerInventory inv = p.getInventory();
		inv.setHelmet(getArmorPiece(Material.LEATHER_HELMET));
		inv.setChestplate(getArmorPiece(Material.LEATHER_CHESTPLATE));
		inv.setLeggings(getArmorPiece(Material.LEATHER_LEGGINGS));
		inv.setBoots(getArmorPiece(Material.LEATHER_BOOTS));
		
		inv.getHelmet().addUnsafeEnchantments(_kit.getHelmetEnchants());
		inv.getChestplate().addUnsafeEnchantments(_kit.getChestplateEnchants());
		inv.getLeggings().addUnsafeEnchantments(_kit.getLeggingEnchants());
		inv.getBoots().addUnsafeEnchantments(_kit.getBootEnchants());
	}
	
	private ItemStack getArmorPiece(Material leatherArmor) {
		return getArmorPiece(leatherArmor, getTeam().getId());
	}
	
	private ItemStack getArmorPiece(Material leatherArmor, String color) {
		ItemStack piece = new ItemStack(leatherArmor);
		LeatherArmorMeta meta = (LeatherArmorMeta) piece.getItemMeta();
		meta.setColor(TW.getArmorColor(color));
		piece.setItemMeta(meta);
		return piece;
	}
	
	public void ignite(int ticks) {
		Player p = getBukkitPlayer();
		if (p != null)
			p.setFireTicks(ticks);
	}
	
	public void giveKit() {
		_kit = TacoWar.getKit(_kit.getId(), true);
		Player p = getBukkitPlayer();
		if (p != null) {
			giveArmor();
			PlayerInventory inv = p.getInventory();
			int currentSlot = 0;
			for (Weapon w : _kit.getWeapons()) {
				if (currentSlot > 8)
					return;
				inv.setItem(currentSlot++, w.asItem());
			}
			for (Food f : _kit.getFood()) {
				if (currentSlot > 8)
					return;
				inv.setItem(currentSlot++, f.asItem());
			}
		}
	}
	
	public String getColorfulName() {
		return _team.getColor() + getName();
	}
	
	public Player getBukkitPlayer() {
		return Bukkit.getPlayer(_name);
	}
	
	public void setTeam(WarTeam team) {
		if (_team != null)
			_team.remove(this);
		_team = team;
		team.add(this);
	}
	
	public WarTeam getTeam() {
		return _team;
	}
	
	public boolean sameTeam(WarPlayer player) {
		return player == this || player.getTeam().contains(this);
	}
	
	public void sendMessage(String message) {
		Player p = getBukkitPlayer();
		if (p != null)
			TacoWar.chat.sendPlayerMessage(p, message);
	}
	
	public void respawn() {
		teleport(TacoWar.currentGame().getMap().getRandomSpawn(_team));
		setKit(TacoWar.currentGame().getKit());
	}
	
	public void teleport(Location location) {
		Player p = getBukkitPlayer();
		if (p == null)
			return;
		
		Location currLocation = getLocation();
		//don't teleport if they are already there
		if (currLocation != null &&
				currLocation.getBlockX() != location.getBlockX() &&
				currLocation.getBlockY() != location.getBlockY() &&
				currLocation.getBlockZ() != location.getBlockZ())
			p.teleport(location);
	}
	
	public void clearInventory() {
		Player p = getBukkitPlayer();
		if (p != null)
			p.getInventory().clear();
	}
	
	public Weapon getHeldWeapon() {
		Player p = getBukkitPlayer();
		if (p != null)
			return _kit.getWeapon(p.getInventory().getHeldItemSlot());
		return null;
	}
	
	public Weapon getWeapon(String id) {
		return _kit.getWeapon(id);
	}
	
	public void removeWeapon(String id) {
		Inventory inv = getInventory();
		if (inv == null)
			return;
		int slot = _kit.slotOf(id);
		if (slot < 0)
			return;
		inv.setItem(slot, null);
	}
	
	public Inventory getInventory() {
		Player p = getBukkitPlayer();
		if (p == null)
			return null;
		return p.getInventory();
	}
	
	public void updateAmmoCount() {
		Player p = getBukkitPlayer();
		Weapon w = getHeldWeapon();
		if (p != null) {
			int ammo = w.getAmmo();
			int level = 0;
			if (ammo < 0) {
				level = 999;
			} else if (ammo == 0) {
				level = 0;
			} else {
				level = ammo;
			}
			p.setLevel(level);
			if (w.asItem().getType() == Material.BOW) {
				int slot = _kit.slotOf(w.getId());
				int amount = w.getAmmo();
				if (amount < 0)
					amount = 64;
				p.getInventory().setItem(slot + 9, new ItemStack(Material.ARROW, amount));
			}
		}
	}
	
	public Location getLocation() {
		Player p = getBukkitPlayer();
		if (p != null)
			return p.getLocation();
		return null;
	}
	
	//negative is harm
	//positive is health
	public void setHealthRelative(int health) {
		Player p = getBukkitPlayer();
		if (p != null)
			p.setHealth(p.getHealth() + health);
	}
	
	public void setMaxHealth(int maxHealth) {
		Player p = getBukkitPlayer();
		if (p != null)
			p.setMaxHealth(maxHealth);
	}
	
	public int getHealth() {
		Player p = getBukkitPlayer();
		if (p != null)
			return (int) p.getHealth();
		return -1;
	}
	
	//modified version of Assist's alternative to Player.getTargetBlock() via bukkit.org forums
	public Block getTargetBlock(int range) {
		//Taco start
		Player p = getBukkitPlayer();
		if (p == null)
			return null;
		Location loc = p.getEyeLocation();
		//Taco end
		Vector dir = loc.getDirection().normalize();
		
		Block b = null;
		
		for (int i = 0; i <= range; i++) {
			b = loc.add(dir).getBlock();
			//Taco start
			if (b.getType() != Material.AIR)
				return b;
			//Taco end
		}
		
		return b;
	}
	
	public void addDamager(PlayerDamage damage) {
		_damagers.add(damage);
	}
	
	public void clearDamagers() {
		_damagers.clear();
	}
	
	public List<PlayerDamage> getDamagers() {
		return _damagers;
	}
	
	public void removeExpiredDamagers() {
		long expireTime = TacoWar.config.getDamageExpireThresholdMillis();
		List<PlayerDamage> newDamagers = new ArrayList<PlayerDamage>();
		long time = System.currentTimeMillis();
		for (PlayerDamage d : _damagers) {
			if (time - d.getTime() < expireTime)
				newDamagers.add(d);
		}
		_damagers = newDamagers;
	}
	
	public WarPlayer getLastDamager() {
		if (_damagers.size() == 0)
			return null;
		for (int i = 0; i < _damagers.size(); i++) {
			WarPlayer player = _damagers.get(_damagers.size() - (i + 1)).getAttacker();
			if (player != null)
				return player;
		}
		return null;
	}
	
	public List<WarPlayer> getLastDamagersNotKiller() {
		List<WarPlayer> damagers = new ArrayList<WarPlayer>();
		WarPlayer last = getLastDamager();
		for (PlayerDamage d : _damagers) {
			WarPlayer damager = d.getAttacker();
			if (damager != last) {
				damagers.add(damager);
			}
		}
		return damagers;
	}
}
