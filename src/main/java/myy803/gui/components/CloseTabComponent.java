package myy803.gui.components;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import myy803.gui.MainFrame;
import myy803.gui.controller.TabController;
import myy803.model.Document;

public class CloseTabComponent extends JPanel {
	private static final long serialVersionUID = 4148815077976600552L;
	private JLabel titleLabel;
	private Document document;

	public CloseTabComponent(final TabController controller, Document doc) {
		super(new GridBagLayout());
		this.document = doc;
		setOpaque(false);
		ImageIcon icon = doc.getDocumentType().getIcon(true);
		titleLabel = new JLabel() {
			private static final long serialVersionUID = 1826571417697457181L;

			@Override
			public String getText() {
				if (!document.isSaved())
					return doc.getName() + " *";
				return doc.getName();
			}
		};
		titleLabel.setIcon(icon);
		titleLabel.setFont(MainFrame.MAIN_FONT);
		titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		JButton btnClose = new JButton("X");
		btnClose.setFont(MainFrame.MAIN_FONT.deriveFont(10f));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1;

		add(titleLabel, gbc);

		gbc.gridx++;
		gbc.weightx = 0;
		add(btnClose, gbc);

		btnClose.addActionListener(e -> controller.closeDocumentTab(doc));
	}

	public Document getDocument() {
		return this.document;
	}

}
