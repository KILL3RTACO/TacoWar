package com.kill3rtaco.war;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
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
import org.bukkit.inventory.ItemStack;

import com.kill3rtaco.tacoapi.TacoAPI;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.Kill;
import com.kill3rtaco.war.game.Player;

public class GameListener implements Listener {
	
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
		//get killer, print to kill feed
		if(e instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent e2 = (EntityDamageByEntityEvent) e;
			EntityType attackerType = e2.getDamager().getType();
			if(attackerType != EntityType.PLAYER) {
				//if killed by a mob, add and print to kill feed
				if(e2.getDamager() instanceof LivingEntity) {
					String weapon = TacoAPI.getChatUtils().toProperCase(attackerType.name());
					Kill kill = game.getKillFeed().addToFeed(player, weapon, player);
					game.getKillFeed().printKill(kill);
				}
				return;
			}
			Player attacker = game.getPlayer((org.bukkit.entity.Player) e2.getDamager());
			if(attacker == null) {
				return;
			}
			
			boolean friendly = false;
			
			//should not be needed - cancel friendly fire
			if(attacker.getTeam() == player.getTeam()) {
				if(TacoWar.config.friendlyFireEnabled()) {
					friendly = true;
				} else {
					e2.setCancelled(true);
					return;
				}
			}
			
			ItemStack item = attacker.getBukkitPlayer().getItemInHand();
			String weapon;
			if(item == null) {
				weapon = "Fist";
			} else {
				weapon = TacoAPI.getChatUtils().toProperCase(item.getType().name());
			}
			Kill kill = game.getKillFeed().addToFeed(attacker, weapon, player);
			game.getKillFeed().printKill(kill);
			if(friendly) {
				int penalty = TacoWar.config.friendlyFirePenalty();
				if(penalty != 0) {
					game.addToScore(attacker.getTeam(), Math.abs(penalty) * -1);
				}
			} else {
				System.out.println("onDeath 78");
				game.addToScore(attacker.getTeam(), 1);
			}
		} else {
			//died by natural causes
			String weapon = TacoAPI.getChatUtils().toProperCase(e.getCause().name());
			Kill kill = game.getKillFeed().addToFeed(player, weapon, player);
			game.getKillFeed().printKill(kill);
			int penalty = TacoWar.config.suicidePenalty();
			if(penalty != 0) {
				game.addToScore(player.getTeam(), Math.abs(penalty) * -1);
			}
		}
		event.setDeathMessage(null); //remove death message
		event.getDrops().clear(); //clear any items dropped
		event.setDroppedExp(0); //clear any xp dropped
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
