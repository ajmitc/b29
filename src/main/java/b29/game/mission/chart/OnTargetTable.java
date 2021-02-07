package b29.game.mission.chart;

import b29.game.crew.CrewMember;
import b29.game.crew.CrewStatus;
import b29.util.Util;

/**
 * O-6
 */
public class OnTargetTable {
    private static final boolean[] TABLE = new boolean[]{ false, false, false, true, true, true, true, true };

    public static boolean isOnTarget( int mission_number, boolean hit_by_flak, CrewMember bombardier ) {
        if( bombardier.getStatus() == CrewStatus.SERIOUS_WOUND || bombardier.getStatus() == CrewStatus.KIA )
            return false;
        int r = Util.roll();
        if( mission_number >= 11 )
            r += 1;
        if( hit_by_flak )
            r -= 1;
        return TABLE[ r ];
    }


    private OnTargetTable(){}
}
