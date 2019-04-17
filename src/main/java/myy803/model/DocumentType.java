package myy803.model;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum DocumentType {
	EMPTY, ARTICLE, BOOK, LETTER, REPORT;
	private static final String ICON_PACKAGE = "/myy803/resources/icons/documents/";

	public String getName() {
		String firstLetter = toString().substring(0, 1);
		return firstLetter.toUpperCase() + toString().toLowerCase().substring(1, toString().length());
	}

	public ImageIcon getIcon(boolean small) {
		try {
			Image img = ImageIO.read(DocumentType.class.getResourceAsStream(getIconName(small)));
			ImageIcon imgIcon = new ImageIcon(img);
			imgIcon.setDescription(toString());
			return imgIcon;
		} catch (IOException e) {
			System.err.println("Error reading icon for document type: " + toString() + " from " + ICON_PACKAGE + " package.");
			e.printStackTrace();
		}
		return null;
	}

	public ImageIcon getIcon() {
		return getIcon(false);
	}

	private String getIconName(boolean small) {
		String name = toString();
		if (small)
			name += "_small";
		name = name.toLowerCase();
		return ICON_PACKAGE + name + ".png";
	}
}
