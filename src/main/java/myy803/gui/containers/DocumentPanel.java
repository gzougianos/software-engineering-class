package myy803.gui.containers;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import com.alee.laf.toolbar.WebToolBar;

import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.model.Document;

public class DocumentPanel extends JPanel {
	private static final long serialVersionUID = -1467388562407975227L;
	private static final String LEFT_ARROW = "\u2190";
	private static final String UP_ARROW = "\u2191";
	private static final String RIGHT_ARROW = "\u2192";
	private static final String DOWN_ARROW = "\u2193";
	private static final String[] ARROWS = { RIGHT_ARROW, DOWN_ARROW, LEFT_ARROW, UP_ARROW };
	private static final String[] POSITIONS = { BorderLayout.PAGE_START, BorderLayout.LINE_END, BorderLayout.PAGE_END,
			BorderLayout.LINE_START };
	private int position = 0;
	private WebToolBar toolbar;
	private Document document;

	public DocumentPanel(Document document) {
		super(new BorderLayout());
		this.document = document;
		initToolbar();

	}

	private void initToolbar() {
		toolbar = new WebToolBar();
		toolbar.setFloatable(false);
		JButton changeToolbarLocationButton = new JButton(ARROWS[position]);
		changeToolbarLocationButton.setFont(MainFrame.MAIN_FONT);
		changeToolbarLocationButton.setToolTipText(SwingUtils.toHTML("Change the position of the toolbar."));
		changeToolbarLocationButton.addActionListener(e -> {
			position++;
			if (position > 3)
				position = 0;
			changeToolbarLocationButton.setText(String.valueOf(ARROWS[position]));
			toolbar.setOrientation(position % 2 != 0 ? JToolBar.VERTICAL : JToolBar.HORIZONTAL);
			remove(toolbar);
			add(toolbar, POSITIONS[position]);
			repaint();
			revalidate();
		});
		toolbar.addToEnd(changeToolbarLocationButton);
		add(toolbar, POSITIONS[position]);
	}

	public Document getDocument() {
		return document;
	}
}
