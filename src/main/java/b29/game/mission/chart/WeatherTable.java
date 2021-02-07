package b29.game.mission.chart;

import b29.game.mission.Weather;
import b29.util.Util;

/**
 * O-1
 */
public class WeatherTable {
    public static Weather get() {
        int r = Util.roll( 2, true );
        if( r == 2 || r == 12 )
            return Weather.BAD;
        if( r == 3 || r == 11 )
            return Weather.POOR;
        return Weather.GOOD;
    }

    private WeatherTable(){}
}
