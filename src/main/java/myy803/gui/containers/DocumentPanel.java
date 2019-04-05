package myy803.gui.containers;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import myy803.model.Document;

public class DocumentPanel extends JPanel {
	private static final long serialVersionUID = -1467388562407975227L;
	private Document document;

	public DocumentPanel(Document document) {
		super(new BorderLayout());
		this.document = document;
	}

	public Document getDocument() {
		return document;
	}
}
