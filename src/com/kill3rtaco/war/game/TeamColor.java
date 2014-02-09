package com.kill3rtaco.war.game;

import org.bukkit.ChatColor;
import org.bukkit.Color;

import com.kill3rtaco.tacoapi.util.ChatUtils;

public enum TeamColor {
	
	RED(0xFF0000, 'c'),
	ORANGE(0xFFA500, '6'),
	YELLOW(0xFFFF00, 'e'),
	GREEN(0x00FF00, '2'),
	BLUE(0x0000FF, '9'),
	PURPLE(0x800080, '5'),
	BROWN(0x964B00, '6'),
	BLACK(0, '0'),
	WHITE(0xFFFFFF, 'f');
	
	private Color	_armorColor;
	private char	_chatColor;
	
	private TeamColor(int rgb, char chatColor) {
		_armorColor = Color.fromRGB(rgb);
		_chatColor = chatColor;
	}
	
	public Color getArmorColor() {
		return _armorColor;
	}
	
	public String getChatColor() {
		return ChatColor.COLOR_CHAR + "" + _chatColor;
	}
	
	public String getName() {
		return new ChatUtils().capitalize(name().toLowerCase());
	}
	
	public String getColorfulName() {
		return getChatColor() + getName();
	}
	
	public String toString() {
		return getName();
	}
	
	public static TeamColor getTeamColor(String name) {
		for(TeamColor c : values()) {
			if(c.name().equalsIgnoreCase(name)) {
				return c;
			}
		}
		return null;
	}
}
