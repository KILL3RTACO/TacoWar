package com.kill3rtaco.war.game.tasks;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;

public class LaunchCelebrationRocketTask extends BukkitRunnable {
	
	private int	_rockets;
	
	public LaunchCelebrationRocketTask() {
		this(50);
	}
	
	public LaunchCelebrationRocketTask(int rockets) {
		_rockets = rockets;
	}
	
	@Override
	public void run() {
		if (_rockets == 0) {
			cancel();
			return;
		}
		TacoWar.currentGame().getMap().launchCelebratoryRocket();
		_rockets--;
	}
	
}
