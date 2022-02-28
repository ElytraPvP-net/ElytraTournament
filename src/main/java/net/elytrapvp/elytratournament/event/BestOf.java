package net.elytrapvp.elytratournament.event;

public enum BestOf {
    NONE(0),
    ONE(1),
    THREE(2),
    FIVE(3),
    SEVEN(4);

    private final int neededWins;
    BestOf(int neededWins) {
        this.neededWins = neededWins;
    }

    public int getNeededWins() {
        return neededWins;
    }
}
