package com.kill3rtaco.war;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.potion.PotionEffectType;

import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.util.WarCloneable;

public class TW {
	
	public static final String	BLACK_TEAM		= "&0Black Team";
	public static final String	BLUE_TEAM		= "&9Blue Team";
	public static final String	GREEN_TEAM		= "&aGreen Team";
	public static final String	ORANGE_TEAM		= "&6Orange Team";
	public static final String	PURPLE_TEAM		= "&5Purple Team";
	public static final String	RED_TEAM		= "&cRed Team";
	public static final String	WHITE_TEAM		= "&fWhite Team";
	public static final String	YELLOW_TEAM		= "&eYellow Team";
	
	public static final String	BLACK_TEAM_ID	= "black";
	public static final String	BLUE_TEAM_ID	= "blue";
	public static final String	GREEN_TEAM_ID	= "green";
	public static final String	ORANGE_TEAM_ID	= "orange";
	public static final String	PURPLE_TEAM_ID	= "purple";
	public static final String	RED_TEAM_ID		= "red";
	public static final String	WHITE_TEAM_ID	= "white";
	public static final String	YELLOW_TEAM_ID	= "yellow";
	
	public static final File	DATA_FOLDER		= TacoWar.plugin.getDataFolder();
	public static final File	MAPS_FOLDER		= new File(DATA_FOLDER, "maps");
	public static final File	GT_FOLDER		= new File(DATA_FOLDER, "gametypes");
	public static final File	PL_FOLDER		= new File(DATA_FOLDER, "playlists");
	public static final File	WEAPONS_FOLDER	= new File(DATA_FOLDER, "weapons");
	public static final File	KITS_FOLDER		= new File(DATA_FOLDER, "kits");
	
	public static boolean eic(String test, String... tests) {
		for (String s : tests) {
			if (s.equalsIgnoreCase(test))
				return true;
		}
		return false;
	}
	
	public static String[] teamIds() {
		return new String[]{BLACK_TEAM_ID, BLUE_TEAM_ID, GREEN_TEAM_ID,
				ORANGE_TEAM_ID, PURPLE_TEAM_ID, RED_TEAM_ID, WHITE_TEAM_ID,
				YELLOW_TEAM_ID};
	}
	
	public static boolean validTeamId(String test) {
		return test == null
				|| test.isEmpty()
				|| eic(test, teamIds());
	}
	
	public static String getTeamName(String teamId) {
		if (teamId.equals(BLUE_TEAM_ID)) //goooooo blue teeamm!!
			return BLUE_TEAM;
		if (teamId.equals(GREEN_TEAM_ID))
			return GREEN_TEAM;
		if (teamId.equals(ORANGE_TEAM_ID))
			return ORANGE_TEAM;
		if (teamId.equals(PURPLE_TEAM_ID))
			return PURPLE_TEAM;
		
		if (teamId.equals(RED_TEAM_ID))				//RWBY reference anyone?
			return RED_TEAM;
		if (teamId.equals(WHITE_TEAM_ID))
			return WHITE_TEAM;
		if (teamId.equals(BLACK_TEAM_ID))
			return BLACK_TEAM;
		if (teamId.equals(YELLOW_TEAM_ID))
			return YELLOW_TEAM;
		
		return null;
	}
	
	public static Color getArmorColor(String teamId) {
		if (teamId.equals(BLUE_TEAM_ID))
			return Color.fromRGB(0x0000FF);
		if (teamId.equals(GREEN_TEAM_ID))
			return Color.fromRGB(0x00FF00);
		if (teamId.equals(ORANGE_TEAM_ID))
			return Color.fromRGB(0xFFA500);
		if (teamId.equals(PURPLE_TEAM_ID))
			return Color.fromRGB(0x800080);
		
		//RWBY
		if (teamId.equals(RED_TEAM_ID))
			return Color.fromRGB(0xFF0000);
		if (teamId.equals(WHITE_TEAM_ID))
			return Color.fromRGB(0xFFFFFF);
		if (teamId.equals(BLACK_TEAM_ID))
			return Color.fromRGB(0);
		if (teamId.equals(YELLOW_TEAM_ID))
			return Color.fromRGB(0xFFFF00);
		
		return null;
	}
	
	public static WarTeam createTeam(String id) {
		String name = getTeamName(id);
		if (name == null)
			return null;
		return new WarTeam(id, name);
	}
	
