package myy803.gui.controller;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;

import myy803.CommandFactory;
import myy803.DocumentManager;
import myy803.RecentFileManager;
import myy803.gui.MainFrame;
import myy803.gui.views.AddDocumentView;
import myy803.gui.views.RecentFilePanel;
import myy803.model.Document;
import myy803.model.DocumentType;

public class AddDocumentControllerImpl implements AddDocumentController {
	private AddDocumentView view;
	private DocumentType selectedDocumentType;
	private TabController tabController;

	public AddDocumentControllerImpl() {
		tabController = MainFrame.getInstance().getTabView().getController();
	}

	@Override
	public void chooseAndLoadDocument() {
		CommandFactory.createLoadCommand(tabController).execute();
		fixRecentFiles();
	}

	private void fixRecentFiles() {
		JPanel recentFilesPanel = view.getRecentFilesPanel();
		recentFilesPanel.removeAll();
		String[] recentFiles = RecentFileManager.INSTANCE.getFiles();
		if (recentFiles.length > 0) {
			for (int i = recentFiles.length - 1; i >= 0; i--) {
				String recentFilePath = recentFiles[i];
				RecentFilePanel rfp = new RecentFilePanel(this, recentFilePath);
				JPanel borderLayout = new JPanel(new BorderLayout());
				borderLayout.add(rfp, BorderLayout.PAGE_START);
				Dimension dim2 = rfp.getPreferredSize();
				dim2.width = 200;
				rfp.setPreferredSize(dim2);
				recentFilesPanel.add(borderLayout);
				recentFilesPanel.add(Box.createRigidArea(new Dimension(1, 5)));
			}
		} else {
			JLabel label = new JLabel("No files opened recently.");
			label.setFont(MainFrame.MAIN_FONT);
			label.setHorizontalAlignment(JLabel.CENTER);
			recentFilesPanel.add(label);
		}
		recentFilesPanel.add(Box.createVerticalGlue());
		recentFilesPanel.repaint();
		recentFilesPanel.revalidate();
	}

	@Override
	public void createDocument() {
		Document doc = DocumentManager.INSTANCE.createDocument(selectedDocumentType);
		DocumentManager.INSTANCE.getDocuments().add(doc);
		tabController.createTabAndShowDocument(doc);
	}

	@Override
	public void onChangeDocTypeSelection(DocumentType docType) {
		view.getDocumentTypeLabels().forEach(l -> {
			l.setChosen(docType == l.getDocumentType());
			l.repaint();
		});
		this.selectedDocumentType = docType;
		String preview = DocumentManager.INSTANCE.createDocument(selectedDocumentType).getContent();
		view.getDocumentTextPanePanel().getTextPane().setText(preview);
	}

	@Override
	public AddDocumentView getView() {
		return view;
	}

	@Override
	public void setView(AddDocumentView view) {
		this.view = view;
	}

	@Override
	public void initialize() {
		fixRecentFiles();
		onChangeDocTypeSelection(DocumentType.ARTICLE); //first selection
	}

	@Override
	public void loadDocument(String path) {
		File selectedFile = new File(path);
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
			fixRecentFiles();
		} catch (ClassNotFoundException | IOException e1) {
			System.err.println("Error reading file " + selectedFile.getAbsolutePath());
			e1.printStackTrace();
		}

	}

}
