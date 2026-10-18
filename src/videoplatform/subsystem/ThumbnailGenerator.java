package videoplatform.subsystem;
import videoplatform.model.Thumbnail;
import videoplatform.model.VideoFile;

public class ThumbnailGenerator {
    private static final long THUMBNAIL_SIZE_BYTES = 50_000;

    public Thumbnail generate(VideoFile video) {
        String baseName = video.fileName().substring(0, video.fileName().lastIndexOf('.'));
        int frameSecond = video.durationSeconds() / 2;
        System.out.println("[ThumbnailGenerator] frame at " + frameSecond + "s");
        return new Thumbnail(baseName + "_thumb.jpg", THUMBNAIL_SIZE_BYTES);
    }
}
