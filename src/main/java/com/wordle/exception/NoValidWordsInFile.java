package com.wordle.exception;

/**
 * Exception thrown when no valid 5-letter words are found in the word list file.
 */
public class NoValidWordsInFile extends RuntimeException {
    public NoValidWordsInFile(String path) {
        super("No valid words found in path: " + path);
         }
}
