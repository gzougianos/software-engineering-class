package gr.uoi.cs.myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.junit.Test;

import myy803.DocumentManager;
import myy803.model.Document;
import myy803.model.DocumentType;

public class DocumentRelatedTests {
	private static final DocumentManager DM = DocumentManager.INSTANCE;

	@Test
	public void isDocumentEqualsAfterSaveLoad() throws IOException, ClassNotFoundException {
		File path = new File("testdocument.lat");
		Document doc = DM.createDocument(DocumentType.BOOK);
		doc.setPath(path);
		DM.saveDocument(doc);
		Document doc1 = DM.loadDocument(path);
		assertTrue(doc1.equals(doc));
		Files.delete(path.toPath());
	}

	@Test
	public void authorTest() {
		Document doc = DM.createDocument(DocumentType.BOOK);
		doc.setAuthor("Eleftheria", "Tsaknaki");
		assertTrue(doc.getContent().contains("\\author{Eleftheria \\and Tsaknaki}"));

		//Manual replace
		String replace = doc.getContent().replaceAll("\\\\author\\{Eleftheria \\\\and Tsaknaki\\}",
				"\\\\author\\{Tziolas \\\\and Giorgos\\}");
		doc.setContent(replace);
		assertEquals(doc.getAuthor(), "Tziolas, Giorgos");

		doc.setAuthor("Zougianos", "Giorgos", "Zarras");
		assertEquals(doc.getAuthor(), "Zougianos, Giorgos, Zarras");
		doc.setAuthor((String) null);
		assertEquals(doc.getAuthor(), "-");

		//On empty content the whole command is written
		doc.setContent("");
		assertEquals(doc.getAuthor(), "-");
		doc.setAuthor("Apostolos Zarras ");
		assertEquals(doc.getAuthor(), "Apostolos Zarras");

		//Test empty author
		doc.setAuthor("       ");
		assertEquals(doc.getAuthor(), "-");
	}

	@Test
	public void allDocTypesHaveTemplate() {
		for (DocumentType docType : DocumentType.values()) {
			Document doc = DM.createDocument(docType);
			assertNotEquals(doc.getContent(), "");
		}
	}

	@Test
	public void documentGetName() {
		File path = new File("testdocument.lat");
		Document doc = DM.createDocument(DocumentType.BOOK);
		doc.setPath(path);
		assertEquals(doc.getName(), "testdocument");
	}

	@Test
	public void newDocumentName() {
		Document doc1 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc1.getName(), "unsaved");
		DM.getDocuments().add(doc1);

		Document doc2 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc2.getName(), "unsaved1");
		DM.getDocuments().add(doc2);

		Document doc3 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc3.getName(), "unsaved2");
		DM.getDocuments().add(doc3);

		DM.getDocuments().remove(doc2);

		Document doc4 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc4.getName(), "unsaved3");
		DM.getDocuments().add(doc4);

		DM.getDocuments().clear();

		Document doc5 = DM.createDocument(DocumentType.ARTICLE);
		assertEquals(doc5.getName(), "unsaved");
		DM.getDocuments().add(doc5);

		for (int i = 0; i < 1000; i++) {
			DM.getDocuments().add(DM.createDocument(DocumentType.ARTICLE));
		}

		assertEquals(DM.getDocuments().get(DM.getDocuments().size() - 1).getName(), "unsaved1000");
	}
}
