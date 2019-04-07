package myy803.gui;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public enum Icon {
	//@formatter:off
	LOAD("load.png"),
	SAVE("save.png"),
	SAVE_AS("save_as.png"),
	BOOK("book.png"),
	ARTICLE("article.png"),
	LETTER("letter.png"),
	REPORT("report.png"),
	BOOK_SMALL("book_small.png"),
	ARTICLE_SMALL("article_small.png"),
	LETTER_SMALL("letter_small.png"),
	REPORT_SMALL("report_small.png"),
	FAVICON("favicon.png"),
	TICK("tick.png"),
	SPLASH_SCREEN("splash_screen.png");
	//@formatter:on
	private static final String PACKAGE = "/myy803/resources/icons/";
	private String name;

	private Icon(String name) {
		this.name = name;
	}

	public ImageIcon toImageIcon(int width, int height) {
		Image originalImage = toImage();
		if (width == -1 || height == -1)
			return new ImageIcon(originalImage);
		Image resized = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH); // scale it the smooth way
		ImageIcon imageIcon2 = new ImageIcon(resized); // transform it back
		return imageIcon2;
	}

	public ImageIcon toImageIcon(int widthAndHeight) {
		return toImageIcon(widthAndHeight, widthAndHeight);
	}

	public ImageIcon toImageIcon() {
		return toImageIcon(-1, -1);
	}

	public Image toImage() {
		try {
			return ImageIO.read(MainFrame.class.getResourceAsStream(getFullName()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getFullName() {
		return PACKAGE + name;
	}
}
