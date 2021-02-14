package b29.game.bomber;

import b29.game.crew.CrewMember;
import b29.game.mission.Course;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bomber {
    private static final int DEFAULT_MAX_FUEL = 38;
    private static final int DEFAULT_AUX_MAX_FUEL = 4;

    private String name;
    private int numMissionsCompleted;
    private BomberStatus bomberStatus;
    private List<CrewMember> crew = new ArrayList<>();
    private Map<BomberAreaType, BomberArea> areas = new HashMap<>();
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

    public Bomber() {
        this.name = "";
        this.numMissionsCompleted = 0;
        this.bomberStatus = BomberStatus.SAFE;

        areas.put(BomberAreaType.NOSE, new BomberArea(BomberAreaType.NOSE));
        areas.put(BomberAreaType.NAV_RADIO, new BomberArea(BomberAreaType.NAV_RADIO));
        areas.put(BomberAreaType.FWD_BOMB_BAY, new BomberArea(BomberAreaType.FWD_BOMB_BAY));
        areas.put(BomberAreaType.REAR_BOMB_BAY, new BomberArea(BomberAreaType.REAR_BOMB_BAY));
        areas.put(BomberAreaType.WAIST, new BomberArea(BomberAreaType.WAIST));
        areas.put(BomberAreaType.UTILITY, new BomberArea(BomberAreaType.UTILITY));
        areas.put(BomberAreaType.TAIL, new BomberArea(BomberAreaType.TAIL));
        areas.put(BomberAreaType.STBD_WING, new BomberArea(BomberAreaType.STBD_WING));
        areas.put(BomberAreaType.PORT_WING, new BomberArea(BomberAreaType.PORT_WING));

        for (GunPosition position : GunPosition.values()) {
            Gun gun = new Gun(position, getMaxAmmo(position));
            guns.put(position, gun);
        }

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
            this.crew.add(new CrewMember(crewPosition.getName(), crewPosition));
        }
    }

    public void assignCrewToPositions() {
        areas.get(BomberAreaType.NOSE).getCrewAssignments().put(CrewPosition.BOMBARDIER, getCrewMemberByDefaultRole(CrewPosition.BOMBARDIER));
        areas.get(BomberAreaType.NOSE).getCrewAssignments().put(CrewPosition.PILOT, getCrewMemberByDefaultRole(CrewPosition.PILOT));
        areas.get(BomberAreaType.NOSE).getCrewAssignments().put(CrewPosition.COPILOT, getCrewMemberByDefaultRole(CrewPosition.COPILOT));
        areas.get(BomberAreaType.NAV_RADIO).getCrewAssignments().put(CrewPosition.NAVIGATOR, getCrewMemberByDefaultRole(CrewPosition.NAVIGATOR));
        areas.get(BomberAreaType.NAV_RADIO).getCrewAssignments().put(CrewPosition.ENGINEER, getCrewMemberByDefaultRole(CrewPosition.ENGINEER));
        areas.get(BomberAreaType.NAV_RADIO).getCrewAssignments().put(CrewPosition.RADIO_OPERATOR, getCrewMemberByDefaultRole(CrewPosition.RADIO_OPERATOR));
        areas.get(BomberAreaType.WAIST).getCrewAssignments().put(CrewPosition.CFC_CONTROLLER, getCrewMemberByDefaultRole(CrewPosition.CFC_CONTROLLER));
        areas.get(BomberAreaType.WAIST).getCrewAssignments().put(CrewPosition.LEFT_GUNNER, getCrewMemberByDefaultRole(CrewPosition.LEFT_GUNNER));
        areas.get(BomberAreaType.WAIST).getCrewAssignments().put(CrewPosition.RIGHT_GUNNER, getCrewMemberByDefaultRole(CrewPosition.RIGHT_GUNNER));
        areas.get(BomberAreaType.WAIST).getCrewAssignments().put(CrewPosition.RADAR_OPERATOR, getCrewMemberByDefaultRole(CrewPosition.RADAR_OPERATOR));
        areas.get(BomberAreaType.TAIL).getCrewAssignments().put(CrewPosition.TAIL_GUNNER, getCrewMemberByDefaultRole(CrewPosition.TAIL_GUNNER));
    }

    public CrewMember getCrewMemberByDefaultRole(CrewPosition role) {
        for (CrewMember crewMember : this.crew) {
            if (crewMember.getDefaultRole() == role) {
                return crewMember;
            }
        }
        return null;
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
        for (BomberAreaType bomberAreaType: areas.keySet()){
            if (areas.get(bomberAreaType).getDamage().contains(damage))
                return true;
        }
        return false;
    }

    public boolean hasDamage(BomberAreaType bomberAreaType, Damage damage){
        return areas.get(bomberAreaType).getDamage().contains(damage);
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

        if (fuelLeft > 0 && amount > 0){
            fuelLeft -= amount;
        }

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

    public Map<BomberAreaType, BomberArea> getAreas() {
        return areas;
    }

    public void setAreas(Map<BomberAreaType, BomberArea> areas) {
        this.areas = areas;
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
}
