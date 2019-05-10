package myy803.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;

import myy803.DocumentManager;
import myy803.RecentFileManager;
import myy803.TextCommandManager;
import myy803.commons.Files;
import myy803.commons.Setting;
import myy803.gui.controller.TabController;
import myy803.gui.controller.impl.TabControllerImpl;
import myy803.gui.view.TabbedPanel;
import myy803.gui.view.TabbedView;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 895370362316846430L;
	private static final String TITLE = "Latex Editor";
	public static final Color LIGHT_BLUE = new Color(111, 141, 255);
	public static final Color GRAY = new Color(140, 140, 140);
	public static final Font MAIN_FONT = new Font("Cambria", 0, 14);
	private SplashScreen splashScreen;
	private TabbedView tabbedView;

	public MainFrame() {
		super(TITLE);
		setUpLookAndFeel();
		splashScreen = new SplashScreen();
		SwingUtils.doInBackground(() -> {
			loadData();
		}, () -> {
			setDefaultCloseOperation(EXIT_ON_CLOSE);
			addWindowListener(new FrameWindowListener());
			prepareGraphicEnvironment();
			setVisible(true);
			splashScreen.dispose();
		});
	}

	private void prepareGraphicEnvironment() {
		fixLocationAndSize();
		setIconImage(Icon.FAVICON.toImage());
		getContentPane().setLayout(new BorderLayout());

		TabController tabController = new TabControllerImpl();
		tabbedView = new TabbedPanel(tabController);
		tabController.setView(tabbedView);
		tabController.initialize();
		getContentPane().add(tabbedView.get());
	}

	public TabbedView getTabView() {
		return tabbedView;
	}

	private void loadData() {
		Files.initialize();
		Setting.initialize();
		DocumentManager.INSTANCE.hashCode(); //load document manager
		TextCommandManager.INSTANCE.hashCode(); //load command manager
		RecentFileManager.INSTANCE.hashCode(); //init recent file manager
		KeywordManager.INSTANCE.hashCode(); //load keywords
	}

	/**
	 * Restores the size and the location of the frame from the previous session.
	 */
	private void fixLocationAndSize() {
		setMinimumSize(new Dimension(500, 500));
		setSize(Setting.FRAME_WIDTH.toInt(), Setting.FRAME_HEIGHT.toInt());
		if (Setting.FRAME_X.toInt() != 0 || Setting.FRAME_Y.toInt() != 0)
			setLocation(Setting.FRAME_X.toInt(), Setting.FRAME_Y.toInt());
		else {
			setLocationRelativeTo(null);
			SwingUtils.centerizeWindow(this);
		}
		if (Setting.FRAME_FULLSCREEN.toBoolean()) {
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}

	private void setUpLookAndFeel() {
		try {
			UIManager.setLookAndFeel(new WebLookAndFeel());
		} catch (Exception e) {
			System.err.println("Error setting up web look and feel.");
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(MainFrame::getInstance);
	}

	public static final MainFrame getInstance() {
		return SingletonHolder._instance;
	}

	private static final class SingletonHolder {
		protected static final MainFrame _instance = new MainFrame();
	}
}
