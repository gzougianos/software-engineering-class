package myy803.model.version;

import java.util.List;

import myy803.model.Document;

public class VolatileVersionStrategy implements VersionStrategy {
	private static final long serialVersionUID = 71963449730569574L;

	@Override
	public void previousVersion(Document document) throws NoPreviousVersionException {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Document> getPreviousVersions(Document document) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveVersion(Document document) {
		// TODO Auto-generated method stub

	}

	@Override
	public void cleanHistory(Document document) {
		// TODO Auto-generated method stub

	}

}
