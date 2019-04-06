package myy803.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;

public class KeywordStyledDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	private static final String COLORED_KEYWORDS_FILE = "/myy803/resources/colored_keywords";
	private static final HashSet<String> COLORED_KEYWORDS = new HashSet<>();
	private Style defaultStyle;
	private Style keywordStyle;
	private HashMap<Integer, String> words = new HashMap<>();

	static {
		loadColoredKeywords();
	}

	public KeywordStyledDocument(Style defaultStyle, Style keywordStyle) {
		this.defaultStyle = defaultStyle;
		this.keywordStyle = keywordStyle;
	}

	private static void loadColoredKeywords() {
		try (InputStreamReader isr = new InputStreamReader(
				KeywordStyledDocument.class.getResourceAsStream(COLORED_KEYWORDS_FILE));
				BufferedReader br = new BufferedReader(isr)) {
			String line;
			while ((line = br.readLine()) != null) {
				String trimed = line.trim();
				if (!trimed.isEmpty())
					COLORED_KEYWORDS.add(trimed);
			}
		} catch (IOException e) {
			System.err.println("Error reading colored keywords file: " + COLORED_KEYWORDS_FILE);
			e.printStackTrace();
		}
	}

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset, str, a);
		refreshDocument();
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		refreshDocument();
	}

	private synchronized void refreshDocument() throws BadLocationException {
		String text = getText(0, getLength());
		words.clear();
		findWords(text);

		setCharacterAttributes(0, text.length(), defaultStyle, true);
		for (int position : words.keySet()) {
			String word = words.get(position);
			setCharacterAttributes(position, word.length(), keywordStyle, true);
		}
	}

	private void findWords(String content) {
		content += " ";
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();

		for (int index = 0; index < data.length; index++) {
			char ch = data[index];
			if (ch != '\\' && (!(Character.isLetter(ch) || Character.isDigit(ch) || ch == '_'))) {
				lastWhitespacePosition = index;
				if (word.length() > 0) {
					if (isReservedWord(word)) {
						int pos = lastWhitespacePosition - word.length();
						words.put(pos, word);
					}
					word = "";
				}
			} else {
				word += ch;
			}
		}
	}

	private static final boolean isReservedWord(String word) {
		return COLORED_KEYWORDS.contains(word);
	}
}