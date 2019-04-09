package myy803.gui.containers;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.alee.laf.scroll.WebScrollPane;

import myy803.DocumentManager;
import myy803.gui.DocumentTextPanePanel;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.model.Document;
import myy803.model.DocumentType;

public class AddDocumentPanel extends JPanel implements ActionListener {
	private static final long serialVersionUID = 8694070220324964236L;
	private DocumentTypeLabel articleLabel, reportLabel, bookLabel, letterLabel;
	private DocumentTextPanePanel documentTextPanePanel;
	private WebScrollPane previewScrollPane;
	private DocumentType selectedDocumentType;

	public AddDocumentPanel() {
		super(new BorderLayout(25, 20));
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel inheritedPanel = new JPanel(new BorderLayout());

		JLabel newDocumentLabel = new JLabel("New Document");
		newDocumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newDocumentLabel.setFont(MainFrame.MAIN_FONT.deriveFont(18f));
		newDocumentLabel.setForeground(MainFrame.LIGHT_BLUE);
		inheritedPanel.add(newDocumentLabel, BorderLayout.PAGE_START);

		JPanel documentTypesPanel = new JPanel(new FlowLayout());
		articleLabel = new DocumentTypeLabel(this, DocumentType.ARTICLE, "Article", Icon.ARTICLE);
		bookLabel = new DocumentTypeLabel(this, DocumentType.BOOK, "Book", Icon.BOOK);
		reportLabel = new DocumentTypeLabel(this, DocumentType.REPORT, "Report", Icon.REPORT);
		letterLabel = new DocumentTypeLabel(this, DocumentType.LETTER, "Letter", Icon.LETTER);
		documentTypesPanel.add(articleLabel);
		documentTypesPanel.add(bookLabel);
		documentTypesPanel.add(reportLabel);
		documentTypesPanel.add(letterLabel);
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
		previewScrollPane.getViewport().setOpaque(false);
		previewScrollPane.setOpaque(false);
		previewScrollPane.getVerticalScrollBar()
				.setUnitIncrement(previewScrollPane.getVerticalScrollBar().getUnitIncrement() * 10);
		outerPanel.add(previewScrollPane);

		add(inheritedPanel, BorderLayout.PAGE_START);
		add(outerPanel, BorderLayout.CENTER);

		articleLabel.onMouseClick(); //Initial selection
	}

	public void onSelectionChange(DocumentType selectedDocumentType) {
		if (selectedDocumentType != null) {
			this.selectedDocumentType = selectedDocumentType;
			String preview = DocumentManager.INSTANCE.createDocument(selectedDocumentType).getContent();
			documentTextPanePanel.getTextPane().setText(preview);
			SwingUtilities.invokeLater(() -> previewScrollPane.getVerticalScrollBar().setValue(0));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Document doc = DocumentManager.INSTANCE.createDocument(selectedDocumentType);
		DocumentManager.INSTANCE.getDocuments().add(doc);
		MainFrame.getInstance().getTabbedPanel().createTabAndShowDocument(doc);

	}
}
