package myy803.model;

public class Command {
	private String name;
	private String content;
	private String description;
	private String allowedTypes;
	private String disallowedTypes;
	private int cursorIndex;

	public Command() {
		this("", "", "", "", "", -1);
	}

	public Command(String name, String content, String description, String allowedTypes, String disallowedTypes,
			int cursorIndex) {
		super();
		this.name = name;
		this.content = content;
		this.description = description;
		this.allowedTypes = allowedTypes;
		this.disallowedTypes = disallowedTypes;
		this.cursorIndex = cursorIndex;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCursorIndex() {
		return cursorIndex;
	}

	public void setCursorIndex(int cursorIndex) {
		this.cursorIndex = cursorIndex;
	}

	public String getAllowedTypes() {
		return allowedTypes;
	}

	public void setAllowedTypes(String allowedTypes) {
		this.allowedTypes = allowedTypes;
	}

	public String getDisallowedTypes() {
		return disallowedTypes;
	}

	public void setDisallowedTypes(String disallowedTypes) {
		this.disallowedTypes = disallowedTypes;
	}

	public boolean allowsType(DocumentType type) {
		if (getDisallowedTypes().trim().toLowerCase().contains(type.toString().toLowerCase()))
			return false;
		return getAllowedTypes().trim().equalsIgnoreCase("all")
				|| getAllowedTypes().trim().toLowerCase().contains(type.toString().toLowerCase());
	}

	public boolean hasCursor() {
		return cursorIndex >= 0;
	}

	@Override
	public String toString() {
		return getName();
	}
}
