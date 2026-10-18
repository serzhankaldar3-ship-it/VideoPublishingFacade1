package videoplatform.exception;
public class VideoPublishingException extends Exception {
    public VideoPublishingException(String message, Throwable cause) {
        super(message, cause);
    }

    public VideoPublishingException(String message) {
        super(message);
    }
}
