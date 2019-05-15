package myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import myy803.model.Document;
import myy803.model.DocumentType;
import myy803.model.version.NoPreviousVersionException;

public class StableVersionStrategyTests {
	@Test(expected = NoPreviousVersionException.class)
	public void neverKeptAVersion() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.previousVersion();
	}

	@Test
	public void keepOne() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.setContent(doc.getContent() + "something");

		Document before1Version = doc.clone();
		doc.commitVersion();
		doc.setContent(doc.getContent() + "else");//ends in somethingelse
		doc.previousVersion();

		assertEquals(before1Version, doc);
		assertEquals(doc.getVersionId(), 1);

		doc.setContent(doc.getContent() + "blabla");//Version 1
		Document before2Versions = doc.clone();
		doc.commitVersion();
		assertEquals(doc.getVersionId(), 2);

		doc.setContent(doc.getContent() + "blablabla"); //Version 2
		doc.commitVersion();
		assertEquals(doc.getVersionId(), 3);

		doc.setContent(doc.getContent() + " lalala");

		doc.previousVersion();
		doc.previousVersion();

		assertEquals(doc, before2Versions);
	}

	@Test(expected = NoPreviousVersionException.class)
	public void keepTwoAndGoBackThree() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.setContent(doc.getContent() + "something");
		doc.commitVersion();

		doc.setContent(doc.getContent() + "something");
		doc.commitVersion();

		doc.previousVersion();
		doc.previousVersion();
		doc.previousVersion();
	}

	@Test
	public void previousVersions() {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.setContent(doc.getContent() + "something");

		assertTrue(doc.getPreviousVersions().isEmpty());

		doc.commitVersion();
		assertTrue(doc.getPreviousVersions().size() == 1);
	}
}
