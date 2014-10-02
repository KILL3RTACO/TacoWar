package com.kill3rtaco.war.commands;

import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandContext;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;
import com.kill3rtaco.war.TacoWar;
import com.kill3rtaco.war.TacoWarQueue;
import com.kill3rtaco.war.game.Game;
import com.kill3rtaco.war.game.GameType;
import com.kill3rtaco.war.game.map.WarMap;
import com.kill3rtaco.war.game.player.kit.WarKit;
import com.kill3rtaco.war.game.tasks.MapVoteSession;

public class WarCommands {
	
	// /war , kit gametype map end playlist
	
	@ParentCommand("war")
	@Command(name = "", desc = "Leave or join the queue")
	public static void emptyArgs(CommandContext context) {
		if (TacoWarQueue.toggle(context.getPlayerName())) {
			context.sendMessageToSender("&aYou have been &eadded &ato the War Queue");
		} else {
			context.sendMessageToSender("&aYou have been &cremoved &afrom the War Queue");
		}
	}
	
	@ParentCommand("war")
	@Command(name = "end", desc = "End the current game")
	@CommandPermission("TacoWar.admin.end-game")
	public static void end(CommandContext context) {
		if (!TacoWar.gameRunning()) {
			context.sendMessageToSender("&cThere is no game running at the moment");
			return;
		}
		TacoWar.currentGame().getPlayers().broadcast("&eGame ended prematurely");
		TacoWar.currentGame().end();
		context.sendMessageToSender("&aGame ended");
	}
	
	@ParentCommand("war")
	@Command(name = "gametype", aliases = {"gt"}, args = "<id>", desc = "Set the current GameType (ignores Playlist)")
	@CommandPermission("TacoWar.admin.set-gametype")
	public static void gametype(CommandContext context) {
		if (context.eq(0)) {
			context.sendMessageToSender("&cMust specify the &eGameType ID");
			return;
		}
		if (TacoWar.currentGame() == null) {
			context.sendMessageToSender("&cThe first game has not started yet");
			return;
		}
		if (TacoWar.gameRunning()) {
			context.sendMessageToSender("&cCannot change the &eGameType &cin the middle of a game");
			return;
		}
		Game game = TacoWar.currentGame();
		WarMap map = game.getMap();
		if (map == null) {
			context.sendMessageToSender("&cThe map has not been chosen yet");
			return;
		}
		GameType gametype = TacoWar.getGameType(context.getString(0));
		if (gametype == null) {
			context.sendMessageToSender("&cGameType with the id &e" + context.getString(0) + " &cnot found");
			return;
		}
		if (!map.gameTypeSupported(gametype.getType())) {
			String gtName = GameType.getGameTypeName(gametype.getType());
			context.sendMessageToSender("&cThe map &e\"" + map.getName() + "\" &cdoes not support &e" + gtName);
			return;
		}
		game.setGameType(gametype);
		context.sendMessageToSender("&aGameType set to &e" + gametype.getId());
	}
	
	@ParentCommand("war")
	@Command(name = "kit", args = "<id>", desc = "Set the current Kit (ignores Playlist)")
	@CommandPermission("TacoWar.admin.set-kit")
	public static void kit(CommandContext context) {
		if (context.eq(0)) {
			context.sendMessageToSender("&cMust specify the &eKit ID");
			return;
		}
		if (TacoWar.currentGame() == null) {
			context.sendMessageToSender("&cThe first game has not started yet");
			return;
		}
		WarKit kit = TacoWar.getKit(context.getString(0));
		if (kit == null) {
			context.sendMessageToSender("&cKit with the id &e" + context.getString(0) + " &cnot found");
			return;
		}
		TacoWar.currentGame().setKit(kit, false);
		context.sendMessageToSender("&aKit set to &e" + kit.getId());
	}
	
	@ParentCommand("war")
	@Command(name = "map", aliases = {"select"}, args = "<id>", desc = "Set the current Map (ignores Playlist)")
	@CommandPermission("TacoWar.admin.set-map")
	public static void map(CommandContext context) {
		if (context.eq(0)) {
			context.sendMessageToSender("&cMust specify the &eMap ID");
			return;
		}
		if (TacoWar.currentGame() == null) {
			context.sendMessageToSender("&cThe first game has not started yet");
			return;
		}
		if (TacoWar.gameRunning()) {
			context.sendMessageToSender("&cCannot change the &eMap &cin the middle of a game");
			return;
		}
		WarMap map = TacoWar.getMap(context.getString(0));
		if (map == null) {
			context.sendMessageToSender("&cMap with the id &e" + context.getString(0) + " &cnot found");
			return;
		}
		Game game = TacoWar.currentGame();
		game.setMap(map);
		if (MapVoteSession.isRunning()) {
			game.getPlayers().broadcast("&eVoting ended early!");
			MapVoteSession.end();
			game.getPlayers().broadcast("&eAdmin choese the map");
		}
		game.spawnAtMapLobby(true);
	}
	
	public static void newGame(CommandContext context) {
		
	}
	
	@ParentCommand("war")
	@Command(name = "playlist", args = "<id>", desc = "Set the current Playlist")
	@CommandPermission("TacoWar.admin.set-playlist")
	public static void playlist(CommandContext context) {
		if (context.eq(0)) {
			context.sendMessageToSender("&cMust specify the &ePlaylist ID");
			return;
		}
		TacoWar.config.setPlaylist(context.getString(0));
	}
	
}
