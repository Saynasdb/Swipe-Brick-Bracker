import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public
class Paddle extends Thing {
    boolean crzy = false;
    private double dx;

    public
    Paddle() {
        initialize();
    }

    private
    void initialize() {
        image = new ImageIcon("src/main/resources/paddle.png").getImage();
        getDimensions();
        resetPosition();

    }

    public
    Timer changeSize(int mode, PlayPanel playPanel, Bonus bonus) {
        // 0 short 1 long
        Timer timer = new Timer(10000, new ActionListener() {


            @Override
            public
            void actionPerformed(ActionEvent arg0) {
                image = new ImageIcon("src/main/resources/paddle.png").getImage();
                getDimensions();
                playPanel.activeBonuses.remove(bonus);
            }
        });
        timer.setRepeats(false);
        timer.start();

        if (mode == 0) {
            image = new ImageIcon("src/main/resources/paddle_short.png").getImage();
        } else {
            image = new ImageIcon("src/main/resources/paddle_long.png").getImage();
        }
        getDimensions();

        return timer;
    }

    private
    void resetPosition() {
        x = Constants.DEFAULT_PADDLE_X;
        y = Constants.DEFAULT_PADDLE_Y;
    }

    void move() {
        x += dx;
        if (x <= 0) {
            x = 0;
        }
        if (x >= Constants.WIDTH - width - 14) {
            x = Constants.WIDTH - width - 14;
        }
    }

    void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();


        if (this.isCrzy()) {
            if (key == KeyEvent.VK_LEFT) {
                dx = 3;
            }
            if (key == KeyEvent.VK_RIGHT) {
                dx = -3;
            }
        } else {
            if (key == KeyEvent.VK_LEFT) {
                dx = -3;
            }
            if (key == KeyEvent.VK_RIGHT) {
                dx = 3;
            }
        }
    }

    void keyReleased(KeyEvent e) {

        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    public
    Timer crazy(PlayPanel playPanel, Bonus bonus) {
        Paddle paddle = this;
        Timer timer = new Timer(10000, new ActionListener() {

            @Override

            public
            void actionPerformed(ActionEvent arg0) {

                paddle.setCrzy(false);
                playPanel.activeBonuses.remove(bonus);

            }
        });
        timer.setRepeats(false);
        timer.start();
        paddle.setCrzy(true);
        return timer;
    }

    public
    boolean isCrzy() {
        return crzy;
    }

    public
    Paddle setCrzy(boolean crzy) {
        this.crzy = crzy;
        return this;
    }

    @Override
    public
    String toString() {
        return
                x +
                        "," + y
                ;
    }
}
