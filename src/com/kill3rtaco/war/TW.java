package com.kill3rtaco.war;

public class TW {

	public static final String BLACK_TEAM = "&0Black Team";
	public static final String BLUE_TEAM = "&9Blue Team";
	public static final String GREEN_TEAM = "&aGreen Team";
	public static final String ORANGE_TEAM = "&6Orange Team";
	public static final String PINK_TEAM = "&dPink Team";
	public static final String PURPLE_TEAM = "&5Purple Team";
	public static final String RED_TEAM = "&cRed Team";
	public static final String WHITE_TEAM = "&eYellow Team";
	public static final String YELLOW_TEAM = "&eYellow Team";

	public static boolean eic(String test, String... tests) {
		for (String s : tests) {
			if (s.equalsIgnoreCase(test))
				return true;
		}
		return false;
	}

	public static boolean validTeamId(String test) {
		return test == null
				|| test.isEmpty()
				|| eic(test, "black", "blue", "green", "orange", "pink", "purple", "red", "white", "yellow");
	}

	public static String getTeamName(String test) {
		if (test.equalsIgnoreCase("blue"))
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("green"))
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("orange"))
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("pink"))
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("purple"))
			return BLACK_TEAM;

		if (test.equalsIgnoreCase("red"))				//RWBY reference anyone?
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("white"))
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("black"))
			return BLACK_TEAM;
		if (test.equalsIgnoreCase("yellow"))
			return BLACK_TEAM;

		return null;
	}

}
