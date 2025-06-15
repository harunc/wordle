package com.wordle;

import com.wordle.exception.InvalidWordLengthException;
import com.wordle.utils.FeedbackCalculator;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the FeedbackEngine class.
 */
class FeedbackCalculatorTest {

    @Test
    void testAllGreen() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("APPLE", "APPLE");
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN), feedback);
    }
    
    @Test
    void testAllAbsent() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("FRUIT", "CHECK");
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT), feedback);
    }

    @Test
    void testDuplicateLetters() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("WATER", "OTTER");
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.GREEN, Hint.GREEN, Hint.GREEN), feedback);
    }

    @Test
    void testMixedFeedback() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("WATER", "EARTH");
        assertEquals(List.of(Hint.YELLOW, Hint.GREEN, Hint.YELLOW, Hint.YELLOW, Hint.ABSENT), feedback);
    }
    
    @Test
    void testDuplicateLettersInAnswer() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("LEVEL", "LEAFY");
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT), feedback);
    }
    
    @Test
    void testMoreDuplicatesInGuessThanAnswer() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("PAPER", "PEPPY");
        assertEquals(List.of(Hint.GREEN, Hint.YELLOW, Hint.GREEN, Hint.ABSENT, Hint.ABSENT), feedback);
    }

    @Test
    void testAnswerAllSameLetter_PartialMatch() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("AAAAA", "AAAAB");
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.ABSENT), feedback);
    }

    @Test
    void testGuessAllSameLetter_LimitedByAnswerCount() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("BANAL", "AAAAA");
        assertEquals(List.of(Hint.ABSENT, Hint.GREEN, Hint.ABSENT, Hint.GREEN, Hint.ABSENT), feedback);
    }

    @Test
    void testComplexDuplicateScenario() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("BANAL", "ALALA");
        assertEquals(List.of(Hint.YELLOW, Hint.YELLOW, Hint.YELLOW, Hint.ABSENT, Hint.ABSENT), feedback);
    }

    @Test
    void testMoreDuplicatesInGuess_LimitedYellows() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("BOOKS", "SOOBO");
        assertEquals(List.of(Hint.YELLOW, Hint.GREEN, Hint.GREEN, Hint.YELLOW, Hint.ABSENT), feedback);
    }

    @Test
    void testNoCommonLetters() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("QUERY", "STACK");
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT), feedback);
    }

    @Test
    void testSingleLetterMismatch() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("LEVEL", "LEVER");
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.ABSENT), feedback);
    }

    @Test
    void testRepeatedLetterAnswerMoreThanGuess() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("EEEEE", "EABCE");
        assertEquals(List.of(Hint.GREEN, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.GREEN), feedback);
    }

    @Test
    void testRepeatedLetterGuessMoreThanAnswerComplex() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("RADAR", "ARRRA");
        assertEquals(List.of(Hint.YELLOW, Hint.YELLOW, Hint.YELLOW, Hint.ABSENT, Hint.YELLOW), feedback);
    }

    @Test
    void testSingleGreenAtEachPosition() {
        assertEquals(List.of(Hint.GREEN, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "AXXXX"));
        assertEquals(List.of(Hint.ABSENT, Hint.GREEN, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "XBYYY"));
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.GREEN, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "XXCXX"));
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.GREEN, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "XXXDX"));
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.GREEN),
                FeedbackCalculator.computeFeedback("ABCDE", "XXXXE"));
    }

    @Test
    void testSingleYellowAtEachPosition() {
        assertEquals(List.of(Hint.YELLOW, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "BXXXX"));
        assertEquals(List.of(Hint.ABSENT, Hint.YELLOW, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "XCXXX"));
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.YELLOW, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "XXDXX"));
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.YELLOW, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("ABCDE", "XXXEX"));
        assertEquals(List.of(Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.YELLOW),
                FeedbackCalculator.computeFeedback("ABCDE", "XXXXA"));
    }

    @Test
    void testMultipleYellowsNoGreens() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("ABCDE", "EABCD");
        assertEquals(List.of(Hint.YELLOW, Hint.YELLOW, Hint.YELLOW, Hint.YELLOW, Hint.YELLOW), feedback);
    }

    @Test
    void testMixedGreensAndYellows() {
        List<Hint> feedback = FeedbackCalculator.computeFeedback("AABBC", "ABABA");
        assertEquals(List.of(Hint.GREEN, Hint.YELLOW, Hint.YELLOW, Hint.GREEN, Hint.ABSENT), feedback);
    }

    @Test
    void testAllSameLetterInAnswer_GuessVaried() {
        assertEquals(List.of(Hint.GREEN, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("AAAAA", "AXXXX"));
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.ABSENT, Hint.ABSENT, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("AAAAA", "AAXXX"));
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.ABSENT),
                FeedbackCalculator.computeFeedback("AAAAA", "AAAAB"));
    }

    @Test
    void testCaseInsensitivityMixedCase() {
        List<Hint> feedback1 = FeedbackCalculator.computeFeedback("Water", "wAtEr");
        assertEquals(List.of(Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN, Hint.GREEN), feedback1);
        List<Hint> feedback2 = FeedbackCalculator.computeFeedback("BaNal", "aLaLa");
        assertEquals(List.of(Hint.YELLOW, Hint.YELLOW, Hint.YELLOW, Hint.ABSENT, Hint.ABSENT), feedback2);
    }

    @Test
    void testInvalidInputsNullAndLength() {
        assertThrows(IllegalArgumentException.class, () -> FeedbackCalculator.computeFeedback(null, "ABCDE"));
        assertThrows(IllegalArgumentException.class, () -> FeedbackCalculator.computeFeedback("ABCDE", null));
        assertThrows(InvalidWordLengthException.class, () -> FeedbackCalculator.computeFeedback("ABCD", "ABCDE"));
        assertThrows(InvalidWordLengthException.class, () -> FeedbackCalculator.computeFeedback("ABCDE", "AB"));
        assertThrows(InvalidWordLengthException.class, () -> FeedbackCalculator.computeFeedback("ABCDEF", "ABCDE"));
    }

} 