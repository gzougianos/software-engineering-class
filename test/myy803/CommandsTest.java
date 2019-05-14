package myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.swing.JTextArea;

import myy803.model.DocumentType;
import myy803.model.TextCommand;

import org.junit.Test;

public class CommandsTest {
	private static final TextCommandManager CM = TextCommandManager.INSTANCE;

	@Test
	public void textCommand() {
		JTextArea component = new JTextArea();
		TextCommand tc = CM.getCommands().get(0);
		CommandFactory.createTextCommand(tc, component).execute();
		assertEquals(tc.getContent(), component.getText());
	}

	@Test
	public void commandAllowedDisallowedTypes() {
		TextCommand c = CM.getCommands().get(0);
		c.setDisallowedTypes("LETTER, ARTICLE");
		assertFalse(c.allowsType(DocumentType.LETTER));
		c.setAllowedTypes("LETTER, ARTICLE");
		assertFalse(c.allowsType(DocumentType.LETTER));
		c.setDisallowedTypes("");
		assertTrue(c.allowsType(DocumentType.LETTER));
		c.setAllowedTypes("ALL");
		c.setDisallowedTypes("ARTICLE");
		assertTrue(c.allowsType(DocumentType.LETTER));
		assertTrue(c.allowsType(DocumentType.BOOK));
		assertTrue(c.allowsType(DocumentType.REPORT));
		assertFalse(c.allowsType(DocumentType.ARTICLE));
	}
}
