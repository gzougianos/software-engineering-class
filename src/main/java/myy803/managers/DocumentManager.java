package myy803.managers;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import myy803.commons.Files;
import myy803.model.Document;
import myy803.model.DocumentType;

public enum DocumentManager {
	INSTANCE;
	private static final String TEMPLATES_PACKAGE = "/myy803/resources";
	private final List<Document> documents = new ArrayList<>();
	private final HashMap<DocumentType, Document> prototypes = new HashMap<>();

	private DocumentManager() {
		initPrototypes();
	}

	private void initPrototypes() {
		for (DocumentType docType : DocumentType.values()) {
			Document doc = new Document("", System.currentTimeMillis(), "", 1, "", docType, "");
			doc.setContent(loadTemplate(docType));
			prototypes.put(docType, doc);
		}
	}

	private String loadTemplate(DocumentType docType) {
		String name = docType.toString().toLowerCase();
		String fileName = TEMPLATES_PACKAGE + File.separator + name + "_template.txt";
		InputStream is = DocumentManager.class.getResourceAsStream(fileName);
		if (is == null) {
			System.err.println("No template found for document type: " + name);
			return "";
		}
		try (Scanner sc = new Scanner(is)) {
			sc.useDelimiter("\\A");
			String textFromTemplate = sc.next();
			return textFromTemplate;
		}
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public Document createDocument(DocumentType documentType) {
		String name = getNewName();
		File path = new File(Files.TEMP, name);
		Document protype = prototypes.get(documentType);
		Document clone = protype.clone();
		clone.setPath(path);
		return clone;
	}

	private String getNewName() {
		if (documents.isEmpty())
			return "unsaved.tex";
		return String.format("unsaved%d.tex", documents.size());
	}
}
