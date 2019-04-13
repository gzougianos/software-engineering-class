package gr.uoi.cs.myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import myy803.model.Command;
import myy803.model.DocumentType;

public class OtherTests {

	@Test
	public void allCommandsAreDeclared() {
		for (Command c : Command.values()) {
			assertNotNull(c.getContent());
		}
	}

	@Test
	public void documentType() {
		assertEquals(DocumentType.ARTICLE.getName(), "Article");
		assertEquals(DocumentType.ARTICLE.getIcon().getDescription().toLowerCase(), "article");
	}

}
