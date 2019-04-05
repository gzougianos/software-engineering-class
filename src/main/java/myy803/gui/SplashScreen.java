package myy803.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class SplashScreen extends JFrame {
	private static final long serialVersionUID = -2872560186666271687L;

	public SplashScreen() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setIconImage(Icon.FAVICON.toImage());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLocation(0, 0);
		setSize(dim);
		setUndecorated(true);
		setBackground(new Color(0, 255, 0, 0));

		JPanel panel = new JPanel(new BorderLayout()) {
			private static final long serialVersionUID = -5344741690038297174L;

			@Override
			protected void paintComponent(Graphics g) {

				// Allow super to paint
				super.paintComponent(g);

				// Apply our own painting effect
				Graphics2D g2d = (Graphics2D) g.create();
				// 50% transparent Alpha
				g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

				g2d.setColor(getBackground());
				g2d.fill(getBounds());
				g2d.dispose();

			}

			@Override
			public boolean isOpaque() {
				return false;
			}
		};
		panel.setBackground(new Color(0, 255, 0, 0));
		JLabel l = new JLabel(Icon.SPLASH_SCREEN.toImageIcon());
		l.setOpaque(false);
		add(l);
		setVisible(true);
	}
}
