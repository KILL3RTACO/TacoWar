package com.kill3rtaco.war.listener;

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
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.kill.AttackInfo;
import com.kill3rtaco.war.game.kill.PlayerKill;
import com.kill3rtaco.war.game.player.Player;

public class GameListener implements Listener {
	
	@EventHandler
	//update attackers on players, will be used for possible multi-kill and kill assist system
	public void onPlayerDamagePlayer(EntityDamageByEntityEvent event) {
		
	}
	
	@EventHandler
	//main method for game logic - player deaths
	public void onDeath(PlayerDeathEvent event) {
		if(event.getEntity().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		Game game = TacoWar.plugin.currentGame();
		if(game == null || !game.isRunning()) {
			return;
		}
		org.bukkit.entity.Player p = event.getEntity();
		Player player = game.getPlayer(p);
		if(player == null) {
			return;
		}
		EntityDamageEvent e = event.getEntity().getLastDamageCause();
		AttackInfo info = new AttackInfo(e);
		if(info.getAttacker() == null || info.getAttacker().getType() != EntityType.PLAYER) {
			String weapon = info.getToolActionDisplay();
			boolean suicide = info.isSuicide();
			PlayerKill kill = game.getKillFeed().addToFeed(player, weapon, player);
			game.getKillFeed().printKill(kill);
			int penalty = TacoWar.config.suicidePenalty();
			if(suicide && penalty != 0) {
				game.addToScore(player.getTeam(), Math.abs(penalty) * -1);
			}
		} else {
			Player attacker = game.getPlayer((org.bukkit.entity.Player) info.getAttacker());
			if(attacker == null) {
				return;
			}
			
			boolean friendly = false;
			
			//should not be needed - cancel friendly fire if needed
			if(attacker.getTeam() == player.getTeam()) {
				if(TacoWar.config.friendlyFireEnabled()) {
					friendly = true;
				} else {
					e.setCancelled(true);
					return;
				}
			}
			
			String weapon = info.getToolActionDisplay();
			PlayerKill kill = game.getKillFeed().addToFeed(attacker, weapon, player);
			game.getKillFeed().printKill(kill);
			if(friendly) {
				int penalty = TacoWar.config.friendlyFirePenalty();
				if(penalty != 0) {
					game.addToScore(attacker.getTeam(), Math.abs(penalty) * -1);
				}
			} else {
				game.addToScore(attacker.getTeam(), 1);
			}
		}
		event.setDeathMessage(null); //remove death message
		event.getDrops().clear(); //clear any items dropped
		event.setDroppedExp(0); //clear any xp dropped
		if(game.getScore(game.getTeamInLead()) >= game.getMaxKills()) {
			game.end();
		}
	}
	
	//when a player respawns, set the respawn location to the team's base
	//set to highest to prevent Essentials or Multiverse handling respawn
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onRespawn(PlayerRespawnEvent event) {
		if(event.getPlayer().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			Player player = game.getPlayer(event.getPlayer());
			if(player == null) {
				return;
			}
			Location location = game.getMap().getTeamSpawn(player.getTeam());
			event.setRespawnLocation(location);
			player.addArmor();
			player.giveItems();
		}
	}
	
	//when a player joins after dc'ing, reset the Player object for that player
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			game.updatePlayer(event.getPlayer());
		}
	}
	
	//prevent players from dropping armor (cancel armor slot clicks)
	@EventHandler
	public void onArmorClick(InventoryClickEvent event) {
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			if(game.isPlaying(event.getWhoClicked().getName()) && event.getSlotType() == SlotType.ARMOR) {
				event.setCancelled(true);
			}
		}
	}
	
}
