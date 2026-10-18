package videoplatform.subsystem;
import videoplatform.exception.SubsystemException;

import java.util.HashMap;
import java.util.Map;
public class CloudStorage {
    private static final long DEFAULT_QUOTA_BYTES = 5L * 1024 * 1024 * 1024; // 5 GB

    private final long quotaBytes;
    private final Map<String, Long> storedFiles = new HashMap<>();
    private long usedBytes = 0;

    public CloudStorage() {
        this(DEFAULT_QUOTA_BYTES);
    }

    public CloudStorage(long quotaBytes) {
        this.quotaBytes = quotaBytes;
    }

    public String upload(String fileName, long sizeBytes) throws SubsystemException {
        if (usedBytes + sizeBytes > quotaBytes) {
            throw new SubsystemException("Storage quota exceeded while uploading " + fileName);
        }
        String url = "https://cdn.example.com/videos/" + fileName;
        storedFiles.put(url, sizeBytes);
        usedBytes += sizeBytes;
        System.out.println("[CloudStorage] uploaded " + fileName);
        return url;
    }

    public boolean delete(String url) {
        Long size = storedFiles.remove(url);
        if (size == null) {
            return false;
        }
        usedBytes -= size;
        System.out.println("[CloudStorage] deleted " + url);
        return true;
    }

    public boolean contains(String url) {
        return storedFiles.containsKey(url);
    }
}
