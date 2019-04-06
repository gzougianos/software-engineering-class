package myy803.gui;

import static myy803.gui.MainFrame.MAIN_FONT;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;

public class SwingUtils {
	/**
	 * Converts the given s to HTML string, with fixed font (ACTIVE_FONT).<br>
	 * Mostly used for tooltips in components.
	 * @param s
	 * @return the HTML string
	 */
	public static String toHTML(String s) {
		if (s == null || s.length() == 0)
			return "";

		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		int size = 4;
		s = s.replaceAll("<hr>", "</font><hr><font size=\"" + size + "\">");
		sb.append("<font size=\"" + size + "\">");
		sb.append(s);
		sb.append("</font>");
		sb.append("</html>");
		return sb.toString();
	}

	/**
	 * Increases the speed of the vertical scroll bar of the given scroll pane.
	 * @param scrollPane The scroll Pane.
	 * @param amount the extra speed.
	 */
	public static void increaseScrollBarSpeed(JScrollPane scrollPane, int amount) {
		JScrollBar sb = scrollPane.getVerticalScrollBar();
		if (sb != null) {
			sb.setUnitIncrement(sb.getUnitIncrement() + amount);
		}
	}

	/**
	 * Moves a window to the center of the screen
	 * 
	 * @param win
	 */
	public static void centerizeWindow(Window win) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - win.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - win.getHeight()) / 2);
		win.setLocation(x, y);
	}

	/**
	 * Creates a dialog with the given pane, title and image.<br>
	 * The buttons will have the active font.<br>
	 * 
	 * @param pane
	 * @param dialogTitle
	 * @param dialogIconImage
	 * @return The selected value (integer) of pane's options[] array. If value is
	 *         null returns -1.
	 */
	public static int createOptionPanel(JOptionPane pane, String dialogTitle, Image dialogIconImage) {
		for (JButton button : ExternalSwingUtils.getDescendantsOfType(JButton.class, pane)) {
			button.setFont(MAIN_FONT);
			button.setFocusPainted(false);
		}
		JDialog dialog = pane.createDialog(dialogTitle);
		dialog.setLocationRelativeTo(MainFrame.getInstance());
		dialog.setIconImage(dialogIconImage);
		dialog.setVisible(true);
		dialog.dispose();
		dialog = null;
		if (pane.getValue() == null || pane.getOptions() == null || pane.getOptions().length == 1)
			return -1;
		for (int i = 0; i < pane.getOptions().length; i++) {
			if (pane.getValue() == pane.getOptions()[i])
				return i;
		}
		return -1;
	}

	/**
	 * Creates a JOptionPane with 2 options.<br>
	 * 
	 * @see {@link SwingUtils#createOptionPanel(JOptionPane, String, Image)}.
	 * @param message     The message, component or a string.
	 * @param dialogTitle
	 * @param dialogIcon
	 * @param option1
	 * @param option2
	 * @return True if option1 is selected.
	 */
	public static boolean createDoubleOptionPanel(Object message, String dialogTitle, Icon dialogIcon, String option1,
			String option2) {
		Object[] options = { option1, option2 };
		JOptionPane pane = new JOptionPane(message, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, options,
				options[0]);
		return createOptionPanel(pane, dialogTitle, dialogIcon.toImage()) == 0;
	}

	/**
	 * Creates a simple info panel. The only available option is "OK". As a message
	 * type is used JOptionPane.INFORMATION_MESSAGE.
	 * 
	 * @param title   the title in dialog
	 * @param message the message in dialog (accepts HTML)
	 */
	public static void createSimpleOptionPane(Component parent, String title, String message) {
		createSimpleOptionPane(parent, title, message, null, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Creates a simple info panel. The only available option is "OK".<br>
	 * As a message type is used JOptionPane.INFORMATION_MESSAGE.
	 * 
	 * @param title   the title in dialog
	 * @param message the message in dialog (accepts HTML)
	 * @param icon    A custom icon for the dialog
	 */
	public static void createSimpleOptionPane(Component parent, String title, String message, Icon icon) {
		createSimpleOptionPane(parent, title, message, icon, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Creates a simple info panel. The only available option is "OK".
	 * 
	 * @param title       the title in dialog
	 * @param message     the message in dialog (accepts HTML)
	 * @param messageType The type of the message. JOptionPane.INFORMATION_MESSAGE &
	 *                    etc.
	 */
	public static void createSimpleOptionPane(Component parent, String title, String message, int messageType) {
		createSimpleOptionPane(parent, title, message, null, messageType);
	}

	/**
	 * Creates a simple info panel. The only available option is "OK".
	 * 
	 * @param title       the title in dialog
	 * @param message     the message in dialog (accepts HTML)
	 * @param icon        A custom icon for the dialog
	 * @param messageType The type of the message. JOptionPane.INFORMATION_MESSAGE &
	 *                    etc.
	 */
	public static void createSimpleOptionPane(Component parent, String title, String message, Icon icon, int messageType) {
		if (icon != null)
			JOptionPane.showMessageDialog(parent, message, title, messageType, icon.toImageIcon());
		else
			JOptionPane.showMessageDialog(parent, message, title, messageType);
	}

	/**
	 * Formats kilometres. e.g: If kilos 10000 it will return 10.000
	 * 
	 * @param kilos
	 * @return the formatted string with kilos
	 */
	public static String formatKilometres(int kilos) {
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
		DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		formatter.setDecimalFormatSymbols(symbols);
		return formatter.format(kilos);
	}

	/**
	 * Creates a titled border with the active font.
	 * 
	 * @param title
	 * @return the border
	 */
	public static TitledBorder createTitledBorder(String title) {
		TitledBorder b = BorderFactory.createTitledBorder(title);
		b.setTitleFont(MAIN_FONT);
		return b;
	}

	public static JTextField createSimpleTextField(String text) {
		JTextField txt = new JTextField(text);
		txt.setFont(MAIN_FONT);
		txt.setBorder(BorderFactory.createEmptyBorder());
		txt.setFocusable(false);
		txt.setEditable(false);
		return txt;
	}

	public static JPanel createFlowPanel(int align, Component... components) {
		JPanel panel = new JPanel(new FlowLayout(align));
		for (Component c : components)
			panel.add(c);
		return panel;
	}

	public static JPanel createFlowPanel(Component... components) {
		return createFlowPanel(FlowLayout.LEADING, components);
	}

	/**
	 * Executes a runnable in background using {@link SwingWorker}.<br>
	 * 
	 * @param r the runnable.
	 */
	public static final void doInBackground(Runnable r) {
		doInBackground(r, null);
	}

	/**
	 * Executes a runnable in background using {@link SwingWorker}.<br>
	 * 
	 * @param doInBg the runnable that runs in background.
	 * @param done   the runnable that runs when process in background ends.
	 */
	public static final void doInBackground(Runnable doInBg, Runnable done) {
		new SwingWorker<Object, Object>() {

			@Override
			protected Object doInBackground() throws Exception {
				doInBg.run();
				return null;
			}

			@Override
			protected void done() {
				try {
					get();
				} catch (InterruptedException | ExecutionException e) {
					System.err.println("Cannot do in background.");
					e.printStackTrace();
				}
				if (done != null)
					done.run();
				super.done();
			}
		}.execute();
	}

	/**
	 * Fires the given action listener ONCE after the given delay. The runnable with
	 * run on EDT.
	 * 
	 * @param delay
	 * @param action
	 */
	public static void doLater(long delay, Runnable r) {
		Timer t = new Timer((int) delay, e -> r.run());
		t.setRepeats(false);
		t.start();
	}

	/**
	 * Fires the given action listener ONCE after the given delay. The runnable with
	 * run on background.
	 * 
	 * @param delay
	 * @param action
	 */
	public static void doLaterInBackground(long delay, Runnable r) {
		Timer t = new Timer((int) delay, e -> {
			SwingUtils.doInBackground(r);
		});
		t.setRepeats(false);
		t.start();
	}
}
