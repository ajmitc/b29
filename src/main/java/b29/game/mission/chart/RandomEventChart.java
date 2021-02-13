package b29.game.mission.chart;

import b29.game.mission.RandomEvent;
import b29.util.Util;

public class RandomEventChart {

    public static RandomEvent getRandomEvent(){
        return RandomEvent.values()[Util.roll2d() - 2];
    }

    private RandomEventChart(){}
}
