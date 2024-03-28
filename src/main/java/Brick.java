import javax.swing.*;
import java.util.concurrent.ThreadLocalRandom;

public
class Brick extends Thing {
    int blood = 1;
    int initialBlood = 1;
    int type = 1;
    boolean visible = true;


    public Brick(int type, int x, int y, int initialBlood) {
        this.x = x;
        this.y = y;
        this.type = type;

        switch (type) {
            case 0:
                this.initialBlood = initialBlood;
                this.blood = initialBlood;

                this.image = new ImageIcon("src/main/resources/brick.png").getImage();
                getDimensions();
                break;
            case 1:
                this.initialBlood = initialBlood;
                this.blood = initialBlood;
                this.image = new ImageIcon("src/main/resources/brick_light_dance.png").getImage();
                getDimensions();
                break;
            case 2:
                this.initialBlood = initialBlood;
                this.blood = initialBlood;
                this.image = new ImageIcon("src/main/resources/brick_earthquake.png").getImage();
                getDimensions();
                break;
            case 3:
                this.image = new ImageIcon("src/main/resources/bonus_fireball.png").getImage();
                getDimensions();
                break;
            case 4:
                this.image = new ImageIcon("src/main/resources/bonus_fastball.png").getImage();
                getDimensions();
                break;
            case 5:
                this.image = new ImageIcon("src/main/resources/bonus_multiball.png").getImage();
                getDimensions();

                break;

            case 6:
                this.image = new ImageIcon("src/main/resources/bonus_crazy.png").getImage();
                getDimensions();

                break;

        }


    }

    public int getBlood() {
        return blood;
    }

    public Brick setBlood(int blood) {
        this.blood = blood;
        return this;
    }

    public int getType() {
        return type;
    }

    public Brick setType(int type) {
        this.type = type;
        return this;
    }


    // 1 glass 2 wood 3invisible 4 blink 5 bonus

    public void brakeWood() {
        this.image = new ImageIcon("src/main/resources/brick_wood_1.png").getImage();
        //getDimensions();
        // this.blood=1;
    }

    public void bleed(Ball ball) {
        int blood;
        blood = this.getBlood() - ball.getPower();
        this.setBlood(Math.max(blood, 0));
    }

    public boolean isVisible() {
        return visible;
    }

    public Brick setVisible(boolean visible) {
        this.visible = visible;
        return this;
    }

    public void intersects(Ball ball, PlayPanel playPanel, long currentTime) {
        if (this.getBlood() != 0 && this.isVisible()) {
            if (ball.getCircle().intersects(this.getRectangle())) {
//                playPanel.score++;

                this.bleed(ball);
                ball.setDy(ball.getDy() * (-1));

                if (this.blood == 0) {
                    playPanel.score += this.initialBlood - 0.02 * (System.nanoTime() - currentTime) / 1000000000;
                    if (this.getType() == 1) {
                        Bonus bonus;
                        bonus = new Bonus(1);
                        bonus.setX(this.getX());
                        bonus.setY(this.getY());

                        playPanel.floatingBonuses.add(bonus);
                    }

                    if (this.getType() == 2) {
                        Bonus bonus;
                        bonus = new Bonus(2);
                        bonus.setX(this.getX());
                        bonus.setY(this.getY());

                        playPanel.floatingBonuses.add(bonus);

//                        this.brakeWood();

                    }
                    if (this.getType() == 3) {
                        Bonus bonus;
                        bonus = new Bonus(3);
                        bonus.setX(this.getX());
                        bonus.setY(this.getY());

                        playPanel.floatingBonuses.add(bonus);

                    }
                    if (this.getType() == 4) {
                        Bonus bonus;
                        bonus = new Bonus(4);
                        bonus.setX(this.getX());
                        bonus.setY(this.getY());

                        playPanel.floatingBonuses.add(bonus);

                    }
                    if (this.getType() == 5) {
                        Bonus bonus;
                        bonus = new Bonus(5);
                        bonus.setX(this.getX());
                        bonus.setY(this.getY());

                        playPanel.floatingBonuses.add(bonus);

                    }
                    if (this.getType() == 6) {
                        Bonus bonus;
                        bonus = new Bonus(6);
                        bonus.setX(this.getX());
                        bonus.setY(this.getY());

                        playPanel.floatingBonuses.add(bonus);

                    }

                }


            }
        }
    }

    @Override
    public String toString() {
        return
                blood +
                        "," + type +
                        "," + visible +
                        "," + x +
                        "," + y
                ;
    }
}
