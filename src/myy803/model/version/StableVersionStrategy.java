package myy803.model.version;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import myy803.DocumentManager;
import myy803.commons.Files;
import myy803.model.Document;

public class StableVersionStrategy implements VersionStrategy {
	private static final long serialVersionUID = -5795326622847220821L;
	private static final Pattern VERSION_FILES_PATTERN = Pattern.compile("version(\\d*)");

	public StableVersionStrategy() {

	}

	@Override
	public void rollbackToPreviousVersion(Document document) throws NoPreviousVersionException {
		if (document.getVersionId() == 1) {
			throw new NoPreviousVersionException("Document " + document.getName() + " has no previous versions.");
		}
		File documentFolder = getDocumentVersionsFolder(document);
		File[] documents = documentFolder.listFiles(f -> f.getName().matches(VERSION_FILES_PATTERN.pattern()));
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
		if (document.getVersionId() == 1) {
			return Collections.emptyList();
		}
		File documentFolder = getDocumentVersionsFolder(document);
		File[] documents = documentFolder.listFiles(f -> isVersionFileWithIdSmallerThan(f, document.getVersionId()));
		if (documents.length == 0) {
			return Collections.emptyList();
		}
		List<File> docFileList = Arrays.asList(documents);
		return docFileList.stream().map(t -> {
			try {
				return DocumentManager.INSTANCE.loadDocument(t);
			} catch (ClassNotFoundException | IOException e) {
				System.err.println("Cannot load document in order to get previous versions.");
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	private boolean isVersionFileWithIdSmallerThan(File file, int id) {
		Matcher m = VERSION_FILES_PATTERN.matcher(file.getName());
		if (m.matches()) {
			String group1 = m.group(1);
			int vId = Integer.parseInt(group1);
			return vId < id;
		}
		return false;
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
		File documentFolder = new File(Files.VERSIONS_FOLDER, String.valueOf(doc.hashCode()));
		if (!documentFolder.exists())
			documentFolder.mkdirs();
		return documentFolder;
	}

}
