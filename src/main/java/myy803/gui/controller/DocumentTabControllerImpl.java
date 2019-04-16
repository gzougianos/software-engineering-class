package myy803.gui.controller;

import java.awt.Component;

import javax.swing.JTabbedPane;

import myy803.DocumentManager;
import myy803.gui.views.AddDocumentPanel;
import myy803.gui.views.AddDocumentView;
import myy803.gui.views.DocumentPanel;
import myy803.gui.views.DocumentTabView;
import myy803.model.Document;

public class DocumentTabControllerImpl implements DocumentTabController {
	private DocumentTabView view;

	public DocumentTabControllerImpl() {
	}

	@Override
	public DocumentTabView getView() {
		return view;
	}

	@Override
	public void initialize() {
		AddDocumentController addDocumentController = new AddDocumentControllerImpl();
		AddDocumentView addDocumentView = new AddDocumentPanel(addDocumentController);
		addDocumentController.setView(addDocumentView);
		addDocumentController.initialize();
		view.get().setAddDocumentView(addDocumentView);
		view.get().addTab("+", addDocumentView.get());
	}

	@Override
	public void setView(DocumentTabView view) {
		this.view = view;
	}

	@Override
	public void createTabAndShowDocument(Document doc) {
		JTabbedPane tabbedPane = view.get();
		tabbedPane.remove(view.getAddDocumentView().get());
		DocumentPanel dp = new DocumentPanel(doc);
		tabbedPane.addTab(doc.getName(), dp);
		int index = tabbedPane.indexOfTab(doc.getName());
		tabbedPane.setTabComponentAt(index, view.createCloseTabComponent(doc));
		tabbedPane.addTab("+", view.getAddDocumentView().get());
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
	}

	@Override
	public void closeDocumentTab(Document doc) {
		JTabbedPane tabbedPane = view.get();
		int index = tabbedPane.indexOfTab(doc.getName());
		Component c = tabbedPane.getComponentAt(index);
		if (c instanceof DocumentPanel && index != -1) {
			DocumentManager.INSTANCE.getDocuments().remove(doc);
			tabbedPane.removeTabAt(index);
		}
	}

	@Override
	public void openDocumentTab(Document doc) {
		JTabbedPane tabbedPane = view.get();
		int index = tabbedPane.indexOfTab(doc.getName());
		tabbedPane.setSelectedIndex(index);
	}

}
