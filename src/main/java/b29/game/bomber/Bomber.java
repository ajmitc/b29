package b29.game.bomber;

import b29.game.crew.CrewMember;
import b29.game.mission.Course;

import java.util.*;

public class Bomber {
    public static final int DEFAULT_MAX_FUEL = 38;
    public static final int DEFAULT_AUX_MAX_FUEL = 4;

    private String name;
    private int numMissionsCompleted;
    private BomberStatus bomberStatus;
    private List<CrewMember> crew = new ArrayList<>();
    private Map<GunPosition, Gun> guns = new HashMap<>();
    private Altitude altitude;
    private Pressurization pressurization;
    private boolean carryingBombs;
    private boolean underControl;
    private Course course;
    private boolean performingEvasiveAction;
    private int fuelLeft;
    private int auxAftFuelLeft;
    private int auxFwdFuelLeft;

    private Map<CrewPosition, List<Damage>> crewPositionDamage = new HashMap<>();
    private Map<BomberCompartment, List<Damage>> compartmentDamage = new HashMap<>();

    public Bomber() {
        this.name = "";
        this.numMissionsCompleted = 0;
        this.bomberStatus = BomberStatus.SAFE;

        Arrays.stream(CrewPosition.values()).forEach(crewPosition -> {
            crewPositionDamage.put(crewPosition, new ArrayList<>());
        });
        Arrays.stream(BomberCompartment.values()).forEach(bomberCompartment -> {
            compartmentDamage.put(bomberCompartment, new ArrayList<>());
        });
        Arrays.stream(GunPosition.values()).forEach(gunPosition -> {
            guns.put(gunPosition, new Gun(gunPosition, getMaxAmmo(gunPosition)));
        });

        this.altitude = Altitude.LO;
        this.pressurization = Pressurization.OFF;
        this.carryingBombs = true;
        this.underControl = true;
        this.course = Course.ON_COURSE;
        this.performingEvasiveAction = false;
        this.fuelLeft = DEFAULT_MAX_FUEL;
        this.auxAftFuelLeft = DEFAULT_AUX_MAX_FUEL;
        this.auxFwdFuelLeft = DEFAULT_AUX_MAX_FUEL;
    }

    public void createDefaultCrew(){
        for (CrewPosition crewPosition: CrewPosition.values()) {
            CrewMember crewMember = new CrewMember(crewPosition.getName(), crewPosition);
            crewMember.setBomberCompartment(getCompartmentWith(crewPosition));
            this.crew.add(crewMember);
        }
    }

    public CrewMember getCrewMemberByRole(CrewPosition role) {
        for (CrewMember crewMember : this.crew) {
            if (crewMember.getCrewPosition() == role) {
                return crewMember;
            }
        }
        return null;
    }

    public CrewMember getCrewMemberByDefaultRole(CrewPosition role) {
        for (CrewMember crewMember : this.crew) {
            if (crewMember.getDefaultCrewPosition() == role) {
                return crewMember;
            }
        }
        return null;
    }

    public BomberCompartment getCompartmentWith(CrewPosition crewPosition){
        switch (crewPosition){
            case PILOT:
            case COPILOT:
            case BOMBARDIER:
                return BomberCompartment.NOSE;
            case NAVIGATOR:
            case ENGINEER:
            case RADIO_OPERATOR:
                return BomberCompartment.NAV_RADIO;
            case CFC_CONTROLLER:
            case LEFT_GUNNER:
            case RIGHT_GUNNER:
            case RADAR_OPERATOR:
                return BomberCompartment.WAIST;
            case TAIL_GUNNER:
                return BomberCompartment.TAIL;
        }
        return null;
    }

    public int getMaxCrewMembersIn(BomberCompartment bomberCompartment){
        switch (bomberCompartment) {
            case NOSE:
                return 3;
            case NAV_RADIO:
                return 7;
            case WAIST:
                return 5;
            case UTILITY:
                return 3; // (Remember Utility is never pressurized)
            case TAIL:
                return 1;
        }
        return 0;
    }

    public int getMaxAmmo(GunPosition position) {
        switch (position) {
            case FWD_UPPER_TURRET:
                return 10;
            case FWD_LOWER_TURRET:
                return 15;
            case REAR_UPPER_TURRET:
                return 25;
            case REAR_LOWER_TURRET:
                return 20;
            case TAIL_TURRET:
                return 30;
            case TAIL_CANNON:
                return 10;
        }
        return 0;
    }

    public boolean hasDamage(Damage damage){
        return compartmentDamage.values().contains(damage);
    }

