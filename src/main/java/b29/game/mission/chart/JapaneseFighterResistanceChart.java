package b29.game.mission.chart;

import b29.game.Experience;
import b29.game.bomber.*;
import b29.game.crew.CrewMember;
import b29.game.mission.*;
import b29.util.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * 5-1, 5-2, 5-3, 5-4, 5-5
 */
public class JapaneseFighterResistanceChart {

    /**
     * 5-1
     * @param mission
     * @param bomber
     * @return
     */
    public static JapaneseFighterDensity getJapaneseFighterDensity(Mission mission, Bomber bomber){
        int die = Util.roll2d();

        if (mission.getFighterDensityModifiers().containsKey(mission.getZone()))
            die += mission.getFighterDensityModifiers().get(mission.getZone());
        if (mission.getEscort() == FighterType.FIGHTER_P51)
            die -= 2;
        if (bomber.getAltitude() == Altitude.HI)
            die -= 2;
        if (mission.getWeather() == Weather.BAD)
            die -= 2;
        if (mission.getWeather() == Weather.POOR)
            die -= 1;
        if (mission.getExpectedFighterDensity() == JapaneseFighterDensity.NONE)
            die -= 1;
        if (mission.getMissionTimeOfDay() == TimeOfDay.DAY && bomber.getAltitude() == Altitude.LO)
            die -= 1;
        if (mission.getFormationAssembly() == FormationAssembly.TIGHT)
            die -= 1;
        if (mission.getEscort() == FighterType.FIGHTER_F6F_HELLCAT || mission.getEscort() == FighterType.FIGHTER_P38)
            die -= 1;
        if (mission.getDirection() == Direction.RETURN_HOME)
            die += 1;
        if (mission.getFormationAssembly() == FormationAssembly.DIFFICULT)
            die += 1;
        if (mission.getExpectedFighterDensity() == JapaneseFighterDensity.HEAVY)
            die += 1;

        // TODO If DAY mission and not out-of-formation and 1+ Formation Disrupted results from Table 4-3 from Bad weather while in formation anytime during mission, die += 1

        if (mission.getMissionTimeOfDay() == TimeOfDay.DAY && mission.isOutOfFormation())
            die += 2;

        if (die <= 4)
            return JapaneseFighterDensity.NONE;
        if (die <= 7)
            return JapaneseFighterDensity.LIGHT;
        if (die <= 10)
            return JapaneseFighterDensity.MODERATE;
        return JapaneseFighterDensity.HEAVY;
    }

