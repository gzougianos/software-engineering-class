package myy803;

import myy803.model.version.StableVersionStrategy;
import myy803.model.version.VersionStrategy;
import myy803.model.version.VersionStrategyType;
import myy803.model.version.VolatileVersionStrategy;

public class VersionStrategyFactory {
	public static VersionStrategy createStrategy(VersionStrategyType type) {
		switch (type) {
			case STABLE:
				return new StableVersionStrategy();
			case VOLATILE:
				return new VolatileVersionStrategy();
			default:
				return null;

		}
	}
}
