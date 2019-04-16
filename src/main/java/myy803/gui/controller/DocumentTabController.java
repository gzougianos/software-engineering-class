package myy803.gui.controller;

import myy803.gui.Controller;
import myy803.gui.views.DocumentTabView;
import myy803.model.Document;

public interface DocumentTabController extends Controller<DocumentTabView> {
	void createTabAndShowDocument(Document doc);

	void closeDocumentTab(Document doc);

	void openDocumentTab(Document doc);
}
