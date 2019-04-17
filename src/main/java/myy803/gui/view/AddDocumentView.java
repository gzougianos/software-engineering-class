package myy803.gui.view;

import java.util.List;

import javax.swing.JPanel;

import myy803.gui.View;
import myy803.gui.components.DocumentTypeLabel;
import myy803.gui.controller.AddDocumentController;

public interface AddDocumentView extends View<JPanel> {
	JPanel getRecentFilesPanel();

	DocumentTextPanePanel getDocumentTextPanePanel();

	@Override
	AddDocumentController getController();

	List<DocumentTypeLabel> getDocumentTypeLabels();
}
