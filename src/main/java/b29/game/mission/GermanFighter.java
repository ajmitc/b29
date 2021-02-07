package b29.game.mission;

import b29.game.mission.chart.FighterInfo;

public class GermanFighter {
    private FighterInfo fighterInfo;
    private GermanFighterDamage damage;

    public GermanFighter(FighterInfo fighterInfo){
        this.fighterInfo = fighterInfo;
        damage = null;
    }

    public FighterInfo getFighterInfo() {
        return fighterInfo;
    }

    public GermanFighterDamage getDamage() {
        return damage;
    }

    public void setDamage(GermanFighterDamage damage) {
        this.damage = damage;
    }
}
