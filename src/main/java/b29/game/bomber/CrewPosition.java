package b29.game.bomber;

public enum CrewPosition {
    BOMBARDIER("Bombardier"),
    NAVIGATOR("Navigator"),
    PILOT("Pilot"),
    COPILOT("Copilot"),
    ENGINEER("Engineer"),
    RADIO_OPERATOR("Radio Operator"),
    CFC_CONTROLLER("CFC Controller"),
    LEFT_GUNNER("Left Gunner"),
    RIGHT_GUNNER("Right Gunner"),
    RADAR_OPERATOR("Radar Operator"),
    TAIL_GUNNER("Tail Gunner");

    private String name;

    CrewPosition(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
