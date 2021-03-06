package myy803.model.version;

import java.io.Serializable;
import java.util.List;

import myy803.model.Document;

public interface VersionStrategy extends Serializable {
	public void rollbackToPreviousVersion(Document document) throws NoPreviousVersionException;

	public List<Document> getPreviousVersions(Document document);

	public void saveVersion(Document document);

	public VersionStrategyType type();

}
