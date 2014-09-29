package com.kill3rtaco.war.game.tasks;

import org.bukkit.Location;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TW;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.map.Hill;
import com.kill3rtaco.war.game.map.WarMap;

public class HillMarkerTask extends BukkitRunnable {
	
	@Override
	public void run() {
		if (!TacoWar.gameRunning()) {
			cancel();
			return;
		}
		
		WarMap map = TacoWar.currentGame().getMap();
		if (TacoWar.currentGame().getGameType().getType() != GameType.KOTH || !map.gameTypeSupported(GameType.KOTH))
			return;
		
		for (Hill h : map.getHills()) {
			Location hillLocation = h.getLocation();
			Firework fw = hillLocation.getWorld().spawn(hillLocation, Firework.class);
			FireworkMeta meta = fw.getFireworkMeta();
			meta.setPower(3);
			if (h.getPlayers().size() > 0) {
				if (h.isContested())
					meta.addEffect(TW.getHillWaypointEffect(null));
				else
					meta.addEffect(TW.getHillWaypointEffect(TW.getArmorColor(h.getControllingTeam().getId())));
			} else {
				meta.addEffect(TW.getEmptyHillWaypointEffect());
			}
			fw.setFireworkMeta(meta);
		}
	}
}
