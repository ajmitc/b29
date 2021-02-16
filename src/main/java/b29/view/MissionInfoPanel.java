package b29.view;

import b29.Model;

import javax.swing.*;
import java.awt.*;

public class MissionInfoPanel extends JPanel {
    private Model model;
    private View view;

    private JLabel lblMissionNumber = new JLabel("Mission #");
    private JLabel lblMissionNumberValue = new JLabel("");
    private JLabel lblMissionTimeOfDay = new JLabel("Mission TOD");
    private JLabel lblMissionTimeOfDayValue = new JLabel("DAY");
    private JLabel lblMissionAltitude = new JLabel("Mission Altitude");
    private JLabel getLblMissionAltitudeValue = new JLabel("HI");
    private JLabel lblTarget = new JLabel("Target");
    private JLabel lblTargetValue = new JLabel("");
    private JLabel lblTargetType = new JLabel("Target Type");
    private JLabel lblTargetTypeValue = new JLabel("");
    private JLabel lblEscortAvailable = new JLabel("Escort Available");
    private JLabel getLblEscortAvailableValue = new JLabel("Unknown");
    private JLabel lblEscortRendezvous = new JLabel("Escort Rendezvous");
    private JLabel getLblEscortRendezvousValue = new JLabel("No");
    private JLabel lblFormationPosition = new JLabel("Formation Position");
    private JLabel getLblFormationPositionValue = new JLabel("Unknown");
    private JLabel lblSquadronPosition = new JLabel("Squadron Position");
    private JLabel getLblSquadronPositionValue = new JLabel("Unknown");

    public MissionInfoPanel(Model model, View view){
        super();
        this.model = model;
        this.view = view;

        new GridBagLayoutHelper(this, true)
                .setAnchor(GridBagConstraints.NORTHWEST)
                .setFill(GridBagConstraints.BOTH)
                .add(lblMissionNumber)
                .add(lblMissionNumberValue)
                .nextRow()
                .add(lblMissionTimeOfDay)
                .add(lblMissionTimeOfDayValue)
                .nextRow()
                .add(lblMissionAltitude)
                .add(getLblMissionAltitudeValue)
                .nextRow()
                .add(lblTarget)
                .add(lblTargetValue)
                .nextRow()
                .add(lblTargetType)
                .add(lblTargetTypeValue)
                .nextRow()
                .add(lblFormationPosition)
                .add(getLblFormationPositionValue)
                .nextRow()
                .add(lblSquadronPosition)
                .add(getLblSquadronPositionValue)
                .nextRow()
                .add(lblEscortAvailable)
                .add(getLblEscortAvailableValue)
                .nextRow()
                .add(lblEscortRendezvous)
                .add(getLblEscortRendezvousValue)
                .nextRow()
                ;
    }
}
