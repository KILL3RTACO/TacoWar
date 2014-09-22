package com.kill3rtaco.war.game.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;

import com.kill3rtaco.tacoapi.api.serialization.EnchantmentSerialization;
import com.kill3rtaco.war.Identifyable;
import com.kill3rtaco.war.ValidatedConfig;

public class WarKit extends ValidatedConfig implements Identifyable,
		Iterable<Weapon> {

	public static final String			KEY_ID				= "id";
	public static final String			KEY_NAME			= "name";
	public static final String			KEY_WEAPONS			= "weapons";

	//head
	//shoulders
	//knees and toes
	//knees and toes
	public static final String			KEY_ENCHANTS_HEAD	= "helmet_enchants";
	public static final String			KEY_ENCHANTS_CHEST	= "chestplate_enchants";
	public static final String			KEY_ENCHANTS_LEGS	= "legging_enchants";
	public static final String			KEY_ENCHANTS_FEET	= "boot_enchants";

	private String						_id, _name;
	private HashMap<Integer, Weapon>	_weapons;
	private Map<Enchantment, Integer>	_helmetEnchants, _chestplateEnchants,
										_leggingEnchants, _bootEnchants;

	public WarKit(ConfigurationSection section) {
		super(section);
		_id = getString(KEY_ID, true);
		_name = getString(KEY_NAME, true);
		loadWeapons();
		_helmetEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_HEAD, false));
		_chestplateEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_CHEST, false));
		_leggingEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_LEGS, false));
		_bootEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_FEET, false));
	}

	private void loadWeapons() {

	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public List<Weapon> getWeapons() {
		return new ArrayList<Weapon>(_weapons.values());
	}

	public Weapon getWeapon(int slot) {
		return _weapons.get(slot);
	}

	public Map<Enchantment, Integer> getHelmetEnchants() {
		return _helmetEnchants;
	}

	public Map<Enchantment, Integer> getChestplateEnchants() {
		return _chestplateEnchants;
	}

	public Map<Enchantment, Integer> getLeggingEnchants() {
		return _leggingEnchants;
	}

	public Map<Enchantment, Integer> getBootEnchants() {
		return _bootEnchants;
	}

	public Iterator<Weapon> iterator() {
		return getWeapons().iterator();
	}
}
