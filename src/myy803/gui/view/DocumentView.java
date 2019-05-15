package myy803.gui.view;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import myy803.gui.View;
import myy803.gui.controller.DocumentController;
import myy803.model.Document;
import myy803.model.version.VersionStrategyType;

public interface DocumentView extends View<JPanel> {
	Document getDocument();

	void setLastModifiedDate(long date);

	JButton getSaveButton();

	String getCopyrights();

	void setAuthor(String author);

	JTextPane getTextPane();

	@Override
	DocumentController getController();

	void restore();

	JComboBox<VersionStrategyType> getVersionStrategyTypeComboBox();

	JButton getRollBackButton();
}
