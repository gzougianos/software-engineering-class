package myy803.gui.views;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import myy803.gui.ExternalSwingUtils;
import myy803.gui.MainFrame;
import myy803.gui.controller.DocumentTabController;
import myy803.model.Document;

public class DocumentTabbedPanel extends JTabbedPane implements DocumentTabView {
	private static final long serialVersionUID = -7298563748251359603L;
	private AddDocumentView addDocumentView;
	private DocumentTabController controller;

	public DocumentTabbedPanel(DocumentTabController controller) {
		super();
		this.controller = controller;
		setBorder(null);
		setFont(MainFrame.MAIN_FONT);
	}

	public void openDocumentTab(Document doc) {
		int index = -1;
		for (int i = 0; i < getTabCount(); i++) {
			Component c = getTabComponentAt(i);
			if (c instanceof CloseTabComponent) {
				CloseTabComponent tabC = (CloseTabComponent) c;
				if (tabC.titleLabel.getText().replaceAll(" *", "").contains(doc.getName())) {
					index = i;
					break;
				}
			}
		}
		if (index >= 0)
			setSelectedIndex(index);
	}

	public void repaintLabels() {
		for (CloseTabComponent c : ExternalSwingUtils.getDescendantsOfType(CloseTabComponent.class, this)) {
			c.titleLabel.repaint();
			c.revalidate();
		}
	}

	private class CloseTabComponent extends JPanel {
		private static final long serialVersionUID = 4148815077976600552L;
		private String name;
		private JLabel titleLabel;
		private Document document;

		private CloseTabComponent(Document doc) {
			super(new GridBagLayout());
			this.name = doc.getName();
			this.document = doc;
			setOpaque(false);
			ImageIcon icon = doc.getDocumentType().getIcon(true);
			titleLabel = new JLabel(name) {
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

			btnClose.addActionListener(e -> getController().closeDocumentTab(doc));
		}
	}

	@Override
	public DocumentTabController getController() {
		return controller;
	}

	@Override
	public AddDocumentView getAddDocumentView() {
		return addDocumentView;
	}

	@Override
	public JComponent createCloseTabComponent(Document doc) {
		return new CloseTabComponent(doc);
	}

	@Override
	public void setAddDocumentView(AddDocumentView view) {
		addDocumentView = view;

	}

	@Override
	public DocumentTabbedPanel get() {
		return this;
	}

}
