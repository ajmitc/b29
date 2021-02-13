package b29.game.mission;

import b29.game.Experience;
import b29.game.mission.chart.FighterInfo;

public class JapaneseFighter {
    private FighterInfo fighterInfo;
    private Experience experience;
    private JapaneseFighterDamage damage;
    // See Chart pg 15, Table 5-3 Note c for details
    private boolean ram;

    public JapaneseFighter(FighterInfo fighterInfo){
        this.fighterInfo = fighterInfo;
        experience = Experience.AVERAGE;
        damage = null;
        ram = false;
    }

    public FighterInfo getFighterInfo() {
        return fighterInfo;
    }

    public Experience getExperience() {
        return experience;
    }

    public void setExperience(Experience experience) {
        this.experience = experience;
    }

    public JapaneseFighterDamage getDamage() {
        return damage;
    }

    public void setDamage(JapaneseFighterDamage damage) {
        this.damage = damage;
    }

    public boolean isRam() {
        return ram;
    }

    public void setRam(boolean ram) {
        this.ram = ram;
    }
}
