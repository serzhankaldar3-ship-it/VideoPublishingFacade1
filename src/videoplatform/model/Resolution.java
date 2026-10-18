package videoplatform.model;

public enum Resolution {
    P480("480p", 0.3),
    P720("720p", 0.6),
    P1080("1080p", 1.0);

    private final String label;
    private final double sizeFactor;

    Resolution(String label, double sizeFactor) {
        this.label = label;
        this.sizeFactor = sizeFactor;
    }

    public String label() {
        return label;
    }

    public double sizeFactor() {
        return sizeFactor;
    }
}
