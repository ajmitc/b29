package b29.game.mission;

public enum FighterAltitude {
    LOW("Low"),
    LEVEL("Level"),
    HIGH("High");

    private String name;

    FighterAltitude(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
