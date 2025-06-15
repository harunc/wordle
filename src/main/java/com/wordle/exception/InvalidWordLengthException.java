package com.wordle.exception;

/**
 * Exception thrown when a word does not meet the required length criteria.
 */
public class InvalidWordLengthException extends RuntimeException {
    public InvalidWordLengthException(String message) {
        super(message);
    }
} 