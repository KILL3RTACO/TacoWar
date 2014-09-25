package com.kill3rtaco.war.game.player;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
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
	private List<PlayerDamage>	_damages	= new ArrayList<PlayerDamage>();

	public WarPlayer(String name) {
		_name = name;
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
			_team.removePlayer(this);
		_team = team;
		team.addPlayer(this);
	}

	public WarTeam getTeam() {
		return _team;
	}

	public boolean sameTeam(WarPlayer player) {
		return player == this || player.getTeam().hasPlayer(this);
	}

	public void sendMessage(String message) {
		Player p = getBukkitPlayer();
		if (p != null)
			TacoWar.chat.sendPlayerMessage(p, message);
	}

	public void respawn() {
		//teleport at base
		//clear inventory
		//give kit
	}

	public void teleport(Location location) {
		Player p = getBukkitPlayer();
		if (p != null)
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

	public void updateAmmoCount() {
		Player p = getBukkitPlayer();
		Weapon w = getHeldWeapon();
		if (p != null) {
			int ammo = w.getAmmo();
			int level = 0;
			if (ammo < -1) {
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

	public void addDamage(PlayerDamage damage) {
		_damages.add(damage);
	}

	public void clearDamages() {
		_damages.clear();
	}

	public void removeExpiredDamages() {
		long expireTime = TacoWar.config.getDamageExpireThresholdMillis();
		List<PlayerDamage> newDamages = new ArrayList<PlayerDamage>();
		long time = System.currentTimeMillis();
		for (PlayerDamage d : _damages) {
			if (time - d.getTime() < expireTime)
				newDamages.add(d);
		}
		_damages = newDamages;
	}

}
