package myy803.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import myy803.commons.Setting;

public class FrameWindowListener extends WindowAdapter {
	@Override
	public void windowClosing(WindowEvent evt) {
		MainFrame.getInstance().setVisible(false);
		saveLocationAndSize(evt);
		Setting.save();
		System.exit(0);
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	private void saveLocationAndSize(WindowEvent evt) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double screenWidth = screenSize.getWidth();
		double screenheight = screenSize.getHeight();
		int frameWidth = MainFrame.getInstance().getWidth();
		int frameHeight = MainFrame.getInstance().getHeight();
		// Save location and size only when its not in full screen.
		boolean isFullScreen = frameWidth >= screenWidth - 100 && frameHeight >= screenheight - 100;
		if (!isFullScreen) {
			Setting.FRAME_X.update(MainFrame.getInstance().getX());
			Setting.FRAME_Y.update(MainFrame.getInstance().getY());
			Setting.FRAME_WIDTH.update(frameWidth);
			Setting.FRAME_HEIGHT.update(frameHeight);
		}
		Setting.FRAME_FULLSCREEN.update(isFullScreen);
	}
}
