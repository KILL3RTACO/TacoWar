package com.kill3rtaco.war;

import java.io.File;
import java.io.IOException;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class ValidatedConfig {
	
	protected YamlConfiguration	_config;
	private boolean				_valid	= true;
	
	public ValidatedConfig(YamlConfiguration config) {
		_config = config;
	}
	
	protected String getString(String path, boolean req) {
		if(_config.isString(path))
			return _config.getString(path);
		if(req)
			_valid = false;
		return null;
	}
	
	protected Location getPoint(String s) {
		String[] split = s.split("\\s+");
		if(split.length < 3)
			return null;
		int x = getInt(split[0]);
		int y = getInt(split[1]);
		int z = getInt(split[2]);
		int pitch = 0;
		int yaw = 0;
		if(split.length > 3)
			yaw = getInt(split[3]);
		if(split.length > 4)
			pitch = getInt(split[4]);
		return new Location(TacoWar.config.getWarWorld(), x, y, z, yaw, pitch);
	}
	
	private int getInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return 0;
		}
	}
	
	public boolean isValid() {
		return _valid;
	}
	
	public void save(File file) {
		try {
			if(file != null) {
				file.getParentFile().mkdirs();
				_config.save(file);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
