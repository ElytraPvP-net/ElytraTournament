package net.elytrapvp.elytratournament.event;

public enum BestOf {
    NONE(0, "BoO"),
    ONE(1, "Bo1"),
    THREE(2, "Bo3"),
    FIVE(3, "Bo5"),
    SEVEN(4, "Bo7");

    private final int neededWins;
    private final String toString;
    BestOf(int neededWins, String toString) {
        this.neededWins = neededWins;
        this.toString = toString;
    }

    public int getNeededWins() {
        return neededWins;
    }

    public String toString() {
        return toString;
    }
}
