package b29.game.mission.chart;

import b29.game.bomber.*;
import b29.game.crew.CrewStatus;
import b29.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 7-1 through 7-14
 */
public class BomberDamageTable {

    /**
     * 7-1
     *
     * @return
     */
    public static List<DamageResult> getNoseDamage(Bomber bomber){
        int die = Util.roll2d();

        switch (die){
            case 2: {
                List<CrewPosition> crewPositions = new ArrayList<>();
                switch (Util.roll()){
                    case 1:
                        return Arrays.asList(new DamageResult(Damage.OXYGEN_SHELL_HIT, CrewPosition.PILOT), new DamageResult(Damage.OXYGEN_SHELL_HIT, CrewPosition.COPILOT));
                    case 2:
                        return Arrays.asList(new DamageResult(Damage.OXYGEN_SHELL_HIT, CrewPosition.PILOT));
                    case 3:
                        return Arrays.asList(new DamageResult(Damage.OXYGEN_SHELL_HIT, CrewPosition.COPILOT));
                    case 4:
                    case 5:
                        return Arrays.asList(new DamageResult(Damage.OXYGEN_SHELL_HIT, CrewPosition.BOMBARDIER));
                    case 6:{
                        // TODO Fire, roll to extinguish on Table 7-12
                        return Arrays.asList(new DamageResult(Damage.OXYGEN_OUT, BomberCompartment.NOSE)); // TODO See Section 7.4
                    }
                }
            }
            case 3:{
                return Arrays.asList(new DamageResult(Damage.BOMBARDIER_GUN_SIGHT_DAMAGED, BomberCompartment.NOSE));
            }
            case 4: {
                // TODO I don't understand the description in Table 7-1
                // TODO See Table 7-1 Note (c)
                return Arrays.asList(new DamageResult(Damage.LANDING_GEAR_HIT, BomberCompartment.NOSE));
            }
            case 5: {
                return Arrays.asList(new DamageResult(Damage.HYDRAULIC_RESERVOIR_HIT, BomberCompartment.NOSE));
            }
            case 6: {
                return getNoseCrewMembersHit();
            }
            case 7:
            case 11:{
                return Arrays.asList(new DamageResult(Damage.SUPERFICIAL_DAMAGE, BomberCompartment.NOSE));
            }
            case 8:{
                int numHits = bomber.countDamage(BomberCompartment.NOSE, Damage.WINDSHEILD_HIT);
                if (numHits == 2) {
                    // treat as #6
                    return getNoseCrewMembersHit();
                }
                return Arrays.asList(new DamageResult(Damage.WINDSHEILD_HIT, BomberCompartment.NOSE));
            }
            case 9:{
                return Arrays.asList(new DamageResult(Damage.COCKPIT_INSTRUMENTS_HIT, BomberCompartment.NOSE));
            }
            case 10:{
                return Arrays.asList(new DamageResult(Damage.NORDEN_BOMBSIGHT_DAMAGED, BomberCompartment.NOSE));
            }
            case 12:{
                return Arrays.asList(new DamageResult(Damage.RADIO_COMPASS_DAMAGED, BomberCompartment.NOSE));
            }
        }
        return Collections.emptyList();
    }

