import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedList;

public
class MainFrame extends JFrame {

    JPanel poster;
    PlayPanel playPanel;
    HomeButtons homeButtons;
    ClickListener clickListener;
    GridBagConstraints c;
    private JButton newGameButton, gameHistoryButton, settingsButton, exitButton;
    private JFrame preparationFrame;
    private JTextField playerNameField;
    private JComboBox<String> difficultyComboBox;
    private JComboBox<Color> ballColorComboBox;
    private JButton startGameButton;
    private JLabel statusLabel;
    LinkedList<PlayerScore> scores;
    Image home;

    MainFrame() {
        super("Swipe Brick Bracker");
        this.setSize(Constants.WIDTH, Constants.HEIGHT + 60);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
        playPanel = new PlayPanel();

        clickListener = new ClickListener();
        homeButtons = new HomeButtons();
        scores = new LinkedList<>();
        this.setLocationRelativeTo(null);
        DataManager.scoresLoader(scores);

        poster = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(
                        RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(
                        RenderingHints.KEY_RENDERING,
                        RenderingHints.VALUE_RENDER_QUALITY);

                g2d.drawImage(home, 0, 0, Constants.WIDTH, Constants.HEIGHT, this);
                Toolkit.getDefaultToolkit().sync();
            }
        };

        poster.revalidate();
        home = new ImageIcon("src/main/resources/home.png").getImage();


        homeButtons.newGameButton.addActionListener(clickListener);
        homeButtons.pauseButton.addActionListener(clickListener);
        homeButtons.exitButton.addActionListener(clickListener);
        homeButtons.exitButton.addActionListener(clickListener);
        homeButtons.scoresButton.addActionListener(clickListener);
        homeButtons.settingsButton.addActionListener(clickListener);


        GridBagLayout gridBagLayout = new GridBagLayout();
        GridLayout gridLayout = new GridLayout();
        this.setLayout(gridBagLayout);
        c = new GridBagConstraints();


        c.weighty = 0.02;
        c.weightx = 1;
        c.gridy = 1;
        this.add(homeButtons, c);


        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.98;
        c.fill = GridBagConstraints.BOTH;
        this.add(poster, c);

