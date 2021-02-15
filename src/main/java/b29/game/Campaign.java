package b29.game;

import b29.game.mission.Mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Campaign {
    private List<Mission> missions = new ArrayList<>();
    private UrbanAreaDamage urbanAreaDamage = new UrbanAreaDamage();

    public Campaign(){

    }

    public int getNextMissionNumber(){
        Optional<Integer> maxMissionNumber = missions.stream().map(mission -> mission.getMissionNumber()).max(Integer::compareTo);
        return maxMissionNumber.isPresent()? maxMissionNumber.get() + 1: 1;
    }

    public List<Mission> getMissions() {
        return missions;
    }

    public UrbanAreaDamage getUrbanAreaDamage() {
        return urbanAreaDamage;
    }
}
