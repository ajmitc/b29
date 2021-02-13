package b29.game.mission.chart;

import b29.game.bomber.Bomber;
import b29.game.bomber.Damage;
import b29.game.mission.FormationAssembly;
import b29.game.mission.FormationPosition;
import b29.game.mission.Mission;
import b29.game.mission.Weather;
import b29.util.Util;

public class FormationAssemblyChart {
    public static FormationAssemblyResult getFormationAssembly(Mission mission, Bomber bomber){
        int die = Util.roll2d();

        if (die == 2 || die == 4){
            if (die == 2 && mission.getWeather() == Weather.GOOD) {
                return new FormationAssemblyResult(FormationAssembly.NORMAL, "Formation assembly uneventful");
            }

            if (Util.roll() <= 3) {
                // TODO Cross off 1 fuel
            }
            return new FormationAssemblyResult(FormationAssembly.DIFFICULT, "Difficulty assembling");
        }
        else if (die == 3){
            // Formation Drop-outs
            if (Util.roll() <= 3){
                // You are now the lead bomber
                mission.setFormationPosition(FormationPosition.LEAD);
                return new FormationAssemblyResult(FormationAssembly.NORMAL, "Some bombers drop out.  You are now the lead bomber");
            }
            else {
                // You are now the tail bomber
                mission.setFormationPosition(FormationPosition.TAIL);
                return new FormationAssemblyResult(FormationAssembly.NORMAL, "Some bombers drop out.  You are now the tail bomber");
            }
        }
        else if (die == 5 || die == 6 || die == 8 || die == 9 || (die == 10 && mission.getMissionNumber() <= 10)){
            return new FormationAssemblyResult(FormationAssembly.NORMAL, "Formation assembly uneventful");
        }
        else if (die == 7){
            if (mission.getWeather() == Weather.GOOD){
                return new FormationAssemblyResult(FormationAssembly.TIGHT, "Assembly goes well and tight formation is built");
            }
        }
        else if (die == 10){
            return new FormationAssemblyResult(FormationAssembly.DELAYED, "Delay in assembling the formation");
            // TODO Cross off 1 fuel
        }
        else if (die == 11){
            if (mission.getWeather() == Weather.BAD) {
                if (Util.roll() <= 3) {
                    // TODO Cross off 1 fuel
                }
                return new FormationAssemblyResult(FormationAssembly.DIFFICULT, "Difficulty assembling");
            }
        }
        else if (die == 12){
            // Possible mid-air collision
            die = Util.roll2d();
            if (mission.getWeather() == Weather.BAD)
                die += 2;
            else if (mission.getWeather() == Weather.POOR)
                die += 1;
            if (bomber.hasDamage(Damage.FROSTED_WINDOWS))
                die += 1;
            if (die >= 12){
                // Collision!
                return new FormationAssemblyResult(FormationAssembly.MID_AIR_COLLISION, "Mid-air collision!");
            }
        }

        return new FormationAssemblyResult(FormationAssembly.NORMAL, "Formation assembly uneventful");
    }

    public static class FormationAssemblyResult{
        public FormationAssembly formationAssembly;
        public String message;

        public FormationAssemblyResult(FormationAssembly formationAssembly, String message){
            this.formationAssembly = formationAssembly;
            this.message = message;
        }
    }
    private FormationAssemblyChart(){}
}
