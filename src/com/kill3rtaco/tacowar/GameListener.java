package com.kill3rtaco.tacowar;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import com.kill3rtaco.tacowar.game.Game;
import com.kill3rtaco.tacowar.game.Player;
import com.kill3rtaco.tacowar.game.TeamColor;

public class GameListener implements Listener {
	
	//all of game logic is in this class
	
	//test for death by natural causes
	public void onDamage(EntityDamageEvent event) {
		if(event.getEntity().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			
		}
	}
	
	//if killed by player, update kill feed
	public void onDamageByPlayer(EntityDamageByEntityEvent event) {
		if(event.getEntity().getWorld() != TacoWar.config.getWarWorld()) {
			return;
		}
		if(event.getEntityType() != EntityType.PLAYER) {
			return;
		}
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			
		}
	}
	
	private void point(TeamColor awardTo, int award) {
		
	}
	
	//when a player respawns, set the respawn location to the team's base
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
		}
	}
	
	//when a player joins after dc'ing, reset the Player object for that player
	public void onJoin(PlayerJoinEvent event) {
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			game.updatePlayer(event.getPlayer());
		}
	}
	
}
