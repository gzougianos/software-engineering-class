package myy803.model.commands;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;

import myy803.model.TextCommand;

public class AddTextCommand implements Command {
	private TextCommand textCommand;
	private JTextComponent textComponent;

	public AddTextCommand(TextCommand textCommand, JTextComponent component) {
		this.textCommand = textCommand;
		this.textComponent = component;
	}

	@Override
	public void execute() {
		int cursorIndex = textComponent.getCaretPosition();
		try {
			textComponent.getDocument().insertString(cursorIndex, textCommand.getContent(), new SimpleAttributeSet());
			if (textCommand.hasCursor()) {
				textComponent.requestFocusInWindow();
				SwingUtilities.invokeLater(() -> {
					int totalCursor = cursorIndex + textCommand.getCursorIndex();
					textComponent.setCaretPosition(totalCursor);
				});
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}
