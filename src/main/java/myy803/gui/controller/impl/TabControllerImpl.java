package myy803.gui.controller.impl;

import java.awt.Component;
import java.util.List;

import javax.swing.JTabbedPane;

import myy803.DocumentManager;
import myy803.gui.ExternalSwingUtils;
import myy803.gui.components.CloseTabComponent;
import myy803.gui.controller.AddDocumentController;
import myy803.gui.controller.DocumentController;
import myy803.gui.controller.TabController;
import myy803.gui.view.AddDocumentPanel;
import myy803.gui.view.AddDocumentView;
import myy803.gui.view.DocumentPanel;
import myy803.gui.view.TabbedView;
import myy803.model.Document;

public class TabControllerImpl implements TabController {
	private TabbedView view;

	public TabControllerImpl() {
	}

	@Override
	public TabbedView getView() {
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
	public void setView(TabbedView view) {
		this.view = view;
	}

	@Override
	public void createTabAndShowDocument(Document doc) {
		JTabbedPane tabbedPane = view.get();
		tabbedPane.remove(view.getAddDocumentView().get());
		DocumentController controller = new DocumentControllerImpl();
		DocumentPanel dp = new DocumentPanel(controller, doc);
		controller.setView(dp);
		controller.initialize();
		tabbedPane.addTab(doc.getName(), dp);
		int index = tabbedPane.indexOfTab(doc.getName());
		tabbedPane.setTabComponentAt(index, view.createCloseTabComponent(doc));
		tabbedPane.addTab("+", view.getAddDocumentView().get());
		tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 2);
	}

	@Override
	public void closeDocumentTab(Document doc) {
		JTabbedPane tabbedPane = view.get();
		int index = indexOfTab();
		if (index == -1)
			return;
		Component c = tabbedPane.getComponentAt(index);
		if (c instanceof DocumentPanel) {
			DocumentManager.INSTANCE.getDocuments().remove(doc);
			tabbedPane.removeTabAt(index);
		}
	}

	@Override
	public void openDocumentTab(Document doc) {
		JTabbedPane tabbedPane = view.get();
		int index = indexOfTab();
		if (index == -1)
			return;
		tabbedPane.setSelectedIndex(index);
	}

	private int indexOfTab() {
		JTabbedPane tabbedPane = view.get();
		List<CloseTabComponent> closeTabComps = ExternalSwingUtils.getDescendantsOfClass(CloseTabComponent.class, tabbedPane);
		for (CloseTabComponent closeTabComp : closeTabComps) {
			for (int i = 0; i < tabbedPane.getTabCount(); i++) {
				Component c = tabbedPane.getTabComponentAt(i);
				if (c == closeTabComp)
					return i;
			}
		}
		return -1;
	}

	@Override
	public void repaintTabComponents() {
		view.getCloseTabComponents().forEach(l -> {
			l.repaint();
			l.revalidate();
		});
	}
}
