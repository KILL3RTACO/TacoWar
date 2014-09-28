package com.kill3rtaco.war.game.tasks;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.player.WarPlayer;

public class StartGameSoonTask extends BukkitRunnable {
	
	private int	_totalTime	= TacoWar.config.getTimeBeforeGame();
	private int	_timeLeft	= TacoWar.config.getTimeBeforeGame();
	
	@Override
	public void run() {
		if (_timeLeft == 0) {
			TacoWar.currentGame().start();
			cancel();
			return;
		}
		
		for (WarPlayer p : TacoWar.currentGame().getPlayers()) {
			Player bukkitPlayer = p.getBukkitPlayer();
			if (bukkitPlayer == null)
				continue;
			bukkitPlayer.setLevel(_timeLeft);
			bukkitPlayer.setExp((_totalTime - _timeLeft) / _totalTime);
		}
		_timeLeft--;
	}
	
}
