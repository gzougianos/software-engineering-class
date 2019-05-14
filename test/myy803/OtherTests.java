package myy803;

import static org.junit.Assert.assertEquals;
import myy803.model.DocumentType;

import org.junit.Test;

public class OtherTests {

	@Test
	public void documentType() {
		assertEquals(DocumentType.ARTICLE.getName(), "Article");
		assertEquals(DocumentType.ARTICLE.getIcon().getDescription().toLowerCase(), "article");
	}

}
