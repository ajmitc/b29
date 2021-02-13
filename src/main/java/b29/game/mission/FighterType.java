package b29.game.mission;

public enum FighterType {
    FIGHTER_F6F_HELLCAT( "F6F Hellcat" ),
    FIGHTER_P38( "P-38" ),
    FIGHTER_P51( "P-51" ),

    FIGHTER_NICK("Kawasaki Ki-45 Toryu"),
    FIGHTER_TONY("Kawasaki Ki-61 Hien"),
    FIGHTER_ZEKE("Mitsubishi A6M5 Type 'Zero', Model 52"),
    FIGHTER_FRANK("Nakajima Ki-84 Hayate"),
    FIGHTER_TOJO("Nakajima Ki-44 Shoki"),
    FIGHTER_OSCAR("Nakajima Ki-43 Hayabusa"),
    FIGHTER_GEORGE("Kawanishi N1K2-J Shiden"),
    FIGHTER_JACK("Mitsubishi J2M Raiden"),
    FIGHTER_IRVING("Irving"),

    // For behavior, see Chart book pg 15, Table 5-3 Note b
    BAKA_SUICIDE_ROCKER("Baka Suicide Rocket"),

    LIGHT_FLAK("Japanese launch rockets into bomber formation"),
    MEDIUM_FLAK("Japanese launch rockets into bomber formation");

    private String name;
    FighterType( String name ) {
        this.name = name;
    }

    public String getName(){ return name; }
    public String toString(){ return name; }
}
