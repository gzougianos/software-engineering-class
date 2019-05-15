package myy803.model.version;

public enum VersionStrategyType {
	STABLE, VOLATILE, NONE;

	public String getName() {
		String firstLetter = toString().substring(0, 1);
		return firstLetter.toUpperCase() + toString().toLowerCase().substring(1, toString().length());
	}
}