    public boolean hasDamage(BomberCompartment bomberAreaType, Damage damage){
        return compartmentDamage.get(bomberAreaType).contains(damage);
    }

    public int countDamage(BomberCompartment bomberCompartment, Damage damage){
        return (int) compartmentDamage.get(bomberCompartment).stream().filter(d -> d == damage).count();
    }

    public void addDamage(BomberCompartment bomberCompartment, Damage damage){
        compartmentDamage.get(bomberCompartment).add(damage);
    }

    public void setEngineOut(int engineNumber){
        compartmentDamage.get(engineNumber <= 2? BomberCompartment.PORT_WING: BomberCompartment.STBD_WING).add(Damage.getEngineOut(engineNumber));
    }

    public boolean hasEngineOut(){
        return hasDamage(Damage.ENGINE_1_OUT) ||
                hasDamage(Damage.ENGINE_2_OUT) ||
                hasDamage(Damage.ENGINE_3_OUT) ||
                hasDamage(Damage.ENGINE_4_OUT);
    }

    public int countEnginesOut(){
        int totalOut = 0;
        totalOut += hasDamage(Damage.ENGINE_1_OUT)? 1: 0;
        totalOut += hasDamage(Damage.ENGINE_2_OUT)? 1: 0;
        totalOut += hasDamage(Damage.ENGINE_3_OUT)? 1: 0;
        totalOut += hasDamage(Damage.ENGINE_4_OUT)? 1: 0;
        return totalOut;
    }

    /**
     * Use some fuel
     * @return true if fuel remains, false if all fuel is used
     */
    public boolean consumeFuel(int amount){
        if (auxFwdFuelLeft > 0 && amount > 0) {
            auxFwdFuelLeft -= 1;
            amount -= 1;
        }

        if (auxAftFuelLeft > 0 && amount > 0) {
            auxAftFuelLeft -= 1;
            amount -= 1;
        }

        fuelLeft -= amount;

        if (fuelLeft < 0)
            fuelLeft = 0;

        return fuelLeft > 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumMissionsCompleted() {
        return numMissionsCompleted;
    }

    public void setNumMissionsCompleted(int numMissionsCompleted) {
        this.numMissionsCompleted = numMissionsCompleted;
    }

    public BomberStatus getBomberStatus() {
        return bomberStatus;
    }

    public void setBomberStatus(BomberStatus bomberStatus) {
        this.bomberStatus = bomberStatus;
    }

    public List<CrewMember> getCrew() {
        return crew;
    }

    public void setCrew(List<CrewMember> crew) {
        this.crew = crew;
    }

    public Map<GunPosition, Gun> getGuns() {
        return guns;
    }

    public void setGuns(Map<GunPosition, Gun> guns) {
        this.guns = guns;
    }

    public Altitude getAltitude() {
        return altitude;
    }

    public void setAltitude(Altitude altitude) {
        this.altitude = altitude;
    }

    public Pressurization getPressurization() {
        return pressurization;
    }

    public void setPressurization(Pressurization pressurization) {
        this.pressurization = pressurization;
    }

    public boolean isCarryingBombs() {
        return carryingBombs;
    }

    public void setCarryingBombs(boolean carryingBombs) {
        this.carryingBombs = carryingBombs;
    }

    public boolean isUnderControl() {
        return underControl;
    }

    public void setUnderControl(boolean underControl) {
        this.underControl = underControl;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public boolean isOnCourse(){
        return course == Course.ON_COURSE;
    }

    public boolean isOffCourse(){
        return course == Course.OFF_COURSE;
    }

    public boolean isPerformingEvasiveAction() {
        return performingEvasiveAction;
    }

    public void setPerformingEvasiveAction(boolean performingEvasiveAction) {
        this.performingEvasiveAction = performingEvasiveAction;
    }

    public int getFuelLeft() {
        return fuelLeft;
    }

    public void setFuelLeft(int fuelLeft) {
        this.fuelLeft = fuelLeft;
    }

    public int getAuxAftFuelLeft() {
        return auxAftFuelLeft;
    }

    public void setAuxAftFuelLeft(int auxAftFuelLeft) {
        this.auxAftFuelLeft = auxAftFuelLeft;
    }

    public int getAuxFwdFuelLeft() {
        return auxFwdFuelLeft;
    }

    public void setAuxFwdFuelLeft(int auxFwdFuelLeft) {
        this.auxFwdFuelLeft = auxFwdFuelLeft;
    }

    public Map<CrewPosition, List<Damage>> getCrewPositionDamage() {
        return crewPositionDamage;
    }

    public Map<BomberCompartment, List<Damage>> getCompartmentDamage() {
        return compartmentDamage;
    }
}
