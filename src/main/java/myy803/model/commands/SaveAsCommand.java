package myy803.model.commands;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import myy803.DocumentManager;
import myy803.commons.Setting;
import myy803.gui.components.DocumentFileChooser;
import myy803.gui.view.DocumentView;
import myy803.model.Document;

public class SaveAsCommand implements Command {
	private DocumentView view;

	public SaveAsCommand(DocumentView documentView) {
		this.view = documentView;
	}

	@Override
	public void execute() {
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
}
