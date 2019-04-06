package myy803.gui.containers;

import java.awt.BorderLayout;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.toolbar.WebToolBar;

import myy803.gui.DocumentTextPane;
import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.model.Document;

public class DocumentPanel extends JPanel {
	private static final long serialVersionUID = -1467388562407975227L;
	private static final String LEFT_ARROW = "\u2190";
	private static final String UP_ARROW = "\u2191";
	private static final String RIGHT_ARROW = "\u2192";
	private static final String[] ARROWS = { RIGHT_ARROW, LEFT_ARROW, UP_ARROW };
	private static final String[] POSITIONS = { BorderLayout.PAGE_START, BorderLayout.LINE_END, BorderLayout.LINE_START };
	private int position = 0;
	private WebToolBar toolbar;
	private Document document;
	private DocumentTextPane textPane;
	private WebScrollPane scrollPane;
	private JSlider fontSlider;
	private JButton changeToolbarLocationButton;

	public DocumentPanel(Document document) {
		super(new BorderLayout());
		this.document = document;
		initToolbar();
		initFontSlider();
		initTextPane();

		toolbar.addToEnd(fontSlider);
		toolbar.addToEnd(changeToolbarLocationButton);
		add(toolbar, POSITIONS[position]);

		add(scrollPane, BorderLayout.CENTER);

		add(createBottomBar(), BorderLayout.PAGE_END);
	}

	private JPanel createBottomBar() {
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

		JLabel authorLabel = new JLabel("Author: " + document.getAuthor());
		authorLabel.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(authorLabel);

		JLabel copyrightsLabel = new JLabel("Copyright: " + document.getCopyright());
		copyrightsLabel.setFont(MainFrame.MAIN_FONT);
		mainPanel.add(copyrightsLabel);
		return mainPanel;
	}

	private void initTextPane() {
		textPane = new DocumentTextPane();
		textPane.setText(document.getContent());
		textPane.setCaretPosition(0);
		textPane.setFontSize(fontSlider.getValue());
		scrollPane = new WebScrollPane(textPane);
		scrollPane.setDrawBorder(false);
		scrollPane.setFocusable(false);
		scrollPane.getViewport().setOpaque(false);
		scrollPane.setOpaque(false);
		scrollPane.getVerticalScrollBar().setUnitIncrement(scrollPane.getVerticalScrollBar().getUnitIncrement() * 10);
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
	}

	private void initFontSlider() {
		fontSlider = new JSlider(14, 20, 14);
		fontSlider.setFont(MainFrame.MAIN_FONT);
		fontSlider.setFocusable(false);
		fontSlider.setValue(17);
		fontSlider.addChangeListener(e -> {
			textPane.setFontSize(fontSlider.getValue());
		});
		fontSlider.setMinorTickSpacing(1); // step
		fontSlider.setSnapToTicks(true); // should be activated for custom tick space

	}

	public Document getDocument() {
		return document;
	}

}
