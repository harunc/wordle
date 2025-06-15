package com.wordle.utils;

import com.wordle.Hint;
import com.wordle.exception.InvalidWordLengthException;

import java.util.*;

/**
 * Core logic for computing feedback on Wordle guesses.
 */
public class FeedbackCalculator {
    private FeedbackCalculator() {
        throw new IllegalStateException("Utility class");
    }
    /**
     * Computes feedback for a guess against the answer.
     * Uses a two-pass algorithm to handle duplicate letters correctly.
     *
     * @param answer the correct word
     * @param guess the player's guess
     * @return a list of hints, one for each letter in the guess
     */
    public static List<Hint> computeFeedback(String answer, String guess) {
        validateInputAnswerAndGuess(answer, guess);

        answer = answer.toUpperCase();
        guess = guess.toUpperCase();

        Hint[] hints = new Hint[5];
        Map<Character, Integer> freq = new HashMap<>();

        for (int i = 0; i < 5; i++) {
            char a = answer.charAt(i);
            char g = guess.charAt(i);

            if (g == a) {
                hints[i] = Hint.GREEN;
            } else {
                hints[i] = null;
                freq.merge(a, 1, Integer::sum);
            }
        }

        for (int i = 0; i < 5; i++) {
            if (hints[i] != null) continue;

            char g = guess.charAt(i);
            int count = freq.getOrDefault(g, 0);

            if (count > 0) {
                hints[i] = Hint.YELLOW;
                freq.merge(g,-1, Integer::sum);
            } else {
                hints[i] = Hint.ABSENT;
            }
        }

        return Arrays.asList(hints);
    }
    /**
     * Computes feedback for a guess against the answer.
     * Uses a one-pass algorithm to give feedback after setting position vectors in another pass.
     *
     * @param answer the correct word
     * @param guess the player's guess
     * @return a list of hints, one for each letter in the guess
     */
    public static List<Hint> computeFeedback1(String answer, String guess) {
        validateInputAnswerAndGuess(answer,guess);

        answer = answer.toUpperCase();
        guess = guess.toUpperCase();

        Hint[] hints = new Hint[5];
        Arrays.fill(hints, Hint.ABSENT);
        Map<Character, Queue<Integer>> charPositionsOfGuess = getGuessPositionVector(guess);

        for (int i = 0; i < 5; i++) {
            char c = answer.charAt(i);
            Queue<Integer> q = charPositionsOfGuess.get(c);
            if (q == null || q.isEmpty()) {
                continue;
            }
            if(q.contains(i)) {
                hints[i] = Hint.GREEN;
                q.remove(i);
            }
            else{
                Integer guessIndex = charPositionsOfGuess.get(c).poll();
                if(guessIndex != null) {
                    hints[guessIndex] = (answer.charAt(guessIndex) == c) ? Hint.GREEN : Hint.YELLOW;
                }
            }
        }

        return Arrays.asList(hints);
    }
    /**
     * Pass chars and sets it's positions to related position map.
     *
     * @param word the player's guess
     * @return position vector of chars. For instance OTTER -> [0],[1,2],[3],[4]
     */
    private static Map<Character, Queue<Integer>> getGuessPositionVector(String word){
        Map<Character, Queue<Integer>> positions = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            char c = word.charAt(i);
            positions.computeIfAbsent(c, k -> new LinkedList<>()).add(i);
        }
        return positions;
    }
    /**
     * Validates inputs if they are not null and has correct length(5)
     *
     * @param answer the answer
     * @param guess the player's guess
     * @throws IllegalArgumentException if one of the words are null
     * @throws InvalidWordLengthException if one of word length is not 5
     */
    private static void validateInputAnswerAndGuess(String answer, String guess){
        if (answer == null || guess == null)
            throw new IllegalArgumentException("Answer and guess must not be null.");
        if (answer.length() != 5 || guess.length() != 5)
            throw new InvalidWordLengthException("Answer and guess must be 5-letter words.");
    }


} 