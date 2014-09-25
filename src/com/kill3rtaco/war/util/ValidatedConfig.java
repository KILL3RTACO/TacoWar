package com.kill3rtaco.war.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

public abstract class ValidatedConfig {

	protected ConfigurationSection	_config;
	protected boolean				_valid	= true;

	public ValidatedConfig(ConfigurationSection config) {
		_config = config;
		reload();
	}

	public abstract void reload();

	protected int getInt(String path, int def) {
		if (!_config.isInt(path))
			return def;
		return _config.getInt(path);
	}

	protected String getString(String path, boolean req) {
		if (_config.isString(path))
			return _config.getString(path);
		if (req)
			_valid = false;
		return null;
	}

	protected List<String> getStringList(String path, boolean req) {
		List<String> list;
		if (!_config.isSet(path) || !_config.isList(path))
			return new ArrayList<String>();

		if (_config.isString(path)) {
			list = new ArrayList<String>();
			list.add(_config.getString(path));
		} else {
			list = _config.getStringList(path);
		}
		return list;
	}

	public boolean isValid() {
		return _valid;
	}

	public void save(File file) {
		YamlConfiguration yaml = new YamlConfiguration();
		Map<String, Object> map = _config.getValues(true);
		for (String k : map.keySet()) {
			yaml.set(k, map.get(k));
		}
		try {
			if (file != null) {
				file.getParentFile().mkdirs();
				yaml.save(file);
				return;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void set(String path, Object value) {
		_config.set(path, value);
	}

	public ConfigurationSection getConfig() {
		return _config;
	}
}
