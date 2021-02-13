package b29.game.mission.chart;

import b29.game.mission.MapAreaCode;
import b29.game.mission.Target;

import java.util.*;

/**
 * G-11
 */
public class FlightLogGazeteer {
    private static Map<Target, Map<Integer, ZoneInfo>> zoneInfo = new HashMap<>();
    private static List<Target> targetsWithoutSearchlightsOrAAGuns = Arrays.asList(new Target[]{
            Target.AKASHI,
            Target.AOMORI,
            Target.CHIBA,
            Target.CHOSHI,
            Target.FUKUI,
            Target.FUKUOKA,
            Target.FUKUYAMA,
            Target.GIFU,
            Target.HACHIOJI,
            Target.HAMAMATSU,
            Target.HIMEJI,
            Target.HIRATSUKA,
            Target.HITACHI,
            Target.ICHINOMIYA,
            Target.IMABARI,
            Target.ISEZAKI,
            Target.IWO_JIMA,
            Target.KAGOSHIMA,
            Target.KOCHI,
            Target.KOFU,
            Target.KUMAGAYA,
            Target.KUMAMATO,
            Target.KUWANA,
            Target.MAEBASHI,
            Target.MATSUYAMA,
            Target.MITO,
            Target.MOJI,
            Target.NAGAOKA,
            Target.NAMAZU,
            Target.NISHINOMIYA,
            Target.NOBEOKA,
            Target.OGAKI,
            Target.OITA,
            Target.OKAYAMA,
            Target.OKAZAKI,
            Target.OKINAWA,
            Target.OMUTA,
            Target.SAGA,
            Target.SAKAI,
            Target.SENDAI,
            Target.SHIMIZU,
            Target.SHIZUOKA,
            Target.TAKAMATSU,
            Target.TOKUSHIMA,
            Target.TOKUYAMA,
            Target.TOYAMA,
            Target.TOYOHASHI,
            Target.TSU,
            Target.TSURUGA,
            Target.UBE,
            Target.UJI_YAMADA,
            Target.UTSUNOMIYA,
            Target.UWAJIMA,
            Target.WAKAYAMA,
            Target.YOKKAICHII
    });

