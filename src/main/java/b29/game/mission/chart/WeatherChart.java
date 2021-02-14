package b29.game.mission.chart;

import b29.game.bomber.Altitude;
import b29.game.bomber.Bomber;
import b29.game.mission.Direction;
import b29.game.mission.Mission;
import b29.game.mission.Weather;
import b29.util.Util;

/**
 * 4-2, 4-3
 */
public class WeatherChart {
    public static Weather get(Mission mission, Bomber bomber){
        Weather previousWeather = mission.getWeather();

        int die = Util.roll2d();
        die -= bomber.getAltitude() == Altitude.HI? 1: 0;
        die += previousWeather == Weather.POOR? 1: 0;
        die += previousWeather == Weather.BAD? 2: 0;

        Weather weather = null;
        if (die <= 8){
            weather = Weather.GOOD;
        }
        else if (die <= 10){
            weather = Weather.POOR;
        }
        else {
            weather = Weather.BAD;
        }

        if (die == 2 || die == 10 || die == 11){
            if (mission.getDirection() == Direction.TO_TARGET) {
                // Strong headwind
                bomber.consumeFuel(1);
            } else {
                // Strong tailwind
                bomber.consumeFuel(-1);
            }
        }

        return weather;
    }

    public static BadWeatherResult getBadWeatherResult(Mission mission, Bomber bomber, boolean spentFuel){
        int die = Util.roll();

        if (mission.isOutOfFormation()){
            // TODO +1 if radar is not working or radar operator is KIA or seriously wounded
            die -= spentFuel? 1: 0;
        }

        if (die <= 4){
            return BadWeatherResult.SAFE_PASSAGE;
        }
        if (die == 5){
            return BadWeatherResult.FORMATION_DISRUPTED;
        }
        if (die == 6){
            return BadWeatherResult.FORMATION_DISRUPTED_STORM_DAMAGE;
        }
        return BadWeatherResult.FORMATION_DISRUPTED_ELECTRICAL_DAMAGE;
    }

    public static CollisionResult getCollisionResult(boolean outOfFormation){
        int die = Util.roll2d();
        if (!outOfFormation)
            die += 3;
        if (die <= 11)
            return CollisionResult.NO_COLLISION;
        die = Util.roll2d();
        if (die <= 8)
            return CollisionResult.CLOSE_CALL_NO_EFFECT;
        if (die <= 10)
            return CollisionResult.SHALLOW_DIVE;
        if (die == 11)
            return CollisionResult.STEEP_DIVE;
        return CollisionResult.MID_AIR_COLLISION;
    }

    private WeatherChart(){}
}
