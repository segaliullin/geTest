import common.ConsumerShutdownable;
import common.ProducerShutdownable;
import common.Shutdownable;
import systemA.SystemA;
import systemB.SystemB;

import java.util.ArrayList;
import java.util.List;

public class Main {

    private static final int CONSUMER_SYSTEMS_COUNT = 4;
    private static final List<Shutdownable> systemsList = new ArrayList<>();

    /**
     * Entry point. Initiates SystemA and several SystemB instances.
     * @param args
     */
    public static void main(String[] args) {
        ProducerShutdownable systemA = SystemA.getInstance();
        systemsList.add(systemA);

        for (int i = 0; i < CONSUMER_SYSTEMS_COUNT; i++) {
            ConsumerShutdownable systemB = new SystemB(systemA);
            systemsList.add(systemB);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            java.lang.System.out.println("SIGINT received");
            for (Shutdownable sys: systemsList) {
                sys.shutdown();
            }
        }));
    }
}
