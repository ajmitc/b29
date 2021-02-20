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
    private Experience experience;
    private List<CrewAilment> ailments = new ArrayList<>();
    private List<CrewStatus> wounds = new ArrayList<>();
    private int numMissionsFlown;

    public CrewMember(String name, CrewPosition defaultCrewPosition) {
        this.name = name;
        this.crewPosition = defaultCrewPosition;
        this.defaultCrewPosition = defaultCrewPosition;
        this.kills = 0;
        this.experience = Experience.GREEN;
        this.numMissionsFlown = 0;
        this.bomberCompartment = null;
    }

    /**
     * Return the crew member's overall status, based on wounds. Consolidate wounds as necessary.
     * @return
     */
    public CrewStatus getStatus() {
        if (hasWound(CrewStatus.KIA))
            return CrewStatus.KIA;
        int lw = countWounds(CrewStatus.LIGHT_WOUND);
        // 4 Light Wounds == KIA
        if (lw >= 4){
            wounds.add(CrewStatus.KIA);
            return CrewStatus.KIA;
        }
        // 3 Light Wounds == 1 Serious Wound
        if (lw == 3){
            wounds.remove(CrewStatus.LIGHT_WOUND);
            wounds.remove(CrewStatus.LIGHT_WOUND);
            wounds.remove(CrewStatus.LIGHT_WOUND);
            wounds.add(CrewStatus.SERIOUS_WOUND);
            lw = countWounds(CrewStatus.LIGHT_WOUND);
        }
        // 1 Light Wound + 1 Serious Wound == KIA
        // 1+ Serious Wounds == KIA
        int sw = countWounds(CrewStatus.SERIOUS_WOUND);
        if ((lw > 0 && sw > 0) || sw > 1){
            wounds.add(CrewStatus.KIA);
            return CrewStatus.KIA;
        }
        if (sw > 0)
            return CrewStatus.SERIOUS_WOUND;
        if (lw > 0)
            return CrewStatus.LIGHT_WOUND;
        return CrewStatus.OK;
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

    public boolean isSwOrKia() {
        CrewStatus status = getStatus();
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

    public List<CrewStatus> getWounds() {
        return wounds;
    }

    public CrewStatus addWound(CrewStatus wound){
        wounds.add(wound);
        return getStatus();
    }

    public int countWounds(CrewStatus wound){
        return (int) wounds.stream().filter(w -> w == wound).count();
    }

    public boolean hasWound(CrewStatus wound){
        return wounds.contains(wound);
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
