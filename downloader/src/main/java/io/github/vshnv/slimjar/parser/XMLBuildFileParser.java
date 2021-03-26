package io.github.vshnv.slimjar.parser;

import io.github.vshnv.slimjar.data.Dependency;
import io.github.vshnv.slimjar.data.Repository;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.font.EAttribute;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class XMLBuildFileParser implements BuildFileParser {
    private static final Collector<Node, ?, Map<String, String>> toNodeMap = Collectors.toMap(
            Node::getNodeName,
            Node::getNodeValue
    );
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
        NodeList dependencyNode = document.getElementsByTagName("dependencies");
        NodeList repositoryNode = document.getElementsByTagName("repositories");
        Collection<Dependency> dependencies = parseDependencies(dependencyNode);
        Collection<Repository> repositories = parseRepositories(repositoryNode);
        return new BuildData(dependencies, repositories);
    }

    private Collection<Dependency> parseDependencies(NodeList node) {
        if (node == null) {
            return Collections.emptySet();
        }
        return streamOf(node)
                .map(Node::getChildNodes)
                .map(dependency -> streamOf(dependency).collect(toNodeMap))
                .map(Dependency::fromMap)
                .collect(Collectors.toSet());
    }

    private Collection<Repository> parseRepositories(NodeList node) {
        if (node == null) {
            return Collections.emptySet();
        }
        return streamOf(node)
                .map(Node::getChildNodes)
                .map(repository -> streamOf(repository).collect(toNodeMap))
                .map(Repository::fromMap)
                .collect(Collectors.toSet());
    }

    private Stream<Node> streamOf(NodeList nodeList) {
        Stream.Builder<Node> stream = Stream.builder();
        int nodeCount = nodeList.getLength();
        for (int i = 0; i < nodeCount; i++) {
            stream.add(nodeList.item(i));
        }
        return stream.build();
    }
}
