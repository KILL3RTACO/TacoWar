package com.kill3rtaco.war.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import com.kill3rtaco.war.game.player.WarPlayer;

public class WarUtil {
	
	public static String getDamageCauseName(DamageCause cause) {
		switch (cause) {
			case ENTITY_ATTACK:
				return random("Attacked", "Jumped", "Wrecked");
			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
				return random("Explosion", "Exploded");
			case CONTACT:
				return random("Cactus", "Pricked", "Poked");
			case DROWNING:
				return random("Drowned", "Glub Glub");
			case FALL:
				return random("Fell", "Tripped");
			case FALLING_BLOCK:
				return random("Squished", "Squashed", "Look Out Below");
			case FIRE:
			case FIRE_TICK:
				return random("Fire", "Burned");
			case LAVA:
				return "Lava";
			case LIGHTNING:
				return random("Lightning", "Zeus'd", "Thor'd");
			case MAGIC:
				return "Magic";
			case POISON:
				return "Poisoned";
			case MELTING:
				return "Melted";
			case PROJECTILE:
				return random("Shot", "Sniped");
			case STARVATION:
				return "Starved";
			case SUFFOCATION:
				return "Suffocated";
			case SUICIDE:
				return "Suicide";
			case THORNS:
				return "Thorns Armor";
			case VOID:
				return "Void";
			case WITHER:
				return "Withered";
			default:
				return "Natural Causes";
		}
	}
	
	public static String random(String... strings) {
		if (strings.length == 0)
			throw new IllegalArgumentException("Parameter length cannot be 0");
		return strings[new Random().nextInt(strings.length)];
	}
	
	public static boolean isSuicide(DamageCause cause) {
		switch (cause) {
			case CONTACT:
			case DROWNING:
			case FALL:
			case FIRE:
			case FIRE_TICK:
			case LAVA:
			case SUFFOCATION:
			case SUICIDE:
			case VOID:
				return true;
			default:
				return false;
		}
	}
	
	public static WarPlayer getPlayer(List<WarPlayer> players, Player p) {
		return getPlayer(players, p.getName());
	}
	
	public static WarPlayer getPlayer(List<WarPlayer> players, String name) {
		for (WarPlayer p : players) {
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}
	
	public static boolean hasPlayer(List<WarPlayer> players, Player p) {
		return hasPlayer(players, p.getName());
	}
	
	public static boolean hasPlayer(List<WarPlayer> players, String name) {
		return getPlayer(players, name) != null;
	}
	
	public static WarPlayer removePlayer(List<WarPlayer> players, Player p) {
		return removePlayer(players, p.getName());
	}
	
	public static WarPlayer removePlayer(List<WarPlayer> players, WarPlayer player) {
		return removePlayer(players, player.getName());
	}
	
	public static WarPlayer removePlayer(List<WarPlayer> players, String name) {
		for (WarPlayer p : players) {
			if (p.getName().equalsIgnoreCase(name)) {
				players.remove(p);
				return p;
			}
		}
		return null;
	}
	
	public static void broadcast(List<WarPlayer> players, String message) {
		for (WarPlayer p : players) {
			p.sendMessage(message);
		}
	}
	
	public static void teleport(List<WarPlayer> players, Location location) {
		for (WarPlayer p : players) {
			p.teleport(location);
		}
	}
	
	public static void respawn(List<WarPlayer> players) {
		for (WarPlayer p : players) {
			p.respawn();
		}
	}
	
	public static <T extends WarCloneable<T>> List<T> cloneList(List<T> list) {
		List<T> clones = new ArrayList<T>();
		for (T t : list) {
			if (t != null)
				clones.add(t.cloneObject());
		}
		return clones;
	}
	
	public static <T extends WarCloneable<T>> T cloneOrNot(T cloneable, boolean clone) {
		if (clone && cloneable != null)
			return cloneable.cloneObject();
		return cloneable;
	}
	
	public static <T extends WarCloneable<T>> List<T> cloneOrNotList(List<T> list, boolean clone) {
		if (clone)
			return cloneList(list);
		return list;
	}
	
	public static boolean possibleCrit(Player player) {
		if (player == null)
			return false;
		return isOnGround(player) &&
				!isInsideBlockOrVehicle(player) &&
				!player.getActivePotionEffects().contains(PotionEffectType.BLINDNESS);
		
	}
	
	public static boolean isOnGround(Player player) {
		return player.getLocation().getY() - player.getLocation().getBlockY() > 0 &&
				player.getVelocity().getY() < 0;
	}
	
	public static boolean isInsideBlockOrVehicle(Player player) {
		return player.isInsideVehicle() &&
				player.getLocation().getBlock().getType() != Material.AIR &&
				player.getLocation().clone().add(0, 1, 0).getBlock().getType() != Material.AIR;
	}
}
