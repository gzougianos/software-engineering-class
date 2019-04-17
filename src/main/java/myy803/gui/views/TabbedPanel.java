package myy803.gui.views;

import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import myy803.gui.ExternalSwingUtils;
import myy803.gui.MainFrame;
import myy803.gui.components.CloseTabComponent;
import myy803.gui.controller.TabController;
import myy803.model.Document;

public class TabbedPanel extends JTabbedPane implements TabbedView {
	private static final long serialVersionUID = -7298563748251359603L;
	private AddDocumentView addDocumentView;
	private TabController controller;

	public TabbedPanel(TabController controller) {
		super();
		this.controller = controller;
		setBorder(null);
		setFont(MainFrame.MAIN_FONT);
	}

	@Override
	public TabController getController() {
		return controller;
	}

	@Override
	public AddDocumentView getAddDocumentView() {
		return addDocumentView;
	}

	@Override
	public JComponent createCloseTabComponent(Document doc) {
		return new CloseTabComponent(getController(), doc);
	}

	@Override
	public void setAddDocumentView(AddDocumentView view) {
		addDocumentView = view;

	}

	@Override
	public TabbedPanel get() {
		return this;
	}

	@Override
	public List<CloseTabComponent> getCloseTabComponents() {
		return ExternalSwingUtils.getDescendantsOfClass(CloseTabComponent.class, this);
	}

}
