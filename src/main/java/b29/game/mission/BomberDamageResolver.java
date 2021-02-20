package b29.game.mission;

import b29.game.bomber.*;
import b29.game.crew.CrewMember;
import b29.game.mission.chart.BomberDamageTable;
import b29.util.Util;

import java.util.List;

public class BomberDamageResolver {
    /**
     * Apply Damage to Bomber
     * @param mission
     * @param bomber
     * @param damageResults
     */
    public void apply(Mission mission, Bomber bomber, List<BomberDamageTable.DamageResult> damageResults){
        for (BomberDamageTable.DamageResult damageResult: damageResults){
            if (damageResult.compartment == BomberCompartment.NOSE) {
                applyNoseDamage(mission, bomber, damageResult);
            }
        }
    }

    /**
     * Apply damage to the Nose and Cockpit Instruments areas
     * @param mission
     * @param bomber
     * @param damageResult
     */
    private void applyNoseDamage(Mission mission, Bomber bomber, BomberDamageTable.DamageResult damageResult){
        switch (damageResult.damage){
            case OXYGEN_SHELL_HIT:{
                bomber.getCrewPositionDamage().get(damageResult.crewPosition).add(Damage.OXYGEN_SHELL_HIT);
                applyPossibleCompromisedPressurization(bomber);
                break;
            }
            case OXYGEN_OUT:
            case BOMBARDIER_GUN_SIGHT_DAMAGED:
            case LANDING_GEAR_HIT:
            case NORDEN_BOMBSIGHT_DAMAGED:
            case RADIO_COMPASS_DAMAGED:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                applyPossibleCompromisedPressurization(bomber);
                break;
            }
            case HYDRAULIC_RESERVOIR_HIT:{
                bomber.setBrakeCapability(BrakeCapability.RELIABILITY_REDUCED);
                if (mission.getZone() == mission.getTargetZone()){
                    if (Util.roll() >= 5){
                        // Roll for Bombardier wound on 7-13
                        CrewMember crewMember = bomber.getCrewMemberByRole(CrewPosition.BOMBARDIER);
                        crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                    }
                }
                applyPossibleCompromisedPressurization(bomber);
                if (Util.roll() <= 3){
                    // TODO Hydraulic fluid catches fire, roll to extinguish (See Section 7.5) on table 7-12
                }
                break;
            }
            case CREWMEMBER_HIT:{
                // Roll on 7-13 for crew member
                CrewMember crewMember = bomber.getCrewMemberByRole(damageResult.crewPosition);
                crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                applyPossibleCompromisedPressurization(bomber);
                break;
            }
            case WINDSHEILD_HIT:{
                bomber.addDamage(BomberCompartment.NOSE, Damage.WINDSHEILD_HIT);
                int numHits = bomber.countDamage(BomberCompartment.NOSE, Damage.WINDSHEILD_HIT);
                if (numHits == 2){
                    boolean nosePressurized = bomber.isCompartmentPressurized(BomberCompartment.NOSE);
                    boolean navPressurized = bomber.isCompartmentPressurized(BomberCompartment.NAV_RADIO);
                    bomber.addDamage(BomberCompartment.NOSE, Damage.PRESSURIZATION_FAILURE);
                    bomber.addDamage(BomberCompartment.NAV_RADIO, Damage.PRESSURIZATION_FAILURE);
                    bomber.setCompartmentPressurization(BomberCompartment.NOSE, false);
                    bomber.setCompartmentPressurization(BomberCompartment.NAV_RADIO, false);
                    if (nosePressurized || navPressurized){
                        BomberDamageTable.ExplosiveDecompressionResult explosiveDecompressionResult =
                                BomberDamageTable.getExplosiveDecompressionResult();
                        if (explosiveDecompressionResult == BomberDamageTable.ExplosiveDecompressionResult.ALL_CREW_MEMBERS_WOUNDED){
                            for (CrewMember crewMember : bomber.getCrew()) {
                                if (crewMember.getBomberCompartment() == BomberCompartment.NOSE || crewMember.getBomberCompartment() == BomberCompartment.NAV_RADIO) {
                                    crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                                }
                            }
                        }
                        else {
                            for (CrewMember crewMember: bomber.getCrew()){
                                if (crewMember.getBomberCompartment() == BomberCompartment.NOSE || crewMember.getBomberCompartment() == BomberCompartment.NAV_RADIO){
                                    if (BomberDamageTable.isCrewMemberWoundedFromExplosiveCompression()){
                                        crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                                    }
                                }
                            }
                        }
                    }
                }
                break;
            }
            case COCKPIT_INSTRUMENTS_HIT:{
                // Roll for damage on 7-9
                for (BomberDamageTable.DamageResult dr: BomberDamageTable.getCockpitInstrumentsDamage(bomber)){
                    applyNoseDamage(mission, bomber, dr);
                }
                break;
            }
            case PILOT_FLIGHT_CONTROLS_DAMAGED:
            case COPILOT_FLIGHT_CONTROLS_DAMAGED:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                if (bomber.hasDamage(BomberCompartment.NOSE, Damage.PILOT_FLIGHT_CONTROLS_DAMAGED) &&
                        bomber.hasDamage(BomberCompartment.NOSE, Damage.COPILOT_FLIGHT_CONTROLS_DAMAGED)){
                    int die = Util.roll2d();
                    die -= bomber.countEnginesOut();
                    if (bomber.hasDamage(Damage.AUTOPILOT_INOP))
                        die /= 2;
                    if (die <= 0){
                        // TODO Crew must bail out immediately
                    }
                    else
                        mission.setZonesBeforeBailOut(die);
                }
                break;
            }
            case ALARM_BELL_INOP:
            case LANDING_GEAR_INOP:
            case PROPELLER_FEATHERING_SYSTEM_INOP:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                break;
            }
            case ELEVATORS_SYSTEM_HIT:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                if (bomber.countDamage(BomberCompartment.NOSE, Damage.ELEVATORS_SYSTEM_HIT) >= 2 &&
                        !bomber.hasDamage(Damage.TAILPLANE_RIPPED_OFF_PORT) &&
                        !bomber.hasDamage(Damage.TAILPLANE_RIPPED_OFF_STBD)){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.ELEVATORS_INOP_PORT);
                    bomber.addDamage(BomberCompartment.TAIL, Damage.ELEVATORS_INOP_STBD);
                }
                break;
            }
            case AUTOPILOT_INOP:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                bomber.setUnderControl(false);
                break;
            }
            case RUDDER_SYSTEM_HIT:{
                if (!bomber.hasDamage(BomberCompartment.TAIL, Damage.RUDDER_INOP)) {
                    bomber.addDamage(damageResult.compartment, damageResult.damage);
                    if (bomber.countDamage(BomberCompartment.NOSE, Damage.RUDDER_SYSTEM_HIT) >= 2) {
                        bomber.addDamage(BomberCompartment.TAIL, Damage.RUDDER_INOP);
                    }
                }
                break;
            }
            case AILERONS_SYSTEM_HIT:{
                if (!bomber.hasDamage(BomberCompartment.TAIL, Damage.AILERONS_INOP_PORT) && !bomber.hasDamage(BomberCompartment.TAIL, Damage.AILERONS_INOP_STBD)) {
                    bomber.addDamage(damageResult.compartment, damageResult.damage);
                    if (bomber.countDamage(BomberCompartment.NOSE, Damage.AILERONS_SYSTEM_HIT) >= 2) {
                        bomber.addDamage(BomberCompartment.TAIL, Damage.AILERONS_INOP_PORT);
                        bomber.addDamage(BomberCompartment.TAIL, Damage.AILERONS_INOP_STBD);
                    }
                }
                break;
            }
            case FLAP_SYSTEM_INOP:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                bomber.addDamage(BomberCompartment.PORT_WING, Damage.FLAP_INOP);
                bomber.addDamage(BomberCompartment.STBD_WING, Damage.FLAP_INOP);
                break;
            }
        }
    }

    private static void applyPossibleCompromisedPressurization(Bomber bomber){
        if (Util.roll() == 6){
            // Neither nose nor nav/radio may be pressurized
            boolean nosePressurized = bomber.isCompartmentPressurized(BomberCompartment.NOSE);
            boolean navPressurized = bomber.isCompartmentPressurized(BomberCompartment.NAV_RADIO);
            bomber.addDamage(BomberCompartment.NOSE, Damage.PRESSURIZATION_FAILURE);
            bomber.addDamage(BomberCompartment.NAV_RADIO, Damage.PRESSURIZATION_FAILURE);
            bomber.setCompartmentPressurization(BomberCompartment.NOSE, false);
            bomber.setCompartmentPressurization(BomberCompartment.NAV_RADIO, false);
            if (nosePressurized || navPressurized){
                if (Util.roll() == 6) {
                    BomberDamageTable.ExplosiveDecompressionResult explosiveDecompressionResult =
                            BomberDamageTable.getExplosiveDecompressionResult();
                    if (explosiveDecompressionResult == BomberDamageTable.ExplosiveDecompressionResult.ALL_CREW_MEMBERS_WOUNDED) {
                        // Roll on 7-13 for all crew members
                        for (CrewMember crewMember : bomber.getCrew()) {
                            if (crewMember.getBomberCompartment() == BomberCompartment.NOSE || crewMember.getBomberCompartment() == BomberCompartment.NAV_RADIO) {
                                crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                            }
                        }
                    } else {
                        for (CrewMember crewMember : bomber.getCrew()) {
                            if (crewMember.getBomberCompartment() == BomberCompartment.NOSE || crewMember.getBomberCompartment() == BomberCompartment.NAV_RADIO) {
                                if (BomberDamageTable.isCrewMemberWoundedFromExplosiveCompression()) {
                                    crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                                }
                            }
                        }
                    }
                }
            }
        }
    }



    private void applyTailDamage(Mission mission, Bomber bomber, BomberDamageTable.DamageResult damageResult){
        switch (damageResult.damage){
            case FIRE:{
                // Roll on Table 7-12
                if (BomberDamageTable.isFireExtinguished(bomber.isCompartmentPressurized(BomberCompartment.TAIL), bomber.getAltitude())){
                    // TODO Now what?
                }
                else {
                    // TODO Now what?
                }
                break;
            }
            case OXYGEN_OUT:{
                if (damageResult.compartment != null) {
                    bomber.addDamage(damageResult.compartment, damageResult.damage);
                    bomber.getCrewPositionDamage().get(CrewPosition.TAIL_GUNNER).add(damageResult.damage);
                }
                else if (damageResult.crewPosition != null){
                    bomber.getCrewPositionDamage().get(damageResult.crewPosition).add(damageResult.damage);
                }
                applyPossibleCompromisedPressurization(bomber);
                break;
            }
            case TAIL_GUNNER_GUN_SIGHT_DAMAGED:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                applyPossibleCompromisedPressurization(bomber);
                break;
            }
            case CREWMEMBER_HIT:{
                // Roll on 7-13 for crew member
                CrewMember crewMember = bomber.getCrewMemberByRole(damageResult.crewPosition);
                crewMember.addWound(BomberDamageTable.getCrewMemberWound());
                applyPossibleCompromisedPressurization(bomber);
                break;
            }
            case RUDDER_HIT:{
                if (!bomber.hasDamage(BomberCompartment.TAIL, Damage.RUDDER_INOP)) {
                    bomber.addDamage(damageResult.compartment, damageResult.damage);
                    if (bomber.countDamage(BomberCompartment.TAIL, Damage.RUDDER_HIT) >= 3) {
                        bomber.addDamage(BomberCompartment.TAIL, Damage.RUDDER_INOP);
                    }
                }
                break;
            }
            case TAILPLANE_DAMAGED:{
                int die = Util.roll();
                if (die == 3){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.ELEVATORS_INOP_PORT);
                }
                else if (die == 4){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.ELEVATORS_INOP_STBD);
                }
                else if (die == 5){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.TAILPLANE_ROOT_HIT_PORT);
                }
                else if (die == 6){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.TAILPLANE_ROOT_HIT_STBD);
                }

                if (!bomber.hasDamage(BomberCompartment.TAIL, Damage.TAILPLANE_RIPPED_OFF_PORT) && bomber.countDamage(BomberCompartment.TAIL, Damage.TAILPLANE_ROOT_HIT_PORT) >= 3){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.TAILPLANE_RIPPED_OFF_PORT);
                }
                if (!bomber.hasDamage(BomberCompartment.TAIL, Damage.TAILPLANE_RIPPED_OFF_STBD) && bomber.countDamage(BomberCompartment.TAIL, Damage.TAILPLANE_ROOT_HIT_STBD) >= 3){
                    bomber.addDamage(BomberCompartment.TAIL, Damage.TAILPLANE_RIPPED_OFF_STBD);
                }
            }
            case TAIL_TURRET_INOP:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                bomber.getGuns().get(GunPosition.TAIL_TURRET).setOperational(false);
                bomber.getGuns().get(GunPosition.TAIL_CANNON).setOperational(false);
                break;
            }
        }
    }


    private void applyUtilityDamage(Mission mission, Bomber bomber, BomberDamageTable.DamageResult damageResult) {
        switch (damageResult.damage) {
            case COMPRESSED_AIR_DUCT_HIT:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                if (bomber.countDamage(BomberCompartment.UTILITY, damageResult.damage) >= 2){
                    if (Util.roll() >= 5){
                        bomber.addDamage(BomberCompartment.TAIL, Damage.PRESSURIZATION_FAILURE);
                        bomber.getCrewPositionDamage().get(CrewPosition.TAIL_GUNNER).add(Damage.PRESSURIZATION_FAILURE);
                    }
                }
                break;
            }
            case AMMUNITION_FEED_TRAY_HIT:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                break;
            }
            case SUPERFICIAL_DAMAGE:{
                break;
            }
            case AMMUNITION_BOX_DAMAGED:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                Gun tailTurret = bomber.getGuns().get(GunPosition.TAIL_TURRET);
                if (tailTurret.getAmmoRemaining() > 0) {
                    int iterations = (Util.roll() / 2) + 1;
                    for (int i = 0; i < iterations && tailTurret.getAmmoRemaining() > 0; ++i) {
                        int numBursts = Util.roll2d();
                        tailTurret.adjAmmoRemaining(-numBursts);
                    }
                }
                break;
            }
            case OXYGEN_CONTAINER_HIT:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                if (bomber.countDamage(BomberCompartment.UTILITY, Damage.OXYGEN_CONTAINER_HIT) >= 2){
                    // TODO Fire, crewman must enter Utility compartment from adjacent compartment (Waist or Tail)
                    //  and roll to extinguish on Table 7-12.  ALL B-29 crew oxygen is out!
                }
                break;
            }
            case ELECTRICAL_SYSTEM_HIT:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                if (bomber.countDamage(BomberCompartment.UTILITY, Damage.ELECTRICAL_SYSTEM_HIT) >= 2){
                    // TODO Lots of bad things!  See Table 7-10 "Electrical System" result
                }
                break;
            }
            case AFT_LOWER_TURRET_INOP:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                bomber.getGuns().get(GunPosition.REAR_LOWER_TURRET).setOperational(false);
                break;
            }
            case GAS_TANK_AUX_GENERATOR_HIT:{
                bomber.addDamage(damageResult.compartment, damageResult.damage);
                if (bomber.countDamage(BomberCompartment.UTILITY, Damage.GAS_TANK_AUX_GENERATOR_HIT) == 1){
                    bomber.addDamage(BomberCompartment.UTILITY, Damage.AUX_GENERATOR_INOP);
                    // TODO Lots of bad things!  See Table 7-10 "Electrical System" result
                }
                else if (bomber.countDamage(BomberCompartment.UTILITY, Damage.GAS_TANK_AUX_GENERATOR_HIT) >= 2){
                    // TODO Fire! Crewmen must enter Utililty compartment from adjacent compartment (Waist or Tail) and
                    //  roll to extinguish on Table 7-12
                }
                break;
            }
            case FIRE_EXTINGUISHER_DESTROYED:{
                // TODO Remove fire extinguisher from board
                break;
            }
        }
    }
}
