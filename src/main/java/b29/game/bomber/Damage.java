package b29.game.bomber;

public enum Damage {
    AFT_LOWER_TURRET_INOP,  // Utility
    AILERONS_INOP_PORT,
    AILERONS_INOP_STBD,
    AILERONS_SYSTEM_HIT, // Nose
    ALARM_BELL_INOP,
    AMMUNITION_BOX_DAMAGED,
    AMMUNITION_FEED_TRAY_HIT,
    AUTOPILOT_INOP,
    AUX_GENERATOR_INOP,  // Utility
    BOMB_BAY_DOORS_OPEN,
    BOMBARDIER_GUN_SIGHT_DAMAGED,
    BURST_INSIDE_PLANE,    // BIP
    CFC_SYSTEM_FAILURE,
    COCKPIT_INSTRUMENTS_HIT,
    COMPRESSED_AIR_DUCT_HIT,   // Utility
    COPILOT_FLIGHT_CONTROLS_DAMAGED,
    CREWMEMBER_HIT,
    ELECTRICAL_SYSTEM_HIT,   // Utility
    ELEVATORS_SYSTEM_HIT, // Nose
    ELEVATORS_INOP_PORT,
    ELEVATORS_INOP_STBD,
    ENGINE_1_OUT,          // Wings
    ENGINE_2_OUT,
    ENGINE_3_OUT,
    ENGINE_4_OUT,
    FIRE,                   // Any compartment (handled with Table 7-12)
    FIRE_EXTINGUISHER_DESTROYED,  // Utility
    FLAP_INOP,              // Wings
    FLAP_SYSTEM_INOP,       // Nose
    FROSTED_WINDOWS,
    GAS_TANK_AUX_GENERATOR_HIT,  // Utility
    GYRO_FLUX_GATE_DAMAGED,
    HYDRAULIC_RESERVOIR_HIT,
    INTERCOM_FAILURE,
    LANDING_GEAR_HIT,    // Nose, Waist?
    LANDING_GEAR_INOP,   // Nose, Waist?
    LORAN_DAMAGED,
    NAVIGATOR_TOOLS_DAMAGED,
    NORDEN_BOMBSIGHT_DAMAGED,  // Nose
    OXYGEN_SHELL_HIT,
    OXYGEN_OUT,
    OXYGEN_CONTAINER_HIT,      // Utility
    PILOT_FLIGHT_CONTROLS_DAMAGED,
    PRESSURIZATION_FAILURE,
    PROPELLER_FEATHERING_SYSTEM_INOP,  // Nose
    RADAR_FAILURE,
    RADIO_COMPASS_DAMAGED,
    RADAR_DAMAGED,
    RUBBER_LIFE_RAFTS_HIT,
    RUDDER_SYSTEM_HIT,  // Nose (controls)
    RUDDER_HIT,  // Tail
    RUDDER_INOP, // Tail
    RUNAWAY_PROP,                   // Wings
    SUPERFICIAL_DAMAGE,
    TAIL_GUNNER_GUN_SIGHT_DAMAGED,  // Tail
    TAILPLANE_DAMAGED,              // Tail
    TAILPLANE_ROOT_HIT_PORT,
    TAILPLANE_ROOT_HIT_STBD,
    TAILPLANE_RIPPED_OFF_PORT,
    TAILPLANE_RIPPED_OFF_STBD,
    TAIL_TURRET_INOP,               // Tail (tail gun & cannon may not fire)
    WINDMILLING_PROP,
    WINDSHEILD_HIT,
    WING_FLAPS_INOP_PORT,
    WING_FLAPS_INOP_STBD
    ;

    public static Damage getEngineOut(int engineNumber){
        return engineNumber == 1? ENGINE_1_OUT: engineNumber == 2? ENGINE_2_OUT: engineNumber == 3? ENGINE_3_OUT: ENGINE_4_OUT;
    }
}
