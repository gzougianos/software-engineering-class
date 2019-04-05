package myy803.commons;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

public enum Setting {
	// @formatter:off
	FRAME_FULLSCREEN(false),
	FRAME_X(0),
	FRAME_Y(0),
	FRAME_WIDTH(600),
	FRAME_HEIGHT(600);
	// @formatter:on
	private String key;

	private Object defaultValue;
	private Object currentValue;

	private Setting(Object defaultValue) {
		this.key = this.toString();
		this.defaultValue = defaultValue;
	}

	public boolean toBoolean() {
		return Boolean.parseBoolean(toStr());
	}

	public long toLong() {
		return Long.parseLong(toStr());
	}

	public int toInt() {
		return Integer.parseInt(toStr());
	}

	public String toStr() {
		if (currentValue == null)
			return String.valueOf(defaultValue);
		return String.valueOf(currentValue);
	}

	public void update(Object newValue) {
		currentValue = newValue;
	}

	public static void save() {
		Properties prop = new Properties();
		try (OutputStream os = new FileOutputStream(Files.SETTINGS)) {
			for (Setting s : Setting.class.getEnumConstants()) {
				prop.setProperty(s.key, s.toStr());
			}
			prop.store(os, null);
		} catch (IOException e) {
			System.err.println("Error saving settings.");
			e.printStackTrace();
		}
	}

	public static void initialize() {
		Properties prop = new Properties();
		try (FileInputStream fis = new FileInputStream(Files.SETTINGS)) {
			prop.load(fis);
			for (Setting s : Setting.class.getEnumConstants()) {
				s.update(prop.getProperty(s.key, String.valueOf(s.defaultValue)));
			}
		} catch (IOException e) {
			System.err.println("Error loading settings.");
			e.printStackTrace();
		}
	}
}
