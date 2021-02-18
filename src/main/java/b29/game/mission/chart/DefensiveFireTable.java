package b29.game.mission.chart;

import b29.game.Experience;
import b29.game.bomber.Bomber;
import b29.game.bomber.BomberCompartment;
import b29.game.bomber.Damage;
import b29.game.bomber.GunPosition;
import b29.game.mission.*;
import b29.util.Util;

import java.util.HashMap;
import java.util.Map;

/**
 * M-1
 */
public class DefensiveFireTable {
    private static final Map<FighterApproach, Map<FighterAltitude, Map<GunPosition, Map<FighterType, Integer>>>> TABLE = new HashMap<>();

    static {
        // { area: { altitude: { gun: { fighter: min_roll_for_hit } } } }
        for (FighterApproach approach : FighterApproach.values()) {
            TABLE.put(approach, new HashMap<>());
            for (FighterAltitude altitude : FighterAltitude.values()) {
                TABLE.get(approach).put(altitude, new HashMap<>());
            }
        }

        /*
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).put(GunPosition.NOSE, new HashMap<>());
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).get(GunPosition.NOSE).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).get(GunPosition.NOSE).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.HIGH).get(GunPosition.NOSE).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).put(GunPosition.NOSE, new HashMap<>());
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).get(GunPosition.NOSE).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).get(GunPosition.NOSE).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LEVEL).get(GunPosition.NOSE).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).put(GunPosition.NOSE, new HashMap<>());
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).get(GunPosition.NOSE).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).get(GunPosition.NOSE).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_12).get(FighterAltitude.LOW).get(GunPosition.NOSE).put(FighterType.FIGHTER_190, 6);


        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).put(GunPosition.STBD_CHEEK, new HashMap<>());
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).put(GunPosition.STBD_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.HIGH).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LEVEL).put(GunPosition.STBD_CHEEK, new HashMap<>());
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LEVEL).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LEVEL).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LEVEL).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).put(GunPosition.STBD_CHEEK, new HashMap<>());
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_1_30).get(FighterAltitude.LOW).get(GunPosition.STBD_CHEEK).put(FighterType.FIGHTER_190, 6);


        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).put(GunPosition.PORT_CHEEK, new HashMap<>());
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).put(GunPosition.PORT_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.HIGH).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LEVEL).put(GunPosition.PORT_CHEEK, new HashMap<>());
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LEVEL).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LEVEL).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LEVEL).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).put(GunPosition.PORT_CHEEK, new HashMap<>());
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_10_30).get(FighterAltitude.LOW).get(GunPosition.PORT_CHEEK).put(FighterType.FIGHTER_190, 6);


        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).put(GunPosition.STBD_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.HIGH).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).put(GunPosition.STBD_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LEVEL).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).put(GunPosition.STBD_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_3).get(FighterAltitude.LOW).get(GunPosition.STBD_WAIST).put(FighterType.FIGHTER_190, 6);


        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).put(GunPosition.PORT_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.HIGH).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).put(GunPosition.PORT_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LEVEL).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 5);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 4);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 5);

        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).put(GunPosition.PORT_WAIST, new HashMap<>());
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_9).get(FighterAltitude.LOW).get(GunPosition.PORT_WAIST).put(FighterType.FIGHTER_190, 6);


        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 4);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 3);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 4);

        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).put(GunPosition.RADIO, new HashMap<>());
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.RADIO).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.RADIO).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.RADIO).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).put(GunPosition.TAIL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_109, 4);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_110, 3);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.HIGH).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_190, 4);

        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LEVEL).put(GunPosition.TAIL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LEVEL).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_109, 4);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LEVEL).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_110, 3);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LEVEL).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_190, 4);

        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 4);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 3);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 4);

        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).put(GunPosition.TAIL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_109, 4);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_110, 3);
        TABLE.get(FighterApproach.AREA_6).get(FighterAltitude.LOW).get(GunPosition.TAIL_TURRET).put(FighterType.FIGHTER_190, 4);


        TABLE.get(FighterApproach.VERTICAL_CLIMB).get(FighterAltitude.LOW).put(GunPosition.BALL_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.VERTICAL_CLIMB).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_109, 3);
        TABLE.get(FighterApproach.VERTICAL_CLIMB).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_110, 3);
        TABLE.get(FighterApproach.VERTICAL_CLIMB).get(FighterAltitude.LOW).get(GunPosition.BALL_TURRET).put(FighterType.FIGHTER_190, 3);


        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).put(GunPosition.TOP_TURRET, new HashMap<>());
        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).get(GunPosition.TOP_TURRET).put(FighterType.FIGHTER_190, 6);

        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).put(GunPosition.RADIO, new HashMap<>());
        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).get(GunPosition.RADIO).put(FighterType.FIGHTER_109, 6);
        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).get(GunPosition.RADIO).put(FighterType.FIGHTER_110, 6);
        TABLE.get(FighterApproach.VERTICAL_DIVE).get(FighterAltitude.HIGH).get(GunPosition.RADIO).put(FighterType.FIGHTER_190, 6);
         */
    }


