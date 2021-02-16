package b29.game.mission;

public enum BomberDamageType {
    NO_EFFECT("No effect"),
    SUPERFICIAL_DAMAGE("Superficial Damage"),
    NORDEN_SIGHT("Norden Sight"),
    NOSE_GUN_INOPERABLE("Nose Gun Inoperable"),
    PORT_CHEEK_INOPERABLE("Port Cheek Gun Inoperable"),
    STBD_CHEEK_INOPERABLE("Starboard Cheek Gun Inoperable"),
    BOMBARDIER_AND_NAVIGATOR_HIT("Bombardier and Navigator Hit"),
    NAVIGATOR("Navigator Hit"),
    BOMBARDIER("Bombardier Hit"),
    NAVIGATOR_EQUIPMENT_INOPERABLE("Navigator Equipment Inoperable"),
    BOMB_CONTROLS_INOPERABLE("Bomb Controls Inoperable"),
    BOMBARDIER_HEAT_OUT("Bombardier Heat Out"),
    NAVIGATOR_HEAT_OUT("Navigator Heat Out"),
    BOMBARDIER_AND_NAVIGATOR_HEAT_OUT("Bombardier and Navigator Heat Out"),
    BOMBARDIER_OXYGEN_HIT("Bombardier Oxygen Hit"),
    NAVIGATOR_OXYGEN_HIT("Navigator Oxygen Hit"),
    BOMBARDIER_AND_NAVIGATOR_OXYGEN_HIT("Bombardier and Navigator Oxygen Hit"),
    FIRE_AND_NOSE_OXYGEN_OUT("Fire and Nose Oxygen Out"),
    PILOT_AND_COPILOT_HEAT_OUT("Pilot and Copilot Heat Out"),
    PILOT_HIT("Pilot Hit"),
    COPILOT_HIT("Copilot Hit"),
    PILOT_AND_COPILOT_HIT("Pilot and Copilot Hit"),
    TOP_TURRET_INOPERABLE("Top Turret Inoperable"),
    TOP_TURRENT_INOPERABLE_AND_ENGINEER_HIT("Top Turret Inoperable and Engineer Hit"),
    INSTRUMENTS("Instruments"),
    PILOT_AND_COPILOT_OXYGEN_HIT("Pilot and Copilot Oxygen Hit"),
    PILOT_OXYGEN_HIT("Pilot Oxygen Hit"),
    COPILOT_OXYGEN_HIT("Copilot Oxygen Hit"),
    ENGINEER_OXYGEN_HIT("Engineer Oxygen Hit"),
    ENGINEER_HIT("Engineer Hit"),
    FIRE_AND_PILOT_COMPARTMENT_OXYGEN_OUT("Fire and Pilot Compartment Oxygen Hit"),
    WINDOW("Window Hit"),
    CONTROL_CABLES("Control Cables Hit"),
    BOMBS_DROP_MANUALLY("Bombs Drop Manually"),
    BOMBS_DETONATE("Bombs Detonate"),
    RUBBER_RAFTS_HIT("Rubber Rafts Hit"),
    BOMB_BAY_DOORS_INOPERABLE("Bomb Bay Doors Inoperable"),
    RADIO_ROOM_HEAT_OUT("Radio Room Heat Out"),
    INTERCOM_SYSTEM_OUT("Intercom System Out"),
    RADIO_OUT("Radio Out"),
    RADIO_ROOM_OXYGEN_HIT("Radio Room Oxygen Hit"),
    RADIO_OPERATOR_HIT("Radio Operator Hit"),
    FIRE_AND_RADIO_ROOM_OXYGEN_OUT("Fire and Radio Room Oxygen Out"),
    PORT_WAIST_OXYGEN_HIT("Port Waist Oxygen Hit"),
    STBD_WAIST_OXYGEN_HIT("Starboard Waist Oxygen Hit"),
    PORT_WAIST_GUNNER_HIT("Port Waist Gunner Hit"),
    STBD_WAIST_GUNNER_HIT("Starboard Waist Gunner Hit"),
    BALL_GUNNER_OXYGEN_HIT("Ball Gunner Oxygen Hit"),
    BALL_GUNNER_HIT("Ball Gunner Hit"),
    FIRE_AND_WAIST_OXYGEN_OUT("Fire and Waist Oxygen Out"),
    PORT_WAIST_GUN_INOPERABLE("Port Waist Gun Inoperable"),
    STBD_WAIST_GUN_INOPERABLE("Starboard Waist Gun Inoperable"),
    BALL_GUNNER_HEAT_OUT("Ball Gunner Heat Out"),
    BALL_TURRET_INOPERABLE("Ball Turret Inoperable"),
    BALL_TURRET_MECHANISM_INOPERABLE("Ball Turret Mechanism Inoperable"),
    PORT_WAIST_GUNNER_AND_STBD_WAIST_GUNNER_HIT("Port and Starboard Waist Gunners Hit"),
    PORT_WAIST_GUNNER_HEAT_OUT("Port Waist Gunner Heat Out"),
    STBD_WAIST_GUNNER_HEAT_OUT("Starboard Waist Gunner Heat Out"),
    TAIL_GUNNER_HEAT_OUT("Tail Gunner Heat Out"),
    TAIL_GUNNER_HIT("Tail Gunner Hit"),
    TAIL_WHEEL_DAMAGED("Tailwheel Damaged"),
    AUTOPILOT_INOPERABLE("Autopilot Inoperable"),
    TAIL_TURRET_INOPERABLE("Tail Turret Inoperable"),
    RUDDER("Rudder Hit"),
    PORT_ELEVATOR_INOPERABLE("Port Elevator Inoperable"),
    STBD_ELEVATOR_INOPERABLE("Starboard Elevator Inoperable"),
    PORT_TAILPLANE_ROOT_HIT("Port Tailplane Root Hit"),
    STBD_TAILPLANE_ROOT_HIT("Starboard Tailplane Root Hit"),
    TAIL_OXYGEN_HIT("Tail Oxygen Hit"),
    FIRE_AND_TAIL_OXYGEN_OUT("Fire and Tail Oxygen Out");

    private String name;

    BomberDamageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
