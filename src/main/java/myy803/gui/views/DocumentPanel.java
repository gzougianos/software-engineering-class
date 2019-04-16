package myy803.gui.views;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.WebToolBar;

import myy803.CommandManager;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.gui.controller.DocumentController;
import myy803.model.Document;
import myy803.model.DocumentType;

public class DocumentPanel extends JPanel implements DocumentListener, DocumentView {
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
	private JButton changeToolbarLocationButton, saveButton, saveAsButton, commandsButton;
	private JButton loadButton;
	private JTextField copyrightField;
	private JLabel authorLabel, lastModifiedDateField;
	private DocumentController controller;

	public DocumentPanel(DocumentController controller, Document document) {
		super(new BorderLayout());
		this.controller = controller;
		this.document = document;
		initToolbar();
		initFontSlider();
		initTextPane();

		toolbar.add(loadButton);
		toolbar.add(saveButton);
		toolbar.add(saveAsButton);
		toolbar.addSeparator();
		toolbar.add(commandsButton);
		toolbar.addToEnd(fontSlider);
		toolbar.addSeparatorToEnd();
		toolbar.addToEnd(changeToolbarLocationButton);
		add(toolbar, POSITIONS[position]);

		add(scrollPane, BorderLayout.CENTER);

		add(createBottomBar(), BorderLayout.PAGE_END);
		addSaveShortcut();
	}

	private void addSaveShortcut() {
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
		mainPanel.add(Box.createHorizontalStrut(10));

		authorLabel = new JLabel("Author: " + document.getAuthor());
		authorLabel.setFont(MainFrame.MAIN_FONT);
		authorLabel.setVisible(document.getDocumentType() != DocumentType.LETTER);
		mainPanel.add(authorLabel);

		mainPanel.add(Box.createHorizontalStrut(10));

		JLabel copyrightsLabel = new JLabel("Copyright: ");
		copyrightsLabel.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(copyrightsLabel);

		mainPanel.add(Box.createHorizontalStrut(3));

		copyrightField = new JTextField(10);
		copyrightField.setText(document.getCopyright());
		copyrightField.setFont(MainFrame.MAIN_FONT);
		copyrightField.getDocument().addDocumentListener(this);
		mainPanel.add(SwingUtils.createFlowPanel(copyrightField));
		mainPanel.add(Box.createHorizontalGlue());

		lastModifiedDateField = new JLabel("Last Modified Date: " + SwingUtils.formatDate(document.getLastModifiedDate()));
		lastModifiedDateField.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(lastModifiedDateField);

		mainPanel.add(Box.createHorizontalStrut(10));

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
		loadButton.addActionListener(e -> controller.load());

		saveButton = new JButton(Icon.SAVE.toImageIcon(ICON_DIMENSION));
		saveButton.setFont(MainFrame.MAIN_FONT);
		saveButton.setToolTipText(SwingUtils.toHTML("Save file"));
		saveButton.addActionListener(e -> controller.save());
		saveButton.setEnabled(!getDocument().isSaved());

		saveAsButton = new JButton(Icon.SAVE_AS.toImageIcon(ICON_DIMENSION));
		saveAsButton.setFont(MainFrame.MAIN_FONT);
		saveAsButton.setToolTipText(SwingUtils.toHTML("Save file as..."));
		saveAsButton.addActionListener(e -> controller.saveAs());

		commandsButton = new JButton(Icon.COMMAND.toImageIcon(ICON_DIMENSION));
		commandsButton.setFont(MainFrame.MAIN_FONT);
		commandsButton.setToolTipText(SwingUtils.toHTML("This document type does not allow commands."));
		commandsButton.addActionListener(e -> controller.openCommandSelection());
		commandsButton.setEnabled(docTypeAllowsCommands());
		if (commandsButton.isEnabled())
			commandsButton.setToolTipText(SwingUtils.toHTML("Add command..."));
	}

	private boolean docTypeAllowsCommands() {
		return CommandManager.INSTANCE.getCommands().stream().anyMatch(c -> c.allowsType(document.getDocumentType()));
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

	@Override
	public Document getDocument() {
		return document;
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		controller.textPaneChanged(e);
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		controller.textPaneChanged(e);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
	}

	@Override
	public DocumentPanel get() {
		return this;
	}

	@Override
	public DocumentController getController() {
		return this.controller;
	}

	@Override
	public void setLastModifiedDate(long date) {
		String lastDate = SwingUtils.formatDate(date);
		lastModifiedDateField.setText("Last Modified Date: " + lastDate);
	}

	@Override
	public JButton getSaveButton() {
		return saveButton;
	}

	@Override
	public String getCopyrights() {
		return copyrightField.getText();
	}

	@Override
	public void setAuthor(String author) {
		authorLabel.setText(author);
	}

	@Override
	public JTextPane getTextPane() {
		return documentTextPanePanel.getTextPane();
	}

}
