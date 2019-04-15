package myy803.gui;

import javax.swing.JComponent;

public interface View<T extends JComponent> {
	T get();

	Controller<?> getController();
}
