package myy803;

import javax.swing.text.JTextComponent;

import myy803.gui.controller.TabController;
import myy803.model.TextCommand;
import myy803.model.commands.AddTextCommand;
import myy803.model.commands.Command;
import myy803.model.commands.LoadCommand;

public class CommandFactory {
	private CommandFactory() {
	}

	public static Command createTextCommand(TextCommand cmd, JTextComponent component) {
		return new AddTextCommand(cmd, component);
	}

	public static Command createLoadCommand(TabController tabController) {
		return new LoadCommand(tabController);
	}
}