    public static Map<GunPosition, Map<FighterType, Integer>> getGuns(FighterApproach approach, FighterAltitude altitude) {
        return TABLE.get(approach).get(altitude);
    }


    public static int getMinHitRoll(FighterType fighter, FighterApproach approach, FighterAltitude altitude, GunPosition gun) {
        Map<GunPosition, Map<FighterType, Integer>> guns = TABLE.get(approach).get(altitude);
        if (guns.containsKey(gun)) {
            return guns.get(gun).get(fighter);
        }
        return 99; // Return a number too big to allow any hit
    }

    public static DefensiveFireResolution getDefensiveFireResolution(Mission mission, Bomber bomber, JapaneseFighter japaneseFighter, GunPosition gunsFiring){
        int die = Util.roll2d();

        if (die == 2)
            return DefensiveFireResolution.GUNS_JAM;

        if (die == 3 && gunsFiring == GunPosition.TAIL_CANNON)
            return DefensiveFireResolution.TAIL_CANNON_JAMS;

        if (japaneseFighter.getFighterInfo().approach == FighterApproach.VERTICAL_DIVE)
            die -= 3;
        if (gunsFiring == GunPosition.TAIL_CANNON)
            die -= 2;

        // TODO See Section 5.3.D
        if (gunsFiring == GunPosition.TAIL_TURRET &&
                (japaneseFighter.getFighterInfo().approach == FighterApproach.AREA_10_30 ||
                        japaneseFighter.getFighterInfo().approach == FighterApproach.AREA_12 ||
                        japaneseFighter.getFighterInfo().approach == FighterApproach.AREA_1_30))
            die -= 1;

        if (gunsFiring == GunPosition.TAIL_TURRET){
            // die -= 1 for each hit on Utility compartment ammunition feed trays
            int count = bomber.countDamage(BomberCompartment.UTILITY, Damage.AMMUNITION_FEED_TRAY);
            die -= count;
        }

        if (japaneseFighter.getExperience() == Experience.VETERAN)
            die -= 1;

        if (bomber.isPerformingEvasiveAction())
            die -= 1;

        if (mission.getMissionTimeOfDay() == TimeOfDay.NIGHT) {
            die -= 1;
            // TODO die -= 1 if spotted and currently fixed by spotlight
            if (mission.isSpottedBySearchlight())
                die -= 1;
        }

        if (japaneseFighter.getExperience() == Experience.GREEN)
            die += 1;

        if (japaneseFighter.getFighterInfo().fighterType == FighterType.FIGHTER_NICK ||
                japaneseFighter.getFighterInfo().fighterType == FighterType.FIGHTER_IRVING)
            die += 1;

        if (japaneseFighter.getFighterInfo().approach == FighterApproach.AREA_3 || japaneseFighter.getFighterInfo().approach == FighterApproach.AREA_9)
            die += 1;

        if (japaneseFighter.getFighterInfo().approach == FighterApproach.AREA_6)
            die += 2;

        if (japaneseFighter.getFighterInfo().approach == FighterApproach.VERTICAL_CLIMB)
            die += 3;

        if (die <= 1)
            return DefensiveFireResolution.FIGHTER_ATTACKS_NORMALLY;
        if (die == 2)
            return DefensiveFireResolution.GUNS_JAM;
        if (die == 3)
            return DefensiveFireResolution.TAIL_CANNON_JAMS;
        if (die <= 9)
            return DefensiveFireResolution.FIGHTER_ATTACKS_NORMALLY;
        return DefensiveFireResolution.FIGHTER_HIT;
    }


    private DefensiveFireTable() {
    }
}
