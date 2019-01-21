package systemA;

import common.AbstractSystem;
import common.ProducerShutdownable;

import java.util.*;
import java.util.concurrent.*;

/**
 * SystemA - message generator, may have multiple event producers and event preprocessors.
 * Event producers used only to generate messages and put them to unsortedInputQueue.
 * Event preprocessors used to
 */
public class SystemA extends AbstractSystem implements ProducerShutdownable {

    private static final int SYSTEM_CLIENT_ID = -1;
    private static final int MIN_CLIENT_ID = 0;
    private static final int MAX_CLIENT_ID = Integer.MAX_VALUE;

    private static SystemA instance;

    public static synchronized SystemA getInstance() {
        if (instance == null) {
            instance = new SystemA();
        }
        System.out.println(instance.toString());
        return instance;
    }

    /**
     * Unsorted queue which receives "raw" messages
     */
    private BlockingQueue<Event> unsortedInputQueue = new LinkedBlockingDeque<>();

    /**
     * Stores clients queues. Queues should
     */
    private Map<Integer, BlockingQueue<Event>> consumerQueue = new ConcurrentHashMap();

    public SystemA() {
        consumerQueue.put(SYSTEM_CLIENT_ID, new PriorityBlockingQueue<>());

        int availableCores = Runtime.getRuntime().availableProcessors(); // logical, not physical
        executorService = Executors.newFixedThreadPool(availableCores);
        executorService.submit(new EventProducer(unsortedInputQueue));
        executorService.submit(new EventProducer(unsortedInputQueue));
        executorService.submit(new EventPreprocessor(unsortedInputQueue, consumerQueue));
        executorService.submit(new EventPreprocessor(unsortedInputQueue, consumerQueue));
        executorService.submit(new EventPreprocessor(unsortedInputQueue, consumerQueue));
        executorService.submit(new EventPreprocessor(unsortedInputQueue, consumerQueue));
    }

    /**
     * {@inheritDoc}
     *
     * Duplicates already existing (system) queue to new one-client-only queue, then
     * puts queue to "clientId -> sorted queue" map
     */
    public synchronized int subscribeTopic() {
        int clientID = ThreadLocalRandom.current().nextInt(MIN_CLIENT_ID, MAX_CLIENT_ID);
        consumerQueue.put(clientID, new PriorityBlockingQueue<>(consumerQueue.get(SYSTEM_CLIENT_ID)));
        return clientID;
    }

    /**
     * {@inheritDoc}
     */
    public Event getNextEvent(int clientID) {
        try {
            return consumerQueue.get(clientID).take();
        } catch (InterruptedException e) {
            System.out.println(new StringBuilder()
                    .append(this.getClass().getSimpleName())
                    .append(" ")
                    .append(Thread.currentThread().getId())
                    .append(": interrupted on queue take")
                    .toString());
        }
        return null;
    }

    @Override
    public boolean isReadyForShutdown() {
        return executorService.isTerminated();
    }
}
