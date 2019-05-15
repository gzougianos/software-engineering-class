package myy803.gui.controller;

import javax.swing.event.DocumentEvent;

import myy803.gui.Controller;
import myy803.gui.view.DocumentView;
import myy803.model.version.VersionStrategyType;

public interface DocumentController extends Controller<DocumentView> {
	void saveAs();

	void save();

	void load();

	void textPaneChanged(DocumentEvent event);

	void openCommandSelection();

	void onVersionStrategyChange(VersionStrategyType type);

	void rollToPreviousVersion();
}
