package myy803.commons;

import java.io.File;
import java.io.IOException;

public class Files {
	public static final File MAIN_DIRECTORY = new File(System.getenv("APPDATA"), "Latex Editor");
	public static final File RECENT_FILES = new File(MAIN_DIRECTORY, "recent_files");
	public static final File SETTINGS = new File(MAIN_DIRECTORY, "settings.properties");
	public static final File USER_HOME = new File(System.getProperty("user.home"));
	public static final File VERSIONS_FOLDER = new File(Files.MAIN_DIRECTORY, "versions");

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
		if (!VERSIONS_FOLDER.exists())
			VERSIONS_FOLDER.mkdirs();
		else {
			deleteFilesInFolder(VERSIONS_FOLDER);
			VERSIONS_FOLDER.mkdirs();
		}

	}

	private static void deleteFilesInFolder(File folder) {
		File[] files = folder.listFiles();
		if (files != null) { //some JVMs return null for empty dirs
			for (File f : files) {
				if (f.isDirectory()) {
					deleteFilesInFolder(f);
				} else {
					f.delete();
				}
			}
		}
		folder.delete();
	}
}
