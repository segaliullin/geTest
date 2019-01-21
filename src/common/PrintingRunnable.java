package common;

public abstract class PrintingRunnable implements Runnable{

    protected void printStdout(Object msg) {
        System.out.println(new StringBuilder()
            .append(this.getClass().getSimpleName())
            .append(" ")
            .append(Thread.currentThread().getId())
            .append(": ")
            .append(msg.toString())
            .toString());
    }
}
