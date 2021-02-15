package b29;

import b29.game.Campaign;
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
import b29.view.ViewUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

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
                            if (model.getGame().getCampaign() == null){
                                model.getGame().setCampaign(new Campaign());
                            }
                            if (model.getGame().getMission() == null){
                                int missionNumber = model.getGame().getCampaign().getNextMissionNumber();
                                model.getGame().setMission(new Mission(missionNumber));
                                model.getGame().getCampaign().getMissions().add(model.getGame().getMission());
                            }
                            if (model.getGame().getBomber() == null){
                                model.getGame().setBomber(new Bomber());
                                model.getGame().getBomber().createDefaultCrew();
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


                            Bomber bomber = model.getGame().getBomber();
                            // Load fuel tanks
                            // On any “LO” altitude mission, extra fuel tanks are NOT included unless All gunners and all guns
                            // are aboard, add auxiliary fuel tanks in the fwd bomb bay
                            // Flying to a designated target in Zones 13 or 14, add auxiliary fuel tanks in the aft bomb bay
                            // If both of the above conditions apply, then auxiliary fuel tanks are considered to be in both bomb bays.
                            if (mission.getMissionAltitude() != Altitude.LO || (
                                    bomber.getCrewMemberByDefaultRole(CrewPosition.CFC_CONTROLLER) != null &&
                                    bomber.getCrewMemberByDefaultRole(CrewPosition.LEFT_GUNNER) != null &&
                                    bomber.getCrewMemberByDefaultRole(CrewPosition.RIGHT_GUNNER) != null &&
                                    bomber.getCrewMemberByDefaultRole(CrewPosition.TAIL_GUNNER) != null &&
                                    bomber.getGuns().containsKey(GunPosition.FWD_UPPER_TURRET) && bomber.getGuns().get(GunPosition.FWD_UPPER_TURRET).isOperational() &&
                                    bomber.getGuns().containsKey(GunPosition.FWD_LOWER_TURRET) && bomber.getGuns().get(GunPosition.FWD_LOWER_TURRET).isOperational() &&
                                    bomber.getGuns().containsKey(GunPosition.REAR_UPPER_TURRET) && bomber.getGuns().get(GunPosition.REAR_UPPER_TURRET).isOperational() &&
                                    bomber.getGuns().containsKey(GunPosition.REAR_LOWER_TURRET) && bomber.getGuns().get(GunPosition.REAR_LOWER_TURRET).isOperational() &&
                                    bomber.getGuns().containsKey(GunPosition.TAIL_TURRET) && bomber.getGuns().get(GunPosition.TAIL_TURRET).isOperational())){
                                bomber.setAuxFwdFuelLeft(Bomber.DEFAULT_AUX_MAX_FUEL);
                            }
                            else
                                bomber.setAuxFwdFuelLeft(0);

                            if (mission.getTargetZone() == 13 || mission.getTargetZone() == 14)
                                bomber.setAuxAftFuelLeft(Bomber.DEFAULT_AUX_MAX_FUEL);
                            else
                                bomber.setAuxAftFuelLeft(0);

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
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_START_TURN);
                            break;
                        case PLAY_MISSION_START_TURN:
                            if (mission.getNumTurnsTillRegainFormation() > 0)
                                mission.adjNumTurnsTillRegainFormation(-1);
                            if (mission.getNumTurnsTillRegainFormation() == 0) {
                                mission.setOutOfFormation(false);
                                mission.setNumTurnsTillRegainFormation(-1);
                            }
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
                                        // B-29 destroyed, crew KIA
                                        handleMidAirCollision(true);
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
                                handleLanding();
                                mission.setZone(0);
                            }
                            else {
                                // TODO If some engines out, bomber flys slower and must spend 2 turns in each zone
                                //boolean mustMove = mission.getTurnsInCurrentZone() >= 2;

                                int currentZone = model.getGame().getMission().getZone();
                                if (model.getGame().getMission().getDirection() == Direction.TO_TARGET) {
                                    model.getGame().getMission().setZone(currentZone + 1);
                                } else {
                                    model.getGame().getMission().setZone(currentZone - 1);
                                }


                                // Missions 1-10: Formation ends inbound in Zone 3
                                if (mission.getMissionNumber() <= 10 && mission.getDirection() == Direction.RETURN_HOME && mission.getZone() == 3){
                                    mission.setOutOfFormation(true);
                                }

                                // Change altitude, if necessary
                                Altitude goalAltitude = mission.getMissionAltitude();
                                if (bomber.hasDamage(Damage.OXYGEN_OUT)){
                                    // Must descend to LO altitude
                                    goalAltitude = Altitude.LO;
                                }

                                // TODO If any crew member has no oxygen, must descend to LO altitude

                                if (mission.getZone() < 3 && bomber.getAltitude() != goalAltitude) {
                                    if (goalAltitude.ordinal() > bomber.getAltitude().ordinal()) {
                                        // Ascend
                                        bomber.setAltitude(Altitude.values()[bomber.getAltitude().ordinal() + 1]);
                                        altitudeChange = 1;
                                    } else {
                                        // Descend
                                        bomber.setAltitude(Altitude.values()[bomber.getAltitude().ordinal() - 1]);
                                        altitudeChange = -1;
                                    }
                                }
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_PRESSURIZATION);
                            break;
                        case PLAY_MISSION_PRESSURIZATION:
                            handlePressurization();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_CREW_MOVEMENT);
                            break;
                        case PLAY_MISSION_CREW_MOVEMENT:
        /*
        TODO Crew Movement
1. When crewmember is wounded or KIA mark counter appropriately.
2. Crewmembers may freely move through aircraft to perform other duties at the beginning of the
turn subject to pressurization requirements of Section III.C.
3. Maximum number of crewmembers per compartment:
a. Nose: 3
b. Nav/Radio: 7
c. Waist: 5
d. Utility: 3 (Remember Utility is never pressurized)
e. Tail: 1
f. KIA count against totals
4. Crew movement within same compartment may be done at any time but if someone else takes
over a gun it may not be fired that turn.
5. Lightly wounded can move on their own; seriously wounded may move only one compartment
and may not move between Nav/Radio and Waist.
6. If aircraft is pressurized, wounded may not be placed in unpressurized areas.
7. If P and CP have no worse than light wound, CP may move to fight a fire. Otherwise they must
stay in their seats.
8. If P and CP are seriously wounded or KIA, any crewmember may fly the aircraft with first
choice being Flt. Engr. If he is seriously wounded or KIA anyone else may take over. Landing is
subject to modifiers on Tables 8-1 and 8-2.
         */
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_CONSUME_FUEL);
                            break;
                        case PLAY_MISSION_CONSUME_FUEL:
                            int fuelConsumed = getFuelConsumed(true);
                            boolean fuelLeft = bomber.consumeFuel(fuelConsumed);
                            if (!fuelLeft){
                                // Handle out-of-fuel (Table 4-1?)
                                handleOutOfFuel();
                            }
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_WEATHER);
                            break;
                        case PLAY_MISSION_WEATHER:
                            determineWeather();
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
                            if (mission.getDirection() == Direction.TO_TARGET) {
                                // TODO When is the user able to abort?
                                if (ViewUtil.popupConfirm("Abort Mission?", "Abort Mission?")) {
                                    mission.setAborted(true);
                                    // TODO What happens here?
                                    mission.setDirection(Direction.RETURN_HOME);
                                    mission.setOutOfFormation(true);

                                    /*
                                    TODO Aborting Mission: Mission counts if any of following occur after fighter attack:
1. Off Course obtained in Target Zone
2. Bay doors, intercom, bombsight
3. Loss of pressurization if MED or HI mission
4. P, CP, Bomb, or Nav are seriously wounded or KIA
5. One or more engines out
6. Fuel tank damage that provides insufficient fuel to bomb and return
7. Electrical system failure
If target has not been bombed, bombs may be jettisoned.
                                     */
                                }
                            }
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
                            /*
A. Determine Fighter Resistance
1. In Zones 10-14 or Zone 6 if Iwo=Japanese, roll on Table 5-1
2. If pressurized at MED or HI, P decides to depressurize before roll. (May not if repressurized the
previous turn from a voluntary depressurization.)

B. DAY Missions
1. Roll 1D on Table 5-1
2. If roll on 5-1=”Fighter Attack” then roll on Table 5-2 for type of aircraft
3. Once type is determined, roll on Table 5-3 for area of attack
4. Once area is determined, roll on Table 5-4 for angle of attack and place fighter
5. Once angle is determined, roll on Table 5-5 for type of pilot

C. Defensive Fire
1. Each armed and operating G position may fire at one fighter within its field of fire as shown on
Table 5-6
2. If intercom out, roll on Table 5-6 and use parenthetical range (if applicable)
3. Once allocated to a particular target, the position may not be unallocated
4. Mark off each burst. When gun is empty it may not fire
5. Roll once for each firing gun on Table 5-7
6. If fighter is hit, roll on Table 5-8. If destroyed, remove; if FCA, place marker on fighter

D. Japanese Offensive Fire
1. Each surviving fighter rolls on Table 5-9
2. If result is a Hit, roll on Table 5-10 for number of shells hitting
3. For each shell hit, roll on Table 5-11 for appropriate clock position of fighter
4. After determining section, roll on Table 5-12 for each hit, result=# of rolls for Damage Tables
based on fighter type
5. Roll on specific damage table (Tables 7-1 through 7-14) and resolve and record
6. If a compartment is hit crew casualties must be rolled for

E. Successive Attacks
1. Frank or Tojo: Each hit, roll 1D, 1-3=NE; 4-6=Fighter is able to attack again
2. Undamaged Nick, Tony, Zeke, Oscar, or George: Only if B-29 is Out of Formation and one or
more engine out
3. For each eligible, roll once on Table 5-13, if attack, resolve as normal for attack and collision
4. Pre-conditions for successive attack:
  a. Fighter scored hit and
  b. B-29 is Out of Formation and
  c. One or more engines out and
  d. Fighter did not suffer and FCA
5. Fighter removed after second successive attack
                             */
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
                            /*
Over the Target
A. Determine Target Visibility: After all fighter attacks are resolved Roll on Table 6-1

B. Anti-Aircraft Fire – Flak (AAA)
1. After determining Target Visibility, resolve AAA rolling 1D on Table 6-2
2. After determining amount of flak, roll 2D three times on Table 6-3 unless there was no flak
3. For each hit recorded roll on Table 6-4 to determine number of shell hits
4. For each shell hit, roll 1D on Table 6-5 to determine section hit and record
5. After section determined, roll on specific Damage Table(s) 7-1 through 7-14 and record
6. If shell explodes within the aircraft (BIP) from Table 6-4
 a. All crew in applicable compartment are KIA
 b. If Wing, Tail, or Nose, B-29 dives out of control and crew bail out per Table 8-5
 c. If a Bomb Bay with weapons is hit, ship explodes and all crew are KIA
 d. If Nav/Radio hit, roll for P, CP, and B on Table 7-13 and for Explosive Decompression
    on Table 7-11 if pressurized and at MED or HI
 e. If Nav/Radio or Waist, pressurization capability is permanently lost
 f. If Nav/Radio, empty Bomb Bay, Waist, or Utility all of the following occur:
   i. If Day mission, B-29 falls Out of Formation
   ii. Must spend two turns in each following Zone expending one fuel box
   iii. Assume that the compartment received every Damage Table result
   iv. Landing roll on Tables 8-1 and 8-2 is additional -2
   v. No Evasive Action possible
   vi. If successful return to base, aircraft is scrapped

                             */
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_BOMB_RUN);
                            break;
                        case PLAY_MISSION_BOMB_RUN:
                            /*
                            Bomb Run
                            1. Resolve Bomb Run on Table 6-6, then Table 6-7 to determine % within 1000 feet of aim point
                            2. If Urban Area against target on Table 2-2C and at LO roll 1D on Table 6-8 (and Table 6-9, if
                            necessary) to determine if B-29 encounters thermal turbulence
                            3. After bomb run is resolved begin return by turning airplane and spending one turn in Target
                        Box, expending one fuel box
                             */
                            handleOverTarget();
                            model.getGame().setPhaseStep(PhaseStep.PLAY_MISSION_START_TURN);
                            break;
                        case PLAY_MISSION_LANDING:
                            /*
                            TODO Ending the Mission
A. Landings
1. Landing in the Marianas (20th Air Force Base)
2. Iwo Jima if U.S. controlled
3. Landing in Japan.
4. Crash landing or ditching into the sea
5. If P and CP controls are out landing is impossible
6. Landing is rolled for with 2D on Table 8-1 or 8-3 subject to applicable modifiers
7. An unsuccessful landing may lead to destruction of aircraft and deaths of crew
B. Bailing Out: May be performed anywhere
1. Controlled bail out, roll on Table 8-4
2. Uncontrolled bail out, roll on Table 8-5
3. Failure to depressurize results in a roll on Table 7-11
4. If pressurized and bail out is from uncontrolled aircraft, roll on Table 7-11 for crewmembers
exiting a pressurized section
C. The Marianas
1. At LO in Zone 1, a landing may be attempted at 20th Air Force Base if:
a. Has sufficient fuel to exit the Zone
b. Is On Course in Zone 1 (Table 4-5)
c. Is not otherwise restricted by engine damage
2. Roll for landing on Table 8-1, applying Weather modifier only
3. Bailout over base is allowed (Table 8-4)
D. Iwo Jima (Zone 6) if Friendly (Missions 11-35)
1. Resolve weather, navigation, and random events in Zone 6 before attempting to land
2. If weather Is “Good,” roll 1D: 1-4=Good weather for landing; 5-6=Poor weather
3. If weather is Poor, roll 1D: 1-4=Poor weather for landing; 5-6=Bad weather
4. Roll for landing on Table 8-1
5. Bailout over base is allowed (Table 8-4) and if bail out is safe, crewmember is returned to duty
in Zone 1
E. Japan (Zones 10-14)
1. If forced to land or bail out in Japan, consult Table 2-9 and cross reference the Zone B-29
occupies with Mission Target
2. Letter to the right of slash mark will detail if over W=Water, L=Land, or O=Okinawa. If two
letters are listed player chooses which one if B-29 is landing controlled or bail out is voluntary
3. If landing or bail out is forced, roll 1D: 1-3=First letter; 4-6=Second letter
4. If controlled landing on land, roll on Table 8-1 with a -4 modifier; if landing or bailing out of
water, see Section VII.F below
5. Surviving crewmembers are deemed POW. Roll on Table 8-7 for their fate
F. Sea: Ditching is necessary if a forced landing must be made in Zones 1-9 or if Water in Zones
10-14.
1. Roll for landing or bailing out in water on Table 8-3 after checking sea state (Table 8-2)
2. Each crewmember that survives landing or bail out, roll on Table 8-6 for their fate
3. Survivors in Zones 1-9 are returned to duty in the Marianas
4. Survivors in Zone 10-14 may be captured requiring a role on Table 8-7 for POW survival
5. Aircraft landing at sea is permanently lost
                             */
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

    private void handlePressurization() {
        Bomber bomber = model.getGame().getBomber();

        if (bomber.getPressurization() != Pressurization.INOP) {
            // If altitude changed from LO <-> MED, auto de/pressurize
            if (altitudeChange > 0 && bomber.getAltitude() == Altitude.MED) {
                bomber.setPressurization(Pressurization.ON);
            } else if (altitudeChange < 0 && bomber.getAltitude() == Altitude.LO) {
                bomber.setPressurization(Pressurization.OFF);
            }
        }

        if (bomber.getAltitude() != Altitude.LO && bomber.getPressurization() != Pressurization.ON) {
            ViewUtil.popupNotify("Altitude at MED or HI and no pressurization!");
            int die = Util.roll2d();
            if (bomber.hasDamage(Damage.INTERCOM_FAILURE))
                die += 2;
            if (die <= 11){
                // No problem, all crewmembers on oxygen
                ViewUtil.popupNotify("All crew members on oxygen.");
            }
            else {
                // Oxygen malfunction for individual crewman, or crewman fails to don mask in timely manner
                CrewMember crewMember = null;
                while (true) {
                    die = Util.roll();
                    if (die == 1) {
                        die = Util.roll();
                        if (die <= 3)
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.BOMBARDIER);
                        else if (die == 4)
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.PILOT);
                        else
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.COPILOT);
                    } else if (die == 2) {
                        die = Util.roll();
                        if (die == 1)
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.ENGINEER);
                        else if (die <= 3)
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.NAVIGATOR);
                        else
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.RADIO_OPERATOR);
                    } else if (die == 3) {
                        die = Util.roll();
                        if (die <= 2)
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.CFC_CONTROLLER);
                        else if (die <= 4)
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.LEFT_GUNNER);
                        else
                            crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.RIGHT_GUNNER);
                    } else if (die == 4)
                        crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.RADAR_OPERATOR);
                    else if (die == 5)
                        crewMember = bomber.getCrewMemberByDefaultRole(CrewPosition.TAIL_GUNNER);

                    if (crewMember != null)
                        break;
                }

                if (!crewMember.isSwOrKia()) {
                    die = Util.roll();
                    if (die <= 5) {
                        // condition is discovered in time to successfully revive and/or move crewman to another oxygen line
                        ViewUtil.popupNotify(crewMember.getName() + " discovered in time and successfully revived.");
                    } else {
                        // crewman’s condition is discovered too late, he has died as a result of anoxia (KIA)
                        ViewUtil.popupNotify(crewMember.getName() + " discovered too late and has died of anoxia!.");
                        crewMember.setStatus(CrewStatus.KIA);
                    }
                }
                else
                    ViewUtil.popupNotify("No crew members affected");
            }
        }

        /*
        TODO At MED or HI: Remains pressurized unless
a. If Flight Engineer is occupied, may be voluntarily pressurized/depressurized
b. If P or anyone in Waist compartment, may be voluntarily depressurized
c. Crew may not move from pressurized to non-pressurized except from Nav/Radio
directly to Waist and vice-versa unless communications tunnel is damaged per Table 7-3
or 7-4
d. At MED or HI with a fire in a pressurized area, depressurization modifies Table 7-12
e. If combat is anticipated P must decided whether to depressurize
f. If not depressurized for bail out, roll on Table 7-11
3. Pressurization capability lost due to involuntary depressurization if:
a. Damage from Table 7.0 or Random Event on Table 4.9
b. Both inboard engines (2 and 3) are inop, all pressurization is lost
4. If pressurization is lost at MED or HI, check Table 7.3 for Frostbite effect.
a. Roll 2D every turn (+2 if intercom out) 2-11=NE, 12=individual crewman affected
b. Roll 1D
1=Nose (1-3=Bombardier; 4=P; 5-6=CP)
2=Nav/Radio (1=Flt Egr; 2-3=Nav; 4-6=Radio Op)
3=Gunner Section (1-2=CFC; 3-4=LG; 5-6=RG)
4=Radar Operator
5=Tail G
6=No Effect
c. Roll 1D 1-5=Condition discovered in time; 6=KIA
         */
    }

    private void handleDepressurization(){
        Bomber bomber = model.getGame().getBomber();
        if (bomber.getAltitude() != Altitude.LO){
            // TODO See Section 7.3 for potential crewmember frostbite effects

            // TODO All crewmembers must immediately go on oxygen (see Section 7.4 if oxygen is out anywhere on the aircraft)
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
        ViewUtil.popupNotify("Fire in Engine " + engineNumber + " during run-up, trying to extinguish!");

        // Each engine has 2 fire extinguishers

        int die = Util.roll();
        if (die == 1){
            // Fire out, plane does not fly, no credit to Mission Tour, B-29 repairable by next mission
            ViewUtil.popupNotify("Fire extinguished, but plane won't fly.  No credit for Mission Tour.  B-29 repairable by next mission.");
        }
        else{
            ViewUtil.popupNotify("Failed to extinguish fire, trying again!");

            // Fire continues, try again
            die = Util.roll();
            if (die == 1){
                // Fire out, plane does not fly, no credit to Mission Tour, B-29 repairable by next mission
                ViewUtil.popupNotify("Fire extinguished, but plane won't fly.  No credit for Mission Tour.  B-29 repairable by next mission.");
            }
            else {
                // Crew must abandon aircraft and send for additional ground fire-fighting equipment
                ViewUtil.popupNotify("Unable to extinguish fire, crew must abandon aircraft and send for addition ground fire-fighting equipment.");
                die = Util.roll();
                if (die <= 4){
                    // B-29 repairable by next mission
                    ViewUtil.popupNotify("Fire extinguished.  No credit for Mission Tour.  B-29 repairable by next mission.");
                }
                else {
                    // B-29 irrepairably damaged
                    ViewUtil.popupNotify("B-29 irrepairably damaged.  No credit for Mission Tour.");
                    model.getGame().getBomber().setBomberStatus(BomberStatus.WRECKED);
                }
            }
        }

        model.getGame().setPhase(Phase.GAMEOVER);
    }

    private void handleUnusualPowerConditionsDuringTakeoff(){
        ViewUtil.popupNotify("Unusual power conditions during take-off!");
        int die = Util.roll();
        if (die <= 3){
            // Pilot aborts take-off
            ViewUtil.popupNotify("Pilot aborts take-off!");
            model.getGame().getMission().setAborted(true);
            die = Util.roll();
            if (die <= 5){
                // Plane stopped successfully, plane does not fly, no credit to Mission Tour
                ViewUtil.popupNotify("Pilot stops successfully, plane does not fly.  No credit for Mission Tour.");
                model.getGame().setPhase(Phase.GAMEOVER);
            }
            else {
                // Brakes fail, roll on Table 3-5 (Accident)
                ViewUtil.popupNotify("Brakes fail!");
                handleTakeoffAccident(false);
            }
        }
        else {
            // Pilot continues take-off
            ViewUtil.popupNotify("Pilot continues take-off!");
            die = Util.roll();
            if (die <= 4){
                // Mission continues normally
                ViewUtil.popupNotify("Power levels return to normal!  Mission may continue!");
            }
            else {
                // Not enough lift, roll on Table 3-5
                ViewUtil.popupNotify("Not enough lift!");
                handleTakeoffAccident(true);
            }
        }
    }

    private void handleEngineFailureOnTakeoff(){
        int engineNumber = Util.getRandomEngine();
        ViewUtil.popupNotify("Engine " + engineNumber + " failure on take-off!");
        // Set engine out
        model.getGame().getBomber().setEngineOut(engineNumber);
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
            ViewUtil.popupNotify("Not enough lift!");
            handleTakeoffAccident(true);
        }
    }

    private void handleRunawayPropellerAfterTakeoff(){
        int engineNumber = Util.getRandomEngine();
        ViewUtil.popupNotify("Engine " + engineNumber + " has runaway propeller after take-off!");
        // Set engine out
        model.getGame().getBomber().setEngineOut(engineNumber);
        model.getGame().getMission().setAborted(true);

        int die = Util.roll();
        if (die <= 4){
            ViewUtil.popupNotify("Propeller feathered!");
            // TODO Propeller feathered, roll for "Landing on Land" (Table 8-1) - one engine out
        }
        else if (die == 5){
            ViewUtil.popupNotify("Propeller control restored, but mission aborted!");
            // TODO Propeller control restored, mission aborted, roll for "Landing on Land" (Table 8-1)
            model.getGame().getMission().setAborted(true);
        }
        else {
            ViewUtil.popupNotify("Propeller feathering fails, windmilling propeller!");
            // TODO Feathering fails, windmilling prop, roll for "Landing on Land" (Table 8-1) with -2 modifier
        }
    }

    private void handleEngineFireAfterTakeoff(){
        int engineNumber = Util.getRandomEngine();
        ViewUtil.popupNotify("Engine " + engineNumber + " fire after take-off!");
        // Set engine out
        model.getGame().getBomber().setEngineOut(engineNumber);
        model.getGame().getMission().setAborted(true);

        // Roll for extinguishing fire
        int die = Util.roll();
        if (die == 1) {
            // TODO fire is extinguished, mission aborted, roll for "Landing on Land" (Table 8-1) - one engine out
            ViewUtil.popupNotify("Fire extinguished!");
        }
        else {
            ViewUtil.popupNotify("Fire is raging!");
            die = Util.roll();
            if (die == 1){
                // TODO fire is extinguished, mission aborted, roll for "Landing on Land" (Table 8-1) - one engine out
                ViewUtil.popupNotify("Fire extinguished!");
            }
            else {
                // Attempt immediate crash landing
                ViewUtil.popupNotify("Attempt crash landing!");
                die = Util.roll();
                if (die <= 3){
                    // TODO Roll for "Landing on Land" (Table 8-1) - one engine out
                }
                else if (die <= 5){
                    // TODO Roll for "Landing in Water" (Table 8-3) - one engine out
                }
                else {
                    // Explosion, plane destroyed, crew KIA
                    ViewUtil.popupNotify("Explosion!  B-29 destroyed!  Crew KIA!");
                    model.getGame().getBomber().setBomberStatus(BomberStatus.WRECKED);
                    model.getGame().getBomber().getCrew().stream().forEach(crew -> crew.setStatus(CrewStatus.KIA));
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
            // If zone == 1 after take-off,
            // Roll 1D, if < # Guns (fwd upper, fwd lower, aft upper, aft lower, tail turret, tail cannon), -1 fuel
            int gunCount = (int) bomber.getGuns().values().stream().filter(gun -> gun.isOperational()).count();
            if (Util.roll() < gunCount)
                fuelConsumed += 1;
            // Roll 1D, if <= # Crew (CFC, Left Gunner, Right Gunner, Tail Gunner), -1 fuel
            int crewCount = (int) bomber.getCrew().stream()
                    .filter(crew -> !crew.isSwOrKia())
                    .filter(crew -> crew.getDefaultRole() == CrewPosition.CFC_CONTROLLER ||
                            crew.getDefaultRole() == CrewPosition.LEFT_GUNNER ||
                            crew.getDefaultRole() == CrewPosition.RIGHT_GUNNER ||
                            crew.getDefaultRole() == CrewPosition.TAIL_GUNNER).count();
            if (Util.roll() <= crewCount)
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
        bomber.getAreas().get(BomberCompartment.WAIST).getDamage().add(Damage.CFC_SYSTEM_FAILURE);
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
        // +2 fuel
        bomber.consumeFuel(-2);
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
            // -4 fuel
            bomber.consumeFuel(4);
        }
        else {
            // -2 fuel
            bomber.consumeFuel(2);
        }
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
        model.getGame().getBomber().getAreas().get(BomberCompartment.WAIST).getDamage().add(Damage.RADAR_FAILURE);
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
        Mission mission = model.getGame().getMission();
        if (mission.isOutOfFormation()) {
            if (mission.getDirection() == Direction.TO_TARGET && mission.getZone() == 3) {
                FormationAssemblyChart.FormationAssemblyResult formationAssemblyResult =
                        FormationAssemblyChart.getFormationAssembly(model.getGame().getMission(), model.getGame().getBomber());
                // Notify the user of result message
                ViewUtil.popupNotify(formationAssemblyResult.message);
                model.getGame().getMission().setFormationAssembly(formationAssemblyResult.formationAssembly);
                if (formationAssemblyResult.formationAssembly == FormationAssembly.MID_AIR_COLLISION) {
                    handleMidAirCollision(false);
                }
            }
            else if (mission.getZone() > 3){
                Bomber bomber = model.getGame().getBomber();
                // If all engines okay and sufficient fuel, may rejoin at cost of one fuel box
                if (!bomber.hasEngineOut() && bomber.getFuelLeft() > 1 && bomber.getAltitude() == mission.getMissionAltitude()){
                    if (ViewUtil.popupConfirm("Rejoin Formation?", "Rejoin formation (cost 1 fuel)?")){
                        bomber.consumeFuel(1);
                        mission.setOutOfFormation(false);
                        mission.setFormationPosition(FormationPosition.TAIL);
                        ViewUtil.popupNotify("You rejoin the formation in the tail position.");
                    }
                }
            }
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
                    // Tell user Rendezvous Missed
                    ViewUtil.popupNotify("Escort Rendezvous missed!");
                }
                else {
                    // Tell user escort rendezvous successful
                    mission.setEscort(fighterType);
                    ViewUtil.popupNotify("Escort Rendezvous successful!");
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

        /*
        TODO SUCCESSIVE ATTACKS
A. For any Frank or Tojo fighter which scores a hit in its initial attack on the B-29 (even if the
hit has no effect) and was not damaged (“FCA”) during the attack, roll 1D: “1-3” = no effect,
remove the fighter from the board, “4-6” = fighter is able and willing to attack the B-29 again
(assuming, of course, it did not collide with the B-29). Note that undamaged Nick,
Tony, Zeke, Oscar, and George fighters may only make a die roll for successive attacks if the B-
29 is “out of formation”, and one or more of the B-29’s engines are out. Fighters not scoring hitsnever roll for successive attacks (Irving fighters do not appear on “Day” Missions and are
unaffected by this rule). For each fighter eligible to make a successive attack, roll once on Table
5-13, under one of two columns (depending on whether or not all B-29 engines are operating), to
determine the fighter’s new attack position. After any and all fighters conducting a successive
attack are positioned, resolve combat normally using Tables 5-6 through 5-12. Also check for
collision normally on successive attacks (see
Section 5.5 above). Fighters not scoring a hit during this attack are removed from play.
C. A fighter normally does not conduct a second successive attack unless it scored a hit during
the first successive attack, and the B-29 is “out of formation”, and one or more of the B-29’s
engines are out, and the fighter has not suffered a “FCA” result—if all four conditions apply, roll
1D again per the provision of paragraph a. above and, if eligible
for another successful attack, roll 2D on the second column of Table 5-13. After any second
attack, a fighter is removed from play even if it scores a hit every time. The most attacks any one
fighter can make against the B-29 on any given turn is three—one initial and two successive
attacks.
         */
    }

    private void determineJapaneseFighterResistanceNight(JapaneseFighterDensity density){
        Mission mission = model.getGame().getMission();
        // No night attack if current zone is ALL WATER
        List<MapAreaCode> areaCodes = FlightLogGazeteer.getMapAreaCodes(mission.getTarget(), mission.getZone());
        if (areaCodes.size() == 1 && areaCodes.contains(MapAreaCode.WATER))
            return;
        // TODO Implement this
        /*
        COMBAT PROCEDURE – “NIGHT”
MISSIONS
On “Night” missions, roll on Table 2-7 for gunner allocation prior to take off.
At the Designated Target Zone (only), roll on Table 5-14 to determine if your bomber has been
spotted by searchlight (EXCEPTION: do NOT make this roll if the target city is asterisked on
Table 2-9). If the B-29 is spotted and fixed, then it remains so for the remainder of the turn
(exception: see note (b) to Table 5-14). If the bomber is still
within the Designated Target Zone next turn, roll normally on Table 5-14—there are no special
modifiers for having been successfully spotted the previous turn.
Night fighters always attack from the 6 o’clock position. Roll 1D for each night fighter to see if
the fighter is spotted (subtract -1 if any gunners besides just the tail gunner are aboard, even as“observers”): “1-2” = attacking fighter spotted; “3-6” = attacking fighter approaches unseen. If a
fighter is spotted and the intercom is working, then the bomber may take
“Evasive Action” after defensive fire, but before Japanese offensive fire. This means that the
fighter(s) will be affected by the “Evasive Action” modifier, but the gunner(s) will not (at least
for the Initial attack). (EXCEPTION: if within the Designated Target Zone and the B-29
performed “Evasive Action” in an attempt to avoid being fixed by Japanese searchlight, then the
bomber gunner(s) are affected by the “Evasive Action” modifier.)
Defensive fire may never be resolved against a fighter not spotted. If no Japanese fighter is
spotted, the fighter(s) may fire first, with B-29 defensive fire resolved after the
effects of Japanese offensive fire are applied (at that time, any fighter is automatically spotted).
A Japanese Nick night fighter will always make one (and only one) successive attack unless it
was destroyed or received a “FCA” damage result on Table 5-8. An undamaged Irving night
fighter will not make a successive attack unless one or more of the B-29’s engines are out (in the
event of a coordinated attack, only one fighter may make a successive attack if eligible—any
others may not; select only an undamaged fighter for the attack). In either case, use the Vertical
Climb position for the Successive attack (the fighter is automatically
considered spotted for this round of combat).
thermal turbulence: Urban Area mission against a target listed on Table 2-2C (not Table 2-3
targets) at “LO” altitude (only!), roll 1D on Table 6-8 (and, if necessary, Table 6-9) to determine
if B-29 encounters thermal turbulence from incendiary caused fires.
         */
    }

    private void handleOverTarget(){
        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();
        if (mission.getZone() == mission.getTargetZone() && mission.getDirection() == Direction.TO_TARGET && bomber.isCarryingBombs()){
            // TODO Roll on Tables 6-1 to 6-9
        }
    }

    private void handleMidAirCollision(boolean crewKIA){
        Bomber bomber = model.getGame().getBomber();
        bomber.setBomberStatus(BomberStatus.WRECKED);
        if (crewKIA)
            bomber.getCrew().stream().forEach(crew -> crew.setStatus(CrewStatus.KIA));
        else
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

        /*
        TODO IWO JIMA A B-29 in Zone 6 may attempt to land at Iwo Jima if available as a friendly base
(Missions #11- 35). Resolve weather (Section 4.4), navigation (Section 4.5), and random event
(Section 4.6) in Zone 6 before attempting to land. Iwo Jima was often subject to fog,
making landings there more difficult. Accordingly, if “Weather in Zone” for Zone 6 (Table 4-2)
was “Good”, roll 1D and apply the following: “1-4” = “Good” weather for landing, “5-6” =
“Poor” weather for landing. If “Weather in Zone” for Zone 6 (Table 4-2) was “Poor”, roll 1D and
apply the following: “1-4” = “Poor” weather for landing, “5-6” = “Bad”
weather for landing.

         */
    }

    private void handleVictoryConditions(){
        Bomber bomber = model.getGame().getBomber();
        Mission mission = model.getGame().getMission();
        // TODO Roll on tables 9-1 to 9-3

        bomber.setNumMissionsCompleted(bomber.getNumMissionsCompleted() + 1);
        bomber.getCrew().stream().forEach(crew -> {
            if (!crew.isSwOrKia())
                crew.setNumMissionsFlown(crew.getNumMissionsFlown() + 1);
        });

        /*
        TODO Victory Conditions
A. Post-Mission Debriefing
1. Resolve fate of seriously wounded and frostbitten (see Tables 7-13 and 7-14), log results
2. Note percentage of bombs on target
3. Destroyed B-29 and non-returning crew are crossed off

B. Urban Area Damage Assessment
1. Roll on Table 9-1, cross off damage box as appropriate
2. If all boxes are crossed off, city is bombed out and no more U/A missions may be flown against
it, but non-U/A missions may be assigned
3. If a bombed out city is rolled on Table 2-2C, roll instead on Table 2-3

C. Mission Victory Conditions
1. 20th AF Victory: Bomb run on target, B-29 returns to Marianas (or Iwo Jima on Missions 11-
35), and aircraft not irreparably damaged (not Flak BIP or crash landing)
2. Japanese Victory: Either B-29 destroyed in combat, ditched at sea, crash landed in Japan, or was
irreparably damaged in combat
3. Draw: Unable to bomb target or bomb drop off target, bur returned to Marianas (or Iwo Jima on
Missions 11-35), and aircraft not irreparably damaged

D. Campaign Victory Conditions: After Mission 35, compare 20th AF Victories vs. Japanese
Victories, ignoring draws. Side with most victories wins. Individual aircraft and crews can be
measured with Tables 9-2 and 9-3
         */
    }

    private void determineWeather(){
        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();
        Weather weather;

        // If mission.isStormSystemEncountered(), weather is auto-BAD
        if (mission.isStormSystemEncountered()){
            weather = Weather.BAD;
        }
        // If high-pressure system encountered, weather is auto-GOOD
        else if (mission.isHighPressureSystemEncountered()){
            weather = Weather.GOOD;
        }
        else {
            weather = WeatherChart.get(mission, bomber);
        }
        mission.setWeather(weather);

        if (bomber.getFuelLeft() == 0) {
            // TODO Handle out of fuel
        }

        if (weather == Weather.BAD){
            handleBadWeather();
        }
    }


    private void handleBadWeather(){
        boolean spendFuel = false;
        // Ask player if they want to spend one fuel to steer clear of storm
        if (ViewUtil.popupConfirm("Bad Weather", "Do you want to spend 1 fuel to steer clear of the storm?")){
            model.getGame().getBomber().consumeFuel(1);
            spendFuel = true;
        }
        BadWeatherResult badWeatherResult = WeatherChart.getBadWeatherResult(model.getGame().getMission(), model.getGame().getBomber(), spendFuel);
        if (badWeatherResult != BadWeatherResult.SAFE_PASSAGE){
            // Check for collision
            CollisionResult collisionResult = WeatherChart.getCollisionResult(model.getGame().getMission().isOutOfFormation());
            if (collisionResult == CollisionResult.SHALLOW_DIVE){
                // B-29 falls out of formation for one turn and then regains formation, if applicable
                model.getGame().getMission().setOutOfFormation(true);
                model.getGame().getMission().setNumTurnsTillRegainFormation(1);
            }
            else if (collisionResult == CollisionResult.STEEP_DIVE){
                // TODO B-29 falls violently out of formation
                // TODO Roll 1D for each wing
                int die = Util.roll();
                if (die <= 5){
                    // TODO Wing holds and B-29 goes to LO altitude and must remain out of formation, if applicable, for at least one turn or for how many it takes to regain mission altitude - whichever is greater
                }
                else {
                    // TODO Wing rips off and crew must bail out on Table 8-5
                }
            }
            else if (collisionResult == CollisionResult.MID_AIR_COLLISION){
                // TODO B-29 destroyed and crew must bail out on Table 8-5
            }
        }
    }


    private void determineCourse(){
        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();
        if (mission.isOutOfFormation() || mission.getFormationPosition() == FormationPosition.LEAD){
            int die = Util.roll2d();

            CrewMember navigator = bomber.getCrewMemberByDefaultRole(CrewPosition.NAVIGATOR);
            if (navigator.getStatus() != CrewStatus.KIA && navigator.getStatus() != CrewStatus.SERIOUS_WOUND) {
                if (navigator.getExperience() == Experience.VETERAN)
                    die -= 1;
                if (navigator.getExperience() == Experience.GREEN)
                    die += 1;
            }
            else
                // +2 if Navigator KIA or Seriously Wounded
                die += 2;

            if (mission.getZone() <= 5 || (mission.getZone() >= 9 && mission.getZone() <= 9)){
                die += 1;
            }

            if (mission.getCourse() == CourseEnum.OFF_COURSE)
                die += 1;

            if (bomber.hasDamage(Damage.GYRO_FLUX_GATE_DAMAGED))
                die += 1;

            // +1 if LORAN is damaged AND on Missions #11-35 AND in Zone 1-7
            if (bomber.hasDamage(Damage.LORAN_DAMAGED) && mission.getMissionNumber() >= 11 && mission.getZone() <= 7)
                die += 1;

            // +1 if Radio Compass is damaged AND on Missions #11-35 AND in Zone 1 or 6
            if (bomber.hasDamage(Damage.RADIO_COMPASS_DAMAGED) && mission.getMissionNumber() >= 11 && (mission.getZone() == 1 || mission.getZone() == 6))
                die += 1;

            // +1 if Navigator Tools are damaged
            if (bomber.hasDamage(Damage.NAVIGATOR_TOOLS_DAMAGED))
                die += 1;

            // +1 if Radar is damaged or Radar Operator is KIA or Seriously Wounded
            if (bomber.hasDamage(Damage.RADAR_DAMAGED) || bomber.getCrewMemberByRole(CrewPosition.RADAR_OPERATOR).isSwOrKia())
                die += 1;

            // +2 if weather is poor
            if (mission.getWeather() == Weather.POOR)
                die += 2;

            // +3 if weather is bad
            if (mission.getWeather() == Weather.BAD)
                die += 3;

            if (die <= 10){
                mission.setCourse(CourseEnum.ON_COURSE);
            }
            else {
                mission.setCourse(CourseEnum.OFF_COURSE);
                if (!bomber.consumeFuel(1)){
                    // Handle bomber out of fuel
                    handleOutOfFuel();
                }
            }
        }
    }

    private void handleOutOfFuel(){
        // TODO Handle out-of-fuel (Table 4-1?)
    }


    private void handleOxygenOut(){
        // TODO Implement this
        /*
        OXYGEN OUT
Keep a record of all oxygen system hits on the Mission Log Sheet. When the plane’s or a crew
member’s oxygen is knocked out, the B-29 must descend to “LO” altitude (and “Out of
Formation”, if applicable) on the next turn.
(EXCEPTION: a descent to “LO” altitude is not required if it is possible to re-pressurize all
occupied compartments.) In the event of oxygen loss to just a single crewmember, if a “vacancy”
exists somewhere else in the aircraft due to the death of another crewman, the crew member with
no oxygen can occupy that position and the B-29 can remain at “MED” or “HI” altitude (and in
formation, if applicable). Similarly, if a B-29 is forced down to
“LO” altitude by a crewmember oxygen outage, and later, due to the death of a crewmember or
members, there is enough vacant functioning oxygen supply to accommodate all surviving
crewmembers, then the B-29 may re-ascend to “MED” and “HI” altitude
(but may not rejoin formation in this case, if applicable).
         */

        /*
        Oxygen Out
1. Two shell hits to a particular crewmember’s oxygen.
2. Two shell hits to oxygen tanks in Utility knocks out all crewmembers’ oxygen.
3. Must descend to LO and Out of Formation on next turn
4. If there is an open oxygen for an individual he can go on that and stay at MED or HI.
5. If forced LO but subsequently an oxygen position opens up, may climb back but still Out of
Formation.
6. Oxygen fires are possible
         */
        Bomber bomber = model.getGame().getBomber();
        // Check Oxygen tanks
        if (bomber.countDamage(BomberCompartment.UTILITY, Damage.OXYGEN_SHELL_HIT) >= 2 &&
                !bomber.hasDamage(BomberCompartment.UTILITY, Damage.OXYGEN_OUT)){
            ViewUtil.popupNotify("Oxygen Tanks damaged.  Oxygen out for all crew members!");
            ViewUtil.popupNotify("Falling out of formation!  You must descend to LO altitude!");
            model.getGame().getMission().setOutOfFormation(true);
            bomber.addDamage(BomberCompartment.UTILITY, Damage.OXYGEN_OUT);
            bomber.getCrewPositionDamage().entrySet().stream().forEach(crewPositionListEntry -> {
                if (!crewPositionListEntry.getValue().contains(Damage.OXYGEN_OUT))
                    crewPositionListEntry.getValue().add(Damage.OXYGEN_OUT);
            });
        }

        // Check crew position oxygen
        bomber.getCrewPositionDamage().entrySet().stream().forEach(crewPositionListEntry -> {
            if (crewPositionListEntry.getValue().stream().filter(d -> d == Damage.OXYGEN_SHELL_HIT).count() >= 2){
                if (!crewPositionListEntry.getValue().contains(Damage.OXYGEN_OUT)){
                    crewPositionListEntry.getValue().add(Damage.OXYGEN_OUT);
                    ViewUtil.popupNotify(crewPositionListEntry.getKey() + " oxygen out!");
                }
            }
        });
    }

    private void handleFrostbite(){
        // TODO Implement this
        /*
        FROSTBITE Frostbite can occur in two situations:
• If pressurization capability (see Section 4.2) is compromised in any individual compartment—i.e., due to result from Damage Tables 7-1, 7-2, 7-6, 7-7, 7-8, or “Gunner Blister Blow-out” on
Table 4-9—the B-29 may travel one more turn (beyond the current one) at “MED” or “HI”
altitude (if applicable) and in formation (if applicable). Thereafter, the B-29 must immediately
descend to “LO” altitude (and “Out of Formation”, if applicable) for warmth, or risk frostbite to
crewmembers in the affected compartment(s). Of course, if the affected compartment is not
occupied by living crewmembers, there is no frostbite risk. If living crewmembers remain in a
compartment where pressurization capability is compromised and the B-29 remains at “MED” or
“HI” altitude beyond one turn, check for frostbite by rolling at the beginning of each turn (after
movement) on Table 7-14 for each crewman in the affected compartment. Crewmembers in a
non-affected compartment do not suffer frostbite.
• If pressurization capability is lost throughout the B-29—i.e., due to result from Damage Tables
7- 3, 7-4, 7-5, or 7-10 (including any reference to Table 7-10 from Table 4.9)—or if the B-29 has
been voluntarily depressurized and not repressurized for any reason, the B-29 may travel one
more turn (beyond the current one) at “MED” or “HI” altitude (if applicable) and in formation (if
applicable). Thereafter, to avoid risk of frostbite to crewmembers in all compartment(s), the B-29
must be re-pressurized (if able) or immediately descend to “LO” altitude (and “Out of
Formation”, if applicable) for warmth. If the B-29 remains unpressurized at “MED” or “HI”
altitude beyond one turn, check for the possibility of frostbite by rolling one die at the beginning
of each turn (after movement). On a result of “1” at “MED” altitude, or “1-2” at “HI” altitude,
roll on Table 7-14 for each and every crewman in the aircraft. (On any other result, there is no
possibility of frostbite this turn.)
Once a crewmember is frostbitten, he remains so for the remainder of the mission. Frostbitten
crewmembers may
perform no duties.
         */

        /*
         Frostbite
1. Pressurization lost in individual compartments at MED or HI, must descend within one turn to
prevent crew in affected compartments from suffering frostbite per Table 7-14.
2. Pressurization lost throughout aircraft or aircraft depressurized and not repressurized at MED or
HI, must descend within one turn to prevent crew in affected compartments from suffering
frostbite per Table 7-14.
3. At MED, roll 1D, on result of 1 or at HI on result of 1-2, roll on Table 7-14
4. If crewmember becomes frostbitten it remains for rest of flight and crewmember can perform
NO duties.
         */
    }


    private void handleEvasiveAction(){
        // TODO Implement this somewhere
        /*
        “Evasive Action” is only allowed for B-29s flying “Out of Formation” or “Night” Missions.
“Evasive Action” provides a negative modifier to B-29 defensive fire (Table 5-7) and Japanese
offensive fire (Table 5-9) and Japanese searchlights (Table 5- 14), may reduce the chance of mid-
air collision (see Section 5.5) and may reduce bombing accuracy
(see Table 6-6). No “Evasive Action” is allowed if: • B-29 is in formation • Two or more engines
are out • Electrical system is out (see Table 7-10) • Anyone other than the Pilot or Copilot is
flying the plane (see Section 7.6) • Any damage previously received that specifically prohibits
“Evasive Action.”
         */
    }

    private void handleEngineOut(){
        // TODO Implement this somewhere
        /*
        ENGINES OUT
• ONE ENGINE OUT With one engine out, the B-29 can stay in formation (if applicable, see
Section 4.8) if it jettisons its bombs immediately. If the B-29 is already in the Designated Target
Zone when the engine is knocked out, it may bomb the target and still remain in formation.
Otherwise, if the B-29 keeps its bombs aboard and continues to the target with one engine out, it
may not join formation or, if already in formation, drops “Out of Formation” (if applicable). A B-
29 with one engine out and bombs aboard may not
climb in altitude (see Section 4.1) and must spend two turns in each Zone due to slowness caused
by the weight of the bombs. There is no additional fuel cost. Once the B-29 drops its bombs, it
may continue its mission at the normal rate of speed of one Zone per turn and regains the ability
to climb in altitude. When landing with one engine out, subtract -1 from the landing roll on
Tables 8-1 and 8-2
• TWO OR THREE ENGINES OUT The B-29 must jettison its bombs and auxiliary fuel tanks,
drop “Out of Formation” (if applicable), and spend two turns in each Zone due to slowing (note
that a B-29 is never required to spend more than two turns in one Zone, except when accounting
for turn around, see Section 6.5). There is no additional fuel cost. A
B-29 with two or more engines out and at “HI” altitude must drop to “MED” altitude, while a B-
29 at “MED” altitude must drop to “LO” altitude (a B- 29 at “HI” is not required to drop to “LO”
on the same turn; this descent can be done in two turns). Note that if both inboard engines (#2 and
#3) are out, pressurization capability is lost (see Section 4.2). When a B-29 has two or moreengines out, attacking fighters add one (+1) to their Japanese Offensive Fire dice rolls. A B-29
with two or more engines out may not take “Evasive Action” (see
Section 5.7). When landing with two engines out, subtract -2 from the landing roll on Tables 8-1
and 8-2
• ONE ENGINE OPERATING If at “LO” altitude, the B-29 must either land immediately or
the crew must bail out on Table 8-4. If at “MED” altitude, the B-29 may enter the next Zone
(spending two turns in the current Zone), then it must either immediately land or the crew must
bail out on Table 8-4. If at “HI” altitude, the B-29 may enter the next Zone (spending two turns
there) and then one more Zone beyond it, then it must either immediately land
or the crew must bail out on Table 8-4. When landing with only one engine, subtract -3 from the
landing roll on Tables 8-1 and 8-2. Also, see above for additional effects.
• NO ENGINES OPERATING The B-29 must either crash land in its present Zone on either
Table 8-1 or 8-2 (find this zone on Table 2-9 or the Strategic Movement Track to determine if the
B-29 is over land or water, or see Note (c) if both land and water exist in the Zone), or the crew
must bail out on Table 8-4. Once the last engine is out, the player must immediately choose either
to attempt the crash landing or bail out. If crash landing with all engines out, apply the full
modifier on Tables 8-1 and 8-3 for all engines out as well as the modifiers for elevators, rudder,
ailerons, and wing flaps being inoperable. In addition, the landing gear may not be lowered (if not
already lowered). Also, see above for additional effects.
Note that the “20 th Air Force Base” square counts as a Zone for purpose of this rule.
         */

        /*
        Engine(s) Out
1. One Engine Out: Can stay in formation if bombs jettisoned. If not, then Out of Formation. If
over Target, can bomb Target.
2. Two/Three Engines Out:
a. Must jettison bombs and Aux Tanks
b. Drop Out of Formation
c. Spend two turns in each Zone
d. Drop one level until LO.
e. If Engines 2 and 3, pressurization lost
f. Fighters +1 to Offensive Fire
g. No Evasive Action
h. -2 to Landing rolls on Tables 8-1 and 8-2
3. Three Engines Out:
a. LO, either Land or Bail Out on Table 8-4
b. MED, move one Zone spending two turns then either Land or Bail Out
c. HI, move two Zones spending two turns then either Land or Bail Out
d. If Landing, -3 to Tables 8-1 and 8-2
4. All Engines Out:
a. Either crash land in present Zone or Bail Out
b. If land/water Zone must Bail Out on Table 8.4
c. If crash landing, apply all modifiers on Tables 8-1 and 8-3 with all maneuver surfaces
inoperative. Landing gear may not be lowered
         */
    }


    private void handleAircraftFire(){
        /*
        TODO Aircraft Fires: There are three fire extinguishers in aircraft.
1. A crewmember must be immediately detailed to fight the fire.
2. That crewmember cannot perform any other duties.
3. Aircraft may be depressurized prior to fighting fire.
4. Roll on Table 7-12 to determine if fire is extinguished.
5. Two attempts made be made in Waist and Utility if a second extinguisher is present
6. If fire in Nose or Nav/Radio is not extinguished on first attempt, crew must bail out.
7. If fire not extinguished after one attempt or two in Waist or Utility, the aircraft may enter next
zone but then crew must bail out.
         */
    }
}
