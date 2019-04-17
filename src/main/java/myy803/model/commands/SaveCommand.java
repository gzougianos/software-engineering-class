package myy803.model.commands;

import java.io.IOException;

import myy803.CommandFactory;
import myy803.DocumentManager;
import myy803.gui.view.DocumentView;
import myy803.model.Document;

public class SaveCommand implements Command {
	private DocumentView view;

	public SaveCommand(DocumentView documentView) {
		this.view = documentView;
	}

	@Override
	public void execute() {
		if (!view.getDocument().getPath().exists()) {
			CommandFactory.createSaveAsCommand(view).execute();
		} else {
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
