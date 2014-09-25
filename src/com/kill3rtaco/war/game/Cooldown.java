package com.kill3rtaco.war.game;

import java.text.DecimalFormat;

//created for the possibility of being used later
public class Cooldown {

	private long	_timeStart, _duration;

	public Cooldown(int durationSeconds) {
		this(durationSeconds * 1000L);
	}

	public Cooldown(int durationSeconds, boolean start) {
		this(durationSeconds * 1000L, start);
	}

	public Cooldown(long durationMillis) {
		this(durationMillis, true);
	}

	public Cooldown(long durationMillis, boolean start) {
		_duration = durationMillis;
		if (start)
			_timeStart = System.currentTimeMillis();
		else
			_timeStart = -1;
	}

	public void start() {
		if (!started())
			_timeStart = System.currentTimeMillis();
	}

	public boolean started() {
		return _timeStart > -1;
	}

	public long durationMillis() {
		return _duration;
	}

	public double durationSeconds() {
		return _duration / 1000;
	}

	public String durationSecondsFormatted() {
		return new DecimalFormat("#.##").format(durationSeconds());
	}

	public long timeDone() {
		return _timeStart + _duration;
	}

	public boolean completed() {
		return System.currentTimeMillis() >= timeDone();
	}

	public int percentCompleted() {
		if (!started())
			return 0;
		if (completed())
			return 100;
		long complete = timeDone() - System.currentTimeMillis();
		return (int) ((complete / _duration) * 100);
	}

}
