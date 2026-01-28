package ru.job4j.cinema.exceptions;

import org.sql2o.Sql2oException;

public class DuplicateTicketException extends RuntimeException {
    public DuplicateTicketException(String message) {
        super(message);
    }

    public static boolean isDuplicateKeyError(Sql2oException e) {
        if (e == null || e.getMessage() == null) {
            return false;
        }

        String message = e.getMessage().toLowerCase();

        return message.contains("duplicate key")
                || message.contains("unique constraint")
                || message.contains("already exists")
                || message.contains("violates unique constraint")
                || message.contains("sqlstate[23505]")
                || message.contains("sqlstate 23000")     // SQL Standard
                ||  message.contains("ora-00001")           // Oracle
                || (e.getCause() != null && isDuplicateKeyError((Sql2oException) e.getCause()));
    }
}
