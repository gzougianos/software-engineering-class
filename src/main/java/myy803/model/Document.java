package myy803.model;

import java.io.File;
import java.io.Serializable;

public class Document implements Serializable {
	private static final long serialVersionUID = 5357144600210787382L;
	public static final String FILE_EXTENSION = ".lat";
	private String author;
	private long lastModifiedDate;
	private String copyright;
	private int versionId;
	private String content;
	private DocumentType documentType;
	private File path;
	private boolean saved;

	public Document(String author, long lastModifiedDate, String copyright, int versionId, String content,
			DocumentType documentType, File path) {
		this.author = author;
		this.lastModifiedDate = lastModifiedDate;
		this.copyright = copyright;
		this.versionId = versionId;
		this.content = content;
		this.documentType = documentType;
		this.path = path;
		this.saved = path.exists();
	}

	public Document(String author, long lastModifiedDate, String copyright, int versionId, String content,
			DocumentType documentType, String path) {
		this(author, lastModifiedDate, copyright, versionId, content, documentType, new File(path));
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public long getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(long lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getCopyright() {
		return copyright;
	}

	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public DocumentType getDocumentType() {
		return documentType;
	}

	public void setDocumentType(DocumentType documentType) {
		this.documentType = documentType;
	}

	public String getName() {
		return getPath().getName().replaceAll(FILE_EXTENSION, "");
	}

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	public boolean isSaved() {
		return saved;
	}

	public void setSaved(boolean saved) {
		this.saved = saved;
	}

	@Override
	public Document clone() {
		Document doc = new Document(this.getAuthor(), this.getLastModifiedDate(), this.getCopyright(), this.getVersionId(),
				this.getContent(), this.getDocumentType(), this.getPath());
		return doc;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Document) {
			Document doc = (Document) obj;
			boolean equals = true;
			equals &= doc.getContent().equals(this.getContent());
			equals &= doc.getDocumentType() == this.getDocumentType();
			equals &= doc.getVersionId() == this.getVersionId();
			equals &= doc.getLastModifiedDate() == this.getLastModifiedDate();
			equals &= doc.getAuthor().equals(this.getAuthor());
			equals &= doc.getCopyright().equals(this.getCopyright());
			equals &= doc.getPath().equals(this.getPath());
			return equals;
		}
		return super.equals(obj);
	}
}
