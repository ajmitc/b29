package b29.game.mission.chart;

import b29.game.mission.FormationPosition;
import b29.game.mission.SquadronPosition;
import b29.util.Util;

/**
 * G-4
 */
public class FormationPositionTable {

    public static FormationPosition getFormationPosition() {
        int r = Util.roll( 2, true );
        if( r == 2 )
            return FormationPosition.LEAD;
        if( r == 12 )
            return FormationPosition.TAIL;
        return FormationPosition.MIDDLE;
    }

    public static SquadronPosition getSquadronPosition() {
        int r = Util.roll( 2, true );
        if( r <= 2 )
            return SquadronPosition.HIGH;
        if( r <= 4 )
            return SquadronPosition.MIDDLE;
        return SquadronPosition.LOW;
    }

    private FormationPositionTable(){}
}
