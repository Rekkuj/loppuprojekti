package fi.academy.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(RuntimeException user) {
        super(user);
    }
}
