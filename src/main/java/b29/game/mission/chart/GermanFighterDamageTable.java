package b29.game.mission.chart;

import b29.game.bomber.GunPosition;
import b29.game.mission.FighterType;
import b29.game.mission.GermanFighterDamage;
import b29.util.Util;

/**
 * M-2
 */
public class GermanFighterDamageTable {

    public static GermanFighterDamage get(GunPosition gun, FighterType fighter) {
        int r = Util.roll();
        if (fighter != FighterType.FIGHTER_190 && (gun == GunPosition.TOP_TURRET || gun == GunPosition.BALL_TURRET || gun == GunPosition.TAIL_TURRET)) {
            r += 1;
        }
        if (fighter == FighterType.FIGHTER_190 && gun != GunPosition.TOP_TURRET && gun != GunPosition.BALL_TURRET && gun != GunPosition.TAIL_TURRET) {
            r -= 1;
        }
        if (r <= 2)
            return GermanFighterDamage.FCA;
        if (r <= 4)
            return GermanFighterDamage.FBOA;
        return GermanFighterDamage.DESTROYED;
    }

    private GermanFighterDamageTable() {
    }
}