    static {
        for (Target target: Target.values()) {
            zoneInfo.put(target, new HashMap<>());
            zoneInfo.get(target).put(6, new ZoneInfo(-2, MapAreaCode.WATER, MapAreaCode.IWO_JIMA));
            zoneInfo.get(target).put(10, new ZoneInfo(-2, MapAreaCode.WATER));
            zoneInfo.get(target).put(11, new ZoneInfo(-1, MapAreaCode.WATER, MapAreaCode.JAPAN));
        }

        zoneInfo.get(Target.AKASHI).put(11, new ZoneInfo(-2, MapAreaCode.WATER));
        zoneInfo.get(Target.AKASHI).put(12, new ZoneInfo(-1, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.AKITA).put(10, new ZoneInfo(-2, MapAreaCode.WATER, MapAreaCode.JAPAN));
        zoneInfo.get(Target.AKITA).put(11, new ZoneInfo(-1, MapAreaCode.JAPAN));
        zoneInfo.get(Target.AKITA).put(12, new ZoneInfo(0, MapAreaCode.JAPAN));
        zoneInfo.get(Target.AKITA).put(13, new ZoneInfo(0, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.AMAGASAKI).put(12, new ZoneInfo(0, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.AOMORI).put(11, new ZoneInfo(-2, MapAreaCode.WATER));
        zoneInfo.get(Target.AOMORI).put(12, new ZoneInfo(-1, MapAreaCode.WATER, MapAreaCode.JAPAN));
        zoneInfo.get(Target.AOMORI).put(13, new ZoneInfo(0, MapAreaCode.JAPAN));
        zoneInfo.get(Target.AOMORI).put(14, new ZoneInfo(0, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.CHIBA).put(11, new ZoneInfo(0, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.CHIRAN).put(11, new ZoneInfo(-1, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.CHOSHI).put(11, new ZoneInfo(-1, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.EITOKU).put(10, new ZoneInfo(-2, MapAreaCode.WATER, MapAreaCode.JAPAN));
        zoneInfo.get(Target.EITOKU).put(11, new ZoneInfo(-1, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.FUKUI).put(11, new ZoneInfo(-1, MapAreaCode.JAPAN));
        zoneInfo.get(Target.FUKUI).put(12, new ZoneInfo(1, MapAreaCode.JAPAN));

        zoneInfo.get(Target.FUKUOKA).put(11, new ZoneInfo(-1, MapAreaCode.JAPAN));
        zoneInfo.get(Target.FUKUOKA).put(12, new ZoneInfo(0, MapAreaCode.WATER, MapAreaCode.JAPAN));

        zoneInfo.get(Target.FUKUYAMA).put(12, new ZoneInfo(0, MapAreaCode.WATER, MapAreaCode.JAPAN));

        update(Target.GIFU, 10, -2, MapAreaCode.WATER, MapAreaCode.JAPAN);
        update(Target.GIFU, 11, 0, MapAreaCode.JAPAN);

        update(Target.HACHIOJI, 11, 1, MapAreaCode.JAPAN);

        add(Target.HIMEJI, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.HIRATSUKA, 10, -2, MapAreaCode.WATER, MapAreaCode.JAPAN);
        update(Target.HIRATSUKA, 11, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.HIROSHIMA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.HITACHI, 11, -2, MapAreaCode.WATER);
        add(Target.HITACHI, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.ICHINOMIYA, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);
        add(Target.ICHINOMIYA, 0, MapAreaCode.JAPAN);

        add(Target.ISEZAKI, 1, MapAreaCode.JAPAN);

        add(Target.IZUMI, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.KAGAMIGAHARA, 0, MapAreaCode.JAPAN);

        update(Target.KAWASAKI, 11, 1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.KOBE, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.KOFU, 10, -2, MapAreaCode.WATER, MapAreaCode.JAPAN);
        update(Target.KOFU, 11, 0, MapAreaCode.JAPAN);

        add(Target.KOKURA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.KORIYAMA, 11, -1, MapAreaCode.JAPAN);
        add(Target.KORIYAMA, 0, MapAreaCode.JAPAN);

        add(Target.KUDAMATSU, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.KUMAGAYA, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);
        add(Target.KUMAGAYA, 1, MapAreaCode.JAPAN);

        add(Target.KUMAMATO, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.KURE, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.KUSHIKINO, 11, -2, MapAreaCode.WATER);
        add(Target.KUSHIKINO, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.KUWANA, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.KYOTO, 0, MapAreaCode.JAPAN);

        add(Target.MAEBASHI, 0, MapAreaCode.JAPAN);

        update(Target.MATSUYAMA, 11, -1, MapAreaCode.JAPAN);

        add(Target.MIKAGE, 0, MapAreaCode.JAPAN);

        update(Target.MITO, 11, -2, MapAreaCode.WATER);
        add(Target.MITO, 0, MapAreaCode.JAPAN);

        update(Target.MIYAKONOJO, 11, -1, MapAreaCode.JAPAN);

        update(Target.MIYAZAKI, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.MIZUSHIMA, 0, MapAreaCode.JAPAN);

        add(Target.MOJI, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.NAGAOKA, 10, -2, MapAreaCode.WATER, MapAreaCode.JAPAN);
        update(Target.NAGAOKA, 11, 0, MapAreaCode.JAPAN);
        add(Target.NAGAOKA, 1, MapAreaCode.JAPAN);

        update(Target.NAGASAKI, 11, -1, MapAreaCode.WATER);
        add(Target.NAGASAKI, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.NAGOYA, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.NARAO, 11, -2, MapAreaCode.WATER);
        add(Target.NARAO, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.NIIGATA, 0, MapAreaCode.JAPAN);
        add(Target.NIIGATA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.NISHINOMIYA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.NITTAGAHARA, 11, -2, MapAreaCode.WATER);
        add(Target.NITTAGAHARA, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.NOBEOKA, 11, -2, MapAreaCode.WATER);
        add(Target.NOBEOKA, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.OGAKI, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);
        add(Target.OGAKI, 0, MapAreaCode.JAPAN);

        update(Target.OGIKUBU, 11, -1, MapAreaCode.JAPAN);

        update(Target.OITA, 11, -2, MapAreaCode.WATER);
        add(Target.OITA, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.OKAYAMA, 0, MapAreaCode.JAPAN);

        update(Target.OKAZAKI, 11, -1, MapAreaCode.JAPAN);

        update(Target.OKINAWA, 11, -2, MapAreaCode.WATER, MapAreaCode.OKINAWA);

        update(Target.OMIYA, 11, -1, MapAreaCode.JAPAN);

        add(Target.OMURA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.OMUTA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.OSAKA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.OSHIMA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.OTA, 0, MapAreaCode.JAPAN);

        update(Target.SAEKI, 11, -2, MapAreaCode.WATER);
        add(Target.SAEKI, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.SAGA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.SAKAI, 10, -2, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.SASEBO, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.SENDAI, 11, -2, MapAreaCode.WATER);
        add(Target.SENDAI, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.SHIMONOSEKI, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.TACHIARAI, 0, MapAreaCode.JAPAN);

        update(Target.TAKAMATSU, 11, -2, MapAreaCode.WATER);
        add(Target.TAKAMATSU, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.TOKUYAMA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.TOKYO, 11, 1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.TOMIOKA, 0, MapAreaCode.JAPAN);

        update(Target.TOMITAKA, 11, -2, MapAreaCode.WATER);
        add(Target.TOMITAKA, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.TOYAMA, 10, -2, MapAreaCode.WATER, MapAreaCode.JAPAN);
        update(Target.TOYAMA, 11, 0, MapAreaCode.JAPAN);
        add(Target.TOYAMA, 1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.TSURUGA, 1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.UBE, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.UTSUNOMIYA, 0, MapAreaCode.JAPAN);

        update(Target.UWAJIMA, 11, -2, MapAreaCode.WATER);
        add(Target.UWAJIMA, -1, MapAreaCode.WATER, MapAreaCode.JAPAN);

        add(Target.YAWATA, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.YOKKAICHII, 11, 0, MapAreaCode.WATER, MapAreaCode.JAPAN);

        update(Target.YOKOHAMA, 11, 1, MapAreaCode.WATER, MapAreaCode.JAPAN);
    }

    public static int getModifier(Target target, int zone) {
        ZoneInfo info = zoneInfo.get(target).get(zone);
        return info.fighterWaveModifier;
    }

    public static List<MapAreaCode> getMapAreaCodes(Target target, int zone) {
        ZoneInfo info = zoneInfo.get(target).get(zone);
        return info.codes;
    }

    public static boolean targetWithoutSearchlightsOrAAGuns(Target target){
        return targetsWithoutSearchlightsOrAAGuns.contains(target);
    }

    private FlightLogGazeteer() {
    }

    private static void add(Target target, int modifier, MapAreaCode code1){
        int maxZone = zoneInfo.get(target).keySet().stream().max(Integer::compareTo).get();
        zoneInfo.get(target).put(maxZone + 1, new ZoneInfo(modifier, code1));
    }

    private static void add(Target target, int modifier, MapAreaCode code1, MapAreaCode code2){
        int maxZone = zoneInfo.get(target).keySet().stream().max(Integer::compareTo).get();
        zoneInfo.get(target).put(maxZone + 1, new ZoneInfo(modifier, code1, code2));
    }

    private static void update(Target target, int zone, int modifier, MapAreaCode code1){
        update(target, zone, modifier, code1, null);
    }

    private static void update(Target target, int zone, int modifier, MapAreaCode code1, MapAreaCode code2){
        zoneInfo.get(target).get(zone).fighterWaveModifier = modifier;
        zoneInfo.get(target).get(zone).codes.clear();
        zoneInfo.get(target).get(zone).codes.add(code1);
        if (code2 != null)
            zoneInfo.get(target).get(zone).codes.add(code2);
    }

    private static class ZoneInfo {
        public int fighterWaveModifier;
        public List<MapAreaCode> codes = new ArrayList<>();

        public ZoneInfo(int modifier, MapAreaCode code) {
            this.fighterWaveModifier = modifier;
            codes.add(code);
        }

        public ZoneInfo(int modifier, MapAreaCode code1, MapAreaCode code2) {
            this.fighterWaveModifier = modifier;
            codes.add(code1);
            codes.add(code2);
        }
    }
}
