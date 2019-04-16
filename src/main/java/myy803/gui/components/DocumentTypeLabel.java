package myy803.gui.components;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;

import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.gui.controller.AddDocumentController;
import myy803.model.DocumentType;

public class DocumentTypeLabel extends JLabel {
	private static final long serialVersionUID = 1928502133191516275L;
	private static final int ICON_DIMENSION = 120;
	private DocumentType documentType;
	private boolean chosen;
	private AddDocumentController controller;

	public DocumentTypeLabel(AddDocumentController controller, DocumentType documentType) {
		super(documentType.getName());
		this.controller = controller;
		setFont(MainFrame.MAIN_FONT);
		Image img = documentType.getIcon().getImage().getScaledInstance(ICON_DIMENSION, ICON_DIMENSION, Image.SCALE_SMOOTH);
		setIcon(new ImageIcon(img));
		this.documentType = documentType;
		this.chosen = false;
		setBorder(new EmptyBorder(10, 10, 10, 10));
		setHorizontalTextPosition(JLabel.CENTER);
		setVerticalTextPosition(JLabel.BOTTOM);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				controller.onChangeDocTypeSelection(getDocumentType());
			}
		});
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
