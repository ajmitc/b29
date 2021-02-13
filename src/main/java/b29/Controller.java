package b29;

import b29.game.Experience;
import b29.game.Phase;
import b29.game.PhaseStep;
import b29.game.bomber.*;
import b29.game.crew.CrewAilment;
import b29.game.crew.CrewMember;
import b29.game.crew.CrewStatus;
import b29.game.mission.*;
import b29.game.mission.chart.*;
import b29.util.Util;
import b29.view.View;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
                            if (model.getGame().getMission() == null){
                                model.getGame().setMission(new Mission(1));
                            }
                            if (model.getGame().getBomber() == null){
                                model.getGame().setBomber(new Bomber());
                            }
                            Mission mission = model.getGame().getMission();

                            // Get Mission Time Of Day
                            mission.setMissionTimeOfDay(MissionSetupTable.getMissionTimeOfDay(mission.getMissionNumber()));

                            // Set Target
                            MissionSetupTable.TargetInfo targetInfo = MissionSetupTable.getMissionTarget(mission.getMissionNumber(), mission.getMissionTimeOfDay());
                            mission.setTarget(targetInfo.target);
                            mission.setTargetType(targetInfo.targetType);

                            // Set Altitude
                            mission.setMissionAltitude(MissionSetupTable.getMissionAltitude(mission.getMissionNumber(), mission.getMissionTimeOfDay(), mission.getTargetType()));

                            // Set Formation Position
                            mission.setFormationPosition(MissionSetupTable.getFormationPosition(model.getGame().getBomber()));

                            // Set Squadron Position
                            mission.setSquadronPosition(MissionSetupTable.getSquadronPosition());

                            // Set expected level of resistance
                            mission.setExpectedFighterDensity(MissionSetupTable.getExpectedJapaneseFighterResistance(mission));

                            // TODO Night Mission Gunner Allocation

                            // Set Escort Availability
                            mission.setEscortAvailable(MissionSetupTable.isFighterEscortAvailable(mission));

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
                                        mission.getFighterCoverageOut().put(mission.getZone(), FighterEscortChart.getFighterCoverage(model.getGame().getMission().getMissionNumber()));
                                    else
                                        mission.getFighterCoverageBack().put(mission.getZone(), FighterEscortChart.getFighterCoverage(model.getGame().getMission().getMissionNumber()));
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
                            // TODO If mission.isStormSystemEncountered(), weather is auto-BAD
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_NAVIGATION);
                            break;
                        case PLAY_MISSION_NAVIGATION:
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_RANDOM_EVENT);
                            break;
                        case PLAY_MISSION_RANDOM_EVENT:
                            handleRandomEvent();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_ABORT_MISSION);
                            break;
                        case PLAY_MISSION_ABORT_MISSION:
                            // TODO Ask user if they want to abort mission
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FORMATION_ASSEMBLY);
                            break;
                        case PLAY_MISSION_FORMATION_ASSEMBLY:
                            handleFormationAssembly();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_ESCORT_RENDEZVOUS);
                            break;
                        case PLAY_MISSION_FIGHTER_ESCORT_RENDEZVOUS:
                            handleFighterEscortRendezvous();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_FIGHTER_RESISTANCE);
                            break;
                        case PLAY_MISSION_FIGHTER_RESISTANCE:
                            mission.setJapaneseFighters(new ArrayList<>());
                            handleJapaneseFighterResistance();
                            if (mission.getJapaneseFighters().isEmpty())
                                model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_BOMB_RUN_FLAK);
                            else
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
                            handleOverTarget();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_TAKEOFF_MOVE_NEXT_ZONE_LANDING);
                            break;
                        case PLAY_MISSION_LANDING:
                            handleLanding();
                            model.getGame().setPhaseStep(PhaseStep.END_PHASE);
                            break;
                        case END_PHASE:
                            mission.setAbleToPerformEvasiveAction(true);
                            model.getGame().setPhase(Phase.TEARDOWN_MISSION);
                            break;
                    }
                }
                case TEARDOWN_MISSION:{
                    switch (model.getGame().getPhaseStep()){
                        case START_PHASE:
                            handleVictoryConditions();
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

    private void handleRandomEvent(){
        if (Util.roll2d() == 12){
            RandomEvent randomEvent = RandomEventChart.getRandomEvent();
            switch (randomEvent){
                case FOOD_POISONING:
                    handleFoodPoisoning();
                    break;
                case CFC_SYSTEM_FAILS:
                    handleCFCSystemFails();
                    break;
                case EXCEPTIONAL_FUEL_MANAGEMENT_BY_ENGINEER:
                    handleExceptionalFuelManagementByEngineer();
                    break;
                case STORM_SYSTEM_ENCOUNTERED:
                    handleStormSystemEncounter();
                    break;
                case PATHFINDER:
                    handlePathFinder();
                    break;
                case SLOPPY_FUEL_MANAGEMENT_BY_ENGINEER:
                    handleSloppyFuelManagementByEngineer();
                    break;
                case PICKLE_BARREL:
                    handlePickleBarrel();
                    break;
                case HIGH_PRESSURE_SYSTEM_ENCOUNTERED:
                    handleHighPressureSystemEncounter();
                    break;
                case RADAR_SYSTEM_FAILS:
                    handleRadarSystemFailure();
                    break;
                case SEVERE_ICING:
                    handleSevereIcing();
                    break;
                case SHARPSHOOTER:
                    handleSharpshooter();
                    break;
            }
        }
    }

    private void handleFoodPoisoning(){
        Bomber bomber = model.getGame().getBomber();
        for (CrewMember crewMember: bomber.getCrew()){
            int die = Util.roll();
            if (die <= 2)
                continue;
            else if (die <= 4){
                // Mild Food Poisoning
                crewMember.setStatus(CrewStatus.LIGHT_WOUND);
                crewMember.getAilments().add(CrewAilment.FOOD_POISONING);
            }
            else {
                // Severe Food Poisoning
                crewMember.setStatus(CrewStatus.SERIOUS_WOUND);
                crewMember.getAilments().add(CrewAilment.FOOD_POISONING);
                // Out for remainder of mission, but recovers for next mission
            }
        }
    }

    private void handleCFCSystemFails(){
        Bomber bomber = model.getGame().getBomber();
        bomber.getAreas().get(BomberAreaType.WAIST).getDamage().add(Damage.CFC_SYSTEM_FAILURE);
        bomber.getGuns().get(GunPosition.FWD_UPPER_TURRET).setOperational(false);
        bomber.getGuns().get(GunPosition.FWD_LOWER_TURRET).setOperational(false);
        bomber.getGuns().get(GunPosition.REAR_UPPER_TURRET).setOperational(false);
        bomber.getGuns().get(GunPosition.REAR_LOWER_TURRET).setOperational(false);
    }

    private void handleExceptionalFuelManagementByEngineer(){
        Bomber bomber = model.getGame().getBomber();
        if (bomber.getCrewMemberByDefaultRole(CrewPosition.ENGINEER).isSwOrKia()){
            return;
        }
        // TODO Implement this
        // +2 fuel
    }

    private void handleStormSystemEncounter(){
        // Bad weather in each Zone for remainder of mission
        // Roll on Bad Weather table 4 each zone (do not roll on table 3)
        model.getGame().getMission().setStormSystemEncountered(true);
        model.getGame().getMission().setHighPressureSystemEncountered(false);
    }

    private void handlePathFinder(){
        // Navigator is plotting particularly well this mission. +1 modifier on Navigation
        // Table 5-A (p.4) of this book for the remainder of the mission. If Navigator is Seriously
        // Wounded or KIA, result is ignored.
        Bomber bomber = model.getGame().getBomber();
        if (bomber.getCrewMemberByDefaultRole(CrewPosition.NAVIGATOR).isSwOrKia()){
            return;
        }
        model.getGame().getMission().setPathfinder(true);
    }

    private void handleSloppyFuelManagementByEngineer(){
        Bomber bomber = model.getGame().getBomber();
        if (bomber.getCrewMemberByDefaultRole(CrewPosition.ENGINEER).isSwOrKia()){
            // TODO -4 fuel
            return;
        }
        // TODO Implement this
        // -2 fuel
    }

    private void handlePickleBarrel(){
        // Bombardier is aiming particularly well this mission. +1 modifier on Bombing Table 6-6
        Bomber bomber = model.getGame().getBomber();
        if (bomber.getCrewMemberByDefaultRole(CrewPosition.BOMBARDIER).isSwOrKia()){
            return;
        }
        model.getGame().getMission().setPickleBarrel(true);
    }

    private void handleHighPressureSystemEncounter(){
        // Good weather in each zone for the remainder of the mission (no headwind or tailwind). Do not roll on Table 3 (p.2) of this book.
        model.getGame().getMission().setHighPressureSystemEncountered(true);
        model.getGame().getMission().setStormSystemEncountered(false);
    }

    private void handleRadarSystemFailure(){
        // Radar is inoperable for the remainder of the mission. +1 modifier on Navigation Table 5-A (p.4) of this book.
        model.getGame().getBomber().getAreas().get(BomberAreaType.WAIST).getDamage().add(Damage.RADAR_FAILURE);
    }

    private void handleSevereIcing(){
        // B-29 encounters severe icing (only if at High altitude and weather in zone is Poor or Bad).
        // Bomber must immediately jettison bombs and descend to Low altitude for 1 turn in order to
        // melt the ice. Bomber is considered out of formation and evasive action is not possible during
        // this turn. Bomber may attempt to rejoin formation after the ice melts by burning 2 fuel
        // boxes in the next two zones until reaching High altitude again. If bomber is at Medium or
        // Low altitude, or weather in zone is Good, no icing occurs and result is ignored.
        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();
        if (bomber.getAltitude() == Altitude.HI && mission.getWeather() != Weather.GOOD){
            // TODO Tell user bad news
            bomber.setCarryingBombs(false);
            altitudeChange = Altitude.LO.ordinal() - bomber.getAltitude().ordinal();
            bomber.setAltitude(Altitude.LO);
            mission.setOutOfFormation(true);
            mission.setAbleToPerformEvasiveAction(false);
        }
    }

    private void handleSharpshooter(){
        // A gunner is aiming particularly well this mission. +1 modifier on Table
        // (treat as Ace gunner) for the remainder of the mission. Roll 1D to determine gunner:
        // 1 = Bombardier. 2-3 = CFC gunner. 4 = Left Waist Gunner. 5 = Right Waist Gunner. 6 = Tail
        // Gunner.
        Bomber bomber = model.getGame().getBomber();
        int die = Util.roll();
        if (die == 1){
            bomber.getCrewMemberByDefaultRole(CrewPosition.BOMBARDIER).setExperience(Experience.VETERAN);
        }
        else if (die <= 3){
            bomber.getCrewMemberByDefaultRole(CrewPosition.CFC_CONTROLLER).setExperience(Experience.VETERAN);
        }
        else if (die == 4){
            bomber.getCrewMemberByDefaultRole(CrewPosition.LEFT_GUNNER).setExperience(Experience.VETERAN);
        }
        else if (die == 5){
            bomber.getCrewMemberByDefaultRole(CrewPosition.RIGHT_GUNNER).setExperience(Experience.VETERAN);
        }
        else{
            bomber.getCrewMemberByDefaultRole(CrewPosition.TAIL_GUNNER).setExperience(Experience.VETERAN);
        }
    }

    private void handleFormationAssembly(){
        FormationAssemblyChart.FormationAssemblyResult formationAssemblyResult =
                FormationAssemblyChart.getFormationAssembly(model.getGame().getMission(), model.getGame().getBomber());
        // TODO Notify the user of result message
        model.getGame().getMission().setFormationAssembly(formationAssemblyResult.formationAssembly);
        if (formationAssemblyResult.formationAssembly == FormationAssembly.MID_AIR_COLLISION){
            handleMidAirCollision();
        }
    }

    private void handleFighterEscortRendezvous(){
        Mission mission = model.getGame().getMission();
        if (mission.getMissionTimeOfDay() == TimeOfDay.DAY && mission.isEscortAvailable()){
            if ((mission.getZone() == 3 && mission.getTargetZone() == 6) ||
                    (mission.getZone() == 9 && mission.getTargetZone() >= 10)){
                // TODO Roll on Table 4-11
                FighterType fighterType = FighterEscortChart.getEscort(mission.getMissionNumber(), mission.getTargetZone());
                if (fighterType == null){
                    // TODO Tell user Rendezvous Missed
                }
                else {
                    // TODO Tell user escort rendezvous successful
                    mission.setEscort(fighterType);
                }
            }
        }
    }

    private void handleJapaneseFighterResistance(){
        Mission mission = model.getGame().getMission();
        if (mission.getZone() >= 10 || (mission.getZone() == 6 && mission.getMissionNumber() <= 10)){
            if (mission.getMissionTimeOfDay() == TimeOfDay.DAY && mission.getMissionNumber() <= 10 &&
                    mission.getBaseTakeoffTimeOfDay() == TimeOfDay.DAY && mission.getExpectedLandingTimeOfDay() == TimeOfDay.NIGHT &&
                    mission.getDirection() == Direction.RETURN_HOME)
                return;
            JapaneseFighterDensity density = JapaneseFighterResistanceChart.getJapaneseFighterDensity(mission, model.getGame().getBomber());
            if (density != JapaneseFighterDensity.NONE){
                determineJapaneseFighterResistance(density);
            }
        }
    }

    private void determineJapaneseFighterResistance(JapaneseFighterDensity density){
        if (model.getGame().getMission().getMissionTimeOfDay() == TimeOfDay.DAY)
            determineJapaneseFighterResistanceDay(density);
        else
            determineJapaneseFighterResistanceNight(density);
    }

    private void determineJapaneseFighterResistanceDay(JapaneseFighterDensity density){
        Mission mission = model.getGame().getMission();
        int die = Util.roll();
        if ((mission.getMissionNumber() <= 10 && mission.getZone() == 6) || mission.getZone() >= 10){
            if ((density == JapaneseFighterDensity.LIGHT && die == 1) ||
                    (density == JapaneseFighterDensity.MODERATE && die <= 2) ||
                    (density == JapaneseFighterDensity.HEAVY && die <= 3))
                handleJapaneseFighterAttacksDay();
        }
    }

    private void handleJapaneseFighterAttacksDay(){
        // Roll on Tables 5-2 to 5-13
        model.getGame().getMission().setJapaneseFighters(JapaneseFighterResistanceChart.getJapaneseFighters(model.getGame().getMission(), model.getGame().getBomber()));
    }

    private void determineJapaneseFighterResistanceNight(JapaneseFighterDensity density){
        Mission mission = model.getGame().getMission();
        // TODO No night attack if current zone is ALL WATER
        // TODO Implement this
    }

    private void handleOverTarget(){
        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();
        if (mission.getZone() == mission.getTargetZone() && mission.getDirection() == Direction.TO_TARGET && bomber.isCarryingBombs()){
            // TODO Roll on Tables 6-1 to 6-9
        }
    }

    private void handleMidAirCollision(){
        Bomber bomber = model.getGame().getBomber();
        bomber.setBomberStatus(BomberStatus.WRECKED);
        handleBailingOut();
    }

    private void handleBailingOut(){
        Bomber bomber = model.getGame().getBomber();
        Mission mission = model.getGame().getMission();
        if (bomber.getPressurization() == Pressurization.ON){
            // TODO Consult Section 8.2
        }

        if (bomber.isUnderControl()){
            // TODO Roll on 8-4
        }
        else {
            // TODO Roll on 8-5
        }
    }

    private void handleLanding(){
        Bomber bomber = model.getGame().getBomber();
        Mission mission = model.getGame().getMission();

        if (mission.getBaseArea() == MapAreaCode.MARIANAS &&
                mission.getZone() == 1 &&
                bomber.getAltitude() == Altitude.LO &&
                bomber.isOnCourse() &&
                mission.getDirection() == Direction.RETURN_HOME){
            // TODO Roll on 8-1
        }

        if (mission.getBaseArea() == MapAreaCode.IWO_JIMA &&
                mission.getMissionNumber() >= 11 &&
                mission.getZone() == 6 && mission.getDirection() == Direction.RETURN_HOME){
            // TODO Consult Section 8.4
        }

        if (mission.getBaseArea() == MapAreaCode.JAPAN && mission.getZone() >= 10){
            // TODO Consult Section 8.5
        }

        if (mission.getBaseArea() == MapAreaCode.WATER && (mission.getZone() <= 5 || (mission.getZone() >= 7 && mission.getZone() <= 9))){
            // TODO Consult Section 8.6 and roll on 8-2
        }
    }

    private void handleVictoryConditions(){
        Bomber bomber = model.getGame().getBomber();
        Mission mission = model.getGame().getMission();
        // TODO Roll on tables 9-1 to 9-3
    }
}
