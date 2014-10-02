package com.kill3rtaco.war.game;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.kill.PlayerKill;
import com.kill3rtaco.war.game.map.Teleporter;
import com.kill3rtaco.war.game.player.WarPlayer;
import com.kill3rtaco.war.game.player.weapon.Weapon;

public class GameListener implements Listener {
	
//	private HashMap<Player, Location>	playerLocs	= new HashMap<Player, Location>();
	
	@EventHandler(ignoreCancelled = true)
	//detect whether player should be teleported due to teleporter
	public void onMove(PlayerMoveEvent event) {
		if (!TacoWar.gameRunning())
			return;
		Game game = TacoWar.currentGame();
		Player player = event.getPlayer();
		if (player.getWorld() != TacoWar.config.getWarWorld()) { //only continue if it happened on the war world
			return;
		}
		
		WarPlayer p = game.getPlayers().get(player);
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
		if (!TacoWar.gameRunning())
			return;
		Game game = TacoWar.currentGame();
		GameType gametype = game.getGameType();
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
		
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		Game game = TacoWar.currentGame();
		GameType gametype = game.getGameType();
		if (game == null || !game.isRunning()) {
			return;
		}
		if (event.getEntity().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		Player p = event.getEntity();
		WarPlayer died = game.getPlayers().get(p);
		WarPlayer awardPoint;
		if (died == null) {
			return;
		}
		EntityDamageEvent e = event.getEntity().getLastDamageCause();
		AttackInfo info = new AttackInfo(e);
		PlayerKill kill;
		//possible suicide
		if (info.getAttacker() == null || info.getAttacker().getType() != EntityType.PLAYER) {
			String weapon = info.getToolActionDisplay();
			boolean suicide = info.isSuicide();
			awardPoint = died.getLastDamager();
			kill = new PlayerKill(died, weapon, died, awardPoint);
			game.getKillFeed().addToFeed(kill);
			if (gametype.onKill()) {
				int penalty = gametype.getConfig().getInt(GameType.KEY_PENALTY_SUICIDE);
				if (suicide && penalty != 0) {
					died.getTeam().setScoreRelative(Math.abs(penalty) * -1);
				}
			}
		} else { //player kill player
			WarPlayer attacker = game.getPlayers().get((Player) info.getAttacker());
			awardPoint = attacker;
			if (attacker == null) {
				return;
			}
			
			boolean friendly = false;
			int ffPen = gametype.getConfig().getInt(GameType.KEY_PENALTY_FRIENDLY_FIRE);
			
			//cancel friendly fire if needed
			if (attacker.getTeam().contains(died)) {
				if (ffPen >= 0) {
					friendly = true;
				} else {
					e.setCancelled(true);
					return;
				}
			}
			
			String weapon = info.getToolActionDisplay();
			kill = new PlayerKill(attacker, weapon, died);
			game.getKillFeed().addToFeed(kill);
			if (gametype.onKill()) {
				if (friendly) {
					if (ffPen > 0) {
						attacker.getTeam().setScoreRelative(ffPen * -1);
					}
				} else {
					attacker.getTeam().setScoreRelative(1);
				}
			}
		}
		event.setDeathMessage(null); //remove death message
		kill.setAssists(died.getLastDamagersNotKiller());
		kill.broadcast();
		if (info.isLongDistanceSnipe())
			game.broadcast("&e\"Did anyone see that, 'cause I will NOT be doing it again.\"");
		if (awardPoint != null)
			awardPoint.addKill();
		event.getDrops().clear(); //clear any items dropped
		event.setDroppedExp(0); //clear any xp dropped
		died.clearDamagers();
		died.addDeath();
		
		if (game.getTeamInLead().getScore() >= gametype.getConfig().getInt(GameType.KEY_MAX_SCORE)) {
			game.end();
		}
	}
	
	//when a player respawns, set the respawn location to the team's base
	//set to highest to prevent Essentials or Multiverse handling respawn
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event) {
		Game game = TacoWar.currentGame();
		if (game != null && game.isRunning()) {
			WarPlayer player = game.getPlayers().get(event.getPlayer());
			if (player == null) {
				return;
			}
			Location location = null;
			
//			GameType type = game.getGameType();
			location = game.getMap().getRandomSpawn(player.getTeam());
			
			event.setRespawnLocation(location);
			player.respawn();
		}
	}
	
