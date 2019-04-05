package gr.uoi.cs.myy803;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import myy803.DocumentManager;
import myy803.model.Command;
import myy803.model.Document;
import myy803.model.DocumentType;

public class MainTests {
	private static final DocumentManager DM = DocumentManager.INSTANCE;

	@Test
	public void allDocTypesHaveTemplate() {
		for (DocumentType docType : DocumentType.values()) {
			Document doc = DM.createDocument(docType);
			assertNotEquals(doc.getContent(), "");
		}
	}

	@Test
	public void allCommandsAreDeclared() {
		for (Command c : Command.values()) {
			assertNotNull(c.getContent());
		}
	}

	@Test
	public void newDocumentName() {
		Document doc1 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc1.getPath().getName(), "unsaved.tex");
		DM.getDocuments().add(doc1);

		Document doc2 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc2.getPath().getName(), "unsaved1.tex");
		DM.getDocuments().add(doc2);

		Document doc3 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc3.getPath().getName(), "unsaved2.tex");
		DM.getDocuments().add(doc3);

		DM.getDocuments().remove(doc2);

		Document doc4 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc4.getPath().getName(), "unsaved3.tex");
		DM.getDocuments().add(doc4);

		DM.getDocuments().clear();

		Document doc5 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc5.getPath().getName(), "unsaved.tex");
		DM.getDocuments().add(doc5);

		for (int i = 0; i < 1000; i++) {
			DM.getDocuments().add(DM.createDocument(DocumentType.ARTICLE));
		}

		assertEquals(DM.getDocuments().get(DM.getDocuments().size() - 1).getName(), "unsaved1000");
	}
}
