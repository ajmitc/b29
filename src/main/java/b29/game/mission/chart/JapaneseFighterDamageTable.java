package b29.game.mission.chart;

import b29.game.bomber.GunPosition;
import b29.game.mission.FighterType;
import b29.game.mission.JapaneseFighterDamage;
import b29.util.Util;

/**
 * TODO Implement this
 */
public class JapaneseFighterDamageTable {

    public static JapaneseFighterDamage get(GunPosition gun, FighterType fighter) {
        int r = Util.roll();
        if (fighter != FighterType.FIGHTER_190 && (gun == GunPosition.TOP_TURRET || gun == GunPosition.BALL_TURRET || gun == GunPosition.TAIL_TURRET)) {
            r += 1;
        }
        if (fighter == FighterType.FIGHTER_190 && gun != GunPosition.TOP_TURRET && gun != GunPosition.BALL_TURRET && gun != GunPosition.TAIL_TURRET) {
            r -= 1;
        }
        if (r <= 2)
            return JapaneseFighterDamage.FCA;
        if (r <= 4)
            return JapaneseFighterDamage.FBOA;
        return JapaneseFighterDamage.DESTROYED;
    }

    private JapaneseFighterDamageTable() {
    }
}
