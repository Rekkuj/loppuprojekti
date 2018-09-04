package fi.academy.exceptions;

public class NotAuthorizedException extends RuntimeException {
    private String message;
    public NotAuthorizedException(String message) {
//        super(err);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
