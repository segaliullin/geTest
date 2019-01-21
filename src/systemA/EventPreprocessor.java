package systemA;

import common.PrintingRunnable;

import java.util.Map;
import java.util.concurrent.BlockingQueue;

/**
 * Moves messages from unsorted queue to client queues.
 */
public class EventPreprocessor extends PrintingRunnable {

    private BlockingQueue<Event> unsortedInputQueue;
    private Map<Integer, BlockingQueue<Event>> consumerQueue;

    public EventPreprocessor(BlockingQueue<Event> unsortedInputQueue, Map<Integer, BlockingQueue<Event>> consumerQueue) {
        this.unsortedInputQueue = unsortedInputQueue;
        this.consumerQueue = consumerQueue;
    }

    @Override
    public void run() {
        printStdout("ready for duty...");
        while (!Thread.interrupted()) {
            Event e = null;
            try {
                e = unsortedInputQueue.take();
            } catch (InterruptedException e1) {
                printStdout("interrupted on queue await");
                break;
            }

            for (Map.Entry<Integer, BlockingQueue<Event>> entry : consumerQueue.entrySet()) {
                BlockingQueue<Event> v = entry.getValue();
                try {
                    v.put(e);
                } catch (InterruptedException e1) {
                    printStdout("interrupted on queue put");
                    break;
                }
            }
        }
        printStdout("shutting down");
    }
}
