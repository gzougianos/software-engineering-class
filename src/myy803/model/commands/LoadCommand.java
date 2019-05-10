package myy803.model.commands;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

import myy803.DocumentManager;
import myy803.RecentFileManager;
import myy803.commons.Setting;
import myy803.gui.components.DocumentFileChooser;
import myy803.gui.controller.TabController;
import myy803.model.Document;

public class LoadCommand implements Command {
	private TabController tabController;

	public LoadCommand(TabController tabController) {
		this.tabController = tabController;
	}

	@Override
	public void execute() {
		DocumentFileChooser chooser = new DocumentFileChooser();
		chooser.setDialogTitle("Load document");
		if (chooser.showOpenDialog(tabController.getView().get()) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			File folder = selectedFile.getParentFile();
			Setting.LAST_DIRECTORY_SAVED.update(folder.getAbsolutePath());
			try {
				Document doc = DocumentManager.INSTANCE.loadDocument(selectedFile);
				// Check if this file is already opened
				if (DocumentManager.INSTANCE.getDocuments().contains(doc)) {
					tabController.openDocumentTab(doc);
				} else {
					DocumentManager.INSTANCE.getDocuments().add(doc);
					tabController.createTabAndShowDocument(doc);
				}
				RecentFileManager.INSTANCE.push(doc);

			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Error reading file " + selectedFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

}
