package myy803.gui.views;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import myy803.DocumentManager;
import myy803.gui.ExternalSwingUtils;
import myy803.gui.MainFrame;
import myy803.gui.controller.AddDocumentController;
import myy803.gui.controller.AddDocumentControllerImpl;
import myy803.model.Document;

public class DocumentTabbedPanel extends JTabbedPane {
	private static final long serialVersionUID = -7298563748251359603L;
	private AddDocumentPanel addDocPanel;

	public DocumentTabbedPanel() {
		super();
		setBorder(null);
		setFont(MainFrame.MAIN_FONT);
		AddDocumentController controller = new AddDocumentControllerImpl();
		addDocPanel = new AddDocumentPanel(controller);
		controller.initialize();
		addTab("+", addDocPanel);
	}

	public void createTabAndShowDocument(Document doc) {
		remove(addDocPanel);
		DocumentPanel dp = new DocumentPanel(doc);
		addTab(doc.getName(), dp);
		int index = indexOfTab(doc.getName());
		setTabComponentAt(index, new CloseTabComponent(doc));
		addTab("+", addDocPanel);
		setSelectedIndex(getTabCount() - 2);
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

	public AddDocumentPanel getAddDocPanel() {
		return addDocPanel;
	}

	private static class CloseTabComponent extends JPanel implements ActionListener {
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

			btnClose.addActionListener(this);

		}

		private int getIndex() {
			return MainFrame.getInstance().getTabbedPanel().indexOfTab(this.name);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			DocumentTabbedPanel dtp = MainFrame.getInstance().getTabbedPanel();
			Component c = dtp.getComponentAt(getIndex());
			if (c instanceof DocumentPanel) {
				Document documentOfThisTab = ((DocumentPanel) c).getDocument();
				DocumentManager.INSTANCE.getDocuments().remove(documentOfThisTab);
				dtp.removeTabAt(getIndex());
			}
		}
	}

}
