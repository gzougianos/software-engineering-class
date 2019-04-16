package myy803.gui.views;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import myy803.gui.View;
import myy803.model.Document;

public interface DocumentView extends View<JPanel> {
	Document getDocument();

	void setLastModifiedDate(long date);

	JButton getSaveButton();

	String getCopyrights();

	void setAuthor(String author);

	JTextPane getTextPane();
}
