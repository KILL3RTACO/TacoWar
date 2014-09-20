package com.kill3rtaco.war.game.player;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.api.serialization.EnchantmentSerialization;
import com.kill3rtaco.tacoapi.api.serialization.InventorySerialization;
import com.kill3rtaco.war.ValidatedConfig;

public class Kit extends ValidatedConfig {

	private String _id, _name;
	private ItemStack[] _items;
	private Map<Enchantment, Integer> _helmetEnchants, _chestplateEnchants,
			_leggingEnchants, _bootEnchants;

	public Kit(ConfigurationSection section) {
		super(section);
		_id = getString("id", true);
		_name = getString("name", true);
		String json = getString("items", true);
		_items = InventorySerialization.getInventory(json, 36);
		_helmetEnchants = EnchantmentSerialization.getEnchantments(getString("helmet_enchants", false));
		_chestplateEnchants = EnchantmentSerialization.getEnchantments(getString("chestplate_enchants", false));
		_leggingEnchants = EnchantmentSerialization.getEnchantments(getString("legging_enchants", false));
		_bootEnchants = EnchantmentSerialization.getEnchantments(getString("boot_enchants", false));
	}

	public String getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}

	public ItemStack[] getItems() {
		return _items;
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
}
