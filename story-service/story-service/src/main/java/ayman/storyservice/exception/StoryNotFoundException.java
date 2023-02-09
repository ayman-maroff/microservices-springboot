package ayman.storyservice.exception;

public class StoryNotFoundException extends RuntimeException {

    public StoryNotFoundException(String cause) {
        super("Story not found with " + cause);
    }
}
