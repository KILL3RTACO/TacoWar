package com.kill3rtaco.war.util;

import java.util.Comparator;

import com.kill3rtaco.war.game.player.WarPlayer;

public class PlayerComparators {

	public static final Comparator<WarPlayer> KD = new Comparator<WarPlayer>(){

		@Override
		public int compare(WarPlayer wp1, WarPlayer wp2) {
			return new Double(wp1.getKD()).compareTo(wp2.getKD());
		}
		
	};
	
	public static final Comparator<WarPlayer> KDA = new Comparator<WarPlayer>(){
		
		@Override
		public int compare(WarPlayer wp1, WarPlayer wp2) {
			return new Double(wp1.getKDA()).compareTo(wp2.getKDA());
		}
		
	};
	
}
