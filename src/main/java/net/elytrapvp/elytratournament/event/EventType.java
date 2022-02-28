package net.elytrapvp.elytratournament.event;

/**
 * Represents the type of event being run.
 */
public enum EventType {
    /**
     * A tournament with only 1 loss.
     */
    SINGLE_ELIMINATION("Single Elim"),

    /**
     * A tournament with 2 losses.
     */
    DOUBLE_ELIMINATION("Double Elim"),

    /**
     * No selection currently made.
     */
    NONE("None");

    private final String toString;

    EventType(String toString) {
        this.toString = toString;
    }

    public String toString() {
        return toString;
    }
}