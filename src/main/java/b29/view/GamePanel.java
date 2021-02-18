package b29.view;

import b29.Model;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {
    private Model model;
    private View view;

    private CrewPlacementBoard crewPlacementBoard;
    private JapaneseFighterAttackBoard japaneseFighterAttackBoard;

    public GamePanel(Model model, View view){
        super(new BorderLayout());
        this.model = model;
        this.view = view;

        crewPlacementBoard = new CrewPlacementBoard(model, view);
        japaneseFighterAttackBoard = new JapaneseFighterAttackBoard(model, view);

        add(japaneseFighterAttackBoard, BorderLayout.CENTER);
        add(crewPlacementBoard, BorderLayout.EAST);
    }

    public void refresh(){
        crewPlacementBoard.repaint();
        japaneseFighterAttackBoard.repaint();
    }
}
