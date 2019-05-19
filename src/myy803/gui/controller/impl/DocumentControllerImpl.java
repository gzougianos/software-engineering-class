package myy803.gui.controller.impl;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;

import myy803.CommandFactory;
import myy803.VersionsManager;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.gui.controller.DocumentController;
import myy803.gui.controller.TabController;
import myy803.gui.view.AddDocumentView;
import myy803.gui.view.CommandsPanel;
import myy803.gui.view.DocumentView;
import myy803.model.Document;
import myy803.model.TextCommand;
import myy803.model.version.NoPreviousVersionException;
import myy803.model.version.VersionStrategyType;

public class DocumentControllerImpl implements DocumentController, ActionListener {
	private DocumentView view;
	private AddDocumentView addDocView;
	private TabController tabController;
	private Timer timer;

	public DocumentControllerImpl() {
		addDocView = MainFrame.getInstance().getTabView().getAddDocumentView();
		tabController = MainFrame.getInstance().getTabView().getController();
		timer = new Timer(500, this);
		timer.setRepeats(false);
	}

	@Override
	public DocumentView getView() {
		return view;
	}

	@Override
	public void initialize() {
		changeDocSavedStateAndUpdateGUI(true);
		VersionStrategyType vs = VersionsManager.INSTANCE.getStrategy(view.getDocument());
		view.getVersionStrategyTypeComboBox().setSelectedItem(vs);
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
		tabController.repaintTabComponents();
	}

	@Override
	public void textPaneChanged(DocumentEvent event) {
		changeDocSavedStateAndUpdateGUI(false);
		updateAuthor(event);
		view.getRollBackButton().setEnabled(false);
		if (timer.isRunning())
			timer.restart();
		else
			timer.start();
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

	@Override
	public void onVersionStrategyChange(VersionStrategyType type) {
		VersionsManager.INSTANCE.setStrategy(view.getDocument(), type);
		view.getRollBackButton().setEnabled(type != VersionStrategyType.NONE);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		VersionsManager.INSTANCE.commitVersion(view.getDocument());
		view.getRollBackButton().setEnabled(true);
	}

	@Override
	public void rollToPreviousVersion() {
		try {
			VersionsManager.INSTANCE.rollToPreviousVersion(view.getDocument());
			view.restore();
			timer.stop();
		} catch (NoPreviousVersionException e) {
			JOptionPane.showMessageDialog(view.get(), "There are no previous versions.");
		}
	}
}
