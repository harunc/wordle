package com.wordle;

import com.wordle.utils.WordListLoader;

import java.nio.file.Paths;
import java.util.List;

/**
 * Main entry point for the Wordle game.
 */
public class App {
    private static final String DEFAULT_WORD_LIST = "words.txt";
    
    public static void main(String[] args) {
        try {
            String wordListPath = args.length > 0 ? args[0] : DEFAULT_WORD_LIST;
            List<String> words = WordListLoader.load(Paths.get(wordListPath));
            
            WordleGame game = new WordleGame(words);
            game.play();
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
} 