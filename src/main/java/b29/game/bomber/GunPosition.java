package b29.game.bomber;

public enum GunPosition {
    FWD_UPPER_TURRET( "Fwd Upper Turret" ),
    FWD_LOWER_TURRET( "Fwd Lower Turret" ),
    REAR_UPPER_TURRET( "Rear Upper Turret" ),
    REAR_LOWER_TURRET( "Rear Lower Turret" ),
    TAIL_TURRET( "Tail Turret" ),
    TAIL_CANNON( "Tail Cannon" );

    private String name;
    GunPosition( String name ) {
        this.name = name;
    }

    public String getName(){ return name; }
    public String toString(){ return name; }
}
