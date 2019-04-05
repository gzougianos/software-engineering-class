package myy803.gui.containers;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import myy803.DocumentManager;
import myy803.gui.Icon;
import myy803.gui.MainFrame;
import myy803.model.Document;
import myy803.model.DocumentType;

public class DocumentTabbedPanel extends JTabbedPane {
	private static final long serialVersionUID = -7298563748251359603L;
	private AddDocumentPanel addDocPanel;

	public DocumentTabbedPanel() {
		super();
		setBorder(null);
		setFont(MainFrame.MAIN_FONT);
		addDocPanel = new AddDocumentPanel();
		addTab("+", addDocPanel);
	}

	public void createTabAndShowDocument(Document doc) {
		remove(addDocPanel);
		DocumentPanel dp = new DocumentPanel(doc);
		addTab(doc.getName(), dp);
		int index = indexOfTab(doc.getName());
		Icon icon = getIconBasedOnDocType(doc.getDocumentType());
		setTabComponentAt(index, new CloseTabComponent(doc.getName(), icon));
		addTab("+", addDocPanel);
		setSelectedIndex(getTabCount() - 2);
	}

	private Icon getIconBasedOnDocType(DocumentType docType) {
		switch (docType) {
			case ARTICLE:
				return Icon.ARTICLE_SMALL;
			case BOOK:
				return Icon.BOOK_SMALL;
			case REPORT:
				return Icon.REPORT_SMALL;
			default:
				return Icon.LETTER_SMALL;
		}
	}

	private static class CloseTabComponent extends JPanel implements ActionListener {
		private static final long serialVersionUID = 4148815077976600552L;
		private String name;

		private CloseTabComponent(String name, Icon icon) {
			super(new GridBagLayout());
			this.name = name;
			setOpaque(false);
			JLabel lblTitle = new JLabel(name);
			lblTitle.setIcon(icon.toImageIcon());
			lblTitle.setFont(MainFrame.MAIN_FONT);
			lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
			JButton btnClose = new JButton("X");
			btnClose.setFont(MainFrame.MAIN_FONT.deriveFont(10f));

			GridBagConstraints gbc = new GridBagConstraints();
			gbc.gridx = 0;
			gbc.gridy = 0;
			gbc.weightx = 1;

			add(lblTitle, gbc);

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
