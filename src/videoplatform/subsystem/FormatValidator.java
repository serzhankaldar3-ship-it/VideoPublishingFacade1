package videoplatform.subsystem;

import videoplatform.exception.SubsystemException;
import videoplatform.model.VideoFile;

import java.util.Locale;
import java.util.Set;
public class FormatValidator {
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("mp4", "mov", "avi", "mkv");
    private static final long MAX_SIZE_BYTES = 2L * 1024 * 1024 * 1024; // 2 GB

    public void validate(VideoFile video) throws SubsystemException {
        String extension = extensionOf(video.fileName());
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new SubsystemException("Unsupported format: ." + extension);
        }
        if (video.sizeBytes() > MAX_SIZE_BYTES) {
            throw new SubsystemException("File is larger than 2 GB");
        }
        if (video.durationSeconds() <= 0) {
            throw new SubsystemException("Video has no duration");
        }
        System.out.println("[FormatValidator] " + video.fileName() + " is valid");
    }

    private String extensionOf(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }
}
