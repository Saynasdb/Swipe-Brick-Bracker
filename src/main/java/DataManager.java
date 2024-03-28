import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;


public
class DataManager {

    public static
    Path dataSaver(PlayPanel playPanel, Path path) {
        if (path != null) {
            JOptionPane.showMessageDialog(playPanel, "you Are saving a recent loaded game", "Saving Loaded Game", JOptionPane.PLAIN_MESSAGE);
        }

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose a directory to save your file: ");


        int returnValue = jfc.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = Paths.get(selectedFile.getPath());


        } else {
            return null;
        }
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            writer.write("name");
            writer.newLine();
            writer.write(playPanel.name);
            writer.newLine();
            writer.write("blood");
            writer.newLine();
            writer.write(String.valueOf(playPanel.blood));
            writer.newLine();
            writer.write(playPanel.name);
            writer.newLine();
            writer.write("score");
            writer.newLine();
            writer.write(String.valueOf(playPanel.score));
            writer.newLine();
            writer.write("activeBonuses");
            writer.newLine();
            for (Bonus bonus : playPanel.activeBonuses) {
                writer.write(bonus.toString());
                writer.newLine();
            }
            writer.write("floatingBonuses");
            writer.newLine();
            for (Bonus bonus : playPanel.floatingBonuses) {
                writer.write(bonus.toString());
                writer.newLine();
            }
            writer.write("balls");
            writer.newLine();
            for (Ball ball : playPanel.balls) {
                writer.write(ball.toString());
                writer.newLine();
            }

            writer.write("bricks");
            writer.newLine();
            for (Brick brick : playPanel.bricks) {
                if (brick.blood > 0) {
                    writer.write(brick.toString());
                    writer.newLine();
                }
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return path;
    }

    public static
    Path dataLoader(PlayPanel playPanel) {

        Path path = null;
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        jfc.setDialogTitle("Choose your file: ");

        int returnValue = jfc.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            path = Paths.get(selectedFile.getPath());


        } else {
            return null;
        }
        playPanel.reset(null);
        boolean activeBonusMode = false;
        boolean floatingBonusMode = false;
        boolean ballsMode = false;
        boolean bricksMode = false;

        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("name")) {
                    playPanel.name = reader.readLine();
                    continue;
                }
                if (line.equals("blood")) {
                    playPanel.blood = Integer.parseInt(reader.readLine());
                    continue;
                }
                if (line.equals("score")) {
                    playPanel.score = Integer.parseInt(reader.readLine());
                    continue;
                }
                if (line.equals("activeBonuses")) {
                    activeBonusMode = true;
                    continue;
                }
                if (line.equals("floatingBonuses")) {
                    activeBonusMode = false;
                    floatingBonusMode = true;
                    continue;

                }
                if (line.equals("balls")) {
                    floatingBonusMode = false;
                    ballsMode = true;
                    continue;

                }

                if (line.equals("bricks")) {
                    bricksMode = true;
                    continue;
                }
                String[] dataString = line.split(",");

                if (activeBonusMode) {
                    Bonus tempBonus;
                    tempBonus = new Bonus(Integer.parseInt(dataString[0]));
                    tempBonus.setY(0);
                    tempBonus.setX(0);

                    playPanel.activeBonuses.add(tempBonus);
                    continue;
                }
                if (floatingBonusMode) {
                    Bonus tempBonus;
                    tempBonus = new Bonus(Integer.parseInt(dataString[0]));
                    tempBonus.setX(Integer.parseInt(dataString[1]));
                    tempBonus.setY(Integer.parseInt(dataString[2]));
                    playPanel.floatingBonuses.add(tempBonus);
                    continue;
                }
                if (ballsMode) {
                    Ball tempBall;
                    tempBall = new Ball();
                    tempBall.setDx(Double.parseDouble(dataString[0]));
                    tempBall.setDy(Double.parseDouble(dataString[1]));
                    tempBall.setPower(Integer.parseInt(dataString[2]));
                    tempBall.setX(Integer.parseInt(dataString[3]));
                    tempBall.setY(Integer.parseInt(dataString[4]));
                    playPanel.balls.add(tempBall);
                    continue;
                }


                if (bricksMode) {
                    Brick tempBrick;
                    tempBrick = new Brick(
                            Integer.parseInt(dataString[1]),
                            Integer.parseInt(dataString[3]),
                            Integer.parseInt(dataString[4]),
                            Integer.parseInt(dataString[5]));
                    tempBrick.setBlood(Integer.parseInt(dataString[0]));
                    tempBrick.setVisible(Boolean.parseBoolean(dataString[2]));
                    playPanel.bricks.add(tempBrick);
                    continue;
                }
            }
            for (Bonus bonus : new LinkedList <>(playPanel.activeBonuses)) {
                bonus.act(playPanel, false);
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        playPanel.path = path;
        return path;
    }

    public static
    int scoreSaver(LinkedList <PlayerScore> playerScores) {
        Path path = null;

        path = Paths.get("src/main/resources/scores.txt");

        try (BufferedWriter writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (PlayerScore playerScore : playerScores) {
                writer.write(playerScore.toString());
                writer.newLine();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 1;
    }

    public static
    int scoresLoader(LinkedList <PlayerScore> playerScores) {
        Path path = null;

        path = Paths.get("src/main/resources/scores.txt");

        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] dataString = line.split(",");
                DateTimeFormatter formatter=DateTimeFormatter.ofPattern(" yyyy-MM-dd'T'HH:mm:ss.SSSSSSSSS");
                LocalDateTime dateTime = LocalDateTime.parse(dataString[2], formatter);
                PlayerScore playerScore = new PlayerScore(dataString[0], Integer.parseInt(dataString[1]), dateTime);
                playerScores.add(playerScore);

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 1;
    }
}

