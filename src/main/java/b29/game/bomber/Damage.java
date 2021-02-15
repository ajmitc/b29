package b29.game.bomber;

public enum Damage {
    AMMUNITION_FEED_TRAY,
    CFC_SYSTEM_FAILURE,
    ENGINE_1_OUT,
    ENGINE_2_OUT,
    ENGINE_3_OUT,
    ENGINE_4_OUT,
    FROSTED_WINDOWS,
    GYRO_FLUX_GATE_DAMAGED,
    INTERCOM_FAILURE,
    LORAN_DAMAGED,
    NAVIGATOR_TOOLS_DAMAGED,
    OXYGEN_SHELL_HIT,
    OXYGEN_OUT,
    RADAR_FAILURE,
    RADIO_COMPASS_DAMAGED,
    RADAR_DAMAGED
    ;

    public static Damage getEngineOut(int engineNumber){
        return engineNumber == 1? ENGINE_1_OUT: engineNumber == 2? ENGINE_2_OUT: engineNumber == 3? ENGINE_3_OUT: ENGINE_4_OUT;
    }
}
