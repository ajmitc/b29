package b29.game;

import b29.game.mission.Target;

import java.util.HashMap;
import java.util.Map;

public class UrbanAreaDamage {
    private Map<Target, DamageInfo> targetInfoMap = new HashMap<>();

    public UrbanAreaDamage(){
        // TODO Fix/finish this
        targetInfoMap.put(Target.TOKYO, new DamageInfo(3));
    }

    public int getMaxDamage(Target target){
        if (targetInfoMap.containsKey(target)){
            return targetInfoMap.get(target).maxDamage;
        }
        return 0;
    }

    public int getDamage(Target target){
        if (targetInfoMap.containsKey(target)){
            return targetInfoMap.get(target).damage;
        }
        return 0;
    }

    public boolean isBombedOut(Target target){
        if (targetInfoMap.containsKey(target)){
            return targetInfoMap.get(target).damage >= targetInfoMap.get(target).maxDamage;
        }
        return false;
    }

    public void addDamage(Target target){
        if (targetInfoMap.containsKey(target)){
            targetInfoMap.get(target).damage += 1;
        }
    }

    private class DamageInfo {
        public int maxDamage;
        public int damage;

        public DamageInfo(int maxDamage){
            this.maxDamage = maxDamage;
            this.damage = 0;
        }
    }
}