	@EventHandler
	public void onWeaponSwitch(PlayerItemHeldEvent event) {
		if (!TacoWar.gameRunning())
			return;
		WarPlayer player = TacoWar.currentGame().getPlayers().get(event.getPlayer());
		if (player != null)
			player.updateAmmoCount();
	}
	
	@EventHandler
	public void onWeaponUse(PlayerInteractEvent event) {
		if (!TacoWar.gameRunning())
			return;
		WarPlayer player = TacoWar.currentGame().getPlayers().get(event.getPlayer());
		if (player == null)
			return;
		Action action = event.getAction();
		if (action == Action.PHYSICAL)
			return;
		Location location = null;
		for (WarPlayer p : TacoWar.currentGame().getPlayers()) {
			if (p == player)
				continue;
			Player plyr = p.getBukkitPlayer();
			if (plyr == null)
				continue;
			if (player.getBukkitPlayer().hasLineOfSight(plyr)) {
				location = plyr.getLocation();
				break;
			}
		}
		if (location == null)
			location = player.getTargetBlock(500).getLocation();
//		System.out.println("[DEBUG] -> Looking: " + location);
//		System.out.println("[DEBUG] -> heldWeapon == null: " + (player.getHeldWeapon() == null));
		Weapon held = player.getHeldWeapon();
		if (held != null) {
			held.onUse(location);
			player.updateAmmoCount();
			if (held.asItem().getType() != Material.BOW)
				event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockIgnite(BlockIgniteEvent event) {
		if (event.getBlock().getWorld() == TacoWar.config.getWarWorld())
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onTntPlace(BlockPlaceEvent event) {
		Block block = event.getBlock();
		if (block.getWorld() != TacoWar.config.getWarWorld())
			return;
		
		if (block.getType() != Material.TNT) {
			block.setType(Material.AIR);
			TNTPrimed tnt = ((TNTPrimed) block.getWorld().spawnEntity(block.getLocation(), EntityType.PRIMED_TNT));
			tnt.setIsIncendiary(false);
			tnt.setFuseTicks(tnt.getFuseTicks() + 5);
		} else {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onExplosion(EntityExplodeEvent event) {
		List<Block> blocks = event.blockList();
		if (blocks == null || blocks.isEmpty())
			return;
		
		if (blocks.get(0).getWorld() == TacoWar.config.getWarWorld())
			blocks.clear();
	}
	
	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		if (!TacoWar.gameRunning())
			return;
		Projectile projectile = event.getEntity();
		if (!(projectile.getShooter() instanceof Player))
			return;
		Player shooter = (Player) projectile.getShooter();
		WarPlayer player = TacoWar.currentGame().getPlayers().get(shooter);
		if (player == null)
			return;
		Weapon held = player.getHeldWeapon();
		if (held == null)
			return;
		
		//only do this for bows, as the Weapon instance does not actually fire the arrow
		if (held.asItem().getType() == Material.BOW)
			Weapon.setMetadata(projectile, held);
	}
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event) {
		Projectile projectile = event.getEntity();
		if (!projectile.hasMetadata(Weapon.METADATA_KEY))
			return;
		System.out.println("[DEBUG] -> projectile has metadata");
		Weapon firedFrom = (Weapon) projectile.getMetadata(Weapon.METADATA_KEY).get(0).value();
		if (firedFrom == null)
			return;
		System.out.println("[DEBUG] -> firedFrom not null");
		projectile.setBounce(false);
		firedFrom.onProjectileHit(projectile.getLocation());
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent event) {
		Game game = TacoWar.currentGame();
		if (game == null || !game.isRunning()) {
			return;
		}
		if (!game.isPlaying(event.getPlayer().getName())) {
			return;
		}
		for (int i = 9; i < 18; i++) {
			event.getInventory().setItem(i, null);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Game game = TacoWar.currentGame();
		if (game == null || !game.isRunning()) {
			return;
		}
		WarPlayer player = game.getPlayers().get(event.getPlayer().getName());
		if (player == null) {
			return;
		}
		player.updateAmmoCount();
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		if (!TacoWar.gameRunning())
			return;
		if (TacoWar.currentGame().isPlaying(event.getWhoClicked().getName()))
			event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		if (!TacoWar.gameRunning())
			return;
		if (TacoWar.currentGame().isPlaying(event.getPlayer()))
			event.setCancelled(true);
	}
	
}
