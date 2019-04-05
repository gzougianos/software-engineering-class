package myy803.gui.containers;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.model.DocumentType;

public class DocumentTypeLabel extends JLabel {
	private static final long serialVersionUID = 1928502133191516275L;
	private DocumentType documentType;
	private boolean chosen;
	private AddDocumentPanel addDocumentPanel;

	public DocumentTypeLabel(AddDocumentPanel adp, DocumentType docType, String text, Icon icon) {
		super(text);
		setFont(MainFrame.MAIN_FONT);
		setIcon(icon.toImageIcon(120));
		this.documentType = docType;
		this.addDocumentPanel = adp;
		this.chosen = false;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				onMouseClick();
			}
		});
	}

	public void onMouseClick() {
		Component parent = getParent();
		if (parent instanceof JPanel) {
			JPanel parentPanel = (JPanel) parent;
			for (Component c : parentPanel.getComponents()) {
				if (c instanceof DocumentTypeLabel) {
					DocumentTypeLabel dl = (DocumentTypeLabel) c;
					dl.setChosen(false);
					dl.repaint();
				}
			}
			setChosen(true);
			repaint();
			addDocumentPanel.onSelectionChange(getDocumentType());
		}
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (chosen) {
			g.drawImage(Icon.TICK.toImage(), 95, 0, this);
		}
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public boolean isChosen() {
		return chosen;
	}

	public void setChosen(boolean chosen) {
		this.chosen = chosen;
		repaint();
	}

}
