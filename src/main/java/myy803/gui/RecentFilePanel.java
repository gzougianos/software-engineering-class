package myy803.gui;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import myy803.DocumentManager;
import myy803.RecentFileManager;
import myy803.gui.containers.DocumentTabbedPanel;
import myy803.model.Document;

public class RecentFilePanel extends JPanel implements MouseListener {
	private static final long serialVersionUID = 8729420617823048789L;
	private String path;
	private JLabel iconLabel, authorLabel, lastModifiedDateLabel;
	private boolean mouseEntered = false;

	public RecentFilePanel(String path) {
		super(new BorderLayout(5, 0));
		this.path = path;

		iconLabel = new JLabel();
		add(iconLabel, BorderLayout.LINE_START);

		JPanel boxPanel = new JPanel();
		boxPanel.setLayout(new BoxLayout(boxPanel, BoxLayout.Y_AXIS));

		JLabel pathLabel = new JLabel(path);
		pathLabel.setFont(MainFrame.MAIN_FONT);
		boxPanel.add(pathLabel);

		authorLabel = new JLabel("Author: ");
		boxPanel.add(authorLabel);

		lastModifiedDateLabel = new JLabel("Date Modified: ");
		boxPanel.add(lastModifiedDateLabel);
		add(boxPanel, BorderLayout.CENTER);

		addMouseListener(this);

		startReadingFileInBackround();
	}

	private void startReadingFileInBackround() {
		SwingWorker<Document, Void> worker = new SwingWorker<Document, Void>() {

			@Override
			protected Document doInBackground() throws Exception {
				File f = new File(path);
				return DocumentManager.INSTANCE.loadDocument(f);
			}

			@Override
			protected void done() {
				try {
					Document doc = get();
					if (doc != null) {
						Icon i = DocumentTabbedPanel.getIconBasedOnDocType(doc.getDocumentType());
						iconLabel.setIcon(i.toImageIcon());
						authorLabel.setText(doc.getAuthor());
						lastModifiedDateLabel.setText(SwingUtils.formatDate(doc.getLastModifiedDate()));
					}
				} catch (InterruptedException | ExecutionException e) {
					System.err.println("Error reading document contents.");
					e.printStackTrace();
				}
				super.done();
			}
		};
		worker.execute();
	}

	public String getPath() {
		return path;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (mouseEntered) {
			g.drawImage(Icon.TICK.toImage(), 0, 0, this);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		File selectedFile = new File(path);
		try {
			Document doc = DocumentManager.INSTANCE.loadDocument(selectedFile);
			// Check if this file is already opened
			if (DocumentManager.INSTANCE.getDocuments().contains(doc)) {
				MainFrame.getInstance().getTabbedPanel().openDocumentTab(doc);
			} else {
				DocumentManager.INSTANCE.getDocuments().add(doc);
				MainFrame.getInstance().getTabbedPanel().createTabAndShowDocument(doc);
			}
			RecentFileManager.INSTANCE.push(doc);
			MainFrame.getInstance().getTabbedPanel().getAddDocPanel().fixRecentFiles();
		} catch (ClassNotFoundException | IOException e1) {
			System.err.println("Error reading file " + selectedFile.getAbsolutePath());
			e1.printStackTrace();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseEntered = true;
		repaint();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseEntered = false;
		repaint();
	}
}
