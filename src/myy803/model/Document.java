package myy803.model;

import java.io.File;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import myy803.model.version.NoPreviousVersionException;
import myy803.model.version.StableVersionStrategy;
import myy803.model.version.VersionStrategy;

public class Document implements Serializable {
	private static final long serialVersionUID = 5357144600210787382L;
	public static final String FILE_EXTENSION = ".lat";
	private static final String AUTHOR_REGEX = "\\\\author\\{(.*?)\\}";
	private long lastModifiedDate;
	private String copyright;
	private int versionId;
	private String content;
	private DocumentType documentType;
	private File path;
	private boolean saved;
	private VersionStrategy versionStrategy;

	public Document(long lastModifiedDate, String copyright, int versionId, String content, DocumentType documentType, File path) {
		this.lastModifiedDate = lastModifiedDate;
		this.copyright = copyright;
		this.versionId = versionId;
		this.content = content;
		this.documentType = documentType;
		this.path = path;
		this.saved = path.exists();
		setVersionStrategy(new StableVersionStrategy());
	}

	public Document(long lastModifiedDate, String copyright, int versionId, String content, DocumentType documentType, String path) {
		this(lastModifiedDate, copyright, versionId, content, documentType, new File(path));
	}

	public String getAuthor() {
		Matcher m = Pattern.compile(AUTHOR_REGEX).matcher(getContent());
		if (m.find()) {
			String group = m.group(1);
			String[] authors = group.split("\\\\and");
			if (group.isEmpty() || authors.length == 0)
				return "-";
			StringBuilder sb = new StringBuilder();
			for (String author : authors) {
				sb.append(author.trim());
				sb.append(", ");
			}
			String returnVal = sb.toString();
			//Cut the last comma and space from the end
			returnVal = returnVal.substring(0, returnVal.length() - ", ".length());
			return returnVal;
		}
		return "-";
	}

	public void setAuthor(String author) {
		setAuthor(author == null ? (String[]) null : new String[] { author });
	}

	public void setAuthor(String... authors) {
		if (hasAuthor()) {
			StringBuilder sb = new StringBuilder("\\\\author\\{");
			if (authors == null || authors.length == 0) {
				sb.append("\\}");
				setContent(getContent().replaceAll(AUTHOR_REGEX, sb.toString()));
				return;
			}
			for (String author : authors) {
				if (author.isEmpty())
					continue;
				sb.append(author.trim());
				sb.append(" ");
				sb.append("\\\\and");
				sb.append(" ");
			}
			if (sb.toString().endsWith("\\and "))
				sb = new StringBuilder(sb.substring(0, sb.toString().length() - " \\\\and ".length()));
			sb.append("\\}");
			setContent(getContent().replaceAll(AUTHOR_REGEX, sb.toString()));
		} else {
			StringBuilder sb = new StringBuilder("\\author{");
			for (String author : authors) {
				if (author.isEmpty())
					continue;
				sb.append(author.trim());
				sb.append(" ");
				sb.append("\\and");
				sb.append(" ");
			}
			if (sb.toString().endsWith("\\and "))
				sb = new StringBuilder(sb.substring(0, sb.toString().length() - " \\and ".length()));
			sb.append("}");
			setContent(getContent() + System.lineSeparator() + sb.toString());
		}
	}

	private boolean hasAuthor() {
		return Pattern.compile(AUTHOR_REGEX).matcher(getContent()).find();
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

	public void goToPreviousVersion() throws NoPreviousVersionException {
		versionStrategy.previousVersion(this);
	}

	public void keepVersion() {
		versionStrategy.saveVersion(this);
	}

	public void setVersionStrategy(VersionStrategy versionStrategy) {
		this.versionStrategy = versionStrategy;
	}

	public void copyPropertiesFrom(Document doc2) {
		setContent(doc2.getContent());
		setLastModifiedDate(doc2.getLastModifiedDate());
		setSaved(true);
		setVersionId(doc2.getVersionId());
		setDocumentType(doc2.getDocumentType()); //Can this get changed?
		setCopyright(doc2.getCopyright());
		setAuthor(doc2.getAuthor().split(", "));
	}

	@Override
	public Document clone() {
		Document doc = new Document(this.getLastModifiedDate(), this.getCopyright(), this.getVersionId(), this.getContent(),
				this.getDocumentType(), this.getPath());
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
