package com.wordle.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class BannerPrinter {
    private BannerPrinter(){
        throw new IllegalStateException("Utility class");
    }
    public static void printBanner(Path path) {
        try (Stream<String> lines = Files.lines(path)) {
            lines.forEach(System.out::println);
        } catch (IOException e) {
            System.err.println("Could not load banner: " + e.getMessage());
        }
    }
}
