package io.github.vshnv.slimjar.parser;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public final class XMLBuildFileParser implements BuildFileParser {
    private final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();

    public BuildData parse(InputStream inputStream) throws BuildFileParseException {
        try {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputStream);
            return fetchBuildData(doc);
        } catch (SAXException | IOException | ParserConfigurationException exception) {
            throw new BuildFileParseException(exception);
        }
    }

    private BuildData fetchBuildData(Document document) {
        NodeList dependencies = document.getElementsByTagName("dependencies");
        NodeList repositories = document.getElementsByTagName("repositories");
        
    }
}
