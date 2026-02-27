package ru.practicum.ewm.core.interaction.exceptions;

public class EventAlreadyCanceledException extends RuntimeException {
    public EventAlreadyCanceledException(String message) {
        super(message);
    }
}
