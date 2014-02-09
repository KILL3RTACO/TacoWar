package com.kill3rtaco.war.commands;

import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.Game.GameState;
import com.kill3rtaco.war.game.Map;

public class GameCommands {
	
	public static void addPlayer() {
		
	}
	
	@ParentCommand("tacowar")
	@Command(name = "begin", aliases = {"b"}, args = "[map]", desc = "Skip the lobby")
	@CommandPermission("TacoWar.admin.begin")
	public static void begin(CommandContext context) {
		if(context.gt(0) && !select(context)) {
			return;
		}
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.getGameState() == GameState.IN_LOBBY) {
			game.start();
		} else {
			context.sendMessageToSender("Players are not currently in the lobby");
		}
	}
	
	@ParentCommand("tacowar")
	@Command(name = "end", aliases = {"e"}, desc = "End the current game")
	@CommandPermission("TacoWar.admin.end")
	public static void end(CommandContext context) {
		Game game = TacoWar.plugin.currentGame();
		if(game != null && game.isRunning()) {
			game.end();
		} else {
			context.sendMessageToSender("&cThere is no game in progress");
		}
	}
	
	@ParentCommand("tacowar")
	@Command(name = "select", aliases = {"sel"}, args = "<map>", desc = "Select a different map")
	@CommandPermission("TacoWar.admin.select")
	public static boolean select(CommandContext context) {
		Game game = TacoWar.plugin.currentGame();
		if(game == null || game.getGameState() != GameState.IN_LOBBY) {
			context.sendMessageToSender("Players are not currently in the lobby");
			return false;
		}
		if(context.lt(1)) {
			context.sendMessageToSender("&cMust supply a map id");
			return false;
		}
		Map map = game.selectMap(context.getString(0));
		if(map == null) {
			context.sendMessageToSender("&cThe map &e" + context.getString(0) + " &ccannot be found");
			return false;
		}
		return true;
	}
	
	public static void setTeam(CommandContext context) {
		
	}
	
	public static void setScore(CommandContext context) {
		
	}
	
	@ParentCommand("tacowar")
	@Command(name = "start", desc = "Start the Automatic Game Runner")
	@CommandPermission("TacoWar.admin.start")
	public static void start(CommandContext context) {
		if(TacoWar.plugin.isAutomating()) {
			context.sendMessageToSender("&cThe Automatic Game Runner is already running");
			return;
		}
		TacoWar.plugin.startAutomation();
		context.sendMessageToSender("&aNew games will now be made when there isn't one running");
	}
	
	@ParentCommand("tacowar")
	@Command(name = "stop", desc = "Stop the Automatic Game Runner")
	@CommandPermission("TacoWar.admin.stop")
	public static void stop(CommandContext context) {
		if(!TacoWar.plugin.isAutomating()) {
			context.sendMessageToSender("&cThe Automatic Game Runner is not running");
			return;
		}
		TacoWar.plugin.stopAutomation();
		context.sendMessageToSender("&aNew games will no longer be made when there isn't one running");
	}
}
