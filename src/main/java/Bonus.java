import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public
class Bonus extends Thing {
    int dy = 2;
    int type;


    public Bonus(int type) {
        this.type = type;

    }

    void move() {

        y += dy;

    }

    public void act(PlayPanel playPanel, boolean random) {
        Timer tempTimer = null;
        LinkedList<Ball> balls = playPanel.balls;

        int cases = this.getType();
        if (random) cases = ThreadLocalRandom.current().nextInt(1, 8);

        switch (cases) {
            case 1:

                Timer timer = new Timer(500, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        playPanel.isBlinking = !playPanel.isBlinking; // Toggle the blinking state
                        playPanel.repaint(); // Repaint the panel to update the color
                    }
                });
                timer.setInitialDelay(0);
                timer.start();
                new Timer(10000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        timer.stop(); // Stop the blinking timer
                        playPanel.isBlinking = false; // Reset the blinking state
                        playPanel.repaint(); // Repaint the panel to show the default color
                    }
                }).start();
                break;
            case 2:
                playPanel.isShaking=true;
                Timer timer_2 = new Timer(50, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (playPanel.shake && playPanel.shakingRatio < 1.1)
                            playPanel.shakingRatio += 0.1;
                        if (playPanel.shakingRatio >= 1.1)
                            playPanel.shake = !playPanel.shake; // Toggle the blinking state

                        if (!playPanel.shake && playPanel.shakingRatio > 0.5) playPanel.shakingRatio -= 0.1;
                        if (playPanel.shakingRatio <= 0.5)
                            playPanel.shake = !playPanel.shake; // Toggle the blinking state


                        playPanel.repaint(); // Repaint the panel to update the color
                    }
                });
                timer_2.setInitialDelay(0);
                timer_2.start();
                new Timer(10000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        timer_2.stop(); // Stop the blinking timer
                        playPanel.isShaking = false; // Reset the blinking state
                        playPanel.shakingRatio=1;
                        playPanel.repaint(); // Repaint the panel to show the default color
                    }
                }).start();
                break;

            case 3:
                for (Ball ball : balls) {
                    playPanel.timers.add(ball.fire(playPanel, this));
                }

                break;
            case 4:
                for (Ball ball : balls) {

                    playPanel.timers.add(ball.tempChangeSpeed(1.01, playPanel, this));
                }

            case 5:

                playPanel.remainingBalls.add(new Ball(playPanel.lastBallX, playPanel.lastBallY));



                break;
            case 6:
                playPanel.isCrazy=true;
                new Timer(10000, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        playPanel.isCrazy=false; // Reset the blinking state
                        playPanel.repaint(); // Repaint the panel to show the default color
                    }
                }).start();

                break;


        }
        if (tempTimer != null) {
            playPanel.timers.add(tempTimer);
            tempTimer.setInitialDelay(1);
            tempTimer.setRepeats(false);
            tempTimer.start();
        }
    }

    Ellipse2D getCircle() {
        return new Ellipse2D.Double(x, y, image.getWidth(null), image.getHeight(null));

    }

    public int getType() {
        return type;
    }

    public Bonus setType(int type) {
        this.type = type;
        return this;
    }


    @Override
    public String toString() {
        return
                type +
                        "," + x +
                        "," + y
                ;
    }
}
