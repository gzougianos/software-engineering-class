package gr.uoi.cs.myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import myy803.CommandManager;
import myy803.model.Command;
import myy803.model.DocumentType;

public class OtherTests {
	private static final CommandManager CM = CommandManager.INSTANCE;

	@Test
	public void commandAllowedDisallowedTypes() {
		Command c = CM.getCommands().get(0);
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

	@Test
	public void documentType() {
		assertEquals(DocumentType.ARTICLE.getName(), "Article");
		assertEquals(DocumentType.ARTICLE.getIcon().getDescription().toLowerCase(), "article");
	}

}
