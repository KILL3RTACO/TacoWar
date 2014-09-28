package com.kill3rtaco.war.game.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.player.WarPlayer;

public class RemoveExpiredDamageTask extends BukkitRunnable {
	
	@Override
	public void run() {
		Game game = TacoWar.currentGame();
		if (!game.isRunning())
			return;
		for (WarPlayer p : game.getPlayers()) {
			p.removeExpiredDamagers();
		}
	}
	
	public void start() {
		runTaskTimer(TacoWar.plugin, 0L, 20L);
	}
	
}
