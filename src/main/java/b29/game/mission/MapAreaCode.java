package b29.game.mission;

public enum MapAreaCode {
    WATER( "W" ),
    MARIANAS( "M" ),
    IWO_JIMA( "I" ),
    JAPAN( "J" ),
    OKINAWA("O");

    private String name;
    MapAreaCode( String name ) {
        this.name = name;
    }

    public String getName(){ return name; }
    public String toString(){ return name; }
}
