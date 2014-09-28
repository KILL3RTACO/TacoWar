package com.kill3rtaco.war.game.player;

import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;

import com.kill3rtaco.tacoapi.api.serialization.EnchantmentSerialization;
import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.util.Identifyable;
import com.kill3rtaco.war.util.ValidatedConfig;
import com.kill3rtaco.war.util.WarCloneable;

public class WarKit extends ValidatedConfig implements Identifyable, WarCloneable<WarKit> {
	
	public static final String	KEY_FOOD			= "food";
	public static final String	KEY_ID				= "id";
	public static final String	KEY_NAME			= "name";
	public static final String	KEY_WEAPONS			= "weapons";
	
	//head
	//shoulders
	//knees and toes
	//knees and toes
	public static final String	KEY_ENCHANTS_HEAD	= "helmet_enchants";
	public static final String	KEY_ENCHANTS_CHEST	= "chestplate_enchants";
	public static final String	KEY_ENCHANTS_LEGS	= "legging_enchants";
	public static final String	KEY_ENCHANTS_FEET	= "boot_enchants";
	
	private String				_id, _name;
	private List<Weapon>		_weapons;
	private List<Food>			_food;
	private Map<Enchantment, Integer>	_helmetEnchants, _chestplateEnchants,
										_leggingEnchants, _bootEnchants;
	
	protected WarKit() {
		this(new YamlConfiguration());
	}
	
	public WarKit(ConfigurationSection section) {
		super(section);
	}
	
	public void reload() {
		_id = getString(KEY_ID, true);
		_name = getString(KEY_NAME, true);
		loadWeapons();
		loadFood();
		_helmetEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_HEAD, false));
		_chestplateEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_CHEST, false));
		_leggingEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_LEGS, false));
		_bootEnchants = EnchantmentSerialization.getEnchantments(getString(KEY_ENCHANTS_FEET, false));
	}
	
	//weapon list is distinct/unique
	private void loadWeapons() {
		List<String> ids = getStringList(KEY_WEAPONS, true);
		if (ids == null || ids.isEmpty()) {
			_valid = false;
			return;
		}
		//TODO: clone flag
		_weapons = TacoWar.getWeapons(ids);
		if (_weapons == null || _weapons.isEmpty()) {
			_valid = false;
			return;
		}
	}
	
	private void addFood(Food food) {
		for (Food f : _food) {
			if (f.asItem().getType() == food.asItem().getType()) {
				f.setAmount(f.getAmount() + food.getAmount());
				return;
			}
		}
		_food.add(food);
	}
	
	private void loadFood() {
		List<String> ids = getStringList(KEY_FOOD, false);
		for (String s : ids) {
			if (s == null || s.isEmpty())
				continue;
			String[] split = s.split("\\s+");
			String foodMaterial = split[0];
			int amount = 1;
			if (split.length > 1) {
				try {
					amount = Integer.parseInt(split[1]);
				} catch (NumberFormatException e) {}
			}
			addFood(new Food(foodMaterial, amount));
		}
	}
	
	public String getId() {
		return _id;
	}
	
	public String getName() {
		return _name;
	}
	
	public List<Weapon> getWeapons() {
		return getWeapons(false);
	}
	
	public List<Weapon> getWeapons(boolean clone) {
		return TW.cloneOrNotList(_weapons, clone);
	}
	
	public Weapon getWeapon(int slot) {
		if (slot < 0 || _weapons.size() < slot)
			return null;
		Weapon w = _weapons.get(slot);
		if (w == null || w.getAmmo() == 0)
			return null;
		return w;
	}
	
	public Weapon getWeapon(String id) {
		Weapon weapon = null;
		for (Weapon w : _weapons) {
			if (w.equals(id))
				weapon = w;
		}
		return weapon;
	}
	
	public int slotOf(String weaponId) {
		for (int i = 0; i < _weapons.size(); i++) {
			if (_weapons.get(i).getId().equals(weaponId))
				return i;
		}
		return -1;
	}
	
	public List<Food> getFood() {
		return _food;
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
	
	@Override
	public WarKit cloneObject() {
		return new WarKit(_config);
	}
}
