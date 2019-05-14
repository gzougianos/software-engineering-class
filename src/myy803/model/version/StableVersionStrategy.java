package myy803.model.version;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import myy803.DocumentManager;
import myy803.commons.Files;
import myy803.model.Document;

public class StableVersionStrategy implements VersionStrategy {
	private static final long serialVersionUID = -5795326622847220821L;

	public StableVersionStrategy() {

	}

	@Override
	public void previousVersion(Document document) throws NoPreviousVersionException {
		if (document.getVersionId() == 1) {
			throw new NoPreviousVersionException("Document " + document.getName() + " has no previous versions.");
		}
		File documentFolder = getDocumentVersionsFolder(document);
		File[] documents = documentFolder.listFiles(f -> f.getName().matches("version\\d*"));
		if (documents.length == 0) {
			throw new NoPreviousVersionException("Document's previous versions cannot be found in disk.");
		}
		document.setVersionId(document.getVersionId() - 1);
		List<File> documentsList = Arrays.asList(documents);
		documentsList.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
		File lastVersion = documentsList.stream()
				.filter(f -> f.getName().toLowerCase().endsWith(String.valueOf(document.getVersionId()))).findFirst()
				.orElse(null);
		try {
			Document previousDocument = DocumentManager.INSTANCE.loadDocument(lastVersion);
			document.copyPropertiesFrom(previousDocument);
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Cannot load document from previous version.");
			e.printStackTrace();
		}

	}

	@Override
	public List<Document> getPreviousVersions(Document document) {
		return Collections.emptyList();
	}

	@Override
	public void saveVersion(Document doc) {
		Document version = doc.clone();
		version.setPath(new File(getDocumentVersionsFolder(doc), "version" + version.getVersionId()));
		try {
			DocumentManager.INSTANCE.saveDocument(version);
		} catch (IOException e) {
			System.out.println("Cannot save version for document: " + doc.getPath() + " in path:" + version.getPath());
			e.printStackTrace();
		}
		doc.setVersionId(doc.getVersionId() + 1);

	}

	private File getDocumentVersionsFolder(Document doc) {
		File documentFolder = new File(Files.VERSIONS_FOLDER, doc.getName());
		if (!documentFolder.exists())
			documentFolder.mkdirs();
		return documentFolder;
	}
}
