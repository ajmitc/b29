package b29.game.mission;

public enum FormationPosition {
    LEAD( "Lead" ),
    MIDDLE( "Middle" ),
    TAIL( "Tail" ),
    LESS_THAN_10000_FT( "<= 10,000 ft" );

    private String desc;
    FormationPosition( String desc ) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public String toString(){ return desc; }
}
