package myy803.gui;

import java.io.File;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;

import myy803.model.Document;

public class DocumentFileChooser extends WebFileChooser {
	private static final long serialVersionUID = 6055276476857528752L;

	public DocumentFileChooser(Document document) {
		super(document.getPath());
		setSelectedFile(document.getPath().getAbsolutePath());
		setFileHidingEnabled(false);
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "Latex files";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(".tex");
			}
		});
		boostScrollBar();
	}

	private void boostScrollBar() {
		List<JScrollPane> sps = ExternalSwingUtils.getDescendantsOfType(JScrollPane.class, this);
		for (JScrollPane sp : sps)
			SwingUtils.increaseScrollBarSpeed(sp, 40);
	}
}
