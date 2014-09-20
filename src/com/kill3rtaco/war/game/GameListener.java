package com.kill3rtaco.war.game;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.kill.PlayerKill;
import com.kill3rtaco.war.game.map.Teleporter;
import com.kill3rtaco.war.game.player.WarPlayer;

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
		if (attacker.getTeam() == victim.getTeam() && !game.options().friendlyFireEnabled()) {
			event.setCancelled(true);
		}
		//more stuff?...
	}

	@EventHandler
	//main method for game logic - player deaths
	public void onDeath(PlayerDeathEvent event) {
		Game game = TacoWar.currentGame();
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
			if (game.options().isKillBased()) {
				int penalty = game.options().suicidePenalty();
				if (suicide && penalty != 0) {
					game.updateScoreboard(player.getTeam().getName(), Math.abs(penalty) * -1);
				}
			}
		} else {
			WarPlayer attacker = game.getPlayer((org.bukkit.entity.Player) info.getAttacker());
			if (attacker == null) {
				return;
			}

			boolean friendly = false;

			//should not be needed - cancel friendly fire if needed
			if (attacker.getTeam().hasPlayer(player)) {
				if (game.options().friendlyFireEnabled()) {
					friendly = true;
				} else {
					e.setCancelled(true);
					return;
				}
			}

			String weapon = info.getToolActionDisplay();
			kill = game.getKillFeed().addToFeed(attacker, weapon, player);
			if (game.options().isKillBased()) {
				if (friendly) {
					int penalty = game.options().friendlyFirePenalty();
					if (penalty != 0) {
						game.updateScoreboard(attacker.getTeam().getName(), Math.abs(penalty) * -1);
					}
				} else {
					game.updateScoreboard(attacker.getTeam().getName(), 1);
				}
			}
		}
		event.setDeathMessage(null); //remove death message
//		game.getKillFeed().printKill(kill);
		kill.broadcast();
		event.getDrops().clear(); //clear any items dropped
		event.setDroppedExp(0); //clear any xp dropped

		//no point in moving forward if people dying does not award points
		if (game.options().isKillBased() && game.getScore(game.getTeamInLead().getName()) >= game.getMaxScore()) {
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

			GameType type = game.options().baseType();
			if (type == GameType.FFA) {

			} else if (type == GameType.JUGGERNAUT) {
				//get if juggernaut
			} else {
				location = game.getMap().getRandomSpawn(player.getTeam().getId());
			}

			event.setRespawnLocation(location);
			player.addArmor();
			player.giveItems();
			player.resetStats();
		}
	}

	//prevent players from dropping armor (cancel armor slot clicks)
	@EventHandler
	public void onArmorClick(InventoryClickEvent event) {
		Game game = TacoWar.currentGame();
		if (game != null && game.isRunning()) {
			if (game.isPlaying(event.getWhoClicked().getName()) && event.getSlotType() == SlotType.ARMOR) {
				event.setCancelled(true);
			}
		}
	}

}
