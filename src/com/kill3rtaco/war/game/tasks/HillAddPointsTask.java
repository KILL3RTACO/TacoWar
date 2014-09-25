package com.kill3rtaco.war.game.tasks;

import java.util.HashMap;
import java.util.List;

import org.bukkit.scheduler.BukkitRunnable;

import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.map.Hill;
import com.kill3rtaco.war.game.player.WarTeam;
import com.kill3rtaco.war.game.types.KOTH;

public class HillAddPointsTask extends BukkitRunnable {

	private KOTH	_koth;

	public HillAddPointsTask(KOTH koth) {
		_koth = koth;
	}

	@Override
	public void run() {
		HashMap<WarTeam, Integer> points = new HashMap<WarTeam, Integer>();
		int scorePolicy = _koth.getConfig().getInt(KOTH.KEY_SCORE_POLICY);
		int multiplier = _koth.getConfig().getInt(KOTH.KEY_SCORE_MULTIPLIER);
		List<Hill> hills = TacoWar.currentGame().getMap().getHills();
		for (Hill h : hills) {
			WarTeam control = h.getControllingTeam();
			Integer p = points.get(control);
			if (p == null)
				p = 0;

			if (scorePolicy == KOTH.SCORE_POLICY_PLAYER) {
				if (h.isContested())
					continue;
				points.put(control, p + (h.getTeamDensity(control) * multiplier));
			} else if (scorePolicy == KOTH.SCORE_POLICY_TOTAL_PLAYERS) {
				points.put(control, p + (h.getAdjustedTeamDensity(control) * multiplier));
			} else {
				if (h.isContested())
					continue;
				points.put(control, p + multiplier);
			}
		}
	}

}
