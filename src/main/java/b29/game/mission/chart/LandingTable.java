package b29.game.mission.chart;

import b29.game.Experience;
import b29.game.bomber.*;
import b29.game.crew.CrewLandingStatus;
import b29.game.crew.CrewMember;
import b29.game.crew.CrewStatus;
import b29.game.mission.*;
import b29.util.Util;

/**
 * 8-1 through 8-7
 */
public class LandingTable {
    /*
    private static final CrewLandingStatus[] ON_LAND_CREW_STATUS = new CrewLandingStatus[]{
            CrewLandingStatus.KIA,                        // -3
            CrewLandingStatus.ROLL_FOR_WOUNDS_PLUS_ONE,   // -2
            CrewLandingStatus.ROLL_FOR_WOUNDS,            // -1
            CrewLandingStatus.SAFE,                       //  0
            CrewLandingStatus.SAFE,                       //  1
            CrewLandingStatus.SAFE,                       // 2-12
    };

    private static final BomberStatus[] ON_LAND_BOMBER_STATUS = new BomberStatus[]{
            BomberStatus.WRECKED,    // -3
            BomberStatus.WRECKED,    // -2
            BomberStatus.WRECKED,    // -1
            BomberStatus.WRECKED,    //  0
            BomberStatus.REPAIRABLE, //  1
            BomberStatus.SAFE        // 2-12
    };

    private static final CrewLandingStatus[] ON_WATER = new CrewLandingStatus[]{
            CrewLandingStatus.LOST,
            CrewLandingStatus.LOST,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
            CrewLandingStatus.RESCUED,
    };
     */

    /**
     * 8-1
     * @param mission
     * @param bomber
     * @return
     */
    public static CrewLandingStatus getCrewLandingStatusOnLand(Mission mission, Bomber bomber) {
        int die = Util.roll2d();

        if (die == 12)
            return CrewLandingStatus.SAFE;

        if (bomber.getCrewMemberByRole(CrewPosition.PILOT).getExperience() == Experience.VETERAN)
            die += 1;
        if (bomber.hasDamage(BomberCompartment.NOSE, Damage.LANDING_GEAR_INOP))
            die -= 1;
        if (bomber.hasDamage(Damage.WING_FLAPS_INOP_PORT) && bomber.hasDamage(Damage.WING_FLAPS_INOP_STBD))
            die -= 1;
        if (bomber.hasDamage(Damage.AILERONS_INOP_PORT) && bomber.hasDamage(Damage.AILERONS_INOP_STBD))
            die -= 1;
        if (bomber.hasDamage(Damage.ELEVATORS_INOP_PORT) && bomber.hasDamage(Damage.ELEVATORS_INOP_STBD))
            die -= 1;
        if (bomber.hasDamage(Damage.RUDDER_INOP))
            die -= 1;
        if (bomber.countDamage(BomberCompartment.NOSE, Damage.WINDSHEILD_HIT) >= 2)
            die -= 1;
        if (bomber.hasDamage(Damage.TAILPLANE_RIPPED_OFF_PORT) || bomber.hasDamage(Damage.TAILPLANE_RIPPED_OFF_STBD))
            die -= 1;
        die -= bomber.countEnginesOut();
        if (bomber.getBrakeCapability() == BrakeCapability.RELIABILITY_REDUCED)
            die -= 2;
        die -= (2 * (bomber.countDamage(BomberCompartment.PORT_WING, Damage.WINDMILLING_PROP) + bomber.countDamage(BomberCompartment.STBD_WING, Damage.WINDMILLING_PROP)));
        if (mission.getWeather() == Weather.POOR)
            die -= 2;
        if (mission.getActualLandingTimeOfDay() == TimeOfDay.NIGHT)
            die -= 2;
        if (bomber.hasDamage(BomberCompartment.NAV_RADIO, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.FWD_BOMB_BAY, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.REAR_BOMB_BAY, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.WAIST, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.UTILITY, Damage.BURST_INSIDE_PLANE))
            die -= 2;
        if (mission.getWeather() == Weather.BAD)
            die -= 3;
        die -= (3 * (bomber.countDamage(BomberCompartment.PORT_WING, Damage.RUNAWAY_PROP) + bomber.countDamage(BomberCompartment.STBD_WING, Damage.RUNAWAY_PROP)));
        if (bomber.hasDamage(Damage.LANDING_GEAR_INOP))
            die -= 3;
        if (bomber.getBrakeCapability() == BrakeCapability.LOST)
            die -= 6;

        // If pilot and copilot are dead/SW and another crew member is attempting to land the plane...
        if (bomber.getCrewMemberByDefaultRole(CrewPosition.PILOT).isSwOrKia() &&
                bomber.getCrewMemberByDefaultRole(CrewPosition.COPILOT).isSwOrKia() &&
                !bomber.getCrewMemberByRole(CrewPosition.PILOT).isSwOrKia())
            die -= 10;

        if (die <= 0 && bomber.isCarryingBombs() && Util.roll() == 6){
            return CrewLandingStatus.BOMBS_EXPLODE_KIA_WRECKED;
        }

        if (die <= 0 && Util.roll2d() < bomber.getTotalFuelLeft()){
            return CrewLandingStatus.FIRE_EXPLOSION_KIA_WRECKED;
        }

        if (die <= -3){
            return CrewLandingStatus.KIA_WRECKED;
        }
        else if (die == -2){
            return CrewLandingStatus.ROLL_FOR_WOUNDS_PLUS_ONE_WRECKED;
        }
        else if (die == -1){
            return CrewLandingStatus.ROLL_FOR_WOUNDS_WRECKED;
        }
        else if (die == 0){
            return CrewLandingStatus.SAFE_IRREPARABLY_DAMAGED;
        }
        else if (die == 1){
            return CrewLandingStatus.SAFE_REPAIRABLE;
        }
        return CrewLandingStatus.SAFE;
    }

