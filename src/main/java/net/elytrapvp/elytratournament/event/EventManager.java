package net.elytrapvp.elytratournament.event;

import net.elytrapvp.elytratournament.ElytraTournament;
import net.elytrapvp.elytratournament.event.kit.Kit;
import org.bukkit.entity.Player;

/**
 * Manages the current event and event settings.
 */
public class EventManager {
    private final ElytraTournament plugin;
    private Player host;
    private EventType eventType;
    private Event activeEvent;
    private Kit kit;
    private EventStatus eventStatus;
    private BestOf bestOf;

    /**
     * Creates the manager.
     * @param plugin Plugin instance.
     */
    public EventManager(ElytraTournament plugin) {
        this.plugin = plugin;

        host = null;
        eventType = EventType.NONE;
        activeEvent = null;
        kit = null;
        eventStatus = EventStatus.NONE;
    }

    /**
     * Get the active event.
     * @return active event.
     */
    public Event activeEvent() {
        return activeEvent;
    }

    /**
     * Change the active event.
     * @param activeEvent new active event.
     */
    public void activeEvent(Event activeEvent) {
        this.activeEvent = activeEvent;
    }

    /**
     * Get the current best of.
     * @return Current best of.
     */
    public BestOf bestOf() {
        return bestOf;
    }

    /**
     * Change the best of.
     * @param bestOf new best of.
     */
    public void bestOf(BestOf bestOf) {
        this.bestOf = bestOf;
    }

    /**
     * Creates a new event using the existing settings.
     */
    public void create() {
        activeEvent = new Event(plugin);
    }

    /**
     * Get the current event status.
     * @return current event status.
     */
    public EventStatus eventStatus() {
        return eventStatus;
    }

    /**
     * Change the event status.
     * @param eventStatus new event status.
     */
    public void eventStatus(EventStatus eventStatus) {
        this.eventStatus = eventStatus;
    }

    /**
     * Get the current event type.
     * @return current event type.
     */
    public EventType eventType() {
        return eventType;
    }

    /**
     * Change the event type.
     * @param eventType new event type.
     */
    public void eventType(EventType eventType) {
        this.eventType = eventType;
    }

    /**
     * Get the current host.
     * Is null if there is none.
     * @return current host.
     */
    public Player host() {
        return host;
    }

    /**
     * Change the host.
     * @param host new host.
     */
    public void host(Player host) {
        this.host = host;
    }

    /**
     * Get the current kit selected.
     * Is null if not set.
     * @return current kit selected.
     */
    public Kit kit() {
        return kit;
    }

    /**
     * Change the current kit.
     * @param kit new kit.
     */
    public void kit(Kit kit) {
        this.kit = kit;
    }

    /**
     * Resets an event. Used if an event is canceled or ended.
     */
    public void reset() {
        activeEvent = null;
        eventType = EventType.NONE;
        eventStatus = EventStatus.NONE;
        host = null;
        kit = null;
    }
}
