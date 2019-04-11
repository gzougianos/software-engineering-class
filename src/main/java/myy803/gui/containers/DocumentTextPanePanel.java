package myy803.gui.containers;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import myy803.gui.DocumentStyledDocument;
import myy803.gui.MainFrame;

public class DocumentTextPanePanel extends JPanel {
	private static final long serialVersionUID = -7993158487970819302L;
	private static final Color LINES_COLOR = new Color(158, 184, 226);
	private JTextPane textPane;
	private JTextArea linesTextArea;
	private DocumentStyledDocument styledDocument;

	public DocumentTextPanePanel() {
		super(new BorderLayout(5, 0));
		setFont(MainFrame.MAIN_FONT);

		linesTextArea = new JTextArea();
		linesTextArea.setForeground(LINES_COLOR);
		linesTextArea.setOpaque(false);
		linesTextArea.setFont(MainFrame.MAIN_FONT);

		textPane = new JTextPane();
		textPane.setFont(MainFrame.MAIN_FONT);

		styledDocument = new DocumentStyledDocument(textPane, linesTextArea);
		textPane.setStyledDocument(styledDocument);

		add(textPane, BorderLayout.CENTER);
		add(linesTextArea, BorderLayout.LINE_START);
	}

	public void setFontSize(int size) {
		styledDocument.setFontSize(size);
		linesTextArea.setFont(MainFrame.MAIN_FONT.deriveFont((float) size));
	}

	public JTextPane getTextPane() {
		return textPane;
	}
}
