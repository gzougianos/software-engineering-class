package myy803;

import javax.swing.text.JTextComponent;

import myy803.gui.controller.TabController;
import myy803.gui.view.DocumentView;
import myy803.model.TextCommand;
import myy803.model.commands.AddTextCommand;
import myy803.model.commands.Command;
import myy803.model.commands.LoadCommand;
import myy803.model.commands.SaveAsCommand;
import myy803.model.commands.SaveCommand;

public class CommandFactory {
	private CommandFactory() {
	}

	public static Command createTextCommand(TextCommand cmd, JTextComponent component) {
		return new AddTextCommand(cmd, component);
	}

	public static Command createLoadCommand(TabController tabController) {
		return new LoadCommand(tabController);
	}

	public static Command createSaveAsCommand(DocumentView view) {
		return new SaveAsCommand(view);
	}

	public static Command createSaveCommand(DocumentView view) {
		return new SaveCommand(view);
	}
}
