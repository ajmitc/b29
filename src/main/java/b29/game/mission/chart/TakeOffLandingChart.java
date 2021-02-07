package b29.game.mission.chart;

import b29.game.Experience;
import b29.game.crew.CrewStatus;
import b29.game.mission.TakeoffAccident;
import b29.game.mission.TakeoffMalfunction;
import b29.game.mission.TakeoffResult;
import b29.game.mission.TimeOfDay;
import b29.util.Util;

/**
 * 3-1, 3-2, 3-3, 3-4, 3-5
 */
public class TakeOffLandingChart {
    /**
     * 3-1
     * @param missionTimeOfDay
     * @param missionNumber
     * @return
     */
    public static TakeOffLandingTimesInfo getTakeoffLandingTimeOfDayInfo(TimeOfDay missionTimeOfDay, int missionNumber){
        TakeOffLandingTimesInfo info = new TakeOffLandingTimesInfo();
        int die = Util.roll();
        if (missionNumber <= 10)
            die -= 2;
        if (missionTimeOfDay == TimeOfDay.NIGHT){
            if (die <= 3){
                info.baseTakeoff = TimeOfDay.DAY;
                info.expectedBaseLanding = TimeOfDay.DAY;
                info.abortOutMinDayZone = 1;
                info.abortOutMaxDayZone = 2;
                info.ditchOutMinDayZone = 1;
                info.ditchOutMaxDayZone = 3;
                info.ditchBackMinDayZone = 1;
                info.ditchBackMaxDayZone = 3;
            }
            else {
                info.baseTakeoff = TimeOfDay.NIGHT;
                info.expectedBaseLanding = TimeOfDay.DAY;
                info.abortOutMinDayZone = 0;
                info.abortOutMaxDayZone = 0;
                info.ditchOutMinDayZone = 0;
                info.ditchOutMaxDayZone = 0;
                info.ditchBackMinDayZone = 1;
                info.ditchBackMaxDayZone = 6;
            }
        }
        else {
            if (die <= 2){
                info.baseTakeoff = TimeOfDay.NIGHT;
                info.expectedBaseLanding = TimeOfDay.NIGHT;
                info.abortOutMinDayZone = 3;
                info.abortOutMaxDayZone = 14;
                info.ditchOutMinDayZone = 4;
                info.ditchOutMaxDayZone = 14;
                info.ditchBackMinDayZone = 4;
                info.ditchBackMaxDayZone = 14;
            }
            else if (die <= 4){
                info.baseTakeoff = TimeOfDay.DAY;
                info.expectedBaseLanding = TimeOfDay.NIGHT;
                info.abortOutMinDayZone = 1;
                info.abortOutMaxDayZone = 14;
                info.ditchOutMinDayZone = 1;
                info.ditchOutMaxDayZone = 14;
                info.ditchBackMinDayZone = 7;
                info.ditchBackMaxDayZone = 14;
            }
            else {
                info.baseTakeoff = TimeOfDay.NIGHT;
                info.expectedBaseLanding = TimeOfDay.DAY;
                info.abortOutMinDayZone = 4;
                info.abortOutMaxDayZone = 14;
                info.ditchOutMinDayZone = 6;
                info.ditchOutMaxDayZone = 14;
                info.ditchBackMinDayZone = 1;
                info.ditchBackMaxDayZone = 14;
            }
        }
        return info;
    }

    public static class TakeOffLandingTimesInfo{
        public TimeOfDay baseTakeoff;
        public TimeOfDay expectedBaseLanding;
        public int abortOutMinDayZone;
        public int abortOutMaxDayZone;
        public int ditchOutMinDayZone;
        public int ditchOutMaxDayZone;
        public int ditchBackMinDayZone;
        public int ditchBackMaxDayZone;
    }


    /**
     * 3-2
     * @return
     */
    public static TakeoffResult getTakeoffResult(TimeOfDay missionTimeOfDay, Experience pilotExperience){
        int die = Util.roll2d();
        if (die == 2)
            return TakeoffResult.MALFUNCTION;
        if (die == 12 && missionTimeOfDay == TimeOfDay.NIGHT){
            die = Util.roll2d();
            if (pilotExperience == Experience.GREEN)
                die += 1;
            if (pilotExperience == Experience.VETERAN)
                die -= 1;
            if (die <= 8)
                return TakeoffResult.TAKEOFF_OK;
            if (die <= 10)
                return TakeoffResult.ACCIDENT;
            return TakeoffResult.MID_AIR_COLLISION;
        }
        return TakeoffResult.TAKEOFF_OK;
    }


    /**
     * 3-3
     */
    public static TakeoffMalfunction getTakeoffMalfunction(){
        int die = Util.roll();
        switch (die){
            case 1:
                return TakeoffMalfunction.ENGINE_FIRE_DURING_RUN_UP;
            case 2:
                return TakeoffMalfunction.UNUSUAL_POWER_CONDITIONS;
            case 3:
                return TakeoffMalfunction.ENGINE_FAILURE;
            case 4:
                return TakeoffMalfunction.RUNAWAY_PROPELLER_AFTER_TAKEOFF;
            case 5:
                return TakeoffMalfunction.ENGINE_FIRE_AFTER_TAKEOFF;
            case 6:
                return TakeoffMalfunction.FALSE_ALARM;
        }
        return TakeoffMalfunction.FALSE_ALARM;
    }

    /**
     * 3-4
     * @return
     */
    public static CrewStatus getCrewInjury(){
        int die = Util.roll2d();
        if (die <= 4)
            return CrewStatus.KIA;
        if (die <= 6)
            return CrewStatus.SERIOUS_WOUND;
        if (die <= 9)
            return CrewStatus.LIGHT_WOUND;
        return CrewStatus.OK;
    }


    /**
     * 3-5
     * @param notEnoughLift (from 3-3)
     * @return
     */
    public static TakeoffAccident getTakeoffAccident(boolean notEnoughLift){
        int die = Util.roll2d();
        if (notEnoughLift)
            die -= 1;
        if (die <= 4)
            return TakeoffAccident.PLANE_CRASHES_AND_EXPLODES_ALL_KIA;
        if (die <= 7)
            return TakeoffAccident.PLANE_CRASHES_ROLL_FOR_CREW;
        if (die <= 9)
            return TakeoffAccident.CREW_SAFE_B29_IRREPAIRABLY_DAMAGED;
        return TakeoffAccident.CREW_SAFE_B29_REPAIRABLE;
    }

    private TakeOffLandingChart(){}
}
