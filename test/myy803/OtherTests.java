package gr.uoi.cs.myy803;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import myy803.model.DocumentType;

public class OtherTests {

	@Test
	public void documentType() {
		assertEquals(DocumentType.ARTICLE.getName(), "Article");
		assertEquals(DocumentType.ARTICLE.getIcon().getDescription().toLowerCase(), "article");
	}

}
