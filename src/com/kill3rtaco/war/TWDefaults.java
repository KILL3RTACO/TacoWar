package com.kill3rtaco.war;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class TWDefaults {
	
	public static ItemStack	PRIMARY_WEAPON		= new ItemStack(Material.IRON_SWORD);
	public static ItemStack	SECONDARY_WEAPON	= new ItemStack(Material.BOW);
	public static ItemStack	SEEKER_WEAPON		= createSeekerWeapon();
	public static int		MIN_PLAYER_SPEED	= 10;
	public static int		MAX_PLAYER_SPEED	= 1000;
	
	private static ItemStack createSeekerWeapon() {
		ItemStack base = new ItemStack(Material.DIAMOND_SWORD);
		//add sharpness
		base.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		return base;
	}
	
}
