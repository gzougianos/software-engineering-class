package myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import myy803.model.Document;
import myy803.model.DocumentType;
import myy803.model.version.NoPreviousVersionException;

public class VolatileVersionStrategyTests {
	private VersionsManager versionsManager = VersionsManager.INSTANCE;

	@Test
	public void rollbackOne() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);

		doc.setContent(doc.getContent() + "somehig");
		Document beforeChange = doc.clone();

		versionsManager.commitVersion(doc);

		doc.setContent(doc.getContent() + " dsadas");
		versionsManager.rollToPreviousVersion(doc);

		assertEquals(doc, beforeChange);
	}

	@Test(expected = NoPreviousVersionException.class)
	public void neverKeptOne() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);

		versionsManager.rollToPreviousVersion(doc);
	}

	@Test
	public void previousVersions() {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.setContent(doc.getContent() + "something");

		assertTrue(versionsManager.getPreviousVersions(doc).isEmpty());

		versionsManager.commitVersion(doc);
		assertTrue(versionsManager.getPreviousVersions(doc).size() == 1);
	}

	@Before
	public void sleep() throws InterruptedException {
		Thread.sleep(15); //Need to sleep because some times documents are created with the same hascode
	}
}
