package b29.game.mission;

public enum JapaneseFighterDamage {
    FCA( "Fighter damaged but continues attack at -1" ),
    FBOA( "Fighter damaged and breaks off after this attack -2" ),
    DESTROYED( "Fighter destroyed and may not fire at B-17" );

    private String name;
    JapaneseFighterDamage(String name ) {
        this.name = name;
    }

    public String getName(){ return name; }
    public String toString(){ return name; }
}
