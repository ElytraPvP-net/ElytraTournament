package net.elytrapvp.elytratournament.event;

/**
 * The current status of the event.
 */
public enum EventStatus {
    /**
     * Event is currently not running.
     */
    NONE,

    /**
     * The event is waiting to be started.
     */
    WAITING,

    /**
     * The event is currently running.
     */
    RUNNING
}
