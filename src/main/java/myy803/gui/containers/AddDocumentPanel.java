package myy803.gui.containers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.alee.laf.scroll.WebScrollPane;

import myy803.DocumentManager;
import myy803.RecentFileManager;
import myy803.commons.Setting;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.model.Document;
import myy803.model.DocumentType;

public class AddDocumentPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8694070220324964236L;
	private DocumentTextPanePanel documentTextPanePanel;
	private WebScrollPane previewScrollPane;
	private DocumentType selectedDocumentType;
	private JPanel recentFilesPanel;

	public AddDocumentPanel() {
		super(new BorderLayout(25, 20));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel inheritedPanel = new JPanel(new BorderLayout());

		JLabel newDocumentLabel = new JLabel("Create new document");
		newDocumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newDocumentLabel.setFont(MainFrame.MAIN_FONT.deriveFont(18f));
		inheritedPanel.add(newDocumentLabel, BorderLayout.PAGE_START);

		JPanel documentTypesPanel = new JPanel(new FlowLayout());
		for (DocumentType docType : DocumentType.values()) {
			DocumentTypeLabel label = new DocumentTypeLabel(docType);
			documentTypesPanel.add(label);
		}
		inheritedPanel.add(documentTypesPanel, BorderLayout.CENTER);

		JButton createButton = new JButton("Create Document");
		createButton.setFont(MainFrame.MAIN_FONT);
		createButton.setHorizontalAlignment(SwingConstants.CENTER);
		createButton.addActionListener(this);
		inheritedPanel.add(SwingUtils.createFlowPanel(FlowLayout.CENTER, createButton), BorderLayout.PAGE_END);

		documentTextPanePanel = new DocumentTextPanePanel();
		documentTextPanePanel.getTextPane().setEditable(false);
		documentTextPanePanel.getTextPane().setFocusable(false);
		documentTextPanePanel.getTextPane().setOpaque(false);
		documentTextPanePanel.getTextPane().setBorder(null);

		JPanel outerPanel = new JPanel(new BorderLayout());
		outerPanel.setBorder(SwingUtils.createTitledBorder("Preview:"));
		previewScrollPane = new WebScrollPane(documentTextPanePanel);
		previewScrollPane.setDrawBorder(false);
		previewScrollPane.setFocusable(false);
		previewScrollPane.setOpaque(false);
		SwingUtils.increaseScrollBarSpeed(previewScrollPane, 40);
		outerPanel.add(previewScrollPane);

		JPanel centeredPanel = new JPanel(new BorderLayout());
		centeredPanel.add(inheritedPanel, BorderLayout.PAGE_START);
		centeredPanel.add(outerPanel, BorderLayout.CENTER);

		add(centeredPanel, BorderLayout.CENTER);
		add(createFileOpenPanel(), BorderLayout.LINE_START);
		((DocumentTypeLabel) documentTypesPanel.getComponents()[0]).onMouseClick();
	}

	private JPanel createFileOpenPanel() {
		JPanel main = new JPanel(new BorderLayout());
		Dimension dim = main.getPreferredSize();
		dim.width = 225;
		main.setPreferredSize(dim);

		recentFilesPanel = new JPanel();
		recentFilesPanel.setLayout(new BoxLayout(recentFilesPanel, BoxLayout.Y_AXIS));

		fixRecentFiles();

		JPanel borderLayout = new JPanel(new BorderLayout());
		borderLayout.add(recentFilesPanel, BorderLayout.PAGE_START);
		borderLayout.setBorder(SwingUtils.createTitledBorder("Recent Files"));
		WebScrollPane sp = new WebScrollPane(borderLayout);
		sp.setDrawBorder(false);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setMaximumSize(dim);
		sp.setPreferredSize(dim);
		main.add(sp, BorderLayout.CENTER);

		JButton loadDocumentButton = new JButton("Load Document");
		loadDocumentButton.setFont(MainFrame.MAIN_FONT);
		loadDocumentButton.setIcon(Icon.LOAD.toImageIcon(16));
		loadDocumentButton.addActionListener(e -> onLoad());
		main.add(loadDocumentButton, BorderLayout.PAGE_END);

		return main;
	}

	public void fixRecentFiles() {
		recentFilesPanel.removeAll();
		String[] recentFiles = RecentFileManager.INSTANCE.getFiles();
		if (recentFiles.length > 0) {
			for (int i = recentFiles.length - 1; i >= 0; i--) {
				String recentFilePath = recentFiles[i];
				RecentFilePanel rfp = new RecentFilePanel(recentFilePath);
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

	public void onLoad() {
		DocumentFileChooser chooser = new DocumentFileChooser();
		chooser.setDialogTitle("Load document");
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			File folder = selectedFile.getParentFile();
			Setting.LAST_DIRECTORY_SAVED.update(folder.getAbsolutePath());
			try {
				Document doc = DocumentManager.INSTANCE.loadDocument(selectedFile);
				// Check if this file is already opened
				if (DocumentManager.INSTANCE.getDocuments().contains(doc)) {
					MainFrame.getInstance().getTabbedPanel().openDocumentTab(doc);
				} else {
					DocumentManager.INSTANCE.getDocuments().add(doc);
					MainFrame.getInstance().getTabbedPanel().createTabAndShowDocument(doc);
				}
				RecentFileManager.INSTANCE.push(doc);
				fixRecentFiles();
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Error reading file " + selectedFile.getAbsolutePath());
				e.printStackTrace();
			}

		}
	}

	public void onSelectionChange(DocumentType selectedDocumentType) {
		if (selectedDocumentType != null) {
			this.selectedDocumentType = selectedDocumentType;
			String preview = DocumentManager.INSTANCE.createDocument(selectedDocumentType).getContent();
			documentTextPanePanel.getTextPane().setText(preview);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document doc = DocumentManager.INSTANCE.createDocument(selectedDocumentType);
		DocumentManager.INSTANCE.getDocuments().add(doc);
		MainFrame.getInstance().getTabbedPanel().createTabAndShowDocument(doc);

	}

	public DocumentTextPanePanel getDocumentTextPanePanel() {
		return documentTextPanePanel;
	}
}
