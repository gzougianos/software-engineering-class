package myy803.model;

import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public enum Command {
	//@formatter:off
	CHAPTER,
	SECTION,
	SUBSECTION,
	SUBSUBSECTION,
	BEGIN_ITEMIZE,
	BEGIN_ENUMERATE,
	BEGIN_TABLE,
	BEGIN_FIGURE,
	;
	//@formatter:on
	private static final String COMMANDS_FILE = "/myy803/resources/commands.xml";
	private static final String CURSOR_INDEX_TAG = "%cursor%";
	private String content;
	private String description;
	private int cursorIndex;
	private String allowedDocuments;
	private String disallowedDocuments;

	private Command() {
		String name = toString();
		Element element = CommandHolder.elements.get(name);
		if (element == null) {
			System.err.println(name + " command not found in " + COMMANDS_FILE + ".");
			return;
		}
		this.content = getTagValue(element, "content").trim();
		this.description = getTagValue(element, "description");
		this.allowedDocuments = getTagValue(element, "allowed_documents");
		this.disallowedDocuments = getTagValue(element, "disallowed_documents");
		this.cursorIndex = content.indexOf(CURSOR_INDEX_TAG);
		this.content = content.replaceAll(CURSOR_INDEX_TAG, "");
		this.content = content.replaceAll("\t", "");
		this.content = content.replaceAll("\\\\n", System.lineSeparator());
	}

	public String getContent() {
		return this.content;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasCursor() {
		return this.cursorIndex >= 0;
	}

	public int getCursorIndex() {
		return cursorIndex;
	}

	public boolean allowsType(DocumentType type) {
		if (disallowedDocuments.trim().toLowerCase().contains(type.toString().toLowerCase()))
			return false;
		return allowedDocuments.trim().equalsIgnoreCase("all")
				|| allowedDocuments.trim().toLowerCase().contains(type.toString().toLowerCase());
	}

	private String getTagValue(Element element, String tag) {
		NodeList nodeList = element.getElementsByTagName(tag);
		if (nodeList != null) {
			Node node = nodeList.item(0);
			if (node != null)
				return node.getTextContent();
		}
		return "";
	}

	private static final class CommandHolder {
		private static final HashMap<String, Element> elements = new HashMap<>();
		static {
			loadXMLNodeList();
		}

		private static void loadXMLNodeList() {
			try {
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(Command.class.getResourceAsStream(COMMANDS_FILE));
				doc.getDocumentElement().normalize();
				NodeList nList = doc.getElementsByTagName("command");
				for (int temp = 0; temp < nList.getLength(); temp++) {
					Node nNode = nList.item(temp);
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						String name = eElement.getAttribute("name");
						elements.put(name, eElement);
					}
				}
			} catch (SAXException | ParserConfigurationException | IOException e) {
				System.err.println("Error reading XML file with commands. Path: " + COMMANDS_FILE);
				e.printStackTrace();
			}
		}
	}
}
