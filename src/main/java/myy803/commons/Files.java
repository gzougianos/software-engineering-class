package myy803.commons;

import java.io.File;
import java.io.IOException;

public class Files {
	public static final File MAIN_DIRECTORY = new File(System.getenv("APPDATA"), "Latex Editor");
	public static final File RECENT_FILES = new File(MAIN_DIRECTORY, "recent_files");
	public static final File SETTINGS = new File(MAIN_DIRECTORY, "settings.properties");
	public static final File TEMP = new File(System.getenv("TEMP"));

	public static void initialize() {
		if (!MAIN_DIRECTORY.exists())
			MAIN_DIRECTORY.mkdirs();
		if (!Files.SETTINGS.exists()) {
			try {
				Files.SETTINGS.createNewFile();
			} catch (IOException e) {
				System.err.println("Error creating settings file at: " + SETTINGS.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}
}
