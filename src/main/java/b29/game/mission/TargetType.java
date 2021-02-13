package b29.game.mission;

public enum TargetType {
    AIRFIELD("Airfield"),
    AIRCRAFT_FACTORY("Aircraft Factory"),
    URBAN_AREA("Rail Yard");

    private String name;

    TargetType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
