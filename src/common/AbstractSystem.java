package common;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class AbstractSystem implements Shutdownable {

    protected ExecutorService executorService;

    /**
     * Shutting down system. Stopping all tasks (threads)
     */
    @Override
    public void shutdown() {
        System.out.println(this.getClass().getSimpleName() + ": shutting down...");
        executorService.shutdownNow();
        try {
            boolean shutdown = false;
            while (!shutdown) {
                shutdown = executorService.awaitTermination(5, TimeUnit.SECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
