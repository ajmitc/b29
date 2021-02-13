package b29.game.mission.chart;

import b29.game.bomber.Altitude;
import b29.game.bomber.Bomber;
import b29.game.bomber.CrewPosition;
import b29.game.crew.CrewMember;
import b29.game.mission.*;
import b29.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 2-1, 2-2, 2-3, 2-4, 2-5, 2-6, 2-8
 */
public class MissionSetupTable {
    private static List<Target> targets1_10 = new ArrayList<>();
    private static List<TargetType> targetTypes1_10 = new ArrayList<>();
    private static Map<Integer, Target> targets11_35 = new HashMap<>();
    private static Map<Integer, TargetType> targetTypes11_35 = new HashMap<>();

    static {
        targets1_10.add(Target.IWO_JIMA);
        targets1_10.add(Target.NAGOYA);
        targets1_10.add(Target.AKASHI);
        targets1_10.add(Target.NAGOYA);
        targets1_10.add(Target.NAGOYA);
        targets1_10.add(Target.TOKYO);
        targets1_10.add(Target.TOKYO);
        targets1_10.add(Target.TOKYO);
        targets1_10.add(Target.OTA);
        targets1_10.add(Target.KOBE);
        targets1_10.add(Target.OKINAWA);

        targetTypes1_10.add(TargetType.AIRFIELD);
        targetTypes1_10.add(TargetType.URBAN_AREA);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.AIRCRAFT_FACTORY);
        targetTypes1_10.add(TargetType.URBAN_AREA);
        targetTypes1_10.add(TargetType.AIRFIELD);

