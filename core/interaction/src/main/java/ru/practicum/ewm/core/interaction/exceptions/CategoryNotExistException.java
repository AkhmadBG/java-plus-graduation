package ru.practicum.ewm.core.interaction.exceptions;

public class CategoryNotExistException extends RuntimeException {
    public CategoryNotExistException(String message) {
        super(message);
    }
}
