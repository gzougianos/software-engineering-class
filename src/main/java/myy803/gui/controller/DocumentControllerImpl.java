package myy803.gui.controller;

import java.awt.Dimension;

import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;

import myy803.CommandFactory;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.gui.views.AddDocumentView;
import myy803.gui.views.CommandsPanel;
import myy803.gui.views.DocumentView;
import myy803.model.Document;
import myy803.model.TextCommand;

public class DocumentControllerImpl implements DocumentController {
	private DocumentView view;
	private AddDocumentView addDocView;

	public DocumentControllerImpl() {
		addDocView = MainFrame.getInstance().getTabView().getAddDocumentView();
	}

	@Override
	public DocumentView getView() {
		return view;
	}

	@Override
	public void initialize() {

	}

	@Override
	public void setView(DocumentView view) {
		this.view = view;
	}

	@Override
	public void save() {
		CommandFactory.createSaveCommand(view).execute();
		changeDocSavedStateAndUpdateGUI(true);
	}

	@Override
	public void saveAs() {
		CommandFactory.createSaveAsCommand(view).execute();
		changeDocSavedStateAndUpdateGUI(true);
	}

	@Override
	public void load() {
		addDocView.getController().chooseAndLoadDocument();
	}

	private void changeDocSavedStateAndUpdateGUI(boolean saved) {
		view.getDocument().setSaved(saved);
		view.getSaveButton().setEnabled(!view.getDocument().isSaved());
		addDocView.getDocumentTypeLabels().forEach(l -> l.repaint());
	}

	@Override
	public void textPaneChanged(DocumentEvent event) {
		changeDocSavedStateAndUpdateGUI(false);
		updateAuthor(event);
	}

	@Override
	public void openCommandSelection() {
		CommandsPanel panel = new CommandsPanel(view.getDocument().getDocumentType());
		panel.setPreferredSize(new Dimension(500, 500));
		if (SwingUtils.createDoubleOptionPanel(panel, "Add command", Icon.COMMAND, "Add", "Cancel")) {
			TextCommand selectedCommand = panel.getSelectedCommand();
			JTextPane textPane = view.getTextPane();
			CommandFactory.createTextCommand(selectedCommand, textPane).execute();
		}
	}

	private void updateAuthor(DocumentEvent e) {
		Document clone = view.getDocument().clone();
		clone.setContent(view.getTextPane().getText());
		int authorCommandOffset = clone.getContent().indexOf("\\author{");
		int authorCommandEndOffset = clone.getContent().indexOf("}", authorCommandOffset);
		if (e.getOffset() >= authorCommandOffset && e.getOffset() <= authorCommandEndOffset) {
			String author = clone.getAuthor();
			String text;
			if (author.length() > 35)
				text = "Author: " + clone.getAuthor().substring(0, 35) + "...";
			else
				text = "Author: " + clone.getAuthor();
			view.setAuthor(text);
		}
	}
}
