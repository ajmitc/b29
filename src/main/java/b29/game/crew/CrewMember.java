package b29.game.crew;

import b29.game.Experience;
import b29.game.bomber.BomberCompartment;
import b29.game.bomber.CrewPosition;

import java.util.ArrayList;
import java.util.List;

public class CrewMember {
    private String name;
    // Currently assigned role (Pilot, Navigator, etc)
    private CrewPosition crewPosition;
    // Role this crew member was trained to do (Pilot, Navigator, etc)
    private CrewPosition defaultCrewPosition;
    // Current Location of this Crew Member
    private BomberCompartment bomberCompartment;
    private int kills;
    private CrewStatus status;
    private Experience experience;
    private List<CrewAilment> ailments = new ArrayList<>();
    private int numMissionsFlown;

    public CrewMember(String name, CrewPosition defaultCrewPosition) {
        this.name = name;
        this.crewPosition = defaultCrewPosition;
        this.defaultCrewPosition = defaultCrewPosition;
        this.kills = 0;
        this.status = CrewStatus.OK;
        this.experience = Experience.GREEN;
        this.numMissionsFlown = 0;
        this.bomberCompartment = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CrewPosition getCrewPosition() {
        return crewPosition;
    }

    public void setCrewPosition(CrewPosition crewPosition) {
        this.crewPosition = crewPosition;
    }

    public CrewPosition getDefaultCrewPosition() {
        return defaultCrewPosition;
    }

    public void setDefaultCrewPosition(CrewPosition defaultCrewPosition) {
        this.defaultCrewPosition = defaultCrewPosition;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public CrewStatus getStatus() {
        return status;
    }

    public void setStatus(CrewStatus status) {
        this.status = status;
    }

    public boolean isSwOrKia() {
        return status == CrewStatus.SERIOUS_WOUND || status == CrewStatus.KIA;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public List<CrewAilment> getAilments() {
        return ailments;
    }

    public int getNumMissionsFlown() {
        return numMissionsFlown;
    }

    public void setNumMissionsFlown(int numMissionsFlown) {
        this.numMissionsFlown = numMissionsFlown;
    }

    public BomberCompartment getBomberCompartment() {
        return bomberCompartment;
    }

    public void setBomberCompartment(BomberCompartment bomberCompartment) {
        this.bomberCompartment = bomberCompartment;
    }
}
