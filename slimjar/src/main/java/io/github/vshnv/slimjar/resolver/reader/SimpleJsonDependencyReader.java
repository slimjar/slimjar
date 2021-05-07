package io.github.vshnv.slimjar.resolver.reader;

import io.github.vshnv.slimjar.relocation.RelocationRule;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;
import io.github.vshnv.slimjar.resolver.data.Mirror;
import io.github.vshnv.slimjar.resolver.data.Repository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

public class SimpleJsonDependencyReader implements DependencyReader {
    private final JSONParser parser;

    public SimpleJsonDependencyReader(JSONParser parser) {
        this.parser = parser;
    }

    @Override
    public DependencyData read(InputStream inputStream) {
        try {
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final JSONObject jsonObject = (JSONObject) parser.parse(inputStreamReader);
            final JSONArray mirrorsArray = (JSONArray) jsonObject.get("mirrors");
            final JSONArray repositoriesArray = (JSONArray) jsonObject.get("repositories");
            final JSONArray dependenciesArray = (JSONArray) jsonObject.get("dependencies");
            final JSONArray relocationsArray = (JSONArray) jsonObject.get("relocations");
            final Collection<Mirror> mirrors = parseMirrors(mirrorsArray);
            final Collection<Repository> repositories = parseRepositoryList(repositoriesArray);
            final Collection<Dependency> dependencies = parseDependencyList(dependenciesArray);
            final Collection<RelocationRule> relocations = parseRelocations(relocationsArray);
            return new DependencyData(mirrors, repositories, dependencies, relocations);
        } catch (Exception ex) {
            return null;
        }
    }

    private static Collection<RelocationRule> parseRelocations(JSONArray dependenciesArray) {
        final Collection<RelocationRule> relocationRules = new HashSet<>();
        for (final Object elementObj : dependenciesArray) {
            final JSONObject element = (JSONObject) elementObj;
            final String originalPattern = (String) element.get("originalPackagePattern");
            final String relocatedPattern = (String) element.get("relocatedPackagePattern");
            final JSONArray exclusionsArray = (JSONArray) element.get("exclusions");
            final JSONArray inclusionsArray = (JSONArray) element.get("inclusions");
            final Collection<String> exclusions = parseStringArray(exclusionsArray);
            final Collection<String> inclusions = parseStringArray(inclusionsArray);
            final RelocationRule relocationRule = new RelocationRule(originalPattern, relocatedPattern, exclusions, inclusions);
            relocationRules.add(relocationRule);
        }
        return relocationRules;
    }

    private static Collection<String> parseStringArray(JSONArray array) {
        final Collection<String> result = new HashSet<>();
        for (final Object element : array) {
            result.add((String) element);
        }
        return result;
    }

    private static Collection<Dependency> parseDependencyList(JSONArray dependenciesArray) {
        final Collection<Dependency> result = new HashSet<>();
        for (final Object element : dependenciesArray) {
            result.add(parseDependency((JSONObject) element));
        }
        return result;
    }

    private static Dependency parseDependency(JSONObject dependency) {
        final String groupId = (String) dependency.get("groupId");
        final String artifactId = (String) dependency.get("artifactId");
        final String version = (String) dependency.get("version");
        final String snapshotId = (String) dependency.get("snapshotId");
        final Collection<Dependency> transitive = parseDependencyList((JSONArray) dependency.get("transitive"));
        return new Dependency(groupId, artifactId, version, snapshotId, transitive);
    }

    private static Collection<Repository> parseRepositoryList(JSONArray repositoriesArray) {
        final Collection<Repository> result = new HashSet<>();
        for (final Object element : repositoriesArray) {
            try {
                result.add(parseRepository((JSONObject) element));
            } catch (MalformedURLException e) {
                // Ignored malformed repository
            }
        }
        return result;
    }

    private static Repository parseRepository(JSONObject repository) throws MalformedURLException {
        final String urlStr = (String) repository.get("url");
        final URL url = new URL(urlStr);
        return new Repository(url);
    }

    private static Collection<Mirror> parseMirrors(JSONArray mirrorsArray) {
        final Collection<Mirror> result = new HashSet<>();
        for (final Object element : mirrorsArray) {
            try {
                result.add(parseMirror((JSONObject)element));
            } catch (MalformedURLException e) {
                // Ignored malformed repository
            }
        }
        return result;
    }

    private static Mirror parseMirror(JSONObject mirror) throws MalformedURLException {
        final String mirroringStr = (String) mirror.get("mirroring");
        final String originalStr = (String) mirror.get("original");
        final URL mirroring = new URL(mirroringStr);
        final URL original = new URL(originalStr);
        return new Mirror(mirroring, original);
    }
}
