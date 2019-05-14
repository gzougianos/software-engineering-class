package myy803;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import myy803.commons.Files;
import myy803.model.Document;
import myy803.model.DocumentType;
import myy803.model.version.NoPreviousVersionException;

public class VersionStrategiesTests {
	@Test(expected = NoPreviousVersionException.class)
	public void neverKeptAVersion() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.goToPreviousVersion();
	}

	@Test
	public void keepOne() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.setContent(doc.getContent() + "something");

		Document before1Version = doc.clone();
		doc.keepVersion();
		doc.setContent(doc.getContent() + "else");//ends in somethingelse
		doc.goToPreviousVersion();

		assertEquals(before1Version, doc);
		assertEquals(doc.getVersionId(), 1);

		doc.setContent(doc.getContent() + "blabla");//Version 1
		Document before2Versions = doc.clone();
		doc.keepVersion();
		assertEquals(doc.getVersionId(), 2);

		doc.setContent(doc.getContent() + "blablabla"); //Version 2
		doc.keepVersion();
		assertEquals(doc.getVersionId(), 3);

		doc.goToPreviousVersion();
		doc.goToPreviousVersion();

		assertEquals(doc, before2Versions);
	}

	@Test(expected = NoPreviousVersionException.class)
	public void keepTwoAndGoBackThree() throws NoPreviousVersionException {
		Document doc = DocumentManager.INSTANCE.createDocument(DocumentType.ARTICLE);
		doc.setContent(doc.getContent() + "something");
		doc.keepVersion();

		doc.setContent(doc.getContent() + "something");
		doc.keepVersion();

		doc.goToPreviousVersion();
		doc.goToPreviousVersion();
				doc.goToPreviousVersion();
	}

	@Before
	public void before() {
		Files.initialize();//clean version history
	}
}
