package b29.game.crew;

public enum CrewLandingStatus {
    BOMBS_EXPLODE_KIA_WRECKED("Bombs explode, crew KIA, and B-29 wrecked"),
    FIRE_EXPLOSION_KIA_WRECKED("Fuel explodes, crew KIA, and B-29 wrecked"),
    KIA_WRECKED("Crew KIA & B-29 Wrecked"),
    ROLL_FOR_WOUNDS_PLUS_ONE_WRECKED("Crew rolls for wounds (+1) & B-29 Wrecked"),
    ROLL_FOR_WOUNDS_WRECKED("Crew rolls for wounds & B-29 Wrecked"),
    SAFE_IRREPARABLY_DAMAGED("Crew safe, but B-29 irreparably damaged"),
    SAFE_REPAIRABLE("Crew safe & B-29 repairable for next mission"),
    SAFE("Crew and B-29 safe"),
    DITCHING_SUCCESSFUL_ROLL_FOR_WOUNDS("Successful ditching, crew wounded"),
    DITCHING_SUCCESSFUL("Successful ditching, crew uninjured");

    private String name;

    CrewLandingStatus(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
