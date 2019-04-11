package myy803;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import myy803.commons.Files;
import myy803.model.Document;
import myy803.model.DocumentType;

public enum DocumentManager {
	INSTANCE;
	private static final String TEMPLATES_PACKAGE = "/myy803/resources/templates";
	private final List<Document> documents = new ArrayList<>();
	private final HashMap<DocumentType, Document> prototypes = new HashMap<>();

	private DocumentManager() {
		initPrototypes();
	}

	private void initPrototypes() {
		for (DocumentType docType : DocumentType.values()) {
			Document doc = new Document(System.currentTimeMillis(), "", 1, "", docType, "");
			doc.setContent(loadTemplate(docType));
			prototypes.put(docType, doc);
		}
	}

	private String loadTemplate(DocumentType docType) {
		String name = docType.toString().toLowerCase();
		String fileName = TEMPLATES_PACKAGE + File.separator + name + ".txt";
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
		File path = new File(Files.USER_HOME, name);
		Document protype = prototypes.get(documentType);
		Document clone = protype.clone();
		clone.setPath(path);
		return clone;
	}

	public void saveDocument(Document doc) throws IOException {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(doc.getPath()))) {
			oos.writeObject(doc);
		}
	}

	public Document loadDocument(File path) throws ClassNotFoundException, IOException {
		if (!path.exists())
			return null;
		try (ObjectInputStream oos = new ObjectInputStream(new FileInputStream(path))) {
			return (Document) oos.readObject();
		}
	}

	private String getNewName() {
		if (documents.isEmpty())
			return "unsaved" + Document.FILE_EXTENSION;
		Document lastDoc = documents.get(documents.size() - 1);
		String lastId = lastDoc.getPath().getName().replaceAll("[a-zA-Z.]", "");
		return lastId.isEmpty() ? "unsaved1" + Document.FILE_EXTENSION
				: String.format("unsaved%d" + Document.FILE_EXTENSION, Integer.parseInt(lastId) + 1);
	}
}
