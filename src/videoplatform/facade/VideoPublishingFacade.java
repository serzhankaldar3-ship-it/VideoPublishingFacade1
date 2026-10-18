package videoplatform.facade;

import videoplatform.exception.SubsystemException;
import videoplatform.exception.VideoPublishingException;
import videoplatform.model.PublishResult;
import videoplatform.model.Resolution;
import videoplatform.model.Thumbnail;
import videoplatform.model.TranscodedVideo;
import videoplatform.model.VideoFile;
import videoplatform.subsystem.CloudStorage;
import videoplatform.subsystem.FormatValidator;
import videoplatform.subsystem.SubscriberNotifier;
import videoplatform.subsystem.ThumbnailGenerator;
import videoplatform.subsystem.VideoTranscoder;
public class VideoPublishingFacade {
    private static final Resolution DEFAULT_RESOLUTION = Resolution.P720;

    private final FormatValidator validator;
    private final VideoTranscoder transcoder;
    private final ThumbnailGenerator thumbnailGenerator;
    private final CloudStorage storage;
    private final SubscriberNotifier notifier;
    public VideoPublishingFacade() {
        this(new FormatValidator(), new VideoTranscoder(), new ThumbnailGenerator(),
                new CloudStorage(), new SubscriberNotifier());
    }
    public VideoPublishingFacade(FormatValidator validator, VideoTranscoder transcoder,
                                 ThumbnailGenerator thumbnailGenerator, CloudStorage storage,
                                 SubscriberNotifier notifier) {
        this.validator = validator;
        this.transcoder = transcoder;
        this.thumbnailGenerator = thumbnailGenerator;
        this.storage = storage;
        this.notifier = notifier;
    }
    public PublishResult publish(VideoFile video, String title, String channel)
            throws VideoPublishingException {
        requireNotBlank(title, "Title");
        String uploadedVideoUrl = null;
        try {
            validator.validate(video);
            TranscodedVideo transcoded = transcoder.transcode(video, DEFAULT_RESOLUTION);
            Thumbnail thumbnail = thumbnailGenerator.generate(video);

            uploadedVideoUrl = storage.upload(transcoded.fileName(), transcoded.sizeBytes());
            String thumbnailUrl = storage.upload(thumbnail.fileName(), thumbnail.sizeBytes());

            boolean notified = notifySafely(channel, title, uploadedVideoUrl);
            return new PublishResult(uploadedVideoUrl, thumbnailUrl, notified);
        } catch (SubsystemException e) {
            rollbackUpload(uploadedVideoUrl);
            throw new VideoPublishingException("Could not publish \"" + title + "\": " + e.getMessage(), e);
        }
    }
    private boolean notifySafely(String channel, String title, String videoUrl) {
        try {
            notifier.notifySubscribers(channel, title, videoUrl);
            return true;
        } catch (SubsystemException e) {
            System.out.println("[Facade] Video published, but notification failed: " + e.getMessage());
            return false;}}
    private void rollbackUpload(String videoUrl) {
        if (videoUrl != null) {
            storage.delete(videoUrl);}}
    private void requireNotBlank(String value, String fieldName) throws VideoPublishingException {
        if (value == null || value.isBlank()) {
            throw new VideoPublishingException(fieldName + " must not be empty");
        }}}
