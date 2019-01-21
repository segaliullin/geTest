package common;

/**
 * Classes which implement this interface should be able to shutdown gracefully
 * using interrupt signal
 */
public interface Shutdownable {

    void shutdown();

    boolean isReadyForShutdown();

}
