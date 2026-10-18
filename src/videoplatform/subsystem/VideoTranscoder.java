package videoplatform.subsystem;
import videoplatform.model.Resolution;
import videoplatform.model.TranscodedVideo;
import videoplatform.model.VideoFile;
public class VideoTranscoder {

    public TranscodedVideo transcode(VideoFile video, Resolution resolution) {
        String baseName = video.fileName().substring(0, video.fileName().lastIndexOf('.'));
        long newSize = (long) (video.sizeBytes() * resolution.sizeFactor());
        System.out.println("[VideoTranscoder] " + video.fileName() + " -> "
                + resolution.label() + " mp4 (" + newSize + " bytes)");
        return new TranscodedVideo(baseName + "_" + resolution.label() + ".mp4", resolution, newSize);
    }
}
