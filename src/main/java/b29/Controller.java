package b29;

import b29.game.Phase;
import b29.game.PhaseStep;
import b29.game.bomber.*;
import b29.game.mission.*;
import b29.game.mission.chart.*;
import b29.util.Util;
import b29.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller {
    private Model model;
    private View view;

    private int altitudeChange = 0; // 0 - stayed the same, -1: descended, +1 ascended

    public Controller( Model model, View view ) {
        this.model = model;
        this.view = view;

        this.view.getMainMenuPanel().getBtnExit().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        this.view.getMainMenuPanel().getBtnNewCampaign().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        this.view.getMainMenuPanel().getBtnNewMission().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    protected void run(){
        while (model.getGame() != null && model.getGame().getPhase() != Phase.GAMEOVER){
            switch (model.getGame().getPhase()){
                case SETUP_MISSION:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            // Pre-Mission Steps
                            // 1.1.Roll target city (G-1 , G-2 or G-3)
                            // 1.2.Roll B-17 formation position; note attacking fighter modifier (G-4)
                            // 1.3.Roll flight log gazetteer; note attacking fighter modifier (G-11)
                            // 1.4.Combine and record modifications to attackers on Mission Chart
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.PLAY_MISSION);
                            break;
                    }
                }
                case PLAY_MISSION:{
                    Mission mission = model.getGame().getMission();
                    Bomber bomber = model.getGame().getBomber();

                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_TAKEOFF_MOVE_NEXT_ZONE_LANDING);
                            break;
                        case PLAY_MISSION_TAKEOFF_MOVE_NEXT_ZONE_LANDING:
                            // Handle take-off
                            if (mission.getZone() == 0 && mission.getDirection() == Direction.TO_TARGET){
                                TakeoffResult takeoffResult =
                                        TakeOffLandingChart.getTakeoffResult(
                                                mission.getMissionTimeOfDay(),
                                                model.getGame().getBomber().getCrewMemberByDefaultRole(CrewPosition.PILOT).getExperience());
                                switch (takeoffResult){
                                    case MALFUNCTION:
                                        handleTakeoffMalfunction();
                                        break;
                                    case ACCIDENT:
                                        handleTakeoffAccident(false);
                                        break;
                                    case MID_AIR_COLLISION:
                                        // TODO B-29 destroyed, crew KIA
                                        break;
                                    case CLOSE_CALL_NO_EFFECT:
                                        break;
                                    case TAKEOFF_OK:
                                        break;
                                }
                                if (model.getGame().getBomber().getBomberStatus() == BomberStatus.WRECKED){
                                    model.getGame().setPhase(Phase.GAMEOVER);
                                    break;
                                }
                                // Set altitude to LO
                                model.getGame().getBomber().setAltitude(Altitude.LO);
                                mission.setZone(1);
                            }
                            // Handle Landing
                            else if (mission.getZone() == 1 && mission.getDirection() == Direction.RETURN_HOME){
                                // TODO Implement this
                                mission.setZone(0);
                            }
                            else {
                                boolean mustMove = mission.getTurnsInCurrentZone() >= 2;
                                if (!mustMove) {
                                    // TODO Ask player if they want to move to next zone
                                }

                                int currentZone = model.getGame().getMission().getZone();
                                if (model.getGame().getMission().getDirection() == Direction.TO_TARGET) {
                                    model.getGame().getMission().setZone(currentZone + 1);
                                } else {
                                    model.getGame().getMission().setZone(currentZone - 1);
                                }

                                // 2.2. Roll for fighter cover in Zones 2, 3 & 4 (G-5)
                                //   This step is unnecessary in Zone 1, see next footnote. Fighter cover does not extend past Zone 4.
                                /*
                                if (mission.getZone() >= 2 && mission.getZone() <= 4) {
                                    if (mission.getDirection() == Direction.TO_TARGET)
                                        mission.getFighterCoverageOut().put(mission.getZone(), FighterCoverageTable.getFighterCoverage(model.getGame().getMission().getMissionNumber()));
                                    else
                                        mission.getFighterCoverageBack().put(mission.getZone(), FighterCoverageTable.getFighterCoverage(model.getGame().getMission().getMissionNumber()));
                                }
                                // 2.3. Roll for number of attacking fighter waves (B-1 or B-2)
                                //   In Zone 1, no attacking waves form.
                                if (mission.getZone() > 1) {
                                    int waves = NumberGermanFighterWavesTable.get(false);
                                    mission.setNumGermanFighterWaves(waves);
                                }
                                 */
                                // Change altitude, if necessary
                                if (mission.getZone() < 3 && bomber.getAltitude() != mission.getMissionAltitude()){
                                    if (mission.getMissionAltitude().ordinal() > bomber.getAltitude().ordinal()){
                                        // Ascend
                                        bomber.setAltitude(Altitude.values()[bomber.getAltitude().ordinal() + 1]);
                                        altitudeChange = 1;
                                    }
                                    else {
                                        // Descend
                                        bomber.setAltitude(Altitude.values()[bomber.getAltitude().ordinal() - 1]);
                                        altitudeChange = -1;
                                    }
                                }
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_PRESSURIZATION);
                            break;
                        case PLAY_MISSION_PRESSURIZATION:
                            if (bomber.getPressurization() != Pressurization.INOP) {
                                // If altitude changed from LO <-> MED, auto de/pressurize
                                if (altitudeChange > 0 && bomber.getAltitude() == Altitude.MED) {
                                    bomber.setPressurization(Pressurization.ON);
                                } else if (altitudeChange < 0 && bomber.getAltitude() == Altitude.LO) {
                                    bomber.setPressurization(Pressurization.OFF);
                                }
                            }
                            // TODO Implement the rest of this
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_CONSUME_FUEL);
                            break;
                        case PLAY_MISSION_CONSUME_FUEL:
                            int fuelConsumed = getFuelConsumed(true);
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_WEATHER);
                            break;
                        case PLAY_MISSION_WEATHER:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_NAVIGATION);
                            break;
                        case PLAY_MISSION_NAVIGATION:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_RANDOM_EVENT);
                            break;
                        case PLAY_MISSION_RANDOM_EVENT:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_ABORT_MISSION);
                            break;
                        case PLAY_MISSION_ABORT_MISSION:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FORMATION_ASSEMBLY);
                            break;
                        case PLAY_MISSION_FORMATION_ASSEMBLY:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_ESCORT_RENDEZVOUS);
                            break;
                        case PLAY_MISSION_FIGHTER_ESCORT_RENDEZVOUS:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_RESISTANCE);
                            break;
                        case PLAY_MISSION_FIGHTER_RESISTANCE:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_ATTACK_B29_CHOOSE_DEFENSIVE_FIRE);
                            break;
                        case PLAY_MISSION_FIGHTER_ATTACK_B29_CHOOSE_DEFENSIVE_FIRE:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_ATTACK_B29_DEFENSIVE_FIRE);
                            break;
                        case PLAY_MISSION_FIGHTER_ATTACK_B29_DEFENSIVE_FIRE:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_ATTACK_OFFENSIVE_FIRE);
                            break;
                        case PLAY_MISSION_FIGHTER_ATTACK_OFFENSIVE_FIRE:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_ATTACK_B29_DAMAGE);
                            break;
                        case PLAY_MISSION_FIGHTER_ATTACK_B29_DAMAGE:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_BOMB_RUN_FLAK);
                            break;
                        case PLAY_MISSION_BOMB_RUN_FLAK:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_BOMB_RUN);
                            break;
                        case PLAY_MISSION_BOMB_RUN:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_TAKEOFF_MOVE_NEXT_ZONE_LANDING);
                            break;
                        case PLAY_MISSION_LANDING:
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.TEARDOWN_MISSION);
                            break;
                    }
                }
                case TEARDOWN_MISSION:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            model.getGame().setPhase(Phase.SETUP_MISSION);
                            break;
                    }
                }
            }
        }
    }

    private void handleTakeoffMalfunction(){
        TakeoffMalfunction malfunction = TakeOffLandingChart.getTakeoffMalfunction();
        switch (malfunction){
            case ENGINE_FIRE_DURING_RUN_UP:
                handleEngineFireDuringRunUp();
                break;
            case UNUSUAL_POWER_CONDITIONS:
                handleUnusualPowerConditionsDuringTakeoff();
                break;
            case ENGINE_FAILURE:
                handleEngineFailureOnTakeoff();
                break;
            case RUNAWAY_PROPELLER_AFTER_TAKEOFF:
                handleRunawayPropellerAfterTakeoff();
                break;
            case ENGINE_FIRE_AFTER_TAKEOFF:
                handleEngineFireAfterTakeoff();
                break;
            case FALSE_ALARM:
                break;
        }
    }

    private void handleEngineFireDuringRunUp(){
        int engineNumber = Util.getRandomEngine();

        // Each engine has 2 fire extinguishers

        int die = Util.roll();
        if (die == 1){
            // TODO Fire out, plane does not fly, no credit to Mission Tour, B-29 repairable by next mission
        }
        else{
            // Fire continues, try again
            die = Util.roll();
            if (die == 1){
                // TODO Fire out, plane does not fly, no credit to Mission Tour, B-29 repairable by next mission
            }
            else {
                // Crew must abandon aircraft and send for additional ground fire-fighting equipment
                die = Util.roll();
                if (die <= 4){
                    // TODO B-29 repairable by next mission
                }
                else {
                    // TODO B-29 irrepairably damaged
                }
            }
        }

        model.getGame().setPhase(Phase.GAMEOVER);
    }

    private void handleUnusualPowerConditionsDuringTakeoff(){
        int die = Util.roll();
        if (die <= 3){
            // TODO Pilot aborts take-off
            die = Util.roll();
            if (die <= 5){
                // Plane stopped successfully, plane does not fly, no credit to Mission Tour
            }
            else {
                // TODO Brakes fail, roll on Table 3-5 (Accident)
                handleTakeoffAccident(false);
            }
        }
        else {
            // Pilot continues take-off
            die = Util.roll();
            if (die <= 4){
                // Mission continues normally
            }
            else {
                // TODO Not enough lift, roll on Table 3-5
                handleTakeoffAccident(true);
            }
        }
    }

    private void handleEngineFailureOnTakeoff(){
        int engineNumber = Util.getRandomEngine();
        // TODO Set engine out
        model.getGame().getMission().setAborted(true);
        int die = Util.roll();
        if (die <= 2){
            // TODO Roll for Landing on Land (Table 8-1) - one engine out
        }
        else if (die <= 4){
            // TODO Roll for Landing in Water (Table 8-3) - one engine out
        }
        else {
            // Not enough lift
            handleTakeoffAccident(true);
        }
    }

    private void handleRunawayPropellerAfterTakeoff(){
        int engineNumber = Util.getRandomEngine();
        // TODO Set engine out
        int die = Util.roll();
        if (die <= 4){
            // TODO Propeller feathered, roll for "Landing on Land" (Table 8-1) - one engine out
        }
        else if (die == 5){
            // TODO Propeller control restored, mission aborted, roll for "Landing on Land" (Table 8-1)
            model.getGame().getMission().setAborted(true);
        }
        else {
            // TODO Feathering fails, windmilling prop, roll for "Landing on Land" (Table 8-1) with -2 modifier
        }
    }

    private void handleEngineFireAfterTakeoff(){
        int engineNumber = Util.getRandomEngine();
        // TODO Set engine out
        // Roll for extinguishing fire
        int die = Util.roll();
        if (die == 1) {
            // TODO fire is extinguished, mission aborted, roll for "Landing on Land" (Table 8-1) - one engine out
            model.getGame().getMission().setAborted(true);
        }
        else {
            die = Util.roll();
            if (die == 1){
                // TODO fire is extinguished, mission aborted, roll for "Landing on Land" (Table 8-1) - one engine out
                model.getGame().getMission().setAborted(true);
            }
            else {
                // TODO Attempt immediate crash landing
                die = Util.roll();
                if (die <= 3){
                    // TODO Roll for "Landing on Land" (Table 8-1) - one engine out
                }
                else if (die <= 5){
                    // TODO Roll for "Landing in Water" (Table 8-3) - one engine out
                }
                else {
                    // TODO Explosion, plane destroyed, crew KIA
                }
            }
        }
    }

    private void handleTakeoffAccident(boolean notEnoughLift){
        TakeoffAccident accident = TakeOffLandingChart.getTakeoffAccident(notEnoughLift);
        switch (accident){
            case PLANE_CRASHES_AND_EXPLODES_ALL_KIA:
                break;
            case PLANE_CRASHES_ROLL_FOR_CREW:
                break;
            case CREW_SAFE_B29_IRREPAIRABLY_DAMAGED:
                break;
            case CREW_SAFE_B29_REPAIRABLE:
                break;
        }
    }

    private int getFuelConsumed(boolean enteringNewZone){
        int fuelConsumed = 0;
        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();
        if (mission.getZone() == 1 && mission.getDirection() == Direction.TO_TARGET){
            fuelConsumed += 1;
        }
        if (enteringNewZone) {
            if (bomber.isCarryingBombs())
                fuelConsumed += 2;
            else
                fuelConsumed += 1;
        }
        else
            fuelConsumed += 1;
        if (altitudeChange > 0)
            fuelConsumed += (altitudeChange * 2);
        return fuelConsumed;
    }
}
