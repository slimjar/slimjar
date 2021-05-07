package io.github.vshnv.slimjar.resolver.reader;

import com.google.gson.*;
import io.github.vshnv.slimjar.relocation.RelocationRule;
import io.github.vshnv.slimjar.resolver.data.Dependency;
import io.github.vshnv.slimjar.resolver.data.DependencyData;
import io.github.vshnv.slimjar.resolver.data.Mirror;
import io.github.vshnv.slimjar.resolver.data.Repository;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashSet;

public class SimpleJsonDependencyReader implements DependencyReader {
    @Override
    public DependencyData read(InputStream inputStream) {
        final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        final JsonObject element = JsonParser.parseReader(inputStreamReader).getAsJsonObject();
        final JsonArray mirrorsArray = element.getAsJsonArray("mirrors");
        final JsonArray repositoriesArray = element.getAsJsonArray("repositories");
        final JsonArray dependenciesArray = element.getAsJsonArray("dependencies");
        final JsonArray relocationsArray = element.getAsJsonArray("relocations");
        final Collection<Mirror> mirrors = parseMirrors(mirrorsArray);
        final Collection<Repository> repositories = parseRepositoryList(repositoriesArray);
        final Collection<Dependency> dependencies = parseDependencyList(dependenciesArray);
        final Collection<RelocationRule> relocations = parseRelocations(relocationsArray);
        return new DependencyData(mirrors, repositories, dependencies, relocations);
    }

    private static Collection<RelocationRule> parseRelocations(JsonArray dependenciesArray) {
        final Collection<RelocationRule> relocationRules = new HashSet<>();
        for (final JsonElement element : dependenciesArray) {
            final JsonObject elementObject = element.getAsJsonObject();
            final String originalPattern = elementObject.get("originalPackagePattern").getAsString();
            final String relocatedPattern = elementObject.get("relocatedPackagePattern").getAsString();
            final JsonArray exclusionsArray = elementObject.getAsJsonArray("exclusions");
            final JsonArray inclusionsArray = elementObject.getAsJsonArray("inclusions");
            final Collection<String> exclusions = parseStringArray(exclusionsArray);
            final Collection<String> inclusions = parseStringArray(inclusionsArray);
            final RelocationRule relocationRule = new RelocationRule(originalPattern, relocatedPattern, exclusions, inclusions);
            relocationRules.add(relocationRule);
        }
        return relocationRules;
    }

    private static Collection<String> parseStringArray(JsonArray array) {
        final Collection<String> result = new HashSet<>();
        for (final JsonElement element : array) {
            result.add(element.getAsString());
        }
        return result;
    }

    private static Collection<Dependency> parseDependencyList(JsonArray dependenciesArray) {
        final Collection<Dependency> result = new HashSet<>();
        for (final JsonElement element : dependenciesArray) {
            result.add(parseDependency(element.getAsJsonObject()));
        }
        return result;
    }

    private static Dependency parseDependency(JsonObject dependency) {
        final String groupId = dependency.get("groupId").getAsString();
        final String artifactId = dependency.get("artifactId").getAsString();
        final String version = dependency.get("version").getAsString();
        final JsonElement snapshotElement = dependency.get("snapshotId");
        String snapshotId = null;
        if (dependency.has("snapshotId")) {
            snapshotId = dependency.get("snapshotId").getAsString();
        }
        final Collection<Dependency> transitive = parseDependencyList(dependency.getAsJsonArray("transitive"));
        return new Dependency(groupId, artifactId, version, snapshotId, transitive);
    }

    private static Collection<Repository> parseRepositoryList(JsonArray repositoriesArray) {
        final Collection<Repository> result = new HashSet<>();
        for (final JsonElement element : repositoriesArray) {
            try {
                result.add(parseRepository(element.getAsJsonObject()));
            } catch (MalformedURLException e) {
                // Ignored malformed repository
            }
        }
        return result;
    }

    private static Repository parseRepository(JsonObject repository) throws MalformedURLException {
        final String urlStr = repository.get("url").getAsString();
        final URL url = new URL(urlStr);
        return new Repository(url);
    }

    private static Collection<Mirror> parseMirrors(JsonArray mirrorsArray) {
        final Collection<Mirror> result = new HashSet<>();
        for (final JsonElement element : mirrorsArray) {
            try {
                result.add(parseMirror(element.getAsJsonObject()));
            } catch (MalformedURLException e) {
                // Ignored malformed repository
            }
        }
        return result;
    }

    private static Mirror parseMirror(JsonObject mirror) throws MalformedURLException {
        final String mirroringStr = mirror.get("mirroring").getAsString();
        final String originalStr = mirror.get("original").getAsString();
        final URL mirroring = new URL(mirroringStr);
        final URL original = new URL(originalStr);
        return new Mirror(mirroring, original);
    }
}
