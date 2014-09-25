package com.kill3rtaco.war.util;

import org.bukkit.Location;

import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.map.WarMap;

public class MapUtil {

	public static WarMap getMap(CommandContext context, boolean needsToBeExperimental) {
		if (context.lt(1)) {
			context.sendMessageToSender("&cMust supply a map id");
			return null;
		}
		WarMap m = TacoWar.getMap(context.getString(0));
		if (m == null) {
			context.sendMessageToSender("&cMap '&e" + context.getString(0) + "' &cnot found");
			return null;
		}
		if (needsToBeExperimental && !m.isReady()) {
			context.sendMessageToSender("&cMap '&e" + context.getString(0) + "' &cis not and experimental map");
		}
		return m;
	}

	public static Location getLightningStrikeLocation(Location loc) {
		return TacoWar.config.getWarWorld().getHighestBlockAt(loc).getLocation();
	}

	public static Location getLocation(String s) {
		String[] split = s.split("\\s+");
		if (split.length < 3)
			return null;
		int x = getInt(split[0]);
		int y = getInt(split[1]);
		int z = getInt(split[2]);
		int pitch = 0;
		int yaw = 0;
		if (split.length > 3)
			yaw = getInt(split[3]);
		if (split.length > 4)
			pitch = getInt(split[4]);
		return new Location(TacoWar.config.getWarWorld(), x, y, z, yaw, pitch);
	}

	private static int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}

}
