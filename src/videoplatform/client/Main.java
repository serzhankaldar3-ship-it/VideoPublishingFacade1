package videoplatform.client;
import videoplatform.exception.VideoPublishingException;
import videoplatform.facade.VideoPublishingFacade;
import videoplatform.model.PublishResult;
import videoplatform.model.VideoFile;
public class Main {
    public static void main(String[] args) {
        VideoPublishingFacade facade = new VideoPublishingFacade();
        System.out.println("=== 1. Successful publish ===");
        tryPublish(facade, new VideoFile("trip_to_almaty.mov", 800_000_000L, 600),
                "My trip to Almaty", "TravelWithAida");
        System.out.println("\n=== 2. Unsupported format ===");
        tryPublish(facade, new VideoFile("setup.exe", 5_000_000L, 10),
                "Not a video", "TravelWithAida");
        System.out.println("\n=== 3. No channel: published, but notification fails ===");
        tryPublish(facade, new VideoFile("lecture_1.mp4", 300_000_000L, 3000),
                "Lecture 1", "");
        System.out.println("\n=== 4. Empty title ===");
        tryPublish(facade, new VideoFile("clip.mp4", 10_000_000L, 30), " ", "TravelWithAida");
    }
    private static void tryPublish(VideoPublishingFacade facade, VideoFile video,
                                   String title, String channel) {
        try {
            PublishResult result = facade.publish(video, title, channel);
            System.out.println("OK: " + result);
        } catch (VideoPublishingException e) {
            System.out.println("FAILED: " + e.getMessage());}}}