	public static String getDamageCauseName(DamageCause cause) {
		switch (cause) {
			case ENTITY_ATTACK:
				return random("Attacked", "Jumped", "Wrecked");
			case BLOCK_EXPLOSION:
			case ENTITY_EXPLOSION:
				return random("Explosion", "Exploded");
			case CONTACT:
				return random("Cactus", "Pricked", "Poked");
			case DROWNING:
				return random("Drowned", "Glub Glub");
			case FALL:
				return random("Fell", "Tripped");
			case FALLING_BLOCK:
				return random("Squished", "Squashed", "Look Out Below");
			case FIRE:
			case FIRE_TICK:
				return random("Fire", "Burned");
			case LAVA:
				return "Lava";
			case LIGHTNING:
				return random("Lightning", "Zeus'd", "Thor'd");
			case MAGIC:
				return "Magic";
			case POISON:
				return "Poisoned";
			case MELTING:
				return "Melted";
			case PROJECTILE:
				return random("Shot", "Sniped");
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
	
	public static String random(String... strings) {
		if (strings.length == 0)
			throw new IllegalArgumentException("Parameter length cannot be 0");
		return strings[new Random().nextInt(strings.length)];
	}
	
	public static boolean isSuicide(DamageCause cause) {
		switch (cause) {
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
	
	public static WarPlayer getPlayer(List<WarPlayer> players, Player p) {
		return getPlayer(players, p.getName());
	}
	
	public static WarPlayer getPlayer(List<WarPlayer> players, String name) {
		for (WarPlayer p : players) {
			if (p.getName().equalsIgnoreCase(name))
				return p;
		}
		return null;
	}
	
	public static boolean hasPlayer(List<WarPlayer> players, Player p) {
		return hasPlayer(players, p.getName());
	}
	
	public static boolean hasPlayer(List<WarPlayer> players, String name) {
		return getPlayer(players, name) != null;
	}
	
	public static WarPlayer removePlayer(List<WarPlayer> players, Player p) {
		return removePlayer(players, p.getName());
	}
	
	public static WarPlayer removePlayer(List<WarPlayer> players, WarPlayer player) {
		return removePlayer(players, player.getName());
	}
	
	public static WarPlayer removePlayer(List<WarPlayer> players, String name) {
		for (WarPlayer p : players) {
			if (p.getName().equalsIgnoreCase(name)) {
				players.remove(p);
				return p;
			}
		}
		return null;
	}
	
	public static void broadcast(List<WarPlayer> players, String message) {
		for (WarPlayer p : players) {
			p.sendMessage(message);
		}
	}
	
	public static void teleport(List<WarPlayer> players, Location location) {
		for (WarPlayer p : players) {
			p.teleport(location);
		}
	}
	
	public static void respawn(List<WarPlayer> players) {
		for (WarPlayer p : players) {
			p.respawn();
		}
	}
	
	public static <T extends WarCloneable<T>> List<T> cloneList(List<T> list) {
		List<T> clones = new ArrayList<T>();
		for (T t : list) {
			if (t != null)
				clones.add(t.cloneObject());
		}
		return clones;
	}
	
	public static <T extends WarCloneable<T>> T cloneOrNot(T cloneable, boolean clone) {
		if (clone && cloneable != null)
			return cloneable.cloneObject();
		return cloneable;
	}
	
	public static <T extends WarCloneable<T>> List<T> cloneOrNotList(List<T> list, boolean clone) {
		if (clone)
			return cloneList(list);
		return list;
	}
	
	public static boolean possibleCrit(Player player) {
		if (player == null)
			return false;
		return isOnGround(player) &&
				!isInsideBlockOrVehicle(player) &&
				!player.getActivePotionEffects().contains(PotionEffectType.BLINDNESS);
		
	}
	
	public static boolean isOnGround(Player player) {
		return player.getLocation().getY() - player.getLocation().getBlockY() > 0 &&
				player.getVelocity().getY() < 0;
	}
	
	public static boolean isInsideBlockOrVehicle(Player player) {
		return player.isInsideVehicle() &&
				player.getLocation().getBlock().getType() != Material.AIR &&
				player.getLocation().clone().add(0, 1, 0).getBlock().getType() != Material.AIR;
	}
	
	public static FireworkEffect getWaypointEffect(Color color) {
		FireworkEffect.Builder fw = FireworkEffect.builder();
		
		fw.withTrail();
		if (color != null) {
			fw.withFade(color);
			fw.with(Type.BURST);
		} else {
			fw.withFade(Color.BLACK);
			fw.with(Type.CREEPER);
		}
		
		return fw.build();
	}
	
	public static FireworkEffect randomFireworkEffect() {
		Random random = new Random();
		FireworkEffect.Builder fw = FireworkEffect.builder();
		
		//colors
		int colorDensity = random.nextInt(3) + 1;
		for (int i = 0; i < colorDensity; i++) {
			Color randColor = randomColor();
			if (random.nextBoolean()) //regular
				fw.withColor(randColor);
			else
				//fade
				fw.withFade(randColor);
		}
		
		//effects
		fw.trail(random.nextBoolean());
		fw.flicker(random.nextBoolean());
		
		//burst effect
		fw.with(Type.values()[random.nextInt(Type.values().length)]);
		
		return fw.build();
		
	}
	
	public static Color randomColor() {
		Random random = new Random();
		return Color.fromRGB(random.nextInt(266), random.nextInt(266), random.nextInt(266));
	}
	
	public static int getNearestDegree(double degree, double factor) {
		return (int) (Math.round(degree / factor) * factor);
	}
	
}
