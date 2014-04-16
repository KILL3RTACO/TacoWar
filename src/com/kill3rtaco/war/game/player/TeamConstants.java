package com.kill3rtaco.war.game.player;

import org.bukkit.Color;

public class TeamConstants {
	
	public static Color	RED_ARMOR		= Color.fromRGB(0xFF0000);
	public static Color	ORANGE_ARMOR	= Color.fromRGB(0xFFA500);
	public static Color	YELLOW_ARMOR	= Color.fromRGB(0xFFFF00);
	public static Color	GREEN_ARMOR		= Color.fromRGB(0x00FF00);
	public static Color	BLUE_ARMOR		= Color.fromRGB(0x0000FF);
	public static Color	PURPLE_ARMOR	= Color.fromRGB(0x800080);
	public static Color	BROWN_ARMOR		= Color.fromRGB(0x964B00);
	public static Color	BLACK_ARMOR		= Color.fromRGB(0);
	public static Color	WHITE_ARMOR		= Color.fromRGB(0xFFFFFF);
	
	public static boolean validTeamName(String name) {
		String[] names = new String[]{"red", "orange", "yellow", "blue",
				"purple", "brown", "black", "white", "hiders", "seekers",
				"infected", "survivors"};
		for(String s : names) {
			if(s.equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
	
	public static Color getArmorColor(String color) {
		if(color.equalsIgnoreCase("red")) {
			return RED_ARMOR;
		} else if(color.equalsIgnoreCase("orange")) {
			return ORANGE_ARMOR;
		} else if(color.equalsIgnoreCase("yellow")) {
			return YELLOW_ARMOR;
		} else if(color.equalsIgnoreCase("blue")) {
			return BLUE_ARMOR;
		} else if(color.equalsIgnoreCase("purple")) {
			return PURPLE_ARMOR;
		} else if(color.equalsIgnoreCase("brown")) {
			return BROWN_ARMOR;
		} else if(color.equalsIgnoreCase("black")) {
			return BLACK_ARMOR;
		} else if(color.equalsIgnoreCase("white")) {
			return WHITE_ARMOR;
		}
		
		return null;
	}
	
}
