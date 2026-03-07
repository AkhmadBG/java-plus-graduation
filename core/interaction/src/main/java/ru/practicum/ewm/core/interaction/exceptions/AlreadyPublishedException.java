package ru.practicum.ewm.core.interaction.exceptions;

public class AlreadyPublishedException extends RuntimeException {
    public AlreadyPublishedException(String message) {
        super(message);
    }
}
