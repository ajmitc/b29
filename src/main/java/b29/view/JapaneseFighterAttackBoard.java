package b29.view;

import b29.Model;
import b29.game.Experience;
import b29.game.bomber.Altitude;
import b29.game.bomber.Bomber;
import b29.game.bomber.Pressurization;
import b29.game.mission.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JapaneseFighterAttackBoard extends JPanel {
    private static final String BOARD_IMAGE_NAME = "B-29_reprint_map_small.jpg";

    private static final Point ALTITUDE_LOW_COORD = new Point(1022, 56);
    private static final Point ALTITUDE_MED_COORD = new Point(1066, 56);
    private static final Point ALTITUDE_HIGH_COORD = new Point(1110, 56);
    private static final int TOKEN_WIDTH = 35;

    private static final Point WEATHER_GOOD_COORD = new Point(952, 592);
    private static final Point WEATHER_POOR_COORD = new Point(996, 592);
    private static final Point WEATHER_BAD_COORD = new Point(1038, 592);

    private static final Point CABIN_PRESSURE_ON_COORD = new Point(1022, 141);
    private static final Point CABIN_PRESSURE_OFF_COORD = new Point(1066, 141);
    private static final Point CABIN_PRESSURE_INOP_COORD = new Point(1110, 141);

    private static final Point ESCORT_COORD = new Point(475, 482);
    private static final Point VERTICAL_DIVE_COORD = new Point(636, 73);
    private static final Point VERTICAL_CLIMB_COORD = new Point(511, 731);
    private static final Map<FighterApproach, Map<FighterAltitude, Point>> APPROACH_POINTS = new HashMap<>();

    private static final List<Point> ZONE_COORDS = new ArrayList<>();

    private static final int FIGHTER_TOKEN_WIDTH = 55;

    private static final int MISSION_DETAILS_X = 20;
    private static final int MISSION_DETAILS_Y = 20;
    private static final int MISSION_DETAILS_Y_OFFSET = 20;

    static {
        ZONE_COORDS.add(new Point(1115, 591));  // Home Base
        ZONE_COORDS.add(new Point(1115, 638));  // Zone 1
        ZONE_COORDS.add(new Point(1115, 680));
        ZONE_COORDS.add(new Point(1115, 722));
        ZONE_COORDS.add(new Point(1115, 768));
        ZONE_COORDS.add(new Point(1115, 809));
        ZONE_COORDS.add(new Point(1115, 851));  // Zone 6
        ZONE_COORDS.add(new Point(1075, 851));
        ZONE_COORDS.add(new Point(1031, 851));
        ZONE_COORDS.add(new Point(988, 851));
        ZONE_COORDS.add(new Point(947, 851));  // Zone 10
        ZONE_COORDS.add(new Point(904, 851));
        ZONE_COORDS.add(new Point(858, 851));
        ZONE_COORDS.add(new Point(815, 851));
        ZONE_COORDS.add(new Point(771, 851));  // Zone 14

        APPROACH_POINTS.put(FighterApproach.AREA_12, new HashMap<>());
        APPROACH_POINTS.get(FighterApproach.AREA_12).put(FighterAltitude.HIGH, new Point(573, 35));
        APPROACH_POINTS.get(FighterApproach.AREA_12).put(FighterAltitude.LEVEL, new Point(573, 110));
        APPROACH_POINTS.get(FighterApproach.AREA_12).put(FighterAltitude.LOW, new Point(573, 179));

        APPROACH_POINTS.put(FighterApproach.AREA_1_30, new HashMap<>());
        APPROACH_POINTS.get(FighterApproach.AREA_1_30).put(FighterAltitude.HIGH, new Point(869, 152));
        APPROACH_POINTS.get(FighterApproach.AREA_1_30).put(FighterAltitude.LEVEL, new Point(805, 186));
        APPROACH_POINTS.get(FighterApproach.AREA_1_30).put(FighterAltitude.LOW, new Point(742, 220));

        APPROACH_POINTS.put(FighterApproach.AREA_3, new HashMap<>());
        APPROACH_POINTS.get(FighterApproach.AREA_3).put(FighterAltitude.HIGH, new Point(1100, 391));
        APPROACH_POINTS.get(FighterApproach.AREA_3).put(FighterAltitude.LEVEL, new Point(1032, 391));
        APPROACH_POINTS.get(FighterApproach.AREA_3).put(FighterAltitude.LOW, new Point(961, 391));

        APPROACH_POINTS.put(FighterApproach.AREA_6, new HashMap<>());
        APPROACH_POINTS.get(FighterApproach.AREA_6).put(FighterAltitude.HIGH, new Point(573, 838));
        APPROACH_POINTS.get(FighterApproach.AREA_6).put(FighterAltitude.LEVEL, new Point(573, 767));
        APPROACH_POINTS.get(FighterApproach.AREA_6).put(FighterAltitude.LOW, new Point(573, 697));

        APPROACH_POINTS.put(FighterApproach.AREA_9, new HashMap<>());
        APPROACH_POINTS.get(FighterApproach.AREA_9).put(FighterAltitude.HIGH, new Point(47, 391));
        APPROACH_POINTS.get(FighterApproach.AREA_9).put(FighterAltitude.LEVEL, new Point(118, 391));
        APPROACH_POINTS.get(FighterApproach.AREA_9).put(FighterAltitude.LOW, new Point(188, 391));

        APPROACH_POINTS.put(FighterApproach.AREA_10_30, new HashMap<>());
        APPROACH_POINTS.get(FighterApproach.AREA_10_30).put(FighterAltitude.HIGH, new Point(271, 148));
        APPROACH_POINTS.get(FighterApproach.AREA_10_30).put(FighterAltitude.LEVEL, new Point(333, 185));
        APPROACH_POINTS.get(FighterApproach.AREA_10_30).put(FighterAltitude.LOW, new Point(398, 218));
    }

    private Model model;
    private View view;
    private BufferedImage boardImage;
    private BufferedImage altitudeImage;
    private BufferedImage weatherImage;
    private BufferedImage cabinPressureImage;
    private BufferedImage cabinPressureInopImage;
    private Map<FighterType, BufferedImage> fighterImages = new HashMap<>();
    private Map<FighterType, Map<Experience, BufferedImage>> japFighterImages = new HashMap<>();
    private BufferedImage targetImage;
    private BufferedImage fwdUpperGunsImage;
    private BufferedImage fwdLowerGunsImage;
    private BufferedImage aftUpperGunsImage;
    private BufferedImage aftLowerGunsImage;
    private BufferedImage tailGunsImage;
    private BufferedImage bomberDownImage;
    private BufferedImage bomberLeftImage;
    private BufferedImage bomberRightImage;
    private BufferedImage bomberUpImage;

    private int mx, my;

    public JapaneseFighterAttackBoard(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.get(BOARD_IMAGE_NAME, 1200);
        setPreferredSize(new Dimension(boardImage.getWidth(), boardImage.getHeight()));

        altitudeImage = ImageUtil.get("Altitude.png", TOKEN_WIDTH);
        weatherImage = ImageUtil.get("Weather.png", TOKEN_WIDTH);
        cabinPressureImage = ImageUtil.get("Cabin Pressure.png", TOKEN_WIDTH);
        cabinPressureInopImage = ImageUtil.get("Cabin Pressure Inop.png", TOKEN_WIDTH);

        fighterImages.put(FighterType.FIGHTER_F6F_HELLCAT, ImageUtil.get("F6F.png", TOKEN_WIDTH));
        fighterImages.put(FighterType.FIGHTER_P38, ImageUtil.get("P-38.png", TOKEN_WIDTH));
        fighterImages.put(FighterType.FIGHTER_P51, ImageUtil.get("P-51.png", TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_ZEKE, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_ZEKE).put(Experience.GREEN,   ImageUtil.get("A6M5 Zero - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_ZEKE).put(Experience.AVERAGE, ImageUtil.get("A6M5 Zero - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_ZEKE).put(Experience.VETERAN, ImageUtil.get("A6M5 Zero - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_JACK, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_JACK).put(Experience.GREEN,   ImageUtil.get("J2M Raiden - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_JACK).put(Experience.AVERAGE, ImageUtil.get("J2M Raiden - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_JACK).put(Experience.VETERAN, ImageUtil.get("J2M Raiden - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_OSCAR, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_OSCAR).put(Experience.GREEN,   ImageUtil.get("Ki-43 Hayabusa - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_OSCAR).put(Experience.AVERAGE, ImageUtil.get("Ki-43 Hayabusa - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_OSCAR).put(Experience.VETERAN, ImageUtil.get("Ki-43 Hayabusa - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_TOJO, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_TOJO).put(Experience.GREEN,   ImageUtil.get("Ki-44 Shoki - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_TOJO).put(Experience.AVERAGE, ImageUtil.get("Ki-44 Shoki - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_TOJO).put(Experience.VETERAN, ImageUtil.get("Ki-44 Shoki - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_NICK, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_NICK).put(Experience.GREEN,   ImageUtil.get("Ki-45 Toryu - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_NICK).put(Experience.AVERAGE, ImageUtil.get("Ki-45 Toryu - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_NICK).put(Experience.VETERAN, ImageUtil.get("Ki-45 Toryu - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_TONY, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_TONY).put(Experience.GREEN,   ImageUtil.get("Ki-61 Hein - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_TONY).put(Experience.AVERAGE, ImageUtil.get("Ki-61 Hein - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_TONY).put(Experience.VETERAN, ImageUtil.get("Ki-61 Hein - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_FRANK, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_FRANK).put(Experience.GREEN,   ImageUtil.get("Ki-84 Hayate - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_FRANK).put(Experience.AVERAGE, ImageUtil.get("Ki-84 Hayate - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_FRANK).put(Experience.VETERAN, ImageUtil.get("Ki-84 Hayate - Ace.png", FIGHTER_TOKEN_WIDTH));

        japFighterImages.put(FighterType.FIGHTER_GEORGE, new HashMap<>());
        japFighterImages.get(FighterType.FIGHTER_GEORGE).put(Experience.GREEN,   ImageUtil.get("N1K2 Shiden - Grn.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_GEORGE).put(Experience.AVERAGE, ImageUtil.get("N1K2 Shiden - Avg.png", FIGHTER_TOKEN_WIDTH));
        japFighterImages.get(FighterType.FIGHTER_GEORGE).put(Experience.VETERAN, ImageUtil.get("N1K2 Shiden - Ace.png", FIGHTER_TOKEN_WIDTH));

        bomberUpImage    = ImageUtil.get("Zone.png", TOKEN_WIDTH);
        bomberRightImage = ImageUtil.rotateImageByDegrees(bomberUpImage, 90);
        bomberDownImage  = ImageUtil.rotateImageByDegrees(bomberUpImage, 180);
        bomberLeftImage  = ImageUtil.rotateImageByDegrees(bomberUpImage, 270);

        targetImage = ImageUtil.get("Target.png", TOKEN_WIDTH);

        fwdUpperGunsImage = ImageUtil.get("Target Forward Upper.png", TOKEN_WIDTH);
        fwdLowerGunsImage = ImageUtil.get("Target Forward Lower.png", TOKEN_WIDTH);
        aftUpperGunsImage = ImageUtil.get("Target Aft Upper.png", TOKEN_WIDTH);
        aftLowerGunsImage = ImageUtil.get("Target Aft Lower.png", TOKEN_WIDTH);
        tailGunsImage     = ImageUtil.get("Target Tail Gunner.png", TOKEN_WIDTH);



        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mx = e.getX();
                my = e.getY();
                repaint();
            }
        });
    }

    public void paintComponent(Graphics graphics){
        Graphics2D g = (Graphics2D) graphics;

        Mission mission = model.getGame().getMission();
        Bomber bomber = model.getGame().getBomber();

        g.drawImage(boardImage, 0, 0, null);

        Point coord = mission.getWeather() == Weather.GOOD? WEATHER_GOOD_COORD:
                mission.getWeather() == Weather.POOR? WEATHER_POOR_COORD: WEATHER_BAD_COORD;
        g.drawImage(weatherImage, coord.x, coord.y, null);

        coord = bomber.getAltitude() == Altitude.HI? ALTITUDE_HIGH_COORD:
                bomber.getAltitude() == Altitude.MED? ALTITUDE_MED_COORD: ALTITUDE_LOW_COORD;
        g.drawImage(altitudeImage, coord.x, coord.y, null);

        coord = bomber.getPressurizationSystem() == Pressurization.ON? CABIN_PRESSURE_ON_COORD:
                bomber.getPressurizationSystem() == Pressurization.OFF? CABIN_PRESSURE_OFF_COORD: CABIN_PRESSURE_INOP_COORD;
        g.drawImage(bomber.getPressurizationSystem() == Pressurization.INOP? cabinPressureInopImage: cabinPressureImage, coord.x, coord.y, null);

        // Draw bomber token
        coord = ZONE_COORDS.get(mission.getZone());
        Image bomberImage = bomberDownImage;
        if (mission.getDirection() == Direction.TO_TARGET){
            if (mission.getZone() >= 6){
                bomberImage = bomberLeftImage;
            }
        }
        else {
            if (mission.getZone() > 6)
                bomberImage = bomberRightImage;
            else
                bomberImage = bomberUpImage;
        }
        g.drawImage(bomberImage, coord.x, coord.y, null);

        // Draw target token
        coord = ZONE_COORDS.get(mission.getTargetZone());
        g.drawImage(targetImage, coord.x, coord.y, null);

        if (mission.getEscort() != null){
            g.drawImage(fighterImages.get(mission.getEscort()), ESCORT_COORD.x, ESCORT_COORD.y, null);
        }

        for (JapaneseFighter japaneseFighter: mission.getJapaneseFighters()){
            BufferedImage image = japFighterImages.get(japaneseFighter.getFighterInfo().fighterType).get(japaneseFighter.getExperience());
            if (japaneseFighter.getFighterInfo().approach == FighterApproach.VERTICAL_CLIMB)
                coord = VERTICAL_CLIMB_COORD;
            else if (japaneseFighter.getFighterInfo().approach == FighterApproach.VERTICAL_DIVE)
                coord = VERTICAL_DIVE_COORD;
            else
                coord = APPROACH_POINTS.get(japaneseFighter.getFighterInfo().approach).get(japaneseFighter.getFighterInfo().altitude);
            g.drawImage(image, coord.x, coord.y, null);
        }

        // Draw Mission Details
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 250, 300);
        g.setColor(Color.BLACK);
        int x = MISSION_DETAILS_X;
        int y = MISSION_DETAILS_Y;
        g.drawString("Mission #" + mission.getMissionNumber() + " (This bomber: #" + (bomber.getNumMissionsCompleted() + 1) + ")", x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Mission TOD: " + (mission.getMissionTimeOfDay() != null? mission.getMissionTimeOfDay().name(): ""), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Mission Altitude: " + (mission.getMissionAltitude() != null? mission.getMissionAltitude().name(): ""), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Target: " + (mission.getTarget() != null? mission.getTarget().name(): ""), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Target Type: " + (mission.getTargetType() != null? mission.getTargetType().name(): ""), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Escort Available: " + (mission.isEscortAvailable()? "Yes": "No"), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Escort Rendezvous: " + (mission.getEscort() != null? "Yes": "No"), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Formation Position: " + (mission.getFormationPosition() != null? mission.getFormationPosition().name(): ""), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Squadron Position: " + (mission.getSquadronPosition() != null? mission.getSquadronPosition().name(): ""), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("In Formation? " + (!mission.isOutOfFormation()? "Yes": "No"), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Main Fuel: " + bomber.getFuelLeft(), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Aux Fwd Fuel: " + bomber.getAuxFwdFuelLeft(), x, y);
        y += MISSION_DETAILS_Y_OFFSET;
        g.drawString("Aux Aft Fuel: " + bomber.getAuxAftFuelLeft(), x, y);

        g.setColor(Color.WHITE);
        g.fillRect(120, 0, 80, 20);
        g.setColor(Color.BLACK);
        g.drawString(mx + ", " + my, 120, 20);
    }
}