    private static List<DamageResult> getNoseCrewMembersHit(){
        int d = Util.roll();
        if (d == 1){
            return Arrays.asList(new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.PILOT), new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.COPILOT));
        }
        else if (d == 2){
            return Arrays.asList(new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.PILOT));
        }
        else if (d == 3){
            return Arrays.asList(new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.COPILOT));
        }
        else if (d == 4){
            return Arrays.asList(new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.BOMBARDIER));
        }
        else if (d == 5){
            return Arrays.asList(
                    new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.PILOT),
                    new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.COPILOT),
                    new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.BOMBARDIER));
        }
        else {
            if (Util.roll() <= 3){
                return Arrays.asList(
                        new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.PILOT),
                        new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.BOMBARDIER));
            }
            else {
                return Arrays.asList(
                        new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.COPILOT),
                        new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.BOMBARDIER));
            }
        }
    }


    /**
     * 7-2
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getNavRadioDamage(Bomber bomber){
        int die = Util.roll2d();
        // TODO Implement this
        return Collections.emptyList();
    }


    /**
     * 7-3
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getFwdBombBayDamage(Bomber bomber){
        int die = Util.roll2d();

        // TODO Implement this
        return Collections.emptyList();
    }

    /**
     * 7-4
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getAftBombBayDamage(Bomber bomber){
        int die = Util.roll2d();
        // TODO Implement this
        return Collections.emptyList();
    }

    /**
     * 7-5
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getWingsDamage(Bomber bomber){
        int die = Util.roll2d();
        // TODO Implement this
        return Collections.emptyList();
    }

    /**
     * 7-6
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getWaistDamage(Bomber bomber){
        int die = Util.roll2d();
        // TODO Implement this
        return Collections.emptyList();
    }


    /**
     * 7-7
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getUtilityDamage(Bomber bomber){
        int die = Util.roll2d();
        switch (die){
            case 2:{
                return Arrays.asList(new DamageResult(Damage.COMPRESSED_AIR_DUCT_HIT, BomberCompartment.UTILITY));
            }
            case 3:{
                return Arrays.asList(new DamageResult(Damage.AMMUNITION_FEED_TRAY_HIT, BomberCompartment.UTILITY));
            }
            case 4:
            case 7:
            case 11:{
                return Arrays.asList(new DamageResult(Damage.SUPERFICIAL_DAMAGE, BomberCompartment.UTILITY));
            }
            case 5:{
                return Arrays.asList(new DamageResult(Damage.AMMUNITION_BOX_DAMAGED, BomberCompartment.UTILITY));
            }
            case 6:{
                return Arrays.asList(new DamageResult(Damage.OXYGEN_CONTAINER_HIT, BomberCompartment.UTILITY));
            }
            case 8:{
                return Arrays.asList(new DamageResult(Damage.ELECTRICAL_SYSTEM_HIT, BomberCompartment.UTILITY));
            }
            case 9:{
                return Arrays.asList(new DamageResult(Damage.AFT_LOWER_TURRET_INOP, BomberCompartment.UTILITY));
            }
            case 10:{
                return Arrays.asList(new DamageResult(Damage.GAS_TANK_AUX_GENERATOR_HIT, BomberCompartment.UTILITY));
            }
            case 12:{
                return Arrays.asList(new DamageResult(Damage.FIRE_EXTINGUISHER_DESTROYED, BomberCompartment.UTILITY));
            }
        }
        return Collections.emptyList();
    }


    /**
     * 7-8
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getTailDamage(Bomber bomber){
        int die = Util.roll2d();
        switch (die){
            case 2: {
                if (Util.roll() == 6){
                    return Arrays.asList(new DamageResult(Damage.FIRE, BomberCompartment.TAIL), new DamageResult(Damage.OXYGEN_OUT, BomberCompartment.TAIL));
                }
                return Arrays.asList(new DamageResult(Damage.OXYGEN_OUT, CrewPosition.TAIL_GUNNER));
            }
            case 3:{
                return Arrays.asList(new DamageResult(Damage.TAIL_GUNNER_GUN_SIGHT_DAMAGED, BomberCompartment.TAIL));
            }
            case 4:
            case 6:
            case 11:
            case 12:{
                return Arrays.asList(new DamageResult(Damage.SUPERFICIAL_DAMAGE, BomberCompartment.TAIL));
            }
            case 5:{
                return Arrays.asList(new DamageResult(Damage.CREWMEMBER_HIT, CrewPosition.TAIL_GUNNER));
            }
            case 7:{
                return Arrays.asList(new DamageResult(Damage.RUDDER_HIT, BomberCompartment.TAIL));
            }
            case 8:
            case 10:{
                return Arrays.asList(new DamageResult(Damage.TAILPLANE_DAMAGED, BomberCompartment.TAIL));
            }
            case 9:{
                return Arrays.asList(new DamageResult(Damage.TAIL_TURRET_INOP, BomberCompartment.TAIL));
            }
        }
        return Collections.emptyList();
    }


    /**
     * 7-9
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getCockpitInstrumentsDamage(Bomber bomber){
        int die = Util.roll2d();
        switch (die){
            case 2:{
                return Arrays.asList(new DamageResult(Damage.PILOT_FLIGHT_CONTROLS_DAMAGED, BomberCompartment.NOSE));
            }
            case 3:{
                return Arrays.asList(new DamageResult(Damage.ALARM_BELL_INOP, BomberCompartment.NOSE));
            }
            case 4:{
                return Arrays.asList(new DamageResult(Damage.LANDING_GEAR_INOP, BomberCompartment.NOSE));
            }
            case 5:{
                return Arrays.asList(new DamageResult(Damage.ELEVATORS_SYSTEM_HIT, BomberCompartment.NOSE));
            }
            case 6:{
                return Arrays.asList(new DamageResult(Damage.AUTOPILOT_INOP, BomberCompartment.NOSE));
            }
            case 7:{
                return Arrays.asList(new DamageResult(Damage.SUPERFICIAL_DAMAGE, BomberCompartment.NOSE));
            }
            case 8:{
                return Arrays.asList(new DamageResult(Damage.RUDDER_SYSTEM_HIT, BomberCompartment.NOSE));
            }
            case 9:{
                return Arrays.asList(new DamageResult(Damage.AILERONS_SYSTEM_HIT, BomberCompartment.NOSE));
            }
            case 10:{
                return Arrays.asList(new DamageResult(Damage.PROPELLER_FEATHERING_SYSTEM_INOP, BomberCompartment.NOSE));
            }
            case 11:{
                return Arrays.asList(new DamageResult(Damage.FLAP_SYSTEM_INOP, BomberCompartment.NOSE));
            }
            case 12:{
                return Arrays.asList(new DamageResult(Damage.COPILOT_FLIGHT_CONTROLS_DAMAGED, BomberCompartment.NOSE));
            }
        }
        return Collections.emptyList();
    }

    /**
     * 7-10
     *
     * @param bomber
     * @return
     */
    public static List<DamageResult> getEngineerDamage(Bomber bomber){
        int die = Util.roll2d();
        // TODO Implement this
        return Collections.emptyList();
    }

    public static class DamageResult {
        public Damage damage;
        public BomberCompartment compartment;
        public CrewPosition crewPosition;

        public DamageResult(Damage damage, BomberCompartment bomberCompartment){
            this.damage = damage;
            this.compartment = bomberCompartment;
        }

        public DamageResult(Damage damage, CrewPosition crewPosition){
            this.damage = damage;
            this.compartment = null;
            this.crewPosition = crewPosition;
        }
    }


    /**
     * 7-11a
     * @return
     */
    public static ExplosiveDecompressionResult getExplosiveDecompressionResult(){
        int die = Util.roll();
        if (die == 6){
            return ExplosiveDecompressionResult.ALL_CREW_MEMBERS_WOUNDED;
        }
        return ExplosiveDecompressionResult.ROLL_FOR_EACH_CREW_MEMBER_WOUNDED;
    }

    /**
     * 7-11b
     * @return true if player should roll on 7-13 for crew member wound, or false if crew member successfully on oxygen
     */
    public static boolean isCrewMemberWoundedFromExplosiveCompression(){
        return Util.roll() == 6;
    }

    public enum ExplosiveDecompressionResult{
        ROLL_FOR_EACH_CREW_MEMBER_WOUNDED,  // Roll on 7-11b for each crewmember in affected compartment
        ALL_CREW_MEMBERS_WOUNDED      // Roll on 7-13 for each crewmember in affected compartment
    }


    /**
     * 7-12
     *
     * TODO See Section 7.5
     *
     * @param pressurized Is the compartment pressurized?
     * @param altitude Bomber's current altitude
     * @return
     */
    public static boolean isFireExtinguished(boolean pressurized, Altitude altitude){
        int die = Util.roll2d();
        if (!pressurized && altitude != Altitude.LO){
            die -= 1;
        }
        return die <= 9;
    }


    /**
     * 7-13
     *
     * @param fromImpactOfThermalTurbulence
     * @return
     */
    public static CrewStatus getCrewMemberWound(boolean fromImpactOfThermalTurbulence){
        int die = Util.roll();
        if (fromImpactOfThermalTurbulence)
            die -= 1;
        if (die <= 3)
            return CrewStatus.LIGHT_WOUND;
        if (die == 4 || die == 5)
            return CrewStatus.SERIOUS_WOUND;
        return CrewStatus.KIA;
    }

    public static CrewStatus getCrewMemberWound(){
        return getCrewMemberWound(false);
    }


    /**
     * 7-14
     *
     * @param bomberCompartment Bomber compartment where affected crew member is
     * @param compartmentPressurizationDamaged Has the compartment's pressurization system been damaged?
     * @param voluntarilyDepressurized Has the aircraft been voluntarily depressurized?
     * @return
     */
    public static boolean doesCrewMemberGetFrostbite(BomberCompartment bomberCompartment, boolean compartmentPressurizationDamaged, boolean voluntarilyDepressurized){
        int die = Util.roll();
        if (!compartmentPressurizationDamaged &&
                (bomberCompartment == BomberCompartment.NOSE ||
                        bomberCompartment == BomberCompartment.NAV_RADIO ||
                        bomberCompartment == BomberCompartment.WAIST ||
                        bomberCompartment == BomberCompartment.TAIL)){
            die += 1;
        }
        if (voluntarilyDepressurized)
            die += 1;
        if (die <= 2)
            return true;
        return false;
    }

    public static boolean doesCrewMemberGetFrostbite(Bomber bomber, BomberCompartment bomberCompartment){
        boolean compartmentPressurizationDamaged = bomber.hasDamage(bomberCompartment, Damage.PRESSURIZATION_FAILURE);
        boolean voluntarilyDepressurized = bomber.isVoluntaryDepressurization();
        return doesCrewMemberGetFrostbite(bomberCompartment, compartmentPressurizationDamaged, voluntarilyDepressurized);
    }

    private BomberDamageTable() {
    }
}

