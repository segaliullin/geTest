package systemA;

import java.util.Objects;

/**
 * Processing object. Encapsulates some message and its priority.
 * Implements Comparable interface for sorting in queues.
 */
public class Event implements Comparable<Event> {

    private String message;
    private int priority;

    public Event(String message, int priority) {
        this.message = message;
        this.priority = priority;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return priority == event.priority &&
                message.equals(event.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, priority);
    }

    @Override
    public int compareTo(Event o) {
        return Integer.compare(this.priority, o.priority);
    }

    @Override
    public String toString() {
        return "Event{" +
                "PRIORITY=" + priority +
                ", MSG='" + message + '\'' +
                '}';
    }
}
