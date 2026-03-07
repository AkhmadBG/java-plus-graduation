package ru.practicum.ewm.core.interaction.exceptions;

public class EventNotExistException extends RuntimeException {
    public EventNotExistException(String message) {
        super(message);
    }
}
