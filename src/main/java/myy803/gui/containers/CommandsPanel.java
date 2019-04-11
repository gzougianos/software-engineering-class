package myy803.gui.containers;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.alee.laf.scroll.WebScrollPane;

import myy803.gui.MainFrame;
import myy803.gui.SwingUtils;
import myy803.model.Command;
import myy803.model.DocumentType;

public class CommandsPanel extends JPanel implements ListSelectionListener {
	private static final long serialVersionUID = 5301578078614783234L;
	private Command selectedCommand;
	private DocumentType documentType;
	private CommandsList list;

	public CommandsPanel(DocumentType docType) {
		super(new BorderLayout());
		this.documentType = docType;
		add(createCommandsList(), BorderLayout.LINE_START);
	}

	private JScrollPane createCommandsList() {
		JPanel main = new JPanel(new BorderLayout());
		main.setBorder(SwingUtils.createTitledBorder("Commands List"));
		list = new CommandsList();
		list.addListSelectionListener(this);
		main.add(list);
		WebScrollPane sp = new WebScrollPane(main);
		sp.setDrawBorder(false);
		return sp;
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		selectedCommand = list.getSelectedValue();
	}

	public Command getSelectedCommand() {
		return selectedCommand;
	}

	private final class CommandsList extends JList<Command> {
		private static final long serialVersionUID = 4189964359687859885L;
		private DefaultListModel<Command> model;

		public CommandsList() {
			super();
			setOpaque(false);
			model = new DefaultListModel<>();
			for (Command c : Command.values()) {
				if (c.allowsType(documentType)) {
					model.addElement(c);
				}
			}
			setModel(model);
			setCellRenderer(new DefaultListCellRenderer() {
				private static final long serialVersionUID = 5852100270697626735L;

				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					JLabel lab = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					lab.setFont(MainFrame.MAIN_FONT);
					if (value instanceof Command) {
						Command c = (Command) value;
						lab.setText(c.toString());
					}
					return lab;
				}
			});
			if (model.getSize() > 0)
				setSelectedIndex(0);
		}
	}

}
