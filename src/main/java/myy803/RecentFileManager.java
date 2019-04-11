package myy803;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import myy803.commons.Files;
import myy803.model.Document;

public enum RecentFileManager {
	INSTANCE;
	private ArrayList<String> recentFiles = new ArrayList<>();

	private RecentFileManager() {
		load();
	}

	@SuppressWarnings("unchecked")
	private void load() {
		if (!Files.RECENT_FILES.exists())
			return;
		try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(Files.RECENT_FILES));) {
			recentFiles = (ArrayList<String>) oos.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.err.println("Error reading recent files.");
			e.printStackTrace();
		}
	}

	public void save() {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Files.RECENT_FILES));) {
			oos.writeObject(recentFiles);
		} catch (IOException e) {
			System.err.println("Error writing recent files.");
			e.printStackTrace();
		}
	}

	public void push(Document doc) {
		String path = doc.getPath().getAbsolutePath();
		recentFiles.remove(path);
		recentFiles.add(path);
	}

	public String[] getFiles() {
		return recentFiles.toArray(new String[] {});
	}
}
