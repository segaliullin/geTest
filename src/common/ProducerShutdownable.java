package common;

import systemA.Event;

public interface ProducerShutdownable extends Shutdownable {

    /**
     * Subscribes consumer to message topic. Returns clientID which should be used
     * to access message queue.
     * @return clientID
     */
    int subscribeTopic();

    /**
     * Returns next event for processing.
     * @param clientID received on subscribe
     * @return message
     */
    Event getNextEvent(int clientID);

}
