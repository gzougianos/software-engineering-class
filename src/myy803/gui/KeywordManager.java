package myy803.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public enum KeywordManager {
	INSTANCE;
	private static final String COLORED_KEYWORDS_FILE = "/myy803/resources/colored_keywords";
	private HashSet<String> keywords = new HashSet<>();

	private KeywordManager() {
		loadKeywords();
	}

	public boolean isKeyword(String word) {
		return keywords.contains(word);
	}

	private void loadKeywords() {
		try (InputStreamReader isr = new InputStreamReader(KeywordManager.class.getResourceAsStream(COLORED_KEYWORDS_FILE));
				BufferedReader br = new BufferedReader(isr)) {
			String line;
			while ((line = br.readLine()) != null) {
				String trimed = line.trim();
				if (!trimed.isEmpty())
					keywords.add(trimed);
			}
		} catch (IOException e) {
			System.err.println("Error reading colored keywords file: " + COLORED_KEYWORDS_FILE);
			e.printStackTrace();
		}
	}
}
