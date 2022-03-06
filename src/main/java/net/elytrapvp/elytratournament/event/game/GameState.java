package net.elytrapvp.elytratournament.event.game;

/**
 * Represents the current state of a Game.
 */
public enum GameState {
    /**
     * Game is currently waiting to be played.
     */
    WAITING,

    /**
     * Game is currently running the countdown.
     */
    COUNTDOWN,

    /**
     * Game is now running.
     */
    RUNNING,

    /**
     * Game is between rounds in a best of game.
     */
    ROUND_OVER,

    /**
     * Game has ended.
     */
    END
}