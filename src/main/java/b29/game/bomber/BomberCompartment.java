package b29.game.bomber;

public enum BomberCompartment {
    NOSE( "Nose" ),
    NAV_RADIO( "Nav/Radio" ),
    FWD_BOMB_BAY( "Fwd Bomb Bay" ),
    REAR_BOMB_BAY( "Aft Bomb Bay" ),
    WAIST( "Waist" ),
    UTILITY("Utility"),
    TAIL( "Tail" ),
    PORT_WING( "Port Wing" ),
    STBD_WING( "Starboard Wing" );

    private String name;
    BomberCompartment(String name ) {
        this.name = name;
    }

    public String getName(){ return name; }
    public String toString(){ return name; }
}
