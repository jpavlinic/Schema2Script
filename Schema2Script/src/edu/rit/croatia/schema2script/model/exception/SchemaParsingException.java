package model.exception;

/**
 * The SchemaParsingException class represents a custom exception that is thrown
 * when an error occurs during the parsing of a schema file.
 */
public class SchemaParsingException extends Exception {

    /**
     * Constructs a new SchemaParsingException with the specified detail message.
     *
     * @param message the detail message, which provides more information about the error
     */
    public SchemaParsingException(String message) {
        super(message);
    }

    /**
     * Constructs a new SchemaParsingException with the specified detail message
     * and cause.
     *
     * @param message the detail message, which provides more information about the error
     * @param cause   the cause of the exception, which can be used to trace the error
     */
    public SchemaParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
