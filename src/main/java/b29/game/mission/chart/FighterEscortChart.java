package b29.game.mission.chart;


import b29.game.mission.FighterType;
import b29.util.Util;

/**
 * 4-11
 */
public class FighterEscortChart {
    public static FighterType getEscort(int missionNumber, int targetZone){
        int die = Util.roll();
        if (missionNumber <= 15 && targetZone >= 10){
            return die <= 3? null: FighterType.FIGHTER_F6F_HELLCAT;
        }
        else if (missionNumber <= 10 && targetZone == 6){
            return die <= 2? null: FighterType.FIGHTER_P38;
        }
        // TODO Finish this
        return null;
    }

    public static boolean getFighterEscortAvailable() {
        // TODO Implement this
        return false;
    }

    private FighterEscortChart() {
    }
}
