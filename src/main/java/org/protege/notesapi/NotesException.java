package org.protege.notesapi;

/** @author csnyulas */
/** @author csnyulas */
public class NotesException extends Exception {

    private static final long serialVersionUID = 1L;

    public NotesException(String message) {
        super(message);
    }

    public NotesException(Throwable cause) {
        super(cause);
    }

    public NotesException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        String message = getMessage();
        Throwable cause = getCause();
        return "NotesException(" +
                (message != null ? message : "" ) +
                (message != null && cause != null ? ", " : "" ) +
                (cause != null ? cause.toString() : "" ) + ")";
    }
}
