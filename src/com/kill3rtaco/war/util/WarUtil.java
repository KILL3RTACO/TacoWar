package com.kill3rtaco.war.util;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class WarUtil {
	
	public static String getDamageCauseName(DamageCause cause) {
		switch(cause) {
			case ENTITY_ATTACK:
				return "Attacked";
			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
				return "Explosion";
			case CONTACT:
				return "Cactus";
			case DROWNING:
				return "Drowned";
			case FALL:
				return "Fell";
			case FALLING_BLOCK:
				return "Squished";
			case FIRE:
			case FIRE_TICK:
				return "Fire";
			case LAVA:
				return "Lava";
			case LIGHTNING:
				return "Lightning";
			case MAGIC:
				return "Magic";
			case POISON:
				return "Poisoned";
			case MELTING:
				return "Melted";
			case PROJECTILE:
				return "Shot";
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
	
	public static boolean isSuicide(DamageCause cause) {
		switch(cause) {
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
	
}
