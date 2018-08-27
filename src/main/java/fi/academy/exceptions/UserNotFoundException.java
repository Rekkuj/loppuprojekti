package fi.academy.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String user) {
        super(user);
    }
}
