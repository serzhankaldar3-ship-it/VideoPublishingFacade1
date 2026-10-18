package videoplatform.subsystem;

import videoplatform.exception.SubsystemException;
public class SubscriberNotifier {

    public void notifySubscribers(String channel, String title, String videoUrl) throws SubsystemException {
        if (channel == null || channel.isBlank()) {
            throw new SubsystemException("Channel is required to notify subscribers");
        }
        System.out.println("[SubscriberNotifier] '" + channel + "' subscribers notified about \""
                + title + "\" -> " + videoUrl);
    }
}
