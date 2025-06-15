package com.wordle.utils;

import com.wordle.exception.NoValidWordsInFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

/**
 * Utility class to load the word list from a file.
 */
public class WordListLoader {
    private WordListLoader(){
        throw new IllegalStateException("Utility class");
    }

    /**
     * Loads 5-letter words from a file, normalizing to uppercase.
     *
     * @param path the path to the word list file
     * @return a list of 5-letter words
     */
    public static List<String> load(Path path) {
        try(Stream<String> wordsStream = Files.lines(path)){
            List<String> words = wordsStream
                    .map(String::trim)
                    .filter(w -> w.length() == 5 && w.matches("^[A-Za-z]{5}$"))
                    .map(s -> s.toUpperCase(Locale.ENGLISH))
                    .toList();
            if (words.isEmpty()) {
                throw new NoValidWordsInFile(path.toString());
            }
            return words;
        }catch(IOException e){
            throw new UncheckedIOException("Error reading word list from " + path, e);
        }
    }
} 