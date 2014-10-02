package com.kill3rtaco.war.game.player.weapon;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.SmallFireball;
import org.bukkit.entity.Snowball;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;

import com.kill3rtaco.tacoapi.api.serialization.SingleItemSerialization;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.util.Identifyable;
import com.kill3rtaco.war.util.MapUtil;
import com.kill3rtaco.war.util.ValidatedConfig;
import com.kill3rtaco.war.util.WarCloneable;

public class Weapon extends ValidatedConfig implements Identifyable, WarCloneable<Weapon> {
	
	public static final String	KEY_AMMO				= "ammo";
	public static final String	KEY_DESC				= "description";			//list of strings
	public static final String	KEY_ID					= "id";
	public static final String	KEY_ITEM_INFO			= "item";
	public static final String	KEY_NAME				= "name";
	public static final String	KEY_ON_CRIT				= "onCritical";
	public static final String	KEY_ON_DEATH			= "onDeath";				//when this weapon kills a player
	public static final String	KEY_ON_HIT				= "onHit";					//hit player (left click)
	public static final String	KEY_ON_PROJECTILE_HIT	= "onProjectileHit";		//when a projectile hits, if launched
	public static final String	KEY_ON_USE				= "onUse";					//right click
																					
	public static final String	ACTION_ARROW			= "arrow";
	public static final String	ACTION_DAMAGE			= "damage";				//player hit
	public static final String	ACTION_EGG				= "egg";
	public static final String	ACTION_EXPLODE			= "explode";
	public static final String	ACTION_FIREBALL			= "fireball";
	public static final String	ACTION_IGNITE			= "ignite";				//player hit
	public static final String	ACTION_LIGHTNING		= "lightning";
	public static final String	ACTION_LIGHTNING_EFFECT	= "lightning-effect";
	public static final String	ACTION_SNOWBALL			= "snowball";
	
	public static final String	METADATA_KEY			= "tw.weapon";
	public static final String	METADATA_FIRE_FROM_KEY	= "tw.weapon.firedFrom";
	
	private String				_id;
	private int					_ammo;
	private HashMap<String, Integer>	_onCrit, _onDeath, _onHit,
										_onProjectileHit, _onUse;
	private WarPlayer					_holder;
	private ItemStack					_item;
	
	//event actions:
	// - damage [amount-in-half-hearts]
	// - explode [force(def:tnt)]
	// - egg 
	// - lightning
	// - lightning-effect
	// - snowball 
	
	protected Weapon() {
		this(new YamlConfiguration());
	}
	
	public Weapon(ConfigurationSection config) {
		super(config);
	}
	
	public static void setMetadata(Metadatable entity, Weapon from) {
		entity.setMetadata(METADATA_KEY, new FixedMetadataValue(TacoWar.plugin, from));
		if (entity instanceof Arrow)
			entity.setMetadata(METADATA_FIRE_FROM_KEY, new FixedMetadataValue(TacoWar.plugin, ((Arrow) entity).getLocation()));
	}
	
	public void reload() {
		_id = getString(KEY_ID, true);
		_ammo = getInt(KEY_AMMO, -1);
		_onCrit = getActions(KEY_ON_CRIT);
		_onDeath = getActions(KEY_ON_DEATH);
		_onHit = getActions(KEY_ON_HIT);
		_onUse = getActions(KEY_ON_USE);
		createItem();
	}
	
	public String getId() {
		return _id;
	}
	
