package myy803.gui.views;

import java.util.List;

import javax.swing.JComponent;

import myy803.gui.View;
import myy803.gui.components.CloseTabComponent;
import myy803.gui.controller.TabController;
import myy803.model.Document;

public interface TabbedView extends View<TabbedPanel> {
	AddDocumentView getAddDocumentView();

	void setAddDocumentView(AddDocumentView view);

	JComponent createCloseTabComponent(Document doc);

	@Override
	TabController getController();

	List<CloseTabComponent> getCloseTabComponents();
}