    /**
     * 8-2
     * @param weather
     * @return
     */
    public static SeaState getSeaState(Weather weather){
        int die = Util.roll();
        if (weather == Weather.POOR)
            die += 1;
        else if (weather == Weather.BAD)
            die += 2;
        switch (die){
            case 1:
                return SeaState.CALM;
            case 2:
            case 3:
                return SeaState.SMOOTH_TO_SLIGHT;
            case 4:
                return SeaState.MODERATE;
            case 5:
                return SeaState.ROUGH;
            case 6:
                return SeaState.VERY_ROUGH;
            case 7:
                return SeaState.HIGH;
            case 8:
                return SeaState.VERY_HIGH;
        }
        return SeaState.CALM;
    }


    /**
     * 8-3
     * @param mission
     * @param bomber
     * @return
     */
    public static CrewLandingStatus getCrewLandingStatusInWater(Mission mission, Bomber bomber){
        int die = Util.roll2d();

        if (die == 12)
            return CrewLandingStatus.SAFE;

        if (bomber.getCrewMemberByRole(CrewPosition.PILOT).getExperience() == Experience.VETERAN)
            die += 1;
        if (mission.getWeather() == Weather.GOOD)
            die += 1;
        if (mission.getWeather() == Weather.BAD)
            die -= 1;

        SeaState seaState = getSeaState(mission.getWeather());
        switch (seaState){
            case CALM:
                break;
            case SMOOTH_TO_SLIGHT:
                die -= 1;
                break;
            case MODERATE:
                die -= 2;
                break;
            case ROUGH:
                die -= 3;
                break;
            case VERY_ROUGH:
                die -= 4;
                break;
            case HIGH:
                die -= 5;
                break;
            case VERY_HIGH:
                die -= 6;
                break;
        }
        if (mission.getWeather() == Weather.BAD)
            die -= 1;
        if (bomber.isCarryingBombs())
            die -= 1;
        if (bomber.hasDamage(Damage.WING_FLAPS_INOP_PORT) && bomber.hasDamage(Damage.WING_FLAPS_INOP_STBD))
            die -= 1;
        if (bomber.hasDamage(Damage.AILERONS_INOP_PORT) && bomber.hasDamage(Damage.AILERONS_INOP_STBD))
            die -= 1;
        if (bomber.hasDamage(Damage.ELEVATORS_INOP_PORT) && bomber.hasDamage(Damage.ELEVATORS_INOP_STBD))
            die -= 1;
        if (bomber.hasDamage(Damage.RUDDER_INOP))
            die -= 1;
        if (bomber.countDamage(BomberCompartment.NOSE, Damage.WINDSHEILD_HIT) >= 2)
            die -= 1;
        if (bomber.hasDamage(Damage.TAILPLANE_RIPPED_OFF_PORT) || bomber.hasDamage(Damage.TAILPLANE_RIPPED_OFF_STBD))
            die -= 1;
        die -= bomber.countEnginesOut();
        die -= (2 * (bomber.countDamage(BomberCompartment.PORT_WING, Damage.WINDMILLING_PROP) + bomber.countDamage(BomberCompartment.STBD_WING, Damage.WINDMILLING_PROP)));
        if (bomber.hasDamage(BomberCompartment.NAV_RADIO, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.FWD_BOMB_BAY, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.REAR_BOMB_BAY, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.WAIST, Damage.BURST_INSIDE_PLANE) ||
                bomber.hasDamage(BomberCompartment.UTILITY, Damage.BURST_INSIDE_PLANE))
            die -= 2;
        if (mission.getActualLandingTimeOfDay() == TimeOfDay.NIGHT)
            die -= 2;
        die -= (3 * (bomber.countDamage(BomberCompartment.PORT_WING, Damage.RUNAWAY_PROP) + bomber.countDamage(BomberCompartment.STBD_WING, Damage.RUNAWAY_PROP)));
        boolean fwdBombBayDoorsOpen = bomber.hasDamage(BomberCompartment.FWD_BOMB_BAY, Damage.BOMB_BAY_DOORS_OPEN);
        boolean aftBombBayDoorsOpen = bomber.hasDamage(BomberCompartment.REAR_BOMB_BAY, Damage.BOMB_BAY_DOORS_OPEN);
        if (fwdBombBayDoorsOpen && aftBombBayDoorsOpen){
            die -= 5;
        }
        else if (fwdBombBayDoorsOpen || aftBombBayDoorsOpen){
            die -= 4;
        }

        // If pilot and copilot are dead/SW and another crew member is attempting to land the plane...
        if (bomber.getCrewMemberByDefaultRole(CrewPosition.PILOT).isSwOrKia() &&
                bomber.getCrewMemberByDefaultRole(CrewPosition.COPILOT).isSwOrKia() &&
                !bomber.getCrewMemberByRole(CrewPosition.PILOT).isSwOrKia())
            die -= 10;


        if (die <= 2)
            return CrewLandingStatus.KIA_WRECKED;
        if (die == 3)
            return CrewLandingStatus.ROLL_FOR_WOUNDS_PLUS_ONE_WRECKED;
        if (die == 4) // TODO Is the B-29 wrecked?
            return CrewLandingStatus.DITCHING_SUCCESSFUL_ROLL_FOR_WOUNDS;
        return CrewLandingStatus.DITCHING_SUCCESSFUL; // TODO Is the B-29 wrecked?
    }

