# Video Publishing Platform: Facade Pattern (Java 17+)

## Idea
Publishing a video needs five steps in the right order: validate, transcode, make a thumbnail, upload, notify subscribers.
The client should not have to know this. `VideoPublishingFacade.publish(video, title, channel)` does it all.

## Structure
```
videoplatform
├── client/     Main                      (uses ONLY the Facade + data classes)
├── facade/     VideoPublishingFacade     (coordinates, no business logic)
├── subsystem/  FormatValidator           (checks extension, size, duration)
│               VideoTranscoder           (converts to 720p mp4)
│               ThumbnailGenerator        (preview image)
│               CloudStorage              (upload / delete, quota)
│               SubscriberNotifier        (notifies channel subscribers)
├── model/      VideoFile, TranscodedVideo, Thumbnail, PublishResult, Resolution
└── exception/  SubsystemException, VideoPublishingException
```

## Run
```bash
cd VideoPublishingFacade
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out videoplatform.client.Main
```

## Facade pattern checklist
| Requirement | Where |
|---|---|
| 4+ subsystem classes with real work | 5 classes in `subsystem/` |
| One Facade with a small API | `VideoPublishingFacade.publish(...)` |
| Client uses only the Facade | `client/Main.java` imports no subsystem class |
| Subsystem does not know the Facade | no subsystem class imports `facade` |
| Subsystem still usable directly | all subsystem classes are public with public methods |

## Clean Code principles used
1. **Meaningful names**: `publish`, `notifySafely`, `rollbackUpload` explain themselves; the client needs no documentation.
2. **Single Responsibility**: the Facade only coordinates; validation, transcoding, storage and notification logic live in their own classes.
3. **No leaking of subsystem details**: `publish` takes a `VideoFile`, a title and a channel, and returns a small `PublishResult`. No `Resolution`, `Thumbnail` or storage objects are exposed to the client.
4. **Small, focused classes and methods**: each subsystem class has one reason to change; `publish` reads like the list of steps.
5. **Proper error handling**: subsystem failures (`SubsystemException`) are wrapped into one `VideoPublishingException`, so the client handles a single exception type. If a later step fails after the video was uploaded, `rollbackUpload` deletes it (no half-published video). A failed notification does not cancel a successful upload (`notifySafely`), and the result says `subscribersNotified=false`.
6. **No magic numbers**: constants such as `ALLOWED_EXTENSIONS`, `MAX_SIZE_BYTES`, `DEFAULT_RESOLUTION`.
7. **Dependency injection**: the second Facade constructor accepts subsystem objects, so behavior can be tested (for example, a `CloudStorage` with a tiny quota).
8. **Fail fast**: the title is checked and the file is validated before any expensive step.

## Likely defense questions
- **What problem does Facade solve here?** The client would otherwise create five classes and call them in the correct order; the Facade gives one call and hides the order.
- **Facade vs Adapter?** Adapter changes an interface to match what the client expects; Facade simplifies a whole subsystem behind a new, smaller interface.
- **Facade vs Mediator?** The Facade is one-directional (client → subsystem) and subsystem classes do not know it; in Mediator the components talk through the mediator and know it.
- **Why can the subsystem be used without the Facade?** The Facade does not replace the subsystem, it is only a convenient entry point. Advanced clients can still use `VideoTranscoder` or `CloudStorage` directly.
- **Is the Facade a God object?** No: it contains no real logic, only the order of calls and error coordination.
- **How would you add a second Facade?** For example, `VideoModerationFacade` for reports and takedowns, using `CloudStorage` and `SubscriberNotifier`, without making `VideoPublishingFacade` bigger.
- **How do you test it?** Inject a `CloudStorage` with a small quota and check that the video is deleted after a failed thumbnail upload.
