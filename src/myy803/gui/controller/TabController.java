package myy803.gui.controller;

import myy803.gui.Controller;
import myy803.gui.view.TabbedView;
import myy803.model.Document;

public interface TabController extends Controller<TabbedView> {
	void createTabAndShowDocument(Document doc);

	void closeDocumentTab(Document doc);

	void openDocumentTab(Document doc);

	void repaintTabComponents();
}
