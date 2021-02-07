package b29.game.crew;

import b29.game.Experience;
import b29.game.bomber.CrewPosition;

public class CrewMember {
    private String name;
    private CrewPosition defaultRole;
    private int kills;
    private CrewStatus status;
    private Experience experience;

    public CrewMember( String name, CrewPosition defaultRole ) {
        this.name = name;
        this.defaultRole = defaultRole;
        this.kills = 0;
        this.status = CrewStatus.OK;
        this.experience = Experience.GREEN;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public CrewPosition getDefaultRole() {
        return defaultRole;
    }

    public void setDefaultRole( CrewPosition defaultRole ) {
        this.defaultRole = defaultRole;
    }

    public int getKills() {
        return kills;
    }

    public void setKills( int kills ) {
        this.kills = kills;
    }

    public CrewStatus getStatus() {
        return status;
    }

    public void setStatus( CrewStatus status ) {
        this.status = status;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }
}