    /**
     * 8-4
     *
     * Roll for each crew member
     *
     * TODO Ask player if they want to depressurize before rolling on this table
     *
     * @param bomber
     * @return
     */
    public static BailOutStatus getControlledBailOutStatus(Bomber bomber, CrewMember crewMember){
        int die = Util.roll();
        if (bomber.hasDamage(Damage.INTERCOM_FAILURE) && bomber.hasDamage(Damage.ALARM_BELL_INOP))
            die -= 1;
        if (crewMember.countWounds(CrewStatus.LIGHT_WOUND) > 1)
            die -= 1;
        BailOutStatus status = BailOutStatus.OK;
        if (die <= 1 && Util.roll() == 6)
            status = BailOutStatus.CREWMEMBER_KIA;
        return status;
    }


    /**
     * 8-5
     *
     * Roll for each crew member
     *
     * @return
     */
    public static BailOutStatus getUncontrolledBailOutStatus(){
        return Util.roll() == 6? BailOutStatus.OK: BailOutStatus.CREWMEMBER_KIA;
    }


    /**
     * 8-6
     *
     * Roll for each crew member
     *
     * TODO From errata: After checking sea state on Table 8-2, a player may choose to have his crewmembers bailout (see
     * 8.2) rather than attempt ditching at sea.
     *
     * @param mission
     * @param bomber
     * @param seaState
     * @param crewLandingStatus
     * @param allowInRaft (See Table 8-6 modifier for successful ditching)
     * @return
     */
    public static SurvivalAtSeaResult getSurvivalAtSea(Mission mission, Bomber bomber, CrewMember crewMember, SeaState seaState, CrewLandingStatus crewLandingStatus, boolean allowInRaft){
        int die = Util.roll();
        if (mission.getZone() == 1)
            die += 2;
        if (mission.getZone() == 2 || (mission.getZone() == 6 && mission.getMissionNumber() >= 11))
            die += 1;
        switch (seaState){
            case CALM:
                die += 1;
                break;
            case SMOOTH_TO_SLIGHT:
            case MODERATE:
                break;
            case ROUGH:
                die -= 1;
                break;
            case VERY_ROUGH:
                die -= 1;
                break;
            case HIGH:
                die -= 2;
                break;
            case VERY_HIGH:
                die -= 2;
                break;
        }
        if (mission.getWeather() == Weather.GOOD)
            die += 1;

        if (crewLandingStatus == CrewLandingStatus.DITCHING_SUCCESSFUL || crewLandingStatus == CrewLandingStatus.DITCHING_SUCCESSFUL_ROLL_FOR_WOUNDS){
            boolean fwdRaftsHit = bomber.hasDamage(BomberCompartment.FWD_BOMB_BAY, Damage.RUBBER_LIFE_RAFTS_HIT);
            boolean aftRaftsHit = bomber.hasDamage(BomberCompartment.REAR_BOMB_BAY, Damage.RUBBER_LIFE_RAFTS_HIT);
            if ((!fwdRaftsHit && !aftRaftsHit) || ((fwdRaftsHit || aftRaftsHit) && allowInRaft)){
                die += 1;
            }
        }
        if (mission.getMissionNumber() <= 15)
            die -= 1;
        if (mission.getWeather() == Weather.BAD)
            die -= 1;
        if (mission.getZone() >= 9 && mission.getZone() <= 14)
            die -= 1;
        if (crewMember.getStatus() == CrewStatus.SERIOUS_WOUND)
            die -= 1;
        // TODO -2 if landed in water or bailed out while radio was out
        if (die <= 3)
            return SurvivalAtSeaResult.DIES;
        return SurvivalAtSeaResult.RESCUED;
    }


    /**
     * 8-7
     * @param crewMember
     * @return
     */
    public static boolean survivesPOW(CrewMember crewMember){
        int die = Util.roll();
        if (crewMember.getStatus() == CrewStatus.SERIOUS_WOUND)
            die -= 1;
        if (die <= 3)
            return false; // Dies in captivity
        return true; // survives, liberated at end of war
    }
}
