package myy803.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledEditorKit.FontSizeAction;

public class DocumentStyledDocument extends DefaultStyledDocument {
	private static final long serialVersionUID = 1L;
	private static final String COLORED_KEYWORDS_FILE = "/myy803/resources/colored_keywords";
	private static final HashSet<String> COLORED_KEYWORDS = new HashSet<>();
	private JTextPane textPane;
	private JTextArea linesTextArea;
	private Style defaultStyle;
	private Style keywordStyle;
	private Style propertiesStyle;
	private HashMap<Integer, String> words = new HashMap<>();
	private HashMap<Integer, String> properties = new HashMap<>();
	private int fontSize;

	static {
		loadColoredKeywords();
	}

	public DocumentStyledDocument(JTextPane textPane, JTextArea linesTextArea) {
		this.textPane = textPane;
		this.linesTextArea = linesTextArea;
		StyleContext styleContext = new StyleContext();
		defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
		keywordStyle = styleContext.addStyle("Keywords", null);
		propertiesStyle = styleContext.addStyle("Props", null);
		fontSize = MainFrame.MAIN_FONT.getSize();
		fixLines();
		fixFont();
	}

	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
		super.insertString(offset, str, a);
		refreshDocument();
		fixLines();
		fixFont();
	}

	@Override
	public void remove(int offs, int len) throws BadLocationException {
		super.remove(offs, len);
		refreshDocument();
		fixLines();
		fixFont();
	}

	@Override
	public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
		super.replace(offset, length, text, attrs);
		fixLines();
	}

	public void setFontSize(int size) {
		fontSize = size;
		doFontAction();
		fixFont();
	}

	private void doFontAction() {
		int caret = textPane.getCaretPosition();
		int start = textPane.getSelectionStart();
		int end = textPane.getSelectionEnd();
		textPane.selectAll();
		FontSizeAction fa = new FontSizeAction("fontslide", fontSize);
		fa.actionPerformed(new ActionEvent(textPane, 1, String.valueOf(fontSize)));
		SwingUtilities.invokeLater(() -> {
			textPane.setCaretPosition(caret);
			textPane.select(start, end);
		});
	}

	private void fixLines() {
		String text = textPane.getText();
		text = text.replaceAll("\n", System.lineSeparator());
		int lines = text.split(System.lineSeparator()).length;
		//If line count did not change, do nothing
		if (lines + 1 == linesTextArea.getLineCount())
			return;
		JScrollPane parentScrollPane = getParentScrollPane();
		int verticalScrollAmount = -1;
		if (parentScrollPane != null)
			verticalScrollAmount = parentScrollPane.getVerticalScrollBar().getValue();
		linesTextArea.setText("");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < lines; i++) {
			sb.append(i);
			sb.append(System.lineSeparator());
		}
		linesTextArea.setText(sb.toString());
		//When appending text to JTextArea, scrollpane scrolls to bottom
		//So we have to restore the scroller where it was
		if (parentScrollPane != null) {
			final int amount = verticalScrollAmount;
			SwingUtilities.invokeLater(() -> parentScrollPane.getVerticalScrollBar().setValue(amount));
		}
	}

	private void fixFont() {
		StyleConstants.setForeground(keywordStyle, MainFrame.LIGHT_BLUE);
		StyleConstants.setBold(keywordStyle, true);
		StyleConstants.setFontFamily(keywordStyle, MainFrame.MAIN_FONT.getFontName());
		StyleConstants.setFontSize(keywordStyle, fontSize);

		StyleConstants.setForeground(propertiesStyle, new Color(126, 54, 54));
		StyleConstants.setFontFamily(propertiesStyle, MainFrame.MAIN_FONT.getFontName());
		StyleConstants.setFontSize(propertiesStyle, fontSize);

		StyleConstants.setFontFamily(defaultStyle, MainFrame.MAIN_FONT.getFontName());
		StyleConstants.setFontSize(defaultStyle, fontSize);
	}

	private void refreshDocument() throws BadLocationException {
		String text = getText(0, getLength());
		words.clear();
		properties.clear();
		findWords(text);

		setCharacterAttributes(0, text.length(), defaultStyle, true);
		for (int position : properties.keySet()) {
			String word = properties.get(position);
			setCharacterAttributes(position, word.length(), propertiesStyle, true);
		}
		for (int position : words.keySet()) {
			String word = words.get(position);
			setCharacterAttributes(position, word.length(), keywordStyle, true);
		}

	}

	private JScrollPane getParentScrollPane() {
		Component parent = linesTextArea.getParent();
		while (parent != null) {
			if (parent instanceof JScrollPane) {
				return (JScrollPane) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	private void findWords(String content) {
		content += " ";
		int lastWhitespacePosition = 0;
		String word = "";
		char[] data = content.toCharArray();

		for (int index = 0; index < data.length; index++) {
			char ch = data[index];
			if (!isWordCharacter(ch)) {
				lastWhitespacePosition = index;
				if (word.length() > 0) {
					if (isKeyword(word)) {
						int pos = lastWhitespacePosition - word.length();
						words.put(pos, word);
					}
					word = "";
				}
			} else {
				word += ch;
			}
		}
		//		word = "";
		//		int pos = -1;
		//		for (int index = 0; index < data.length; index++) {
		//			char ch = data[index];
		//			if (ch == '{' && word.isEmpty()) {
		//				word = "{";
		//				pos = index;
		//			} else {
		//				if (ch == '}' && !word.isEmpty()) {
		//					word += "}";
		//					System.out.println(pos);
		//					if (pos != -1)
		//						properties.put(pos, word);
		//					word = "";
		//					pos = -1;
		//				} else if (!word.isEmpty()) {
		//					word += String.valueOf(ch);
		//				}
		//
		//			}
		//		}

		Matcher m = Pattern.compile("\\\\*.(\\{.*\\})").matcher(content);
		while (m.find()) {
			String group = m.group(1);
			if (group.matches("(\\{.*\\})")) {
				int pos = m.start(1);
				properties.put(pos, group);
			}
		}
	}

	private boolean isWordCharacter(char c) {
		return Character.isLetter(c) || c == '\\';
	}

	private static final boolean isKeyword(String word) {
		return COLORED_KEYWORDS.contains(word);
	}

	private static void loadColoredKeywords() {
		try (InputStreamReader isr = new InputStreamReader(
				DocumentStyledDocument.class.getResourceAsStream(COLORED_KEYWORDS_FILE));
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
}