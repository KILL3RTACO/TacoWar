package com.kill3rtaco.war.util;

import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.map.Map;

public class MapUtil {
	
	public static Map getMap(CommandContext context) {
		if(context.lt(1)) {
			context.sendMessageToSender("&cMust supply a map id");
			return null;
		}
		Map m = TacoWar.plugin.getExperimentalMap(context.getString(0));
		if(m == null) {
			context.sendMessageToSender("&cMap '" + context.getString(0) + "' is not and experimental map");
		}
		return m;
	}
	
}
