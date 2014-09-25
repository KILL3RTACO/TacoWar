package com.kill3rtaco.war.game.player;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Food {

	public static final Material		DEF_FOOD		= Material.COOKED_BEEF;
	public static final List<String>	DEF_FOOD_LIST	= Arrays.asList("grilled_pork 5", "golden_apple");

	private String						_material;
	private int							_amount;
	private ItemStack					_item;

	public Food(String material, int amount) {
		if (_amount <= 0)
			throw new IllegalArgumentException("Amount cannot be less than 1");
		_material = material;
		_amount = amount;
		setItem(createItem());
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public int getAmount() {
		return _amount;
	}

	public void setItem(ItemStack item) {
		_item = item;
	}

	public ItemStack asItem() {
		return _item;
	}

	protected Material getMaterial() {
		if (_material == null || _material.isEmpty())
			return DEF_FOOD;
		Material mat = Material.getMaterial(_material.toUpperCase());
		if (mat == null || !mat.isEdible())
			return DEF_FOOD;
		return mat;
	}

	public ItemStack createItem() {
		return new ItemStack(getMaterial(), _amount);
	}

}
