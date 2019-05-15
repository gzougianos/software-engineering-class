package myy803;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import myy803.model.Document;
import myy803.model.DocumentType;
import myy803.model.version.NoPreviousVersionException;
import myy803.model.version.VersionStrategyType;

public class StableVersionStrategyTests {
	private VersionsManager versionsManager = VersionsManager.INSTANCE;

	@Test(expected = NoPreviousVersionException.class)
	public void neverKeptAVersion() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		versionsManager.setStrategy(doc, VersionStrategyType.STABLE);
		VersionsManager.INSTANCE.rollToPreviousVersion(doc);
	}

	@Test
	public void main() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		versionsManager.setStrategy(doc, VersionStrategyType.STABLE);
		doc.setContent(doc.getContent() + "something");

		Document before1Version = doc.clone();

		versionsManager.commitVersion(doc);
		doc.setContent(doc.getContent() + "else");//ends in somethingelse
		versionsManager.rollToPreviousVersion(doc);

		assertEquals(before1Version, doc);
		assertEquals(doc.getVersionId(), 1);

		doc.setContent(doc.getContent() + "blabla");//Version 1
		Document before2Versions = doc.clone();
		versionsManager.commitVersion(doc);
		assertEquals(doc.getVersionId(), 2);

		doc.setContent(doc.getContent() + "blablabla"); //Version 2
		versionsManager.commitVersion(doc);
		assertEquals(doc.getVersionId(), 3);

		doc.setContent(doc.getContent() + " lalala");

		versionsManager.rollToPreviousVersion(doc);
		versionsManager.rollToPreviousVersion(doc);

		assertEquals(doc, before2Versions);
	}

	@Test(expected = NoPreviousVersionException.class)
	public void keepTwoAndGoBackThree() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		versionsManager.setStrategy(doc, VersionStrategyType.STABLE);
		doc.setContent(doc.getContent() + "something");
		versionsManager.commitVersion(doc);

		doc.setContent(doc.getContent() + "something");
		versionsManager.commitVersion(doc);

		versionsManager.rollToPreviousVersion(doc);
		versionsManager.rollToPreviousVersion(doc);
		versionsManager.rollToPreviousVersion(doc);
	}

	@Test
	public void previousVersions() {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		versionsManager.setStrategy(doc, VersionStrategyType.STABLE);
		doc.setContent(doc.getContent() + "something");

		assertTrue(versionsManager.getPreviousVersions(doc).isEmpty());

		versionsManager.commitVersion(doc);
		assertTrue(versionsManager.getPreviousVersions(doc).size() == 1);
	}
}
