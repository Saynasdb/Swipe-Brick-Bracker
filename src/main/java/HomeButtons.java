import javax.swing.*;
import java.awt.*;

public
class HomeButtons extends JPanel {
    Button newGameButton;
    Button scoresButton;
    Button exitButton;
    Button pauseButton;
    Button settingsButton;

    GridBagConstraints constraints;

    HomeButtons() {
        newGameButton = new Button("New Game");
        pauseButton = new Button("Pause Game");
        settingsButton = new Button("Settings");



        scoresButton = new Button("Scores");
        exitButton = new Button("Exit");
        setFocusable(false);

        constraints = new GridBagConstraints();
        GridBagLayout gridBagLayout = new GridBagLayout();
        setLayout(gridBagLayout);
        constraints.weightx = 0.25;
        constraints.weighty = 1;
        constraints.gridy = 0;
        constraints.gridx = 0;
        this.add(newGameButton, constraints);
        constraints.gridx = 1;

        constraints.gridx = 2;
        this.add(scoresButton, constraints);
        constraints.gridx = 4;
        this.add(settingsButton, constraints);


    }
}
