package myy803.gui.containers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.WebToolBar;

import myy803.DocumentManager;
import myy803.commons.Setting;
import myy803.gui.DocumentFileChooser;
import myy803.gui.DocumentTextPanePanel;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.model.Document;

public class DocumentPanel extends JPanel implements DocumentListener {
	private static final long serialVersionUID = -1467388562407975227L;
	private static final int ICON_DIMENSION = 20;
	private static final String LEFT_ARROW = "\u2190";
	private static final String UP_ARROW = "\u2191";
	private static final String RIGHT_ARROW = "\u2192";
	private static final String[] ARROWS = { RIGHT_ARROW, LEFT_ARROW, UP_ARROW };
	private static final String[] POSITIONS = { BorderLayout.PAGE_START, BorderLayout.LINE_END, BorderLayout.LINE_START };
	private int position = 0;
	private WebToolBar toolbar;
	private Document document;
	private DocumentTextPanePanel documentTextPanePanel;
	private WebScrollPane scrollPane;
	private JSlider fontSlider;
	private JButton changeToolbarLocationButton, saveButton, saveAsButton;
	private JButton loadButton;
	private JTextField copyrightField;
	private JLabel authorLabel;

	public DocumentPanel(Document document) {
		super(new BorderLayout());
		this.document = document;
		initToolbar();
		initFontSlider();
		initTextPane();

		toolbar.add(loadButton);
		toolbar.add(saveButton);
		toolbar.add(saveAsButton);
		toolbar.addToEnd(fontSlider);
		toolbar.addSeparatorToEnd();
		toolbar.addToEnd(changeToolbarLocationButton);
		add(toolbar, POSITIONS[position]);

		add(scrollPane, BorderLayout.CENTER);

		add(createBottomBar(), BorderLayout.PAGE_END);
		getActionMap().put("save", new AbstractAction() {
			private static final long serialVersionUID = -7334567622977748920L;

			@Override
			public void actionPerformed(ActionEvent e) {
				saveButton.doClick();
			}
		});
		getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("control S"), "save");
	}

	private JPanel createBottomBar() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		authorLabel = new JLabel("Author: " + document.getAuthor());
		authorLabel.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(authorLabel);

		mainPanel.add(Box.createRigidArea(new Dimension(10, 1)));

		JLabel copyrightsLabel = new JLabel("Copyright: ");
		copyrightsLabel.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(copyrightsLabel);

		copyrightField = new JTextField(10);
		copyrightField.setText(document.getCopyright());
		copyrightField.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(SwingUtils.createFlowPanel(copyrightField));
		mainPanel.add(Box.createHorizontalGlue());
		return mainPanel;
	}

	private void initTextPane() {
		documentTextPanePanel = new DocumentTextPanePanel();
		documentTextPanePanel.getTextPane().setText(document.getContent());
		documentTextPanePanel.getTextPane().setCaretPosition(0);
		documentTextPanePanel.setFontSize(fontSlider.getValue());
		documentTextPanePanel.getTextPane().getDocument().addDocumentListener(this);
		scrollPane = new WebScrollPane(documentTextPanePanel);
		scrollPane.setDrawBorder(false);
		scrollPane.setFocusable(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		SwingUtils.increaseScrollBarSpeed(scrollPane, 40);
	}

	private void initToolbar() {
		toolbar = new WebToolBar();
		toolbar.setFloatable(false);
		changeToolbarLocationButton = new JButton(ARROWS[position]);
		changeToolbarLocationButton.setFont(MainFrame.MAIN_FONT);
		changeToolbarLocationButton.setToolTipText(SwingUtils.toHTML("Change the position of the toolbar."));
		changeToolbarLocationButton.addActionListener(e -> {
			position++;
			if (position > 2)
				position = 0;
			changeToolbarLocationButton.setText(String.valueOf(ARROWS[position]));
			toolbar.setOrientation(position != 0 ? JToolBar.VERTICAL : JToolBar.HORIZONTAL);
			fontSlider.setOrientation(position != 0 ? SwingConstants.VERTICAL : SwingConstants.HORIZONTAL);
			remove(toolbar);
			add(toolbar, POSITIONS[position]);
			repaint();
			revalidate();
		});
		loadButton = new JButton(Icon.LOAD.toImageIcon(ICON_DIMENSION));
		loadButton.setFont(MainFrame.MAIN_FONT);
		loadButton.setToolTipText(SwingUtils.toHTML("Load file"));
		loadButton.addActionListener(e -> doLoad());

		saveButton = new JButton(Icon.SAVE.toImageIcon(ICON_DIMENSION));
		saveButton.setFont(MainFrame.MAIN_FONT);
		saveButton.setToolTipText(SwingUtils.toHTML("Save file"));
		saveButton.addActionListener(e -> doSave());
		saveButton.setEnabled(!getDocument().isSaved());

		saveAsButton = new JButton(Icon.SAVE_AS.toImageIcon(20));
		saveAsButton.setFont(MainFrame.MAIN_FONT);
		saveAsButton.setToolTipText(SwingUtils.toHTML("Save file as..."));
		saveAsButton.addActionListener(e -> doSaveAs());
	}

	private void doLoad() {
		DocumentFileChooser chooser = new DocumentFileChooser();
		chooser.setDialogTitle("Load document");
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			try {
				Document doc = DocumentManager.INSTANCE.loadDocument(selectedFile);
				// Check if this file is already opened
				if (DocumentManager.INSTANCE.getDocuments().contains(doc)) {
					MainFrame.getInstance().getTabbedPanel().openDocumentTab(doc);
				} else {
					DocumentManager.INSTANCE.getDocuments().add(doc);
					MainFrame.getInstance().getTabbedPanel().createTabAndShowDocument(doc);
				}
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Error reading file " + selectedFile.getAbsolutePath());
				e.printStackTrace();
			}
		}
	}

	private void doSaveAs() {
		DocumentFileChooser chooser = new DocumentFileChooser(getDocument());
		chooser.setDialogTitle("Save document");
		if (chooser.showSaveDialog(DocumentPanel.this) == JFileChooser.APPROVE_OPTION) {
			File selectedFile = chooser.getSelectedFile();
			if (!selectedFile.getName().endsWith(Document.FILE_EXTENSION))
				selectedFile = new File(selectedFile.getAbsolutePath() + Document.FILE_EXTENSION);
			Setting.LAST_DIRECTORY_SAVED.update(selectedFile.getParentFile().getAbsolutePath());
			document.setPath(selectedFile);
			parseValues();
			saveDoc();
			changeDocumentSavedStateAndUpdateGui(true);
		}
	}

	private void doSave() {
		if (!document.getPath().exists()) {
			doSaveAs();
			return;
		}
		parseValues();
		saveDoc();
		changeDocumentSavedStateAndUpdateGui(true);
	}

	private void parseValues() {
		document.setLastModifiedDate(System.currentTimeMillis());
		document.setContent(documentTextPanePanel.getTextPane().getText());
		document.setCopyright(copyrightField.getText());

	}

	private void saveDoc() {
		try {
			changeDocumentSavedStateAndUpdateGui(true);
			DocumentManager.INSTANCE.saveDocument(document);
		} catch (IOException e1) {
			System.err.println("Error saving as document in " + document.getPath().getAbsolutePath());
			e1.printStackTrace();
		}
	}

	private void initFontSlider() {
		fontSlider = new JSlider(14, 20, 14);
		fontSlider.setFont(MainFrame.MAIN_FONT);
		fontSlider.setFocusable(false);
		fontSlider.setValue(17);
		fontSlider.addChangeListener(e -> {
			documentTextPanePanel.setFontSize(fontSlider.getValue());
		});
		fontSlider.setMinorTickSpacing(1); // step
		fontSlider.setSnapToTicks(true); // should be activated for custom tick space

	}

	public Document getDocument() {
		return document;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		changeDocumentSavedStateAndUpdateGui(false);
		updateAuthor(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		changeDocumentSavedStateAndUpdateGui(false);
		updateAuthor(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	private void updateAuthor(DocumentEvent e) {
		Document clone = document.clone();
		clone.setContent(documentTextPanePanel.getTextPane().getText());
		int authorCommandOffset = clone.getContent().indexOf("\\author{");
		int authorCommandEndOffset = clone.getContent().indexOf("}", authorCommandOffset);
		if (e.getOffset() >= authorCommandOffset && e.getOffset() <= authorCommandEndOffset) {
			String author = clone.getAuthor();
			if (author.length() > 35)
				authorLabel.setText("Author: " + clone.getAuthor().substring(0, 35) + "...");
			else
				authorLabel.setText("Author: " + clone.getAuthor());
		}
	}

	private void changeDocumentSavedStateAndUpdateGui(boolean saved) {
		getDocument().setSaved(saved);
		saveButton.setEnabled(!getDocument().isSaved());
		MainFrame.getInstance().getTabbedPanel().repaintLabels();
	}
}
