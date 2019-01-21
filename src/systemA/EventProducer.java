package systemA;

import common.PrintingRunnable;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Generates messages with random string and random priority.
 * MIN_SLEEP_TIME, MAX_SLEEP_TIME - delays between message generation
 * MIN_MSG_COUNT, MAX_MSG_COUNT - amount of generated messages
 * MIN_MSG_LENGTH, MAX_MSG_LENGTH - length of string in message
 * MIN_MSG_PRIORITY, MAX_MSG_PRIORITY - priority range
 */
public class EventProducer extends PrintingRunnable {

    private static final int MIN_SLEEP_TIME = 60;
    private static final int MAX_SLEEP_TIME = 120;

    private static final int MIN_MSG_COUNT = 16;
    private static final int MAX_MSG_COUNT = 48;

    private static final int MIN_MSG_LENGTH = 64;
    private static final int MAX_MSG_LENGTH = 128;

    private static final int A_LETTER_CHARCODE = 97;
    private static final int Z_LETTER_CHARCODE = 122;

    private static final int MIN_MSG_PRIORITY = 0;
    private static final int MAX_MSG_PRIORITY = 15;

    private BlockingQueue<Event> messageQueue;

    public EventProducer(BlockingQueue<Event> messageQueue) {
        this.messageQueue = messageQueue;
    }

    @Override
    public void run() {
        printStdout("ready for duty...");

        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        while (!Thread.currentThread().isInterrupted()) {
            int sleepTime = threadLocalRandom.nextInt(MIN_SLEEP_TIME, MAX_SLEEP_TIME);
            int messageCount = threadLocalRandom.nextInt(MIN_MSG_COUNT, MAX_MSG_COUNT);
            for (int i = 0; i < messageCount; i++) {
                Event e = new Event(
                        generateRandomString(threadLocalRandom),
                        threadLocalRandom.nextInt(MIN_MSG_PRIORITY, MAX_MSG_PRIORITY)
                );
                try {
                    messageQueue.put(e);
                } catch (InterruptedException e1) {
                    printStdout("interrupted on queue put");
                    break;
                }
            }

            try {
                printStdout(messageCount + " messages generated");
                printStdout("sleeping for " + sleepTime + " sec");
                Thread.sleep(sleepTime * 1000);
            } catch (InterruptedException e) {
                printStdout("interrupted on sleep");
                break;
            }
        }

        printStdout("shutting down");
    }

    private String generateRandomString(ThreadLocalRandom threadLocalRandom) {
        int length = threadLocalRandom.nextInt(MIN_MSG_LENGTH, MAX_MSG_LENGTH);
        StringBuilder builder = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            builder.append((char) threadLocalRandom.nextInt(A_LETTER_CHARCODE, Z_LETTER_CHARCODE));
        }
        return builder.toString();
    }
}
