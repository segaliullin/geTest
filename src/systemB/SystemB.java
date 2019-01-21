package systemB;

import common.AbstractSystem;
import common.ConsumerShutdownable;
import common.ProducerShutdownable;

import java.util.concurrent.Executors;

public class SystemB extends AbstractSystem implements ConsumerShutdownable {

    /**
     * Creates thread pool of consumers. Sizes it to available cores.
     * @param producerSystem provides messages for consumers
     */
    public SystemB(ProducerShutdownable producerSystem) {
        int availableCores = Runtime.getRuntime().availableProcessors();
        executorService = Executors.newFixedThreadPool(availableCores);
        for (int i = 0; i < availableCores; i++) {
            executorService.submit(new EventConsumer(producerSystem));
        }
    }

    @Override
    public boolean isReadyForShutdown() {
        return executorService.isTerminated();
    }
}
