package myy803;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import myy803.model.TextCommand;

public enum TextCommandManager {
	INSTANCE;
	private static final String COMMANDS_FILE = "/myy803/resources/commands.xml";
	private static final String CURSOR_INDEX_TAG = "%cursor%";
	private List<TextCommand> commands;

	private TextCommandManager() {
		loadCommands();
	}

	private void loadCommands() {
		List<TextCommand> commandList = new ArrayList<TextCommand>();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(TextCommandManager.class.getResourceAsStream(COMMANDS_FILE));
			doc.getDocumentElement().normalize();
			NodeList nList = doc.getElementsByTagName("command");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					TextCommand c = buildCommandFromElement(eElement);
					commandList.add(c);
				}
			}
		} catch (SAXException | ParserConfigurationException | IOException e) {
			System.err.println("Error reading XML file with commands. Path: " + COMMANDS_FILE);
			e.printStackTrace();
		}
		commands = Collections.unmodifiableList(commandList);
	}

	private TextCommand buildCommandFromElement(Element element) {
		String name = element.getAttribute("name");
		String content = getTagValue(element, "content").replaceAll("\t", "").replaceAll("\\\\n", System.lineSeparator());
		String description = getTagValue(element, "description");
		String allowedDocuments = getTagValue(element, "allowed_documents");
		String disallowedDocuments = getTagValue(element, "disallowed_documents");
		int cursorIndex = content.indexOf(CURSOR_INDEX_TAG);
		content = content.replaceAll(CURSOR_INDEX_TAG, "");
		return new TextCommand(name, content, description, allowedDocuments, disallowedDocuments, cursorIndex);
	}

	private String getTagValue(Element element, String tag) {
		NodeList nodeList = element.getElementsByTagName(tag);
		if (nodeList != null) {
			Node node = nodeList.item(0);
			if (node != null)
				return node.getTextContent().trim();
		}
		return "";
	}

	public List<TextCommand> getCommands() {
		return commands;
	}
}