    /**
     * 5-2
     *
     * TODO No more than one fighter can be at the same approach and altitude, if this occurs:
     * - If 3+ at same approach, move fighter to different approach
     * - If <= 3 at same approach, assign to different altitude
     *
     * @param mission
     * @param bomber
     * @return
     */
    public static List<JapaneseFighter> getJapaneseFighters(Mission mission, Bomber bomber){
        while (true) {
            int die = Util.roll2d();
            if (bomber.getAltitude() == Altitude.HI)
                die += 1;
            if (bomber.getAltitude() == Altitude.LO)
                die -= 1;
            if (die == 1)
                continue;
            else if (die <= 3)
                return Util.roll() >= 5? buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_JACK): buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_NICK);
            else if (die == 4)
                return Util.roll() >= 5? buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_JACK): buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_TONY);
            else if (die <= 6)
                return buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_ZEKE);
            else if (die == 7)
                return buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_FRANK);
            else if (die == 8)
                return buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_TOJO);
            else if (die == 9)
                return buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_OSCAR);
            else if (die <= 12)
                return buildJapaneseFighters(mission, bomber, FighterType.FIGHTER_GEORGE);
            return null;
        }
    }

    private static List<JapaneseFighter> buildJapaneseFighters(Mission mission, Bomber bomber, FighterType fighterType){
        List<JapaneseFighter> fighters = new ArrayList<>();
        FighterApproachResult approachResult = getFighterApproach(mission, bomber, fighterType, 0);
        if (approachResult.fighterApproach != null) {
            FighterAltitude altitude = getFighterAltitude(approachResult.fighterApproach);
            JapaneseFighter fighter = buildJapaneseFighter(fighterType, approachResult.fighterApproach, altitude);
            fighter.setRam(approachResult.ram);
            fighters.add(fighter);
        }
        fighters.addAll(approachResult.additionalFighters);
        return fighters;
    }

    private static JapaneseFighter buildJapaneseFighter(FighterType fighterType, FighterApproach approach, FighterAltitude altitude){
        FighterInfo fighterInfo = new FighterInfo(fighterType, approach, altitude);
        JapaneseFighter fighter = new JapaneseFighter(fighterInfo);
        fighter.setExperience(getFighterExperience(fighterType));
        return fighter;
    }

    /**
     * 5-3
     * @param mission
     * @param bomber
     * @param lastRoll
     * @return
     */
    private static FighterApproachResult getFighterApproach(Mission mission, Bomber bomber, FighterType fighterType, int lastRoll){
        FighterApproachResult result = new FighterApproachResult();
        int die = Util.roll2d();
        if (die == 2){
            if (lastRoll == 2) {
                if (mission.getSquadronPosition() == SquadronPosition.LOW) {
                    result.additionalFighters.add(buildJapaneseFighter(FighterType.FIGHTER_FRANK, FighterApproach.VERTICAL_CLIMB, FighterAltitude.LEVEL));
                } else if ((mission.getSquadronPosition() == SquadronPosition.HIGH || mission.isOutOfFormation()) &&
                        bomber.getAltitude().ordinal() <= Altitude.MED.ordinal()) {
                    result.additionalFighters.add(buildJapaneseFighter(FighterType.FIGHTER_FRANK, FighterApproach.VERTICAL_DIVE, FighterAltitude.LEVEL));
                }
            }
            FighterApproachResult result1 = getFighterApproach(mission, bomber, fighterType, 2);
            result.fighterApproach = result1.fighterApproach;
            result.ram = result1.ram;
            result.additionalFighters.addAll(result1.additionalFighters);
        }
        else if (die == 3){
            die = Util.roll();
            if (die == 1){
                // Baka suicide rocket/bomb (See Chart book pg 15, Table 5-3 Note b)
                result.fighterApproach = null; // Do not add this fighter
                result.additionalFighters.add(buildJapaneseFighter(FighterType.BAKA_SUICIDE_ROCKER, FighterApproach.AREA_6, FighterAltitude.LEVEL));
            }
            else if (die <= 3){
                // Ramming attack
                result.ram = true;
            }
            else if (die == 4){
                // Rockets launched into bomber formation
                result.fighterApproach = null; // Do not add this fighter
                result.additionalFighters.clear();
                result.additionalFighters.add(buildJapaneseFighter(Util.roll() == 1? FighterType.MEDIUM_FLAK: FighterType.LIGHT_FLAK, FighterApproach.AREA_12, FighterAltitude.LEVEL));
            }
            else {
                // Japanese bombs dropped on formation from above
                result.fighterApproach = null; // Do not add this fighter
                if (mission.isOutOfFormation()) {
                    result.additionalFighters.clear();
                }
                else {
                    result.additionalFighters.add(buildJapaneseFighter(Util.roll() <= 2? FighterType.MEDIUM_FLAK: FighterType.LIGHT_FLAK, FighterApproach.AREA_12, FighterAltitude.LEVEL));
                }
            }
        }
        else if (die == 4){
            result.fighterApproach = FighterApproach.AREA_3;
        }
        else if (die == 5){
            if (lastRoll == 5){
                result.fighterApproach = null; // Do not add this fighter
                die = Util.roll();
                if (die == 6){
                    int hits = Util.roll();
                    for (int i = 0; i < hits; ++i) {
                        die = Util.roll2d();
                        BomberCompartment areaHit = null;
                        switch (die){
                            case 2:
                            case 12:
                                // Nose Hit
                                areaHit = BomberCompartment.NOSE;
                                break;
                            case 3:
                                // Nav/Radio
                                areaHit = BomberCompartment.NAV_RADIO;
                                break;
                            case 4:
                                // Waist
                                areaHit = BomberCompartment.WAIST;
                                break;
                            case 5:
                                // Fwd Bomb Bay
                                areaHit = BomberCompartment.FWD_BOMB_BAY;
                                break;
                            case 6:
                                // Stbd Wing
                                areaHit = BomberCompartment.STBD_WING;
                                break;
                            case 7:
                                // Superficial damage
                                break;
                            case 8:
                                // Port Wing
                                areaHit = BomberCompartment.PORT_WING;
                                break;
                            case 9:
                                // Rear bomb bay
                                areaHit = BomberCompartment.REAR_BOMB_BAY;
                                break;
                            case 10:
                                // tail
                                areaHit = BomberCompartment.TAIL;
                                break;
                            case 11:
                                // Utility
                                areaHit = BomberCompartment.UTILITY;
                                break;
                        }

                        if (areaHit != null){
                            // TODO Resolve damage (7.1)
                        }
                    }
                }
            }
            else {
                // Get approach for this fighter
                FighterApproachResult result1 = getFighterApproach(mission, bomber, fighterType, 5);
                if (result1.fighterApproach != null) {
                    result.fighterApproach = result1.fighterApproach;
                }
                result.ram = result1.ram;
                result.additionalFighters.addAll(result1.additionalFighters);

                // Add additional fighters of same type
                List<JapaneseFighter> additionalFighters = new ArrayList<>();
                die = (Util.roll2d() / 2) - 1;
                for (int i = 0; i < die; ++i) {
                    FighterApproachResult approachResult = getFighterApproach(mission, bomber, fighterType, 5);
                    if (approachResult.fighterApproach != null) {
                        FighterAltitude altitude = getFighterAltitude(approachResult.fighterApproach);
                        JapaneseFighter fighter = buildJapaneseFighter(fighterType, approachResult.fighterApproach, altitude);
                        fighter.setRam(approachResult.ram);
                        additionalFighters.add(fighter);
                    }
                    additionalFighters.addAll(approachResult.additionalFighters);
                }
                result.additionalFighters.addAll(additionalFighters);
            }
        }
        else if (die == 6){
            result.fighterApproach = FighterApproach.AREA_12;
        }
        else if (die == 7){
            result.fighterApproach = FighterApproach.AREA_1_30;
        }
        else if (die == 8){
            result.fighterApproach = FighterApproach.AREA_10_30;
        }
        else if (die == 9){
            if (fighterType == FighterType.FIGHTER_OSCAR || fighterType == FighterType.FIGHTER_ZEKE || fighterType == FighterType.FIGHTER_TONY || fighterType == FighterType.FIGHTER_NICK){
                CrewMember cfcController = bomber.getCrewMemberByDefaultRole(CrewPosition.CFC_CONTROLLER);
                CrewMember tailGunner = bomber.getCrewMemberByDefaultRole(CrewPosition.TAIL_GUNNER);
                if (mission.isOutOfFormation() &&
                        !bomber.hasDamage(Damage.ENGINE_1_OUT) &&
                        !bomber.hasDamage(Damage.ENGINE_2_OUT) &&
                        !bomber.hasDamage(Damage.ENGINE_3_OUT) &&
                        !bomber.hasDamage(Damage.ENGINE_4_OUT) &&
                        !bomber.hasDamage(Damage.INTERCOM_FAILURE) &&
                        !bomber.isCarryingBombs() &&
                        ((!cfcController.isSwOrKia() && cfcController.getCrewPosition() == CrewPosition.CFC_CONTROLLER) ||
                                (!tailGunner.isSwOrKia() && tailGunner.getCrewPosition() == CrewPosition.TAIL_GUNNER))
                ){
                    result.fighterApproach = null;
                    result.additionalFighters.clear();
                    return result;
                }
            }
            result.fighterApproach = FighterApproach.AREA_6;
        }
        else if (die == 10){
            result.fighterApproach = FighterApproach.AREA_9;
        }
        else if (die == 11){
            if (fighterType == FighterType.FIGHTER_OSCAR || fighterType == FighterType.FIGHTER_ZEKE || fighterType == FighterType.FIGHTER_TONY || fighterType == FighterType.FIGHTER_NICK){
                CrewMember cfcController = bomber.getCrewMemberByDefaultRole(CrewPosition.CFC_CONTROLLER);
                CrewMember tailGunner = bomber.getCrewMemberByDefaultRole(CrewPosition.TAIL_GUNNER);
                if (mission.isOutOfFormation() &&
                        !bomber.hasDamage(Damage.ENGINE_1_OUT) &&
                        !bomber.hasDamage(Damage.ENGINE_2_OUT) &&
                        !bomber.hasDamage(Damage.ENGINE_3_OUT) &&
                        !bomber.hasDamage(Damage.ENGINE_4_OUT) &&
                        !bomber.hasDamage(Damage.INTERCOM_FAILURE) &&
                        !bomber.isCarryingBombs() &&
                        ((!cfcController.isSwOrKia() && cfcController.getCrewPosition() == CrewPosition.CFC_CONTROLLER) ||
                                (!tailGunner.isSwOrKia() && tailGunner.getCrewPosition() == CrewPosition.TAIL_GUNNER))){
                    result.fighterApproach = null;
                    result.additionalFighters.clear();
                    return result;
                }
            }
            result.fighterApproach = FighterApproach.AREA_6;
        }
        else if (die == 12){
            if (lastRoll != 12) {
                if (mission.getFormationPosition() == FormationPosition.LEAD || mission.isOutOfFormation()) {
                    result.additionalFighters.add(buildJapaneseFighter(FighterType.FIGHTER_TONY, FighterApproach.AREA_12, getFighterAltitude(FighterApproach.AREA_12)));
                }
                if (mission.getFormationPosition() == FormationPosition.TAIL || mission.isOutOfFormation()) {
                    result.additionalFighters.add(buildJapaneseFighter(FighterType.FIGHTER_FRANK, FighterApproach.AREA_6, getFighterAltitude(FighterApproach.AREA_6)));
                }
                if (!mission.isOutOfFormation() && mission.getFormationPosition() == FormationPosition.MIDDLE){
                    // Formation Casualties
                    die = Util.roll();
                    if (die <= 2){
                        mission.setFormationPosition(FormationPosition.LEAD);
                    }
                    else if (die <= 4){
                        mission.setFormationPosition(FormationPosition.TAIL);
                    }
                }
            }
            FighterApproachResult result1 = getFighterApproach(mission, bomber, fighterType, 12);
            result.fighterApproach = result1.fighterApproach;
            result.ram = result1.ram;
            result.additionalFighters.addAll(result1.additionalFighters);
        }
        return result;
    }

    private static FighterAltitude getFighterAltitude(FighterApproach fighterApproach){
        int die = Util.roll();
        if (fighterApproach == FighterApproach.AREA_10_30 || fighterApproach == FighterApproach.AREA_12 || fighterApproach == FighterApproach.AREA_1_30)
            die += 1;
        if (fighterApproach == FighterApproach.AREA_6)
            die -= 1;
        if (die <= 2)
            return FighterAltitude.LOW;
        if (die <= 4)
            return FighterAltitude.LEVEL;
        return FighterAltitude.HIGH;
    }

    /**
     * 5-4
     */
    private static class FighterApproachResult{
        public FighterApproach fighterApproach;
        public List<JapaneseFighter> additionalFighters = new ArrayList<>();
        public boolean ram = false;
    }


    /**
     * 5-5
     * @param fighterType
     * @return
     */
    private static Experience getFighterExperience(FighterType fighterType){
        int die = Util.roll2d();
        if (fighterType == FighterType.FIGHTER_GEORGE)
            die += 1;
        if (die <= 7)
            return Experience.GREEN;
        if (die <= 11)
            return Experience.AVERAGE;
        return Experience.VETERAN;
    }

    private JapaneseFighterResistanceChart() {
    }
}
