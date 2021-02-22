package b29.game.mission;

import b29.game.bomber.Altitude;
import b29.game.mission.chart.FighterInfo;
import b29.game.mission.chart.JapaneseFighterDensity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mission {
    private int missionNumber;
    private MapAreaCode baseArea;
    private Target target;
    private TargetType targetType;
    private int targetZone;
    private FormationPosition formationPosition;
    private SquadronPosition squadronPosition;
    private TimeOfDay missionTimeOfDay;
    private TimeOfDay baseTakeoffTimeOfDay;
    private TimeOfDay expectedLandingTimeOfDay;
    public int abortOutMinDayZone;
    public int abortOutMaxDayZone;
    public int ditchOutMinDayZone;
    public int ditchOutMaxDayZone;
    public int ditchBackMinDayZone;
    public int ditchBackMaxDayZone;
    private Altitude missionAltitude;
    private JapaneseFighterDensity expectedFighterDensity;

    private Map<Integer, Integer> fighterDensityModifiers;
    private int bombRunPercentage;
    //private int landingModifier;
    private Direction direction;
    private boolean abortable;  // Should we give the player an option to abort?
    private boolean aborted;    // Has the player aborted the mission?
    private int zone;
    private int turnsInCurrentZone;
    private boolean outOfFormation;
    private boolean escortAvailable;

    // Zone that we're going to land in
    // Initially set to home base zone
    private int landingZone;

    // Temporary variables used to track in-game progress
    private int numWaveAttacks;
    private int numFighterDefenseLeft;
    private FighterType escort;
    private Weather weather;
    private FormationAssembly formationAssembly;
    private boolean stormSystemEncountered;
    private boolean highPressureSystemEncountered;
    private boolean pathfinder;
    private boolean pickleBarrel;
    private boolean ableToPerformEvasiveAction;
    private List<JapaneseFighter> japaneseFighters = new ArrayList<>();
    private boolean spottedBySearchlight;

    // Number of turns until bomber regains formation (ie. bomber is out of formation this turn, then regains formation)
    // If numTurnsTillRegainFormation == 0, then regain formation.  If > 0, decrement.  If < 0, ignore.
    private int numTurnsTillRegainFormation;

    // Number of zones the bomber may move before the crew MUST bail out
    private int zonesBeforeBailOut;

    public Mission(int missionNumber) {
        this.missionNumber = missionNumber;
        baseArea = MapAreaCode.MARIANAS;
        target = null;
        targetType = null;
        targetZone = 0;
        formationPosition = null;
        squadronPosition = null;
        missionTimeOfDay = TimeOfDay.DAY;
        baseTakeoffTimeOfDay = TimeOfDay.DAY;
        expectedLandingTimeOfDay = TimeOfDay.DAY;
        abortOutMinDayZone = 1;
        abortOutMaxDayZone = 14;
        ditchOutMinDayZone = 1;
        ditchOutMaxDayZone = 14;
        ditchBackMinDayZone = 1;
        ditchBackMaxDayZone = 14;
        missionAltitude = Altitude.MED;
        expectedFighterDensity = JapaneseFighterDensity.NONE;

        fighterDensityModifiers = new HashMap<>();
        bombRunPercentage = 0;
        //landingModifier = 0;
        direction = Direction.TO_TARGET;
        abortable = false;
        aborted = false;
        zone = 0;  // Home Base
        turnsInCurrentZone = 0;
        outOfFormation = true;
        escortAvailable = false;
        landingZone = 0;

        numWaveAttacks = 0;
        numFighterDefenseLeft = 0;
        escort = null;
        weather = Weather.GOOD;
        formationAssembly = null;
        stormSystemEncountered = false;
        highPressureSystemEncountered = false;
        pathfinder = false;
        pickleBarrel = false;
        ableToPerformEvasiveAction = true;
        spottedBySearchlight = false;
        numTurnsTillRegainFormation = -1;
        zonesBeforeBailOut = -1;

        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_ZEKE, FighterApproach.AREA_12, FighterAltitude.HIGH)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_TONY, FighterApproach.AREA_12, FighterAltitude.LEVEL)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_TOJO, FighterApproach.AREA_12, FighterAltitude.LOW)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_NICK, FighterApproach.AREA_1_30, FighterAltitude.HIGH)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_OSCAR, FighterApproach.AREA_1_30, FighterAltitude.LEVEL)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_GEORGE, FighterApproach.AREA_1_30, FighterAltitude.LOW)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_FRANK, FighterApproach.VERTICAL_CLIMB, FighterAltitude.LEVEL)));
        japaneseFighters.add(new JapaneseFighter(new FighterInfo(FighterType.FIGHTER_JACK, FighterApproach.VERTICAL_DIVE, FighterAltitude.LEVEL)));
    }

    public TimeOfDay getActualLandingTimeOfDay(){
        if (aborted){
            if (direction == Direction.TO_TARGET){
                return (zone >= abortOutMinDayZone && zone <= abortOutMaxDayZone)? TimeOfDay.DAY: TimeOfDay.NIGHT;
            }
        }
        else {
            if (direction == Direction.TO_TARGET) {
                return (zone >= ditchOutMinDayZone && zone <= ditchOutMaxDayZone)? TimeOfDay.DAY: TimeOfDay.NIGHT;
            }
            else {
                return (zone >= ditchBackMinDayZone && zone <= ditchBackMaxDayZone)? TimeOfDay.DAY: TimeOfDay.NIGHT;
            }
        }
        return expectedLandingTimeOfDay;
    }

    public int getMissionNumber() {
        return missionNumber;
    }

    public void setMissionNumber(int missionNumber) {
        this.missionNumber = missionNumber;
    }

    public MapAreaCode getBaseArea() {
        return baseArea;
    }

    public void setBaseArea(MapAreaCode baseArea) {
        this.baseArea = baseArea;
    }

    public Target getTarget() {
        return target;
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public TargetType getTargetType() {
        return targetType;
    }

    public void setTargetType(TargetType targetType) {
        this.targetType = targetType;
    }

    public int getTargetZone() {
        return targetZone;
    }

    public void setTargetZone(int targetZone) {
        this.targetZone = targetZone;
    }

    public FormationPosition getFormationPosition() {
        return formationPosition;
    }

    public void setFormationPosition(FormationPosition formationPosition) {
        this.formationPosition = formationPosition;
    }

    public SquadronPosition getSquadronPosition() {
        return squadronPosition;
    }

    public void setSquadronPosition(SquadronPosition squadronPosition) {
        this.squadronPosition = squadronPosition;
    }

    public TimeOfDay getMissionTimeOfDay() {
        return missionTimeOfDay;
    }

    public void setMissionTimeOfDay(TimeOfDay missionTimeOfDay) {
        this.missionTimeOfDay = missionTimeOfDay;
    }

    public Map<Integer, Integer> getFighterDensityModifiers() {
        return fighterDensityModifiers;
    }

    public void setFighterDensityModifiers(Map<Integer, Integer> fighterDensityModifiers) {
        this.fighterDensityModifiers = fighterDensityModifiers;
    }

    public int getBombRunPercentage() {
        return bombRunPercentage;
    }

    public void setBombRunPercentage(int bombRunPercentage) {
        this.bombRunPercentage = bombRunPercentage;
    }

    /*
    public int getLandingModifier() {
        return landingModifier;
    }

    public void setLandingModifier(int landingModifier) {
        this.landingModifier = landingModifier;
    }
     */

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public boolean isAbortable() {
        return abortable;
    }

    public void setAbortable(boolean abortable) {
        this.abortable = abortable;
    }

    public boolean isAborted() {
        return aborted;
    }

    public void setAborted(boolean aborted) {
        this.aborted = aborted;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public boolean isOutOfFormation() {
        return outOfFormation;
    }

    public void setOutOfFormation(boolean outOfFormation) {
        this.outOfFormation = outOfFormation;
    }

    public int getTurnsInCurrentZone() {
        return turnsInCurrentZone;
    }

    public void setTurnsInCurrentZone(int turnsInCurrentZone) {
        this.turnsInCurrentZone = turnsInCurrentZone;
    }

    public int getNumWaveAttacks() {
        return numWaveAttacks;
    }

    public void setNumWaveAttacks(int numWaveAttacks) {
        this.numWaveAttacks = numWaveAttacks;
    }

    public void incNumWaveAttacks(){
        this.numWaveAttacks += 1;
    }

    public int getNumFighterDefenseLeft() {
        return numFighterDefenseLeft;
    }

    public void setNumFighterDefenseLeft(int numFighterDefenseLeft) {
        this.numFighterDefenseLeft = numFighterDefenseLeft;
    }

    public void decNumFighterDefenseLeft(){
        this.numFighterDefenseLeft -= 1;
    }

    public Altitude getMissionAltitude() {
        return missionAltitude;
    }

    public void setMissionAltitude(Altitude missionAltitude) {
        this.missionAltitude = missionAltitude;
    }

    public boolean isEscortAvailable() {
        return escortAvailable;
    }

    public void setEscortAvailable(boolean escortAvailable) {
        this.escortAvailable = escortAvailable;
    }

    public FighterType getEscort() {
        return escort;
    }

    public void setEscort(FighterType escort) {
        this.escort = escort;
    }

    public TimeOfDay getBaseTakeoffTimeOfDay() {
        return baseTakeoffTimeOfDay;
    }

    public void setBaseTakeoffTimeOfDay(TimeOfDay baseTakeoffTimeOfDay) {
        this.baseTakeoffTimeOfDay = baseTakeoffTimeOfDay;
    }

    public TimeOfDay getExpectedLandingTimeOfDay() {
        return expectedLandingTimeOfDay;
    }

    public void setExpectedLandingTimeOfDay(TimeOfDay expectedLandingTimeOfDay) {
        this.expectedLandingTimeOfDay = expectedLandingTimeOfDay;
    }

    public int getAbortOutMaxDayZone() {
        return abortOutMaxDayZone;
    }

    public int getAbortOutMinDayZone() {
        return abortOutMinDayZone;
    }

    public void setAbortOutMinDayZone(int abortOutMinDayZone) {
        this.abortOutMinDayZone = abortOutMinDayZone;
    }

    public void setAbortOutMaxDayZone(int abortOutMaxDayZone) {
        this.abortOutMaxDayZone = abortOutMaxDayZone;
    }

    public int getDitchOutMinDayZone() {
        return ditchOutMinDayZone;
    }

    public void setDitchOutMinDayZone(int ditchOutMinDayZone) {
        this.ditchOutMinDayZone = ditchOutMinDayZone;
    }

    public int getDitchOutMaxDayZone() {
        return ditchOutMaxDayZone;
    }

    public void setDitchOutMaxDayZone(int ditchOutMaxDayZone) {
        this.ditchOutMaxDayZone = ditchOutMaxDayZone;
    }

    public int getDitchBackMinDayZone() {
        return ditchBackMinDayZone;
    }

    public void setDitchBackMinDayZone(int ditchBackMinDayZone) {
        this.ditchBackMinDayZone = ditchBackMinDayZone;
    }

    public int getDitchBackMaxDayZone() {
        return ditchBackMaxDayZone;
    }

    public void setDitchBackMaxDayZone(int ditchBackMaxDayZone) {
        this.ditchBackMaxDayZone = ditchBackMaxDayZone;
    }

    public Weather getWeather() {
        return weather;
    }

    public void setWeather(Weather baseWeather) {
        this.weather = baseWeather;
    }

    public JapaneseFighterDensity getExpectedFighterDensity() {
        return expectedFighterDensity;
    }

    public void setExpectedFighterDensity(JapaneseFighterDensity expectedFighterDensity) {
        this.expectedFighterDensity = expectedFighterDensity;
    }

    public FormationAssembly getFormationAssembly() {
        return formationAssembly;
    }

    public void setFormationAssembly(FormationAssembly formationAssembly) {
        this.formationAssembly = formationAssembly;
    }

    public boolean isStormSystemEncountered() {
        return stormSystemEncountered;
    }

    public void setStormSystemEncountered(boolean stormSystemEncountered) {
        this.stormSystemEncountered = stormSystemEncountered;
    }

    public boolean isPathfinder() {
        return pathfinder;
    }

    public void setPathfinder(boolean pathfinder) {
        this.pathfinder = pathfinder;
    }

    public boolean isPickleBarrel() {
        return pickleBarrel;
    }

    public void setPickleBarrel(boolean pickleBarrel) {
        this.pickleBarrel = pickleBarrel;
    }

    public boolean isHighPressureSystemEncountered() {
        return highPressureSystemEncountered;
    }

    public void setHighPressureSystemEncountered(boolean highPressureSystemEncountered) {
        this.highPressureSystemEncountered = highPressureSystemEncountered;
    }

    public boolean isAbleToPerformEvasiveAction() {
        return ableToPerformEvasiveAction;
    }

    public void setAbleToPerformEvasiveAction(boolean ableToPerformEvasiveAction) {
        this.ableToPerformEvasiveAction = ableToPerformEvasiveAction;
    }

    public List<JapaneseFighter> getJapaneseFighters() {
        return japaneseFighters;
    }

    public void setJapaneseFighters(List<JapaneseFighter> japaneseFighters) {
        this.japaneseFighters = japaneseFighters;
    }

    public boolean isSpottedBySearchlight() {
        return spottedBySearchlight;
    }

    public void setSpottedBySearchlight(boolean spottedBySearchlight) {
        this.spottedBySearchlight = spottedBySearchlight;
    }

    public int getNumTurnsTillRegainFormation() {
        return numTurnsTillRegainFormation;
    }

    public void setNumTurnsTillRegainFormation(int numTurnsTillRegainFormation) {
        this.numTurnsTillRegainFormation = numTurnsTillRegainFormation;
    }

    public void adjNumTurnsTillRegainFormation(int amount) {
        this.numTurnsTillRegainFormation += amount;
    }

    public int getLandingZone() {
        return landingZone;
    }

    public void setLandingZone(int landingZone) {
        this.landingZone = landingZone;
    }

    public int getZonesBeforeBailOut() {
        return zonesBeforeBailOut;
    }

    public void setZonesBeforeBailOut(int zonesBeforeBailOut) {
        this.zonesBeforeBailOut = zonesBeforeBailOut;
    }
}