        poster.setFocusable(false);
        this.setVisible(true);
    }

    class ClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == homeButtons.pauseButton) {

                if (playPanel.timer.isRunning()) {
                    playPanel.pause();
                    homeButtons.pauseButton.setText("Resume");

                    homeButtons.revalidate();

                } else {
                    playPanel.pause();
                    homeButtons.pauseButton.setText("Pause");

                    homeButtons.revalidate();

                }
            }
            if (e.getSource() == homeButtons.newGameButton) {
                openGamePreparation();
            }
            if (e.getSource() == homeButtons.settingsButton) {
                openSettings();
            }





            if(e.getSource()==homeButtons.scoresButton)

        {
            openGameHistory();
        }
            if(e.getSource()==homeButtons.exitButton)

        {


            MainPage.mainframe.remove(MainPage.mainframe.playPanel);
            playPanel = new PlayPanel();
            MainPage.mainframe.playPanel = playPanel;
            MainPage.mainframe.add(MainPage.mainframe.poster, MainPage.mainframe.c);
            MainPage.mainframe.revalidate();

            repaint();
            homeButtons.remove(homeButtons.pauseButton);
            homeButtons.constraints.gridx = 0;
            homeButtons.add(homeButtons.newGameButton, homeButtons.constraints);

            homeButtons.remove(homeButtons.exitButton);
            homeButtons.add(homeButtons.settingsButton,homeButtons.constraints);
            homeButtons.constraints.gridx = 3;
            MainPage.mainframe.revalidate();
            homeButtons.revalidate();
        }

    }

}

    private void openGamePreparation() {
        preparationFrame = new JFrame("Game Preparation");
        preparationFrame.setSize(400, 300);
        preparationFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel preparationPanel = new JPanel();
        preparationPanel.setLayout(new GridLayout(5, 2));

        JLabel playerNameLabel = new JLabel("Player Name:");
        preparationPanel.add(playerNameLabel);

        playerNameField = new JTextField();
        preparationPanel.add(playerNameField);

        JLabel difficultyLabel = new JLabel("Difficulty:");
        preparationPanel.add(difficultyLabel);

        String[] difficultyLevels = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficultyLevels);
        preparationPanel.add(difficultyComboBox);

        JLabel ballColorLabel = new JLabel("Ball Color:");
        preparationPanel.add(ballColorLabel);

        Color[] ballColors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.PINK, Color.CYAN, Color.MAGENTA, Color.WHITE};
        ballColorComboBox = new JComboBox<Color>(ballColors);
        preparationPanel.add(ballColorComboBox);

        startGameButton = new JButton("Start Game");
        startGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        preparationPanel.add(startGameButton);

        preparationFrame.add(preparationPanel);
        preparationFrame.setVisible(true);
    }

    public void startGame() {
        MainPage.mainframe.remove(MainPage.mainframe.poster);
        MainPage.mainframe.add(MainPage.mainframe.playPanel, MainPage.mainframe.c);

//                playPanel.name = JOptionPane.showInputDialog(MainPage.mainframe, "Enter your Name:", "Your Name", JOptionPane.PLAIN_MESSAGE);
//                playPanel.name = JOptionPane.showInputDialog(MainPage.mainframe, "Enter your Name:", "Your Name", JOptionPane.PLAIN_MESSAGE);
        playPanel.name = playerNameField.getText();
        playPanel.ballColor = (Color) ballColorComboBox.getSelectedItem();
        switch (difficultyComboBox.getSelectedIndex()) {
            case 0:
                playPanel.numberBricks = 5;
                playPanel.brickSpeed = 8;
                playPanel.brickNumberSpeed = 3;
                playPanel.randomLevel = 0.6;
//                playPanel.setParameters();
                break;
            case 1:
                playPanel.numberBricks = 7;
                playPanel.brickSpeed = 15;
                playPanel.brickNumberSpeed = 5;
                playPanel.randomLevel = 0.3;
                break;
            case 2:
                playPanel.numberBricks = 9;
                playPanel.brickSpeed = 27;
                playPanel.brickNumberSpeed = 7;
                playPanel.randomLevel = 0.2;
                break;
        }
        playPanel.initialize();
        playPanel.pause();

        homeButtons.remove(homeButtons.newGameButton);
        homeButtons.remove(homeButtons.settingsButton);
        homeButtons.constraints.gridx = 0;
        homeButtons.add(homeButtons.pauseButton, homeButtons.constraints);
        homeButtons.constraints.gridx = 4;
        homeButtons.add(homeButtons.exitButton, homeButtons.constraints);

        homeButtons.revalidate();
        MainPage.mainframe.revalidate();
        preparationFrame.dispose();
        playPanel.requestFocusInWindow();
        playPanel.pause();
        repaint();
    }

    private void openGameHistory() {
        scores.sort(Comparator.comparing(PlayerScore::getScore).reversed());

        String list = "";
        for (int i = 0; i < Math.min(6, scores.size()); i++) {
            PlayerScore playerScore = scores.get(i);
            list = list.concat(playerScore.getName() + "->" + playerScore.getScore() + "\n");
        }
        JFrame historyFrame = new JFrame("Game History");
        historyFrame.setSize(400, 300);
        historyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Use a JPanel with a vertical BoxLayout for variable number of history entries
        JPanel historyPanel = new JPanel();
        historyPanel.setLayout(new BoxLayout(historyPanel, BoxLayout.Y_AXIS));

        for (PlayerScore score : scores) {
            JLabel scoreLabel = new JLabel(score.getName() + ": " + score.getScore() + ":" + score.localDateTime.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd, yyyy HH:mm:ss")));
            historyPanel.add(scoreLabel);
        }


        // Wrap the historyPanel in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(historyPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        historyFrame.add(scrollPane); // Add the scrollPane instead of historyPanel
        historyFrame.setVisible(true);
    }
    private void openSettings() {
        JFrame settingsFrame = new JFrame("Settings");
        settingsFrame.setSize(400, 300);
        settingsFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new GridLayout(3, 1));

        JCheckBox aimingCheckBox = new JCheckBox("Enable Aiming");
        aimingCheckBox.setSelected(playPanel.aimingEnabled); // Default to enabled
        aimingCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // Enable/disable aiming based on checkbox state
                playPanel.aimingEnabled = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        JCheckBox musicThemeChechBox = new JCheckBox("Enable Music");
        musicThemeChechBox.setSelected(playPanel.musicEnabled); // Default to enabled
        musicThemeChechBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // Enable/disable music based on checkbox state
                playPanel.musicEnabled = e.getStateChange() == ItemEvent.SELECTED;
            }
        });
        JCheckBox saveInHistoryCheckBox = new JCheckBox("Save in History");
        saveInHistoryCheckBox.setSelected(playPanel.saveInHistory); // Default to enabled
        saveInHistoryCheckBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                // Enable/disable saving in history based on checkbox state
                playPanel.saveInHistory = e.getStateChange() == ItemEvent.SELECTED;
                System.out.println(playPanel.saveInHistory);
            }
        });
        settingsPanel.add(aimingCheckBox);
        settingsPanel.add(musicThemeChechBox);
        settingsPanel.add(saveInHistoryCheckBox);

        settingsFrame.add(settingsPanel);
        settingsFrame.setVisible(true);
    }


}


