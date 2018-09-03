package fi.academy.exceptions;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException(RuntimeException err) {
        super(err);
    }
}
