package systemB;

import common.PrintingRunnable;
import common.ProducerShutdownable;
import systemA.Event;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Consumes messages from sorted queue and printing to stdout.
 * Sleeps 900-1100 ms between printing. Also sleeps random time (0-1000 ms) before start to work.
 */
public class EventConsumer extends PrintingRunnable {

    private ProducerShutdownable producerSystem;
    private int clientID;

    public EventConsumer(ProducerShutdownable producerSystem) {
        this.producerSystem = producerSystem;
        this.clientID = producerSystem.subscribeTopic();
    }

    @Override
    public void run() {
        printStdout("ready for duty...");
        randomStartDelay();

        while (!Thread.currentThread().isInterrupted()) {
            Event e = producerSystem.getNextEvent(clientID);
            printStdout(e.toString());
            try {
                Thread.sleep(900 + ThreadLocalRandom.current().nextInt(0, 200));
            } catch (InterruptedException e1) {
                printStdout("interrupted on sleep");
                break;
            }
        }
        printStdout("shutting down");
    }

    private void randomStartDelay() {
        int randomDelay = ThreadLocalRandom.current().nextInt(0, 1000);
        try {
            Thread.sleep(randomDelay);
        } catch (InterruptedException e) {
            printStdout("interrupted on start");
        }
    }
}
