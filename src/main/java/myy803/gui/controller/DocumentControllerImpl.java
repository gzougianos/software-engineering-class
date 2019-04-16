package myy803.gui.controller;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;

import myy803.DocumentManager;
import myy803.commons.Setting;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.gui.components.DocumentFileChooser;
import myy803.gui.views.AddDocumentView;
import myy803.gui.views.CommandsPanel;
import myy803.gui.views.DocumentView;
import myy803.model.Command;
import myy803.model.Document;

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
		if (!view.getDocument().getPath().exists()) {
			saveAs();
		} else {
			updateDocValues();
			saveDoc();
			changeDocSavedStateAndUpdateGUI(true);
			view.setLastModifiedDate(view.getDocument().getLastModifiedDate());
		}
	}

	@Override
	public void saveAs() {
		DocumentFileChooser chooser = new DocumentFileChooser(view.getDocument());
		chooser.setDialogTitle("Save document");
		if (chooser.showSaveDialog(view.get()) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (!selectedFile.getName().endsWith(Document.FILE_EXTENSION))
				selectedFile = new File(selectedFile.getAbsolutePath() + Document.FILE_EXTENSION);
			Setting.LAST_DIRECTORY_SAVED.update(selectedFile.getParentFile().getAbsolutePath());
			view.getDocument().setPath(selectedFile);
			updateDocValues();
			saveDoc();
			changeDocSavedStateAndUpdateGUI(true);
			view.setLastModifiedDate(view.getDocument().getLastModifiedDate());
		}
	}

	private void updateDocValues() {
		Document doc = view.getDocument();
		doc.setLastModifiedDate(System.currentTimeMillis());
		doc.setContent(view.getTextPane().getText());
		doc.setCopyright(view.getCopyrights());
	}

	private void saveDoc() {
		try {
			DocumentManager.INSTANCE.saveDocument(view.getDocument());
		} catch (IOException e1) {
			System.err.println("Error saving as document in " + view.getDocument().getPath().getAbsolutePath());
			e1.printStackTrace();
		}
	}

	private void changeDocSavedStateAndUpdateGUI(boolean saved) {
		view.getDocument().setSaved(saved);
		view.getSaveButton().setEnabled(!view.getDocument().isSaved());
		addDocView.getDocumentTypeLabels().forEach(l -> l.repaint());
	}

	@Override
	public void load() {
		addDocView.getController().chooseAndLoadDocument();
	}

	@Override
	public void textPaneChanged(DocumentEvent event) {
		changeDocSavedStateAndUpdateGUI(false);
		updateAuthor(event);
	}

	@Override
	public void openCommandSelection() {
		CommandsPanel panel = new CommandsPanel(view.getDocument().getDocumentType());
		JTextPane textPane = view.getTextPane();
		panel.setPreferredSize(new Dimension(500, 500));
		if (SwingUtils.createDoubleOptionPanel(panel, "Add command", Icon.COMMAND, "Add", "Cancel")) {
			Command selectedCommand = panel.getSelectedCommand();
			int cursorIndex = textPane.getCaretPosition();
			try {
				textPane.getDocument().insertString(cursorIndex, selectedCommand.getContent(), new SimpleAttributeSet());
				if (selectedCommand.hasCursor()) {
					textPane.requestFocusInWindow();
					SwingUtilities.invokeLater(() -> {
						int totalCursor = cursorIndex + selectedCommand.getCursorIndex();
						textPane.setCaretPosition(totalCursor);
					});
				}
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
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
