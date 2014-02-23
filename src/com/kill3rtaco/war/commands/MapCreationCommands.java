package com.kill3rtaco.war.commands;

import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Map;
import com.kill3rtaco.war.util.MapUtil;

public class MapCreationCommands {
	
	@ParentCommand("twmap")
	@Command(name = "add-spawn", aliases = {"as"}, args = "<map-id> <team>", desc = "Add a spawn for a team")
	@CommandPermission("TacoWar.map-creator.add-spawn")
	public static void addSpawn(CommandContext context) {
		Map m = MapUtil.getMap(context);
		if(m == null)
			return;
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "create", aliases = {"c"}, args = "<id>", desc = "Create a new map")
	@CommandPermission("TacoWar.map-creator.create")
	public static void create(CommandContext context) {
		if(context.lt(1)) {
			context.sendMessageToSender("&CMust supply a map id");
			return;
		}
		if(TacoWar.plugin.experimentalMapExists(context.getString(0))) {
			context.sendMessageToSender("That is already an experimental map");
			return;
		}
		TacoWar.plugin.addExperimentalMap(new Map(context.getString(0)));
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-origin", aliases = {"so"}, args = "<map-id>", desc = "Set the origin for a map")
	@CommandPermission("TacoWar.map-creator.set-origin")
	public static void setOrigin(CommandContext context) {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-lobby-location", aliases = {"sll"}, args = "<map-id>", desc = "Set the lobby location for a map")
	@CommandPermission("TacoWar.map-creator.set-lobby-location")
	public static void setLobbyLocation(CommandContext context) {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "add-spawn", aliases = {"sn"}, args = "<map-id> <name>", desc = "Set the name of a map")
	@CommandPermission("TacoWar.map-creator.set-name")
	public static void setName(CommandContext context) {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-authors", aliases = {"sa"}, args = "<map-id> <authors>", desc = "Set the authors of a map")
	@CommandPermission("TacoWar.map-creator.set-authors")
	public static void setAuthors(CommandContext context) {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-play-time", aliases = {"spt"}, args = "<map-id> <time-of-day>", desc = "Set the time for the map to played")
	@CommandPermission("TacoWar.map-creator.set-play-time")
	public static void setPlayTime(CommandContext context) {
		
	}
	
//	public static void setPermission(CommandContext context) {
//		
//	}
	
}
