package com.wordle;

import com.wordle.exception.EmptyWordListException;
import com.wordle.utils.BannerPrinter;
import com.wordle.utils.FeedbackCalculator;

import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

/**
 * Main game class that handles the game loop and user interface.
 */
public class WordleGame {
    private static final String WELCOME_BANNER_PATH = "welcomeBanner.txt";
    private static final int MAX_ATTEMPTS = 5;
    private static final String GREEN_BG = "\u001B[42m";
    private static final String YELLOW_BG = "\u001B[43m";
    private static final String RESET_STYLE_STRING = "\u001B[0m";
    
    private final List<String> words;
    private final Scanner scanner;
    private final Random random;
    
    /**
     * Creates a new Wordle game with the given word list.
     * 
     * @param words the list of possible answer words
     * @throws EmptyWordListException if the word list is empty
     */
    public WordleGame(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new EmptyWordListException("Cannot start game with empty word list");
        }
        this.words = words;
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }
    
    /**
     * Shows the game introduction.
     */
    public void showIntroduction() {
        BannerPrinter.printBanner(Paths.get(WELCOME_BANNER_PATH));
        System.out.println("Guess the 5-letter word in " + MAX_ATTEMPTS + " tries.");
        System.out.println("After each guess, the color of the tiles will change to show how close your guess was to the word.");
        System.out.println("Green: The letter is correct and in the right position.");
        System.out.println("Yellow: The letter is in the word but in the wrong position.");
        System.out.println("No color: The letter is not in the word.");
        System.out.println("Let's Go!");
    }
    
    /**
     * Runs the game.
     */
    public void play() {
        try {
            String answer = selectRandomWord();
            showIntroduction();
            
            boolean solved = false;
            int attempt = 1;
            
            while (attempt <= MAX_ATTEMPTS && App.isRunning() && !solved) {
                System.out.printf("Attempt %d/%d: ", attempt, MAX_ATTEMPTS);
                String guess = getValidGuess();
                
                if (App.isRunning()) {
                    List<Hint> feedback = FeedbackCalculator.computeFeedback(answer, guess);
                    printColoredFeedback(guess, feedback);
                    
                    if (allGreen(feedback)) {
                        System.out.println("Congratulations! You've found the word in " + attempt + " attempt(s)!");
                        solved = true;
                    }
                }
                attempt++;
            }
            
            if (!solved && App.isRunning()) {
                System.out.println("Out of attempts! The word was: " + answer);
            }
        } finally {
            cleanup();
        }
    }

    /**
     * Cleans up resources used by the game.
     */
    private void cleanup() {
        if (scanner != null) {
            scanner.close();
        }
    }
    
    /**
     * Gets a valid 5-letter guess from the user.
     * 
     * @return a valid guess
     */
    private String getValidGuess() {
        String guess;
        while (true) {
            guess = scanner.nextLine().trim().toUpperCase(Locale.ENGLISH);
            if (guess.length() == 5 && guess.matches("[A-Z]{5}")) {
                break;
            }
            System.out.print("Please enter a valid 5-letter word: ");
        }
        return guess;
    }
    
    /**
     * Prints the guess with colored feedback.
     * 
     * @param guess the player's guess
     * @param feedback the feedback for each letter
     */
    private void printColoredFeedback(String guess, List<Hint> feedback) {
        for (int i = 0; i < 5; i++) {
            char letter = guess.charAt(i);
            String colorCode = switch (feedback.get(i)) {
                case GREEN -> GREEN_BG;
                case YELLOW -> YELLOW_BG;
                case ABSENT -> "";
            };
            
            if (!colorCode.isEmpty()) {
                System.out.print(colorCode + " " + letter + " " + RESET_STYLE_STRING);
            } else {
                System.out.print(" " + letter + " ");
            }
        }
        System.out.println();
    }
    
    /**
     * Checks if all feedback hints are GREEN.
     * 
     * @param feedback the feedback list
     * @return true if all hints are GREEN
     */
    private boolean allGreen(List<Hint> feedback) {
        return feedback.stream().allMatch(hint -> hint == Hint.GREEN);
    }
    
    /**
     * Selects a random word from the word list.
     * 
     * @return the selected word
     */
    private String selectRandomWord() {
        return words.get(random.nextInt(words.size()));
    }
} 