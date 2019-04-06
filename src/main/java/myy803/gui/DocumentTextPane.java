package myy803.gui;

import java.awt.event.ActionEvent;

import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit.FontSizeAction;

public class DocumentTextPane extends JTextPane implements DocumentListener {
	private static final long serialVersionUID = -7993158487970819302L;
	private int fontSize;
	private Style defaultStyle;
	private Style keywordStyle;

	public DocumentTextPane() {
		setFont(MainFrame.MAIN_FONT);
		fontSize = MainFrame.MAIN_FONT.getSize();
		StyleContext styleContext = new StyleContext();
		defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
		keywordStyle = styleContext.addStyle("Keywords", null);
		StyledDocument sd = new KeywordStyledDocument(defaultStyle, keywordStyle);
		setStyledDocument(sd);
		getDocument().addDocumentListener(this);
	}

	public void setFontSize(int size) {
		fontSize = size;
		refixFont();
		doFontAction();
	}

	private void doFontAction() {
		int caret = getCaretPosition();
		int start = getSelectionStart();
		int end = getSelectionEnd();
		selectAll();
		FontSizeAction fa = new FontSizeAction("fontslide", fontSize);
		fa.actionPerformed(new ActionEvent(this, 1, String.valueOf(fontSize)));
		SwingUtilities.invokeLater(() -> {
			setCaretPosition(caret);
			select(start, end);
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
}
