package myy803.gui.views;

import javax.swing.JComponent;

import myy803.gui.View;
import myy803.gui.controller.DocumentTabController;
import myy803.model.Document;

public interface DocumentTabView extends View<DocumentTabbedPanel> {
	AddDocumentView getAddDocumentView();

	void setAddDocumentView(AddDocumentView view);

	JComponent createCloseTabComponent(Document doc);

	@Override
	DocumentTabController getController();
}
