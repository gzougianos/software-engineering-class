package myy803.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit.FontSizeAction;

public class DocumentTextPanePanel extends JPanel implements DocumentListener {
	private static final long serialVersionUID = -7993158487970819302L;
	private static final Color LINES_COLOR = new Color(158, 184, 226);
	private JTextPane textPane;
	private JTextArea linesTextArea;
	private int fontSize;
	private Style defaultStyle;
	private Style keywordStyle;

	public DocumentTextPanePanel() {
		super(new BorderLayout(5, 0));
		setFont(MainFrame.MAIN_FONT);
		fontSize = MainFrame.MAIN_FONT.getSize();
		StyleContext styleContext = new StyleContext();
		defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
		keywordStyle = styleContext.addStyle("Keywords", null);
		StyledDocument sd = new KeywordStyledDocument(defaultStyle, keywordStyle) {
			private static final long serialVersionUID = -477886514394225118L;

			@Override
			public void insertString(int offset, String str, AttributeSet a) throws BadLocationException {
				super.insertString(offset, str, a);
				fixLines();
			}

			@Override
			public void replace(int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
				super.replace(offset, length, text, attrs);
				fixLines();
			}

			@Override
			public void remove(int offs, int len) throws BadLocationException {
				super.remove(offs, len);
				fixLines();
			}
		};

		textPane = new JTextPane();
		textPane.setFont(MainFrame.MAIN_FONT);
		textPane.setStyledDocument(sd);
		textPane.getDocument().addDocumentListener(this);

		linesTextArea = new JTextArea();
		linesTextArea.setForeground(LINES_COLOR);
		linesTextArea.setOpaque(false);
		linesTextArea.setFont(MainFrame.MAIN_FONT);
		fixLines();

		add(textPane, BorderLayout.CENTER);
		add(linesTextArea, BorderLayout.LINE_START);
	}

	private void fixLines() {
		int lines = textPane.getText().split(System.lineSeparator()).length;
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

	private JScrollPane getParentScrollPane() {
		Component parent = getParent();
		while (parent != null) {
			if (parent instanceof JScrollPane) {
				return (JScrollPane) parent;
			}
			parent = parent.getParent();
		}
		return null;
	}

	public void setFontSize(int size) {
		fontSize = size;
		refixFont();
		doFontAction();
		linesTextArea.setFont(MainFrame.MAIN_FONT.deriveFont((float) fontSize));
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

	//@formatter:off
	@Override
	public void insertUpdate(DocumentEvent e) {	refixFont();	}
	@Override
	public void removeUpdate(DocumentEvent e) {	refixFont();	}
	@Override
	public void changedUpdate(DocumentEvent e) {	refixFont();	}
	//@formatter:on
	private void refixFont() {
		StyleConstants.setForeground(keywordStyle, MainFrame.LIGHT_BLUE);
		StyleConstants.setBold(keywordStyle, true);
		StyleConstants.setFontFamily(keywordStyle, MainFrame.MAIN_FONT.getFontName());
		StyleConstants.setFontSize(keywordStyle, fontSize);
		StyleConstants.setFontFamily(defaultStyle, MainFrame.MAIN_FONT.getFontName());
		StyleConstants.setFontSize(defaultStyle, fontSize);
	}

	public JTextPane getTextPane() {
		return textPane;
	}
}
