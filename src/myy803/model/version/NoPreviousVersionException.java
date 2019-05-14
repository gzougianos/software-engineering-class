package myy803.model.version;

public class NoPreviousVersionException extends Exception {
	private static final long serialVersionUID = -6879955487644565402L;

	public NoPreviousVersionException(String msg) {
		super(msg);
	}
}
