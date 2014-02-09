package com.kill3rtaco.war.commands;

import com.kill3rtaco.tacoapi.api.ncommands.Command;
import com.kill3rtaco.tacoapi.api.ncommands.CommandPermission;
import com.kill3rtaco.tacoapi.api.ncommands.ParentCommand;

public class MapCreationCommands {
	
	@ParentCommand("twmap")
	@Command(name = "add-spawn", aliases = {"as"}, args = "<map-id> <team>", desc = "Add a spawn for a team")
	@CommandPermission("TacoWar.map-creator.add-spawn")
	public static void addSpawn() {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-origin", aliases = {"so"}, args = "<map-id>", desc = "Set the origin for a map")
	@CommandPermission("TacoWar.map-creator.set-origin")
	public static void setOrigin() {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-lobby-location", aliases = {"sll"}, args = "<map-id>", desc = "Set the lobby location for a map")
	@CommandPermission("TacoWar.map-creator.set-lobby-location")
	public static void setLobbyLocation() {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "add-spawn", aliases = {"sn"}, args = "<map-id> <team>", desc = "Set the name of a map")
	@CommandPermission("TacoWar.map-creator.set-name")
	public static void setName() {
		
	}
	
	@ParentCommand("twmap")
	@Command(name = "set-authors", aliases = {"sa"}, args = "<map-id> <team>", desc = "Set the authors of a map")
	@CommandPermission("TacoWar.map-creator.set-authors")
	public static void setAuthors() {
		
	}
	
}
