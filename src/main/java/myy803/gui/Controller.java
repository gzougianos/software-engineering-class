package myy803.gui;

public interface Controller<T extends View<?>> {
	T getView();

	void initialize();

	void setView(T view);
}