	protected void createItem() {
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
	
	public ItemStack asItem() {
		return _item;
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
		HashMap<String, Integer> map = getActionMap(event);
		if (map == null)
			return false;
		return map.containsKey(action);
	}
	
	//hasAction should be called first
	public int getActionValue(String event, String action) {
		HashMap<String, Integer> map = getActionMap(event);
		if (map == null)
			return 0;
		return map.get(action);
	}
	
	private HashMap<String, Integer> getActionMap(String event) {
		if (event.equals(KEY_ON_DEATH)) {
			return _onDeath;
		} else if (event.equals(KEY_ON_HIT)) {
			return _onHit;
		} else if (event.equals(KEY_ON_USE)) {
			return _onUse;
		} else if (event.equals(KEY_ON_CRIT)) {
			return _onCrit;
		} else if (event.equals(KEY_ON_PROJECTILE_HIT)) {
			return _onProjectileHit;
		} else {
			return null;
		}
	}
	
	public int getAmmo() {
		return _ammo;
	}
	
	public void setAmmo(int ammo) {
		_ammo = ammo;
		if (_ammo == 0) {
			
		}
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
	
	public void onUse() {
		onUse(_holder.getTargetBlock(500).getLocation());
	}
	
	public void onUse(Location looking) {
		if (doLocationEvents(KEY_ON_USE, looking))
			decrementAmmo();
	}
	
	public void onHit(WarPlayer hit) {
		if (doPlayerEvents(KEY_ON_HIT, hit))
			decrementAmmo();
	}
	
	public void onDeath(WarPlayer killed) {
		if (doPlayerEvents(KEY_ON_DEATH, killed))
			decrementAmmo();
	}
	
	public void onCrit(WarPlayer hit) {
		if (doPlayerEvents(KEY_ON_CRIT, hit))
			decrementAmmo();
	}
	
	public void onProjectileHit(Location hit) {
		if (doLocationEvents(KEY_ON_PROJECTILE_HIT, hit))
			decrementAmmo();
	}
	
	private boolean doLocationEvents(String event, Location location) {
		boolean decrementAmmo = false;
		decrementAmmo = arrow(event) || decrementAmmo;
		decrementAmmo = egg(event) || decrementAmmo;
		decrementAmmo = explosion(event, location) || decrementAmmo;
		decrementAmmo = fireball(event) || decrementAmmo;
		decrementAmmo = lightning(event, location, false) || decrementAmmo;
		decrementAmmo = lightning(event, location, true) || decrementAmmo;
		decrementAmmo = snowball(event) || decrementAmmo;
		return decrementAmmo;
	}
	
	private boolean doPlayerEvents(String event, WarPlayer player) {
		boolean decrementAmmo = false;
		Location location = player.getLocation();
		if (location == null)
			return false;
		decrementAmmo = doLocationEvents(event, player.getLocation()) || decrementAmmo;
		damage(event, player);
		decrementAmmo = ignite(event, player) || decrementAmmo;
		return decrementAmmo;
	}
	
	private boolean damage(String event, WarPlayer player) {
		if (!hasAction(event, ACTION_DAMAGE))
			return false;
		player.setHealthRelative(getActionValue(event, ACTION_DAMAGE));
		return true;
	}
	
	private boolean explosion(String event, Location loc) {
		if (!hasAction(event, ACTION_EXPLODE))
			return false;
		int f = getActionValue(event, ACTION_EXPLODE);
		loc.getWorld().createExplosion(loc.getX(), loc.getY(), loc.getZ(), f, false, false);
		return true;
	}
	
	private boolean lightning(String event, Location location, boolean isEffect) {
		if ((isEffect && !hasAction(event, ACTION_LIGHTNING_EFFECT))
				|| (!isEffect && !hasAction(event, ACTION_LIGHTNING))) {
			return false;
		}
		location = MapUtil.getHighestBlock(location);
		if (isEffect)
			location.getWorld().strikeLightningEffect(location);
		else
			setMetadata(location.getWorld().strikeLightning(location), this);
		return true;
	}
	
	private boolean ignite(String event, WarPlayer player) {
		if (!hasAction(event, ACTION_IGNITE))
			return false;
		int ticks = getActionValue(event, ACTION_IGNITE);
		player.ignite(ticks);
		return true;
	}
	
	private boolean arrow(String event) {
		return projectile(event, ACTION_ARROW, Arrow.class);
	}
	
	private boolean egg(String event) {
		return projectile(event, ACTION_EGG, Egg.class);
	}
	
	private boolean fireball(String event) {
		return projectile(event, ACTION_FIREBALL, SmallFireball.class);
	}
	
	private boolean snowball(String event) {
		return projectile(event, ACTION_SNOWBALL, Snowball.class);
	}
	
	private boolean projectile(String event, String action, Class<? extends Projectile> projectile) {
		if (!hasAction(event, action))
			return false;
		setMetadata(_holder.getBukkitPlayer().launchProjectile(projectile), this);
		return true;
	}
	
	public Weapon cloneObject() {
		Weapon w = new Weapon(_config);
		w.setAmmo(_ammo);
		return w;
	}
	
}
