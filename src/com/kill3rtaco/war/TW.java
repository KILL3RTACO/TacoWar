package com.kill3rtaco.war;

import java.io.File;

import org.bukkit.Color;

public class TW {

	public static final String	BLACK_TEAM		= "&0Black Team";
	public static final String	BLUE_TEAM		= "&9Blue Team";
	public static final String	GREEN_TEAM		= "&aGreen Team";
	public static final String	ORANGE_TEAM		= "&6Orange Team";
	public static final String	PURPLE_TEAM		= "&5Purple Team";
	public static final String	RED_TEAM		= "&cRed Team";
	public static final String	WHITE_TEAM		= "&fWhite Team";
	public static final String	YELLOW_TEAM		= "&eYellow Team";

	public static final String	BLACK_TEAM_ID	= "black";
	public static final String	BLUE_TEAM_ID	= "blue";
	public static final String	GREEN_TEAM_ID	= "green";
	public static final String	ORANGE_TEAM_ID	= "orange";
	public static final String	PURPLE_TEAM_ID	= "purple";
	public static final String	RED_TEAM_ID		= "red";
	public static final String	WHITE_TEAM_ID	= "white";
	public static final String	YELLOW_TEAM_ID	= "yellow";

	public static final File	DATA_FOLDER		= TacoWar.plugin.getDataFolder();
	public static final File	MAPS_FOLDER		= new File(DATA_FOLDER, "maps");
	public static final File	GT_FOLDER		= new File(DATA_FOLDER, "gametypes");
	public static final File	PL_FOLDER		= new File(DATA_FOLDER, "playlists");
	public static final File	WEAPONS_FOLDER	= new File(DATA_FOLDER, "weapons");
	public static final File	KITS_FOLDER		= new File(DATA_FOLDER, "kits");

	public static boolean eic(String test, String... tests) {
		for (String s : tests) {
			if (s.equalsIgnoreCase(test))
				return true;
		}
		return false;
	}

	public static String[] teamIds() {
		return new String[]{BLACK_TEAM_ID, BLUE_TEAM_ID, GREEN_TEAM_ID,
				ORANGE_TEAM_ID, PURPLE_TEAM_ID, RED_TEAM_ID, WHITE_TEAM_ID,
				YELLOW_TEAM_ID};
	}

	public static boolean validTeamId(String test) {
		return test == null
				|| test.isEmpty()
				|| eic(test, teamIds());
	}

	public static String getTeamName(String teamId) {
		if (teamId.equalsIgnoreCase(BLUE_TEAM_ID)) //goooooo blue teeamm!!
			return BLUE_TEAM;
		if (teamId.equalsIgnoreCase(GREEN_TEAM_ID))
			return GREEN_TEAM;
		if (teamId.equalsIgnoreCase(ORANGE_TEAM_ID))
			return ORANGE_TEAM;
		if (teamId.equalsIgnoreCase(PURPLE_TEAM_ID))
			return PURPLE_TEAM;

		if (teamId.equalsIgnoreCase(RED_TEAM_ID))				//RWBY reference anyone?
			return RED_TEAM;
		if (teamId.equalsIgnoreCase(WHITE_TEAM_ID))
			return WHITE_TEAM;
		if (teamId.equalsIgnoreCase(BLACK_TEAM_ID))
			return BLACK_TEAM;
		if (teamId.equalsIgnoreCase(YELLOW_TEAM_ID))
			return YELLOW_TEAM;

		return null;
	}

	public static Color getArmorColor(String teamId) {
		if (teamId.equalsIgnoreCase(BLUE_TEAM_ID))
			return Color.fromRGB(0x0000FF);
		if (teamId.equalsIgnoreCase(GREEN_TEAM_ID))
			return Color.fromRGB(0x00FF00);
		if (teamId.equalsIgnoreCase("orange"))
			return Color.fromRGB(0xFFA500);
		if (teamId.equalsIgnoreCase("purple"))
			return Color.fromRGB(0x800080);

		//RWBY
		if (teamId.equalsIgnoreCase("red"))
			return Color.fromRGB(0xFF0000);
		if (teamId.equalsIgnoreCase("white"))
			return Color.fromRGB(0xFFFFFF);
		if (teamId.equalsIgnoreCase("black"))
			return Color.fromRGB(0);
		if (teamId.equalsIgnoreCase("yellow"))
			return Color.fromRGB(0xFFFF00);

		return null;
	}

}
