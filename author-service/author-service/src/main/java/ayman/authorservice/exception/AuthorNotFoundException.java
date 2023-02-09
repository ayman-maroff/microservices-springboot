package ayman.authorservice.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String cause) {
        super("Author not found with " + cause);
    }
}
