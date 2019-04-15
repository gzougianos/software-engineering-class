package myy803.gui.views;

import javax.swing.JPanel;

import myy803.gui.Controller;
import myy803.gui.View;

public interface AddDocumentView extends View<JPanel> {
	JPanel getRecentFilesPanel();

	DocumentTextPanePanel getDocumentTextPanePanel();

	@Override
	Controller<AddDocumentView> getController();
}
