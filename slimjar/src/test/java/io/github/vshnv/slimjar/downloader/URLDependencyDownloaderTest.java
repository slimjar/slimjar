package io.github.vshnv.slimjar.downloader;

import io.github.vshnv.slimjar.data.Dependency;
import junit.framework.TestCase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collectors;

public class URLDependencyDownloaderTest extends TestCase {
    public void testDependencyDownload() throws IOException {
        AtomicReference<String> output = new AtomicReference<>();
        DependencyDownloader downloader = new URLDependencyDownloader(createOutputWriter(output));
        downloader.download(new Dependency(
                "testA.json",
                this.getClass().getClassLoader().getResource("test.json").toString(),
                Collections.emptyList())
        );
        String expected = new BufferedReader(
                new InputStreamReader(
                        Objects.requireNonNull(this.getClass().getClassLoader().getResourceAsStream("test.json")),
                        StandardCharsets.UTF_8
                )
        )
        .lines()
        .collect(Collectors.joining("\n"));

        assertEquals(expected, output.get());
    }

    private Function<String, OutputWriter> createOutputWriter(AtomicReference<String> output) {
        return name -> (inputStream, length) -> {
            output.set(
                    new BufferedReader(
                            new InputStreamReader(
                                    Objects.requireNonNull(inputStream),
                                    StandardCharsets.UTF_8
                            )
                    ).lines()
                    .collect(Collectors.joining("\n"))
            );
        };
    }
}