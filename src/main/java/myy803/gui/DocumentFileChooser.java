package myy803.gui;

import java.io.File;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import com.alee.laf.filechooser.WebFileChooser;

import myy803.commons.Setting;
import myy803.model.Document;

public class DocumentFileChooser extends WebFileChooser {
	private static final long serialVersionUID = 6055276476857528752L;

	public DocumentFileChooser() {
		super(Setting.LAST_DIRECTORY_SAVED.toStr());
		applyOptions();
	}

	public DocumentFileChooser(Document document) {
		super();
		String currentDir = Setting.LAST_DIRECTORY_SAVED.toStr();
		if (document.getPath().exists())
			currentDir = document.getPath().getAbsolutePath();
		setCurrentDirectory(currentDir);
		if (document.getPath().exists())
			setSelectedFile(document.getPath().getAbsolutePath());
		applyOptions();
	}

	private void applyOptions() {
		setFileHidingEnabled(false);
		setAcceptAllFileFilterUsed(false);
		setFileFilter(new FileFilter() {

			@Override
			public String getDescription() {
				return "LAT files";
			}

			@Override
			public boolean accept(File f) {
				return f.getName().toLowerCase().endsWith(Document.FILE_EXTENSION);
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
