package myy803.model.version;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import myy803.model.Document;

public class VolatileVersionStrategy implements VersionStrategy {
	private static final long serialVersionUID = 71963449730569574L;
	private HashMap<Integer, Document> versions;

	public VolatileVersionStrategy() {
		versions = new HashMap<>();
	}

	@Override
	public void rollbackToPreviousVersion(Document document) throws NoPreviousVersionException {
		int version = document.getVersionId() - 1;
		Document doc = versions.get(version);
		if (doc == null) {
			throw new NoPreviousVersionException(
					"VolatileVersioning: Previous version cannot be found for document " + document.getName());
		}
		document.copyPropertiesFrom(doc);
	}

	@Override
	public List<Document> getPreviousVersions(Document document) {
		return versions.values().stream().sorted((d1, d2) -> d1.getVersionId() - d2.getVersionId()).collect(Collectors.toList());
	}

	@Override
	public void saveVersion(Document document) {
		versions.put(document.getVersionId(), document.clone());
		document.setVersionId(document.getVersionId() + 1);
	}

	@Override
	public VersionStrategyType type() {
		return VersionStrategyType.VOLATILE;
	}

}
