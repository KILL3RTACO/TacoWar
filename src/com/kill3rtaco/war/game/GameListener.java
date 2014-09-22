package com.kill3rtaco.war.game;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.kill.PlayerKill;
import com.kill3rtaco.war.game.map.Teleporter;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.Weapon;

public class GameListener implements Listener {

//	private HashMap<Player, Location>	playerLocs	= new HashMap<Player, Location>();

	@EventHandler(ignoreCancelled = true)
	//detect whether player should be teleported due to teleporter
	public void onMove(PlayerMoveEvent event) {
		Game game = TacoWar.currentGame();
		if (game == null || !game.isRunning()) { //only continue if game in progress
			return;
		}
		org.bukkit.entity.Player player = event.getPlayer();
		if (player.getWorld() != TacoWar.config.getWarWorld()) { //only continue if it happened on the war world
			return;
		}

		WarPlayer p = game.getPlayer(player);
		if (p == null) { //only continue if the player involved is in the game
			return;
		}

		Location from = event.getFrom();
		Location to = event.getTo();
		if (!movedEnough(from, to)) { //only continue if they moved from one block to another
			return;
		}
		Teleporter teleporter = game.getMap().getTeleporter(to);
		if (teleporter == null || !teleporter.isTransmitter()) {
			return;
		}
		teleporter.teleportPlayer(p);
//		event.setCancelled(true);
	}

	private boolean movedEnough(Location from, Location to) {
		return from.getBlockX() != to.getBlockX() ||
				from.getBlockY() != to.getBlockY() ||
				from.getBlockZ() != to.getBlockZ();
	}

	@EventHandler
	//update attackers on players, will be used for possible multi-kill and kill assist system
	public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
		Game game = TacoWar.currentGame();
		GameType gametype = game.getGameType();
		if (game == null || !game.isRunning()) { //only continue if game in progress
			return;
		}
		AttackInfo info = new AttackInfo(event);
		if (info.getAttackerAsPlayer() == null || info.getVictimAsPlayer() == null) {
			return;
		}
		WarPlayer attacker = info.getAttackerAsPlayer(), victim = info.getVictimAsPlayer();
		if (victim.isInvincible()) {
			event.setCancelled(true);
			return;
		}
		if (attacker.getTeam() == victim.getTeam() && gametype.getConfig().getInt(GameType.KEY_PENALTY_FRIENDLY_FIRE) < 0) {
			event.setCancelled(true);
		}
		//more stuff?...
	}

	@EventHandler
	//main method for game logic - player deaths
	public void onDeath(PlayerDeathEvent event) {
		Game game = TacoWar.currentGame();
		GameType gametype = game.getGameType();
		if (game == null || !game.isRunning()) {
			return;
		}
		if (event.getEntity().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		org.bukkit.entity.Player p = event.getEntity();
		WarPlayer player = game.getPlayer(p);
		if (player == null) {
			return;
		}
		EntityDamageEvent e = event.getEntity().getLastDamageCause();
		AttackInfo info = new AttackInfo(e);
		PlayerKill kill;
		if (info.getAttacker() == null || info.getAttacker().getType() != EntityType.PLAYER) {
			String weapon = info.getToolActionDisplay();
			boolean suicide = info.isSuicide();
			kill = game.getKillFeed().addToFeed(player, weapon, player);
			if (gametype.onKill()) {
				int penalty = gametype.getConfig().getInt(GameType.KEY_PENALTY_SUICIDE);
				if (suicide && penalty != 0) {
					game.addPoints(player.getTeam(), Math.abs(penalty) * -1);
				}
			}
		} else {
			WarPlayer attacker = game.getPlayer((org.bukkit.entity.Player) info.getAttacker());
			if (attacker == null) {
				return;
			}

			boolean friendly = false;

			//cancel friendly fire if needed
			if (attacker.getTeam().hasPlayer(player)) {
				if (gametype.getConfig().getInt(GameType.KEY_PENALTY_FRIENDLY_FIRE) >= 0) {
					friendly = true;
				} else {
					e.setCancelled(true);
					return;
				}
			}

			String weapon = info.getToolActionDisplay();
			kill = game.getKillFeed().addToFeed(attacker, weapon, player);
			if (gametype.onKill()) {
				if (friendly) {
					int penalty = gametype.getConfig().getInt(GameType.KEY_PENALTY_FRIENDLY_FIRE);
					if (penalty > 0) {
						game.addPoints(attacker.getTeam(), Math.abs(penalty) * -1);
					}
				} else {
					game.addPoints(attacker.getTeam(), 1);
				}
			}
		}
		event.setDeathMessage(null); //remove death message
//		game.getKillFeed().printKill(kill);
		kill.broadcast();
		event.getDrops().clear(); //clear any items dropped
		event.setDroppedExp(0); //clear any xp dropped

		//no point in moving forward if people dying does not award points
		if (game.getScore(game.getTeamInLead()) >= gametype.getConfig().getInt(GameType.KEY_MAX_SCORE)) {
			game.end();
		}
	}

	//when a player respawns, set the respawn location to the team's base
	//set to highest to prevent Essentials or Multiverse handling respawn
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event) {
		if (event.getPlayer().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		Game game = TacoWar.currentGame();
		if (game != null && game.isRunning()) {
			WarPlayer player = game.getPlayer(event.getPlayer());
			if (player == null) {
				return;
			}
			Location location = null;

			GameType type = game.getGameType();
			location = game.getMap().getRandomSpawn(player.getTeam());

			event.setRespawnLocation(location);
			player.respawn();
		}
	}

	@EventHandler
	public void onWeaponUse(PlayerInteractEvent event) {

	}

	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (!projectile.hasMetadata(Weapon.METADATA_KEY))
			return;
		String weaponId = projectile.getMetadata(Weapon.METADATA_KEY).get(0).asString();
		Weapon firedFrom = TacoWar.getWeapon(weaponId);
		if (firedFrom == null)
			return;
		projectile.setBounce(false);
		firedFrom.onUse(projectile.getLocation());
	}

	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		//prevent players from tampering with inventory
		//also allows secret things to happen in the inventory
		Game game = TacoWar.currentGame();
		if (game != null && game.isRunning()) {
			if (game.isPlaying(event.getPlayer().getName())) {
				event.setCancelled(true);
			}
		}
	}

}