        /*
        targets11_35.put(12, Target.CHERBOURG);
        targets11_35.put(13, Target.AMIENS);
        targets11_35.put(14, Target.MEAULTE);
        targets11_35.put(15, Target.ABBEVILLE);
        targets11_35.put(16, Target.LILLE);
        targets11_35.put(21, Target.ROTTERDAM);
        targets11_35.put(22, Target.ANTWERP);
        targets11_35.put(23, Target.ROUEN);
        targets11_35.put(24, Target.PARIS);
        targets11_35.put(25, Target.PARIS);
        targets11_35.put(26, Target.ROMILLY_SUR_SEINE);
        targets11_35.put(31, Target.ROMILLY_SUR_SEINE);
        targets11_35.put(32, Target.RENNES);
        targets11_35.put(33, Target.RENNES);
        targets11_35.put(34, Target.BREST);
        targets11_35.put(35, Target.BREST);
        targets11_35.put(36, Target.LORIENT);
        targets11_35.put(41, Target.LORIENT);
        targets11_35.put(42, Target.ST_NAZAIRE);
        targets11_35.put(43, Target.ST_NAZAIRE);
        targets11_35.put(44, Target.ST_NAZAIRE);
        targets11_35.put(45, Target.WILHELMSHAVEN);
        targets11_35.put(46, Target.WILHELMSHAVEN);
        targets11_35.put(51, Target.EMDEN);
        targets11_35.put(52, Target.EMDEN);
        targets11_35.put(53, Target.VEGESACK);
        targets11_35.put(54, Target.VEGESACK);
        targets11_35.put(55, Target.BREMEN);
        targets11_35.put(56, Target.BREMEN);
        targets11_35.put(61, Target.HAMM);
        targets11_35.put(62, Target.HAMM);
        targets11_35.put(63, Target.LA_ROCHELLE);
        targets11_35.put(64, Target.LA_ROCHELLE);
        targets11_35.put(65, Target.KIEL);
        targets11_35.put(66, Target.KIEL);

        targetTypes11_35.put(11, TargetType.AIRFIELD);
        targetTypes11_35.put(12, TargetType.AIRFIELD);
        targetTypes11_35.put(13, TargetType.RAIL_YARD);
        targetTypes11_35.put(14, TargetType.AIRCRAFT_FACTORY);
        targetTypes11_35.put(15, TargetType.AIRFIELD);
        targetTypes11_35.put(16, TargetType.INDUSTRY);
        targetTypes11_35.put(21, TargetType.SHIP_YARD);
        targetTypes11_35.put(22, TargetType.INDUSTRY);
        targetTypes11_35.put(23, TargetType.RAIL_YARD);
        targetTypes11_35.put(24, TargetType.INDUSTRY);
        targetTypes11_35.put(25, TargetType.INDUSTRY);
        targetTypes11_35.put(26, TargetType.AIRCRAFT_FACTORY);
        targetTypes11_35.put(31, TargetType.AIRCRAFT_FACTORY);
        targetTypes11_35.put(32, TargetType.RAIL_YARD);
        targetTypes11_35.put(33, TargetType.RAIL_YARD);
        targetTypes11_35.put(34, TargetType.U_BOATS);
        targetTypes11_35.put(35, TargetType.U_BOATS);
        targetTypes11_35.put(36, TargetType.U_BOATS);
        targetTypes11_35.put(41, TargetType.U_BOATS);
        targetTypes11_35.put(42, TargetType.U_BOATS);
        targetTypes11_35.put(43, TargetType.U_BOATS);
        targetTypes11_35.put(44, TargetType.U_BOATS);
        targetTypes11_35.put(45, TargetType.U_BOATS);
        targetTypes11_35.put(46, TargetType.U_BOATS);
        targetTypes11_35.put(51, TargetType.INDUSTRY);
        targetTypes11_35.put(52, TargetType.INDUSTRY);
        targetTypes11_35.put(53, TargetType.U_BOATS);
        targetTypes11_35.put(54, TargetType.U_BOATS);
        targetTypes11_35.put(55, TargetType.AIRCRAFT_FACTORY);
        targetTypes11_35.put(56, TargetType.AIRCRAFT_FACTORY);
        targetTypes11_35.put(61, TargetType.RAIL_YARD);
        targetTypes11_35.put(62, TargetType.RAIL_YARD);
        targetTypes11_35.put(63, TargetType.U_BOATS);
        targetTypes11_35.put(64, TargetType.U_BOATS);
        targetTypes11_35.put(65, TargetType.U_BOATS);
        targetTypes11_35.put(66, TargetType.U_BOATS);
         */
    }

    public static TimeOfDay getMissionTimeOfDay(int missionNumber){
        if (missionNumber <= 10)
            return TimeOfDay.DAY;
        int die = Util.roll2d();
        return die <= 6? TimeOfDay.NIGHT: TimeOfDay.DAY;
    }

    public static TargetInfo getMissionTarget(int missionNumber, TimeOfDay missionTimeOfDay) {
        TargetInfo targetInfo = new TargetInfo();
        if (missionNumber <= 10) {
            int die = Util.roll2d() - 2;
            targetInfo.target = targets1_10.get(die);
            targetInfo.targetType = targetTypes1_10.get(die);
        }
        else {
            int die = Util.roll2d();
            if (missionTimeOfDay == TimeOfDay.DAY) {
                switch (die) {
                    case 2:
                    case 3:
                    case 6:
                    case 8:
                    case 11:
                    case 12:
                        targetInfo.targetType = TargetType.URBAN_AREA;
                        break;
                    case 4:
                    case 5:
                    case 9:
                        targetInfo.targetType = TargetType.AIRFIELD;
                        break;
                    default:
                        targetInfo.targetType = TargetType.AIRCRAFT_FACTORY;
                }
            } else {
                if (die == 12)
                    targetInfo.targetType = TargetType.URBAN_AREA;
                else
                    targetInfo.targetType = TargetType.AIRCRAFT_FACTORY;
            }

            if (targetInfo.targetType == TargetType.URBAN_AREA){
                die = Util.roll2d();
                switch (die){
                    case 2:
                        targetInfo.target = Target.YAWATA;
                        break;
                    case 3:
                    case 7:
                        targetInfo.target = Target.NAGOYA;
                        break;
                    case 4:
                        targetInfo.target = Target.YOKOHAMA;
                        break;
                    case 5:
                        targetInfo.target = Target.KOBE;
                        break;
                    case 6:
                    case 8:
                        targetInfo.target = Target.TOKYO;
                        break;
                    case 9:
                    case 10:
                        targetInfo.target = Target.OSAKA;
                        break;
                    case 11:
                        targetInfo.target = Target.KAWASAKI;
                        break;
                    case 12:
                        targetInfo.target = Target.AMAGASAKI;
                        break;
                }
            }
            else if (targetInfo.targetType == TargetType.AIRFIELD){
                die = Util.roll(2, false);
                switch (die){
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                        targetInfo.target = Target.KANOYA;
                        break;
                    case 21:
                    case 22:
                    case 23:
                        targetInfo.target = Target.KOKUBU;
                        break;
                    case 24:
                    case 25:
                    case 26:
                        targetInfo.target = Target.MIYAZAKI;
                        break;
                    case 31:
                    case 32:
                    case 33:
                        targetInfo.target = Target.MIYAKONOJO;
                        break;
                    case 34:
                    case 35:
                    case 36:
                        targetInfo.target = Target.KANOYA_EAST;
                        break;
                    case 41:
                    case 42:
                        targetInfo.target = Target.IZUMI;
                        break;
                    case 43:
                    case 44:
                        targetInfo.target = Target.KUSHIRA;
                        break;
                    case 45:
                        targetInfo.target = Target.NITTAGAHARA;
                        break;
                    case 46:
                        targetInfo.target = Target.USA;
                        break;
                    case 51:
                        targetInfo.target = Target.SAEKI;
                        break;
                    case 52:
                        targetInfo.target = Target.MATSUYAMA;
                        break;
                    case 53:
                        targetInfo.target = Target.TOMITAKA;
                        break;
                    case 54:
                        targetInfo.target = Target.IBUSUKI;
                        break;
                    case 55:
                    case 56:
                    case 61:
                        targetInfo.target = Target.TACHIARAI;
                        break;
                    case 62:
                    case 63:
                    case 64:
                    case 65:
                        targetInfo.target = Target.OITA;
                        break;
                    case 66:
                        targetInfo.target = Target.CHIRAN;
                        break;
                }
            }
            else if (targetInfo.targetType == TargetType.AIRCRAFT_FACTORY){
                die = Util.roll(2, false);
                switch (die){
                    case 11:
                        targetInfo.target = Target.HANDA;
                        break;
                    case 12:
                        targetInfo.target = Target.OSAKA;
                        break;
                    case 13:
                    case 14:
                        targetInfo.target = Target.EITOKU;
                        break;
                    case 15:
                    case 16:
                    case 21:
                        targetInfo.target = Target.AKASHI;
                        break;
                    case 22:
                        targetInfo.target = Target.KORIYAMA;
                        break;
                    case 23:
                    case 24:
                    case 25:
                    case 26:
                    case 31:
                    case 32:
                    case 33:
                        targetInfo.target = Target.TOKYO;
                        break;
                    case 34:
                    case 35:
                    case 36:
                    case 41:
                    case 42:
                    case 43:
                        targetInfo.target = Target.NAGOYA;
                        break;
                    case 44:
                    case 45:
                    case 46:
                        targetInfo.target = Target.OMURA;
                        break;
                    case 51:
                    case 52:
                    case 53:
                        targetInfo.target = Target.KAGAMIGAHARA;
                        break;
                    case 54:
                        targetInfo.target = Target.SHIZUOKA;
                        break;
                    case 55:
                        targetInfo.target = Target.KURE;
                        break;
                    case 56:
                        targetInfo.target = Target.NARAO;
                        break;
                    case 61:
                        targetInfo.target = Target.OGIKUBU;
                        break;
                    case 62:
                        targetInfo.target = Target.OMIYA;
                        break;
                    case 63:
                        targetInfo.target = Target.TOMIOKA;
                        break;
                    case 64:
                        targetInfo.target = Target.CHIBA;
                        break;
                    case 65:
                        targetInfo.target = Target.HIMEJI;
                        break;
                    case 66:
                        targetInfo.target = Target.MIZUSHIMA;
                        break;
                }
            }
        }
        return targetInfo;
    }

    /**
     * 2-3
     * Used if target returned above is "bombed out"
     * @return
     */
    public static Target getAlternateUrbanAreaTarget(){
        int die = Util.roll();
        return Util.roll() <= 3? getAlternateUrbanAreaTargetChart1(): getAlternateUrbanAreaTargetChart2();
    }

    private static Target getAlternateUrbanAreaTargetChart1(){
        int die = Util.roll(2, false);
        switch (die) {
            case 11:
                return Target.HANDA;
            case 12:
                return Target.HAMAMATSU;
            case 13:
                return Target.YOKKAICHII;
            case 14:
                return Target.TOYOHASHI;
            case 15:
                return Target.FUKUOKA;
            case 16:
                return Target.SHIZUOKA;
            case 21:
                return Target.OKAYAMA;
            case 22:
                return Target.SASEBO;
            case 23:
                return Target.MOJI;
            case 24:
                return Target.NOBEOKA;
            case 25:
                return Target.KURE;
            case 26:
                return Target.KUMAMATO;
            case 31:
                return Target.UBE;
            case 32:
                return Target.SHIMONOSEKI;
            case 33:
                return Target.TAKAMATSU;
            case 34:
                return Target.KOCHI;
            case 35:
                return Target.HIMEJI;
            case 36:
                return Target.TOKUSHIMA;
            case 41:
                return Target.CHIBA;
            case 42:
                return Target.AKASHI;
            case 43:
                return Target.SHIMIZU;
            case 44:
                return Target.KOFU;
            case 45:
                return Target.SENDAI;
            case 46:
                return Target.SAKAI;
            case 51:
                return Target.WAKAYAMA;
            case 52:
                return Target.GIFU;
            case 53:
                return Target.UTSUNOMIYA;
            case 54:
                return Target.ICHINOMIYA;
            case 55:
                return Target.TSURUGA;
            case 56:
                return Target.UWAJIMA;
            case 61:
                return Target.NAMAZU;
            case 62:
                return Target.OITA;
            case 63:
                return Target.KUWANA;
            case 64:
                return Target.HIRATSUKA;
            case 65:
                return Target.FUKUI;
            case 66:
                return Target.KOKURA;
        }
        return null;
    }

    private static Target getAlternateUrbanAreaTargetChart2() {
        int die = Util.roll(2, false);
        switch (die) {
            case 11:
                return Target.HIROSHIMA;
            case 12:
                return Target.OKAZAKI;
            case 13:
                return Target.MATSUYAMA;
            case 14:
                return Target.TOKUYAMA;
            case 15:
                return Target.OMUTA;
            case 16:
                return Target.TSU;
            case 21:
                return Target.AOMORI;
            case 22:
                return Target.ICHINOMIYA;
            case 23:
                return Target.UJI_YAMADA;
            case 24:
                return Target.OGAKI;
            case 25:
                return Target.UWAJIMA;
            case 26:
                return Target.HACHIOJI;
            case 31:
                return Target.TOYAMA;
            case 32:
                return Target.NAGAOKA;
            case 33:
                return Target.MITO;
            case 34:
                return Target.SAGA;
            case 35:
                return Target.MAEBASHI;
            case 36:
                return Target.NISHINOMIYA;
            case 41:
                return Target.MIKAGE;
            case 42:
                return Target.IMABARI;
            case 43:
                return Target.FUKUYAMA;
            case 44:
                return Target.KUMAGAYA;
            case 45:
                return Target.ISEZAKI;
            case 46:
                return Target.CHOSHI;
            case 51:
                return Target.KAGOSHIMA;
            case 52:
                return Target.HITACHI;
            case 53:
                return Target.KUSHIKINO;
            case 54:
                return Target.AKITA;
            case 55:
                return Target.KANSAI;
            case 56:
                return Target.KUDAMATSU;
            case 61:
                return Target.OSHIMA;
            case 62:
                return Target.FUKUOKA;
            case 63:
                return Target.KUMAMATO;
            case 64:
                return Target.SENDAI;
            case 65:
                return Target.NAGASAKI;
            case 66:
                return Target.KYOTO;
        }
        return null;
    }

    public static Altitude getMissionAltitude(int missionNumber, TimeOfDay missionTimeOfDay, TargetType targetType){
        if (missionNumber <= 10)
            return Altitude.HI;
        if (missionTimeOfDay == TimeOfDay.DAY){
            if (targetType == TargetType.URBAN_AREA){
                int die = Util.roll();
                if (die == 1)
                    return Altitude.HI;
                if (die <= 5)
                    return Altitude.MED;
                return Altitude.LO;
            }
            else {
                int die = Util.roll2d();
                if (die == 2)
                    return Altitude.LO;
                if (die == 4)
                    return Altitude.HI;
                return Altitude.MED;
            }
        }
        return Altitude.LO;
    }

    /**
     * 2-4
     * @param bomber
     * @return
     */
    public static FormationPosition getFormationPosition(Bomber bomber){
        int die = Util.roll2d();
        CrewMember bombardier = bomber.getCrewMemberByDefaultRole(CrewPosition.BOMBARDIER);
        CrewMember navigator = bomber.getCrewMemberByDefaultRole(CrewPosition.NAVIGATOR);
        CrewMember radarOperator = bomber.getCrewMemberByDefaultRole(CrewPosition.RADAR_OPERATOR);
        if (bombardier.getNumMissionsFlown() >= 8 && navigator.getNumMissionsFlown() >= 8 && radarOperator.getNumMissionsFlown() >= 8 && die <= 3)
            return FormationPosition.LEAD;
        if (die == 2)
            return FormationPosition.LEAD;
        if (die <= 10)
            return FormationPosition.MIDDLE;
        return FormationPosition.TAIL;
    }

    /**
     * 2-5
     * @return
     */
    public static SquadronPosition getSquadronPosition(){
        int die = Util.roll();
        if (die <= 2)
            return SquadronPosition.HIGH;
        if (die <= 4)
            return SquadronPosition.MIDDLE;
        return SquadronPosition.LOW;
    }

    /**
     * 2-6
     * @param mission
     * @return
     */
    public static JapaneseFighterDensity getExpectedJapaneseFighterResistance(Mission mission){
        int die = Util.roll();
        if (mission.getMissionTimeOfDay() == TimeOfDay.NIGHT || mission.getTarget() == Target.IWO_JIMA)
            die -= 1;
        if (mission.getTarget() == Target.TOKYO || mission.getTarget() == Target.YOKOHAMA)
            die += 1;
        if (die <= 2)
            return JapaneseFighterDensity.NONE;
        if (die == 3)
            return JapaneseFighterDensity.LIGHT;
        if (die == 4)
            return JapaneseFighterDensity.MODERATE;
        return JapaneseFighterDensity.HEAVY;
    }

    /**
     * 2-8
     * @param mission
     * @return
     */
    public static boolean isFighterEscortAvailable(Mission mission){
        int die = Util.roll2d();
        if (mission.getMissionNumber() <= 15 && mission.getTarget() != Target.IWO_JIMA)
            die -= 4;
        if (mission.getExpectedFighterDensity() == JapaneseFighterDensity.HEAVY)
            die += 1;
        if (mission.getExpectedFighterDensity() == JapaneseFighterDensity.NONE || mission.getExpectedFighterDensity() == JapaneseFighterDensity.LIGHT)
            die -= 1;
        return die >= 8;
    }

    public static class TargetInfo{
        public Target target;
        public TargetType targetType;
    }

    private MissionSetupTable() {
    }
}
