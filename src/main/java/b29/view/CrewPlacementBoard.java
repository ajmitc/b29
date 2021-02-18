package b29.view;

import b29.Model;
import b29.game.bomber.Bomber;
import b29.game.bomber.CrewPosition;
import b29.game.crew.CrewMember;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class CrewPlacementBoard extends JPanel {
    private static final String PLACEMENT_IMAGE_NAME = "Crew Placement Board.png";
    private static final String ENGINEER_IMAGE_NAME = "Engineer.png";
    private static final String BOMBARDIER_IMAGE_NAME = "Bombardier.png";
    private static final String PILOT_IMAGE_NAME = "Pilot.png";
    private static final String COPILOT_IMAGE_NAME = "Copilot.png";
    private static final String CFC_IMAGE_NAME = "Fire Controller.png";
    private static final String LEFT_GUNNER_IMAGE_NAME = "Left Gunner.png";
    private static final String RIGHT_GUNNER_IMAGE_NAME = "Right Gunner.png";
    private static final String NAVIGATOR_IMAGE_NAME = "Navigator.png";
    private static final String RADAR_OPERATOR_IMAGE_NAME = "Radar.png";
    private static final String RADIO_OPERATOR_IMAGE_NAME = "Radio.png";
    private static final String TAIL_GUNNER_IMAGE_NAME = "Tail Gunner.png";
    private static final String FIRE_EXTINGUISHER_IMAGE_NAME = "Fire Extinguisher.png";

    private static final Point ENGINEER_COORD = new Point(362, 212);
    private static final Point BOMBARDIER_COORD = new Point(323, 89);
    private static final Point PILOT_COORD = new Point(284, 151);
    private static final Point COPILOT_COORD = new Point(362, 151);
    private static final Point CFC_COORD = new Point(323, 565);
    private static final Point LEFT_GUNNER_COORD = new Point(284, 494);
    private static final Point RIGHT_GUNNER_COORD = new Point(362, 494);
    private static final Point TAIL_GUNNER_COORD = new Point(320, 798);
    private static final Point NAVIGATOR_COORD = new Point(284, 212);
    private static final Point RADAR_OPERATOR_COORD = new Point(323, 625);
    private static final Point RADIO_OPERATOR_COORD = new Point(323, 275);

    private static final Point FIRE_EXTINGUISHER_FWD_COORD = new Point(30, 111);
    private static final Point FIRE_EXTINGUISHER_REAR_COORD = new Point(30, 557);
    private static final Point FIRE_EXTINGUISHER_TAIL_COORD = new Point(30, 763);

    private static final Point FWD_BOMB_BAY_COORD = new Point(323, 350);
    private static final Point AFT_BOMB_BAY_COORD = new Point(323, 420);

    private static final int TOKEN_WIDTH = 50;

    private Model model;
    private View view;
    private BufferedImage boardImage;

    private Map<CrewPosition, BufferedImage> crewPositionImages = new HashMap<>();
    private Map<CrewPosition, Point> crewPositionCoords = new HashMap<>();

    private BufferedImage bombsImage;

    private int mx, my;

    public CrewPlacementBoard(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        boardImage = ImageUtil.get(PLACEMENT_IMAGE_NAME);
        setPreferredSize(new Dimension(boardImage.getWidth(), boardImage.getHeight()));

        crewPositionImages.put(CrewPosition.ENGINEER, ImageUtil.get(ENGINEER_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.BOMBARDIER, ImageUtil.get(BOMBARDIER_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.PILOT, ImageUtil.get(PILOT_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.COPILOT, ImageUtil.get(COPILOT_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.CFC_CONTROLLER, ImageUtil.get(CFC_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.LEFT_GUNNER, ImageUtil.get(LEFT_GUNNER_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.RIGHT_GUNNER, ImageUtil.get(RIGHT_GUNNER_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.TAIL_GUNNER, ImageUtil.get(TAIL_GUNNER_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.NAVIGATOR, ImageUtil.get(NAVIGATOR_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.RADAR_OPERATOR, ImageUtil.get(RADAR_OPERATOR_IMAGE_NAME));
        crewPositionImages.put(CrewPosition.RADIO_OPERATOR, ImageUtil.get(RADIO_OPERATOR_IMAGE_NAME));

        crewPositionCoords.put(CrewPosition.ENGINEER, ENGINEER_COORD);
        crewPositionCoords.put(CrewPosition.BOMBARDIER, BOMBARDIER_COORD);
        crewPositionCoords.put(CrewPosition.PILOT, PILOT_COORD);
        crewPositionCoords.put(CrewPosition.COPILOT, COPILOT_COORD);
        crewPositionCoords.put(CrewPosition.CFC_CONTROLLER, CFC_COORD);
        crewPositionCoords.put(CrewPosition.LEFT_GUNNER, LEFT_GUNNER_COORD);
        crewPositionCoords.put(CrewPosition.RIGHT_GUNNER, RIGHT_GUNNER_COORD);
        crewPositionCoords.put(CrewPosition.TAIL_GUNNER, TAIL_GUNNER_COORD);
        crewPositionCoords.put(CrewPosition.NAVIGATOR, NAVIGATOR_COORD);
        crewPositionCoords.put(CrewPosition.RADAR_OPERATOR, RADAR_OPERATOR_COORD);
        crewPositionCoords.put(CrewPosition.RADIO_OPERATOR, RADIO_OPERATOR_COORD);

        bombsImage = ImageUtil.get("Bombs.png", TOKEN_WIDTH);

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
        g.drawImage(boardImage, 0, 0, null);

        Bomber bomber = model.getGame().getBomber();

        for (CrewMember crewMember: bomber.getCrew()){
            BufferedImage image = crewPositionImages.get(crewMember.getDefaultCrewPosition());
            Point coord = crewPositionCoords.get(crewMember.getCrewPosition());
            g.drawImage(image, coord.x, coord.y, null);
        }

        g.drawImage(ImageUtil.get(FIRE_EXTINGUISHER_IMAGE_NAME), FIRE_EXTINGUISHER_FWD_COORD.x, FIRE_EXTINGUISHER_FWD_COORD.y, null);
        g.drawImage(ImageUtil.get(FIRE_EXTINGUISHER_IMAGE_NAME), FIRE_EXTINGUISHER_REAR_COORD.x, FIRE_EXTINGUISHER_REAR_COORD.y, null);
        g.drawImage(ImageUtil.get(FIRE_EXTINGUISHER_IMAGE_NAME), FIRE_EXTINGUISHER_TAIL_COORD.x, FIRE_EXTINGUISHER_TAIL_COORD.y, null);

        if (bomber.isCarryingBombs()){
            g.drawImage(bombsImage, FWD_BOMB_BAY_COORD.x, FWD_BOMB_BAY_COORD.y, null);
            g.drawImage(bombsImage, AFT_BOMB_BAY_COORD.x, AFT_BOMB_BAY_COORD.y, null);
        }

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 80, 30);
        g.setColor(Color.BLACK);
        g.drawString(mx + ", " + my, 20, 20);
    }
}
