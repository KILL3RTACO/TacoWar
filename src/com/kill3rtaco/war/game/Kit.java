package com.kill3rtaco.war.game;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.api.serialization.InventorySerialization;
import com.kill3rtaco.war.ValidatedConfig;

public class Kit extends ValidatedConfig {
	
	private String		_id, _name;
	private ItemStack[]	_items;
	
	public Kit(ConfigurationSection section) {
		super(section);
		_id = section.getName();
		_name = getString("name", true);
		String json = getString("items", true);
		_items = InventorySerialization.getInventory(json, 36);
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
}
