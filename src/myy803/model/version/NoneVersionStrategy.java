package myy803.model.version;

import java.util.Collections;
import java.util.List;

import myy803.model.Document;

public class NoneVersionStrategy implements VersionStrategy {

	@Override
	public void rollbackToPreviousVersion(Document document) throws NoPreviousVersionException {

	}

	@Override
	public List<Document> getPreviousVersions(Document document) {
		return Collections.emptyList();
	}

	@Override
	public void saveVersion(Document document) {

	}

	@Override
	public VersionStrategyType type() {
		return VersionStrategyType.NONE;
	}

}
