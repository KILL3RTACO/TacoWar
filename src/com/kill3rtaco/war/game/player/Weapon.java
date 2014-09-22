package com.kill3rtaco.war.game.player;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.kill3rtaco.tacoapi.api.serialization.SingleItemSerialization;
import com.kill3rtaco.war.Identifyable;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.ValidatedConfig;

public class Weapon extends ValidatedConfig implements Identifyable {

	public static final String	KEY_AMMO				= "ammo";
	public static final String	KEY_DESC				= "description";		//list of strings
	public static final String	KEY_ID					= "id";
	public static final String	KEY_ITEM_INFO			= "item";
	public static final String	KEY_NAME				= "name";
	public static final String	KEY_ON_DEATH			= "onDeath";
	public static final String	KEY_ON_HIT				= "onHit";				//hit player (left click)
	public static final String	KEY_ON_USE				= "onUse";				//right click

	public static final String	ACTION_DAMAGE			= "damage";
	public static final String	ACTION_EXPLODE			= "explode";
	public static final String	ACTION_EXPLODE_EFFECT	= "explode-effect";
	public static final String	ACTION_LIGHTNING		= "lightning";
	public static final String	ACTION_LIGHTNING_EFFECT	= "lightning-effect";

	public static final String	METADATA_KEY			= "tw.weapon";

	private String				_id;
	private int					_ammo;
	private HashMap<String, Integer>	_onDeath, _onHit, _onUse;
	private WarPlayer					_holder;
	private ItemStack					_item;

	//event actions:
	// - explode [force]
	// - explode-effect
	// - lightning
	// - lightning-effect
	// - damage [amount-in-hearts]

	protected Weapon() {
		this(new YamlConfiguration());
	}

	public Weapon(ConfigurationSection config) {
		super(config);
		load();
	}

	protected void load() {
		_id = getString(KEY_ID, true);
		_ammo = getInt(KEY_AMMO, -1);
		_onDeath = getActions(KEY_ON_DEATH);
		_onHit = getActions(KEY_ON_HIT);
		_onUse = getActions(KEY_ON_USE);
		createItem();
	}

	public String getId() {
		return _id;
	}

	private void createItem() {
		String info = getString(KEY_ITEM_INFO, true);
		if (info == null || info.isEmpty() || !_valid)
			return;

		_item = SingleItemSerialization.getItem(info);
		String name = getString(KEY_NAME, false);
		ItemMeta meta = _item.getItemMeta();
		if (name != null)
			meta.setDisplayName(_config.getString(KEY_NAME));

		List<String> lore = getStringList(KEY_DESC, false);
		if (lore != null)
			meta.setLore(lore);

		_item.setItemMeta(meta);

	}

	public void setHolder(WarPlayer player) {
		_holder = player;
	}

	public WarPlayer getHolder() {
		return _holder;
	}

	private HashMap<String, Integer> getActions(String key) {
		List<String> list = getStringList(key, false);
		if (list == null)
			return new HashMap<String, Integer>();

		HashMap<String, Integer> actions = new HashMap<String, Integer>();
		for (String s : list) {
			String k = "";
			int v = 0;
			String[] split = s.split("\\s+");
			if (split == null || split.length == 0)
				continue;
			k = split[0];
			if (split.length > 1) {
				try {
					v = Integer.parseInt(split[1]);
				} catch (NumberFormatException e) {
					v = (Boolean.parseBoolean(split[1]) ? 1 : 0);
				}
			}
			actions.put(k, v);
		}
		return actions;
	}

	public boolean hasAction(String event, String action) {
		HashMap<String, Integer> map;
		if (event.equals(KEY_ON_DEATH)) {
			map = _onDeath;
		} else if (event.equals(KEY_ON_HIT)) {
			map = _onHit;
		} else if (event.equals(KEY_ON_USE)) {
			map = _onUse;
		} else {
			return false;
		}
		return map.containsKey(action);
	}

	public int getAmmo() {
		return _ammo;
	}

	public void setAmmo(int ammo) {
		_ammo = ammo;
	}

	public void incrementAmmo() {
		incrementAmmo(1);
	}

	public void incrementAmmo(int amount) {
		setAmmo(_ammo + amount);
	}

	public void decrementAmmo() {
		decrementAmmo(1);
	}

	public void decrementAmmo(int amount) {
		setAmmo(_ammo - amount);
	}

	public void onUse(Location looking) {

	}

	public void onUse(Projectile projectile) {
		projectile.setMetadata(METADATA_KEY, new FixedMetadataValue(TacoWar.plugin, _id));
	}

	public void onHit(Location hit) {

	}

	public void onDeath(WarPlayer killed) {

	}

	public Weapon clone() {
		Weapon w = new Weapon(_config);
		w.setAmmo(_ammo);
		return w;
	}

}
