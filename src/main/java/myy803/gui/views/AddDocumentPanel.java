package myy803.gui.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.alee.laf.scroll.WebScrollPane;

import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.gui.components.DocumentTypeLabel;
import myy803.gui.controller.AddDocumentController;
import myy803.model.DocumentType;

public class AddDocumentPanel extends JPanel implements AddDocumentView {
	private static final long serialVersionUID = 8694070220324964236L;
	private DocumentTextPanePanel documentTextPanePanel;
	private WebScrollPane previewScrollPane;
	private JPanel recentFilesPanel;
	private AddDocumentController controller;

	public AddDocumentPanel(AddDocumentController controller) {
		super(new BorderLayout(25, 20));
		this.controller = controller;
		this.controller.setView(this);
		setBorder(new EmptyBorder(10, 10, 10, 10));

		JPanel inheritedPanel = new JPanel(new BorderLayout());

		JLabel newDocumentLabel = new JLabel("Create new document");
		newDocumentLabel.setHorizontalAlignment(SwingConstants.CENTER);
		newDocumentLabel.setFont(MainFrame.MAIN_FONT.deriveFont(18f));
		inheritedPanel.add(newDocumentLabel, BorderLayout.PAGE_START);

		JPanel documentTypesPanel = new JPanel(new FlowLayout());
		for (DocumentType docType : DocumentType.values()) {
			DocumentTypeLabel label = new DocumentTypeLabel(controller, docType);
			documentTypesPanel.add(label);
		}
		inheritedPanel.add(documentTypesPanel, BorderLayout.CENTER);

		JButton createButton = new JButton("Create Document");
		createButton.setFont(MainFrame.MAIN_FONT);
		createButton.setHorizontalAlignment(SwingConstants.CENTER);
		createButton.addActionListener(e -> controller.createDocument());
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
		loadDocumentButton.addActionListener(e -> controller.chooseAndLoadDocument());
		main.add(loadDocumentButton, BorderLayout.PAGE_END);

		return main;
	}

	@Override
	public DocumentTextPanePanel getDocumentTextPanePanel() {
		return documentTextPanePanel;
	}

	@Override
	public JPanel getRecentFilesPanel() {
		return recentFilesPanel;
	}

	@Override
	public JPanel get() {
		return this;
	}

	@Override
	public AddDocumentController getController() {
		return this.controller;
	}
}
