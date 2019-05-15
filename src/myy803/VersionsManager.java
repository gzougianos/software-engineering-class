package myy803;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import myy803.model.Document;
import myy803.model.version.NoPreviousVersionException;
import myy803.model.version.VersionStrategy;
import myy803.model.version.VersionStrategyType;

public enum VersionsManager {
	INSTANCE;
	private HashMap<Document, VersionStrategy> docStrats;

	private VersionsManager() {
		docStrats = new HashMap<>();
	}

	public void setStrategy(Document doc, VersionStrategyType strategyType) {
		if (strategyType == null)
			strategyType = VersionStrategyType.NONE;
		VersionStrategy vs = VersionStrategyFactory.createStrategy(strategyType);
		docStrats.put(doc, vs);
	}

	public VersionStrategyType getStrategy(Document doc) {
		if (!docStrats.containsKey(doc))
			return VersionStrategyType.NONE;
		return docStrats.get(doc).type();
	}

	public void disableStrategy(Document doc) {
		this.setStrategy(doc, null);
	}

	public List<Document> getPreviousVersions(Document doc) {
		if (!docStrats.containsKey(doc))
			return Collections.emptyList();
		return docStrats.get(doc).getPreviousVersions(doc);
	}

	public void rollToPreviousVersion(Document doc) throws NoPreviousVersionException {
		if (!docStrats.containsKey(doc))
			return;
		docStrats.get(doc).rollbackToPreviousVersion(doc);
	}

	public void commitVersion(Document doc) {
		if (!docStrats.containsKey(doc))
			return;
		docStrats.get(doc).saveVersion(doc);
	}

}
