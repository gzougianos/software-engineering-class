package gr.uoi.cs.myy803;

import static org.junit.Assert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.Test;

import myy803.DocumentManager;
import myy803.model.Command;
import myy803.model.Document;
import myy803.model.DocumentType;

public class MainTests {
	@Test
	public void allDocTypesHaveTemplate() {
		for (DocumentType docType : DocumentType.values()) {
			Document doc = DocumentManager.INSTANCE.createDocument(docType);
			assertNotEquals(doc.getContent(), "");
		}
	}

	@Test
	public void allCommandsAreDeclared() {
		for (Command c : Command.values()) {
			assertNotNull(c.getContent());
		}
	}
}
