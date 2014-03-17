package com.kill3rtaco.war;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;

public class TWDefaults {
	
	public static ItemStack		PRIMARY_WEAPON		= new ItemStack(Material.IRON_SWORD);
	public static ItemStack		SECONDARY_WEAPON	= new ItemStack(Material.BOW);
	public static int			MIN_PLAYER_SPEED	= 10;
	public static int			MAX_PLAYER_SPEED	= 1000;
	public static ItemStack		MAX_BOW				= createMaxBow();
	public static ItemStack		MAX_SWORD			= createMaxSword();
	public static FireworkMeta	WAYPOINT			= createWaypoint();
	
	private static ItemStack createMaxBow() {
		ItemStack base = new ItemStack(Material.BOW);
		//add power 5 (instakill if full/critical)
		base.addEnchantment(Enchantment.ARROW_DAMAGE, 5);
		return base;
	}
	
	private static ItemStack createMaxSword() {
		ItemStack base = new ItemStack(Material.DIAMOND_SWORD);
		//add sharpness 5
		base.addEnchantment(Enchantment.DAMAGE_ALL, 5);
		return base;
	}
	
	private static FireworkMeta createWaypoint() {
		FireworkMeta dummy = (FireworkMeta) new ItemStack(Material.FIREWORK).getItemMeta();
		dummy.addEffect(FireworkEffect.builder()
				.with(Type.BURST)
				.withColor(Color.GREEN, Color.BLUE)
				.withTrail()
				.withFlicker()
				.build());
		return dummy;
	}
	
}
