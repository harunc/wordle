package com.wordle.exception;

/**
 * Exception thrown when the word list is empty after initialization.
 */
public class EmptyWordListException extends RuntimeException {
    public EmptyWordListException(String message) {
        super(message);
    }
} 