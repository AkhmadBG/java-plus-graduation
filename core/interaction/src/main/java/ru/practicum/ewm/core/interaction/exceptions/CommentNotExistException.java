package ru.practicum.ewm.core.interaction.exceptions;

public class CommentNotExistException extends RuntimeException {
    public CommentNotExistException(String message) {
        super(message);
    }
}
