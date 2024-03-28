import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;


public
class PlayPanel extends JPanel implements MouseMotionListener {
    public boolean saveInHistory;
    public boolean musicEnabled;
    String name = "";
    int score;
    long startTime;
    float shakingRatio = 1;

    int blood = 3;
    LinkedList<Brick> bricks;
    LinkedList<Bonus> floatingBonuses;
    boolean shake = false;
    LinkedList<Ball> balls;
    LinkedList<Ball> remainingBalls;
    LinkedList<Timer> timers = new LinkedList<>();
    LinkedList<Bonus> activeBonuses = new LinkedList<>();
    Path path;
    boolean isCrazy = false;
    Timer timer;
    Timer brickTimer;
    Timer invisibleBricksTimer;
    int level = 0;
    boolean shooting = false;
    boolean isBlinking = false;
    boolean isShaking = false;
    private int mouseX = 0; // current X position of the mouse
    private int mouseY = 0; // current Y position of the mouse
    boolean aimingEnabled = true;
    long passedTime=0;
    public int lastBallX = Constants.DEFAULT_BALL_X;
    public int lastBallY = Constants.DEFAULT_BALL_Y;
    public int ballWidth;
    public int ballHeight;
    public Color ballColor = Color.RED;
    public int numberBricks=5;
    public int brickSpeed=8;
    public int brickNumberSpeed=3;
    public double randomLevel=4;
    public CustomRandom customRandom;


    public PlayPanel() {

        addMouseMotionListener(this);
//        initialize();
    }

    public int getBlood() {
        return blood;
    }

    public PlayPanel setBlood(int blood) {
        this.blood = blood;
        return this;
    }

    public void bleed() {
        this.setBlood(this.getBlood() - 1);
    }
    public void initialize() {


        customRandom=new CustomRandom(randomLevel);
        startTime = System.nanoTime();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!shooting) {
                    shootBall(mouseX, mouseY);
                }
            }
        });
        setFocusable(true);
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setBackground(new Color(0xFFEBEB));

        balls = new LinkedList<>();
        remainingBalls = new LinkedList<>();
        floatingBonuses = new LinkedList<>();
        bricks = new LinkedList<>();

        Ball ball = new Ball(Constants.DEFAULT_BALL_X, Constants.DEFAULT_BALL_Y);
        balls.add(ball);
        ballHeight = ball.height;
        ballWidth = ball.width;

        timer = new Timer(5, new PlayProcess());
        timer.start();
        timers.add(timer);
        ActionListener brickAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                for (Brick brick : bricks) {
                    brick.setY(brick.getY() + 1);


                }
                if (bricks.isEmpty() || bricks.getLast().getY() > 90) {

                    for (int i = 0; i < 5; i++) {
                        Brick tempBrick;

                        double r =ThreadLocalRandom.current().nextDouble(1.0);


                        int type;
                        if(r>randomLevel){
                            type=ThreadLocalRandom.current().nextInt(-1,3);

                        }
                        else {
                            type=ThreadLocalRandom.current().nextInt(4, 6);
                        }
//                        type = customRandom.nextInt();
                        int brickX= type<=3 ? (i*116+16): i*116+ 46  ;
                        if(type==-1)continue;
                        tempBrick = new Brick(type, brickX, 40, ThreadLocalRandom.current().nextInt(1, level * brickNumberSpeed + 5));
                        bricks.add(tempBrick);

                    }

                }
            }
        };

        brickTimer = new Timer(10, brickAction);
        System.out.println(randomLevel);
        if (musicEnabled){
            SoundPlayer player = new SoundPlayer();
            player.playMusic("src/main/resources/music.wav");

        }


    }


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
        if (!isBlinking) {
            drawObjects(g2d);
        }
        Toolkit.getDefaultToolkit().sync();
    }

    private Point calculateEndPoint(int ballX, int ballY, int mouseX, int mouseY) {
        double minDistance = Double.MAX_VALUE;
        Point endPoint = new Point(mouseX, mouseY);

        // Check for intersection with each brick
        for (Brick brick : bricks) {
            if (brick.getBlood() == 0 || !brick.isVisible()) continue;
            Point intersection = getIntersectionPoint(ballX, ballY, mouseX, mouseY, brick);
            if (intersection == null) continue;
            double distance = intersection.distance(ballX, ballY);
            if (distance < minDistance) {
                minDistance = distance;
                endPoint = intersection;
            }
        }

        // Check for intersection with the panel edges
        if (endPoint.equals(new Point(mouseX, mouseY))) {
            endPoint = getBorderIntersection(ballX, ballY, mouseX, mouseY);
        }

        return endPoint;
    }

    private Point getBorderIntersection(int x1, int y1, int x2, int y2) {
        // Define the panel borders as infinite lines
        int panelLeft = 0;
        int panelRight = getWidth();
        int panelTop = 0;
        int panelBottom = getHeight();

        // Check for intersection with each of the four borders of the panel
        Point leftIntersect = getLineIntersection(x1, y1, x2, y2, panelLeft, panelTop, panelLeft, panelBottom);
        Point rightIntersect = getLineIntersection(x1, y1, x2, y2, panelRight, panelTop, panelRight, panelBottom);
        Point topIntersect = getLineIntersection(x1, y1, x2, y2, panelLeft, panelTop, panelRight, panelTop);
        Point bottomIntersect = getLineIntersection(x1, y1, x2, y2, panelLeft, panelBottom, panelRight, panelBottom);

        // Find the closest intersection point to the starting point (x1, y1)
        Point closestIntersect = null;
        double closestDistance = Double.MAX_VALUE;
//        System.out.println("left intersect: " +leftIntersect);


        if (leftIntersect != null && leftIntersect.distance(x1, y1) < closestDistance) {
            closestDistance = leftIntersect.distance(x1, y1);
            closestIntersect = leftIntersect;
        }
        if (rightIntersect != null && rightIntersect.distance(x1, y1) < closestDistance) {
            closestDistance = rightIntersect.distance(x1, y1);
            closestIntersect = rightIntersect;
        }
        if (topIntersect != null && topIntersect.distance(x1, y1) < closestDistance) {
            closestDistance = topIntersect.distance(x1, y1);
            closestIntersect = topIntersect;
        }
//        if (bottomIntersect != null && bottomIntersect.distance(x1, y1) < closestDistance) {
//            closestIntersect = bottomIntersect;
//        }

        return closestIntersect;
    }

    private Point getIntersectionPoint(int x1, int y1, int x2, int y2, Brick brick) {
        // Define the rectangle edges
        int rectLeft = brick.x;
        int rectRight = brick.x + brick.width;
        int rectTop = brick.y;
        int rectBottom = brick.y + brick.height;

        // Check for intersection with each of the four edges of the rectangle
        Point leftIntersect = getLineIntersection(x1, y1, x2, y2, rectLeft, rectTop, rectLeft, rectBottom);
        Point rightIntersect = getLineIntersection(x1, y1, x2, y2, rectRight, rectTop, rectRight, rectBottom);
        Point topIntersect = getLineIntersection(x1, y1, x2, y2, rectLeft, rectTop, rectRight, rectTop);
        Point bottomIntersect = getLineIntersection(x1, y1, x2, y2, rectLeft, rectBottom, rectRight, rectBottom);


        // Find the closest intersection point to the starting point (x1, y1)
        Point closestIntersect = null;
        double closestDistance = Double.MAX_VALUE;

        if (leftIntersect != null && leftIntersect.distance(x1, y1) < closestDistance) {
            closestDistance = leftIntersect.distance(x1, y1);
            closestIntersect = leftIntersect;
        }
        if (rightIntersect != null && rightIntersect.distance(x1, y1) < closestDistance) {
            closestDistance = rightIntersect.distance(x1, y1);
            closestIntersect = rightIntersect;
        }
        if (topIntersect != null && topIntersect.distance(x1, y1) < closestDistance) {
            closestDistance = topIntersect.distance(x1, y1);
            closestIntersect = topIntersect;
        }
        if (bottomIntersect != null && bottomIntersect.distance(x1, y1) < closestDistance) {
            closestIntersect = bottomIntersect;
        }

        return closestIntersect;
    }

    private Point getLineIntersection(int x3, int y3, int x4, int y4, int x1, int y1, int x2, int y2) {

        // Calculate the parts of the line equations
        //x1 y1 x2 y2 are for a segmented line and other for infinite line
        double denom = (double) (y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1);
        if (denom == 0.0) {
            return null; // Lines are parallel or coincident
        }

        double ua = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) / denom;
        double ub = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) / denom;

        // Check if the intersection is within the bounds of the line segment
        if (ua >= 0 && ua <= 1) {
            double x = x1 + ua * (x2 - x1);
            double y = y1 + ua * (y2 - y1);
            return new Point((int) x, (int) y);
        }

        return null; // No intersection within the bounds of the line segment

    }


    private void
    drawObjects(Graphics2D g2d) {
        // Draw the guide lineF
        if (!shooting && mouseX != 0 && mouseY != 0 && aimingEnabled) {

            // Calculate the end point of the guide line
            int ballX = lastBallX;
            int ballY = lastBallY;

            Point endPoint = calculateEndPoint(ballX + ballWidth / 2, ballY, mouseX, mouseY);
            g2d.setColor(Color.BLUE);
            g2d.drawLine(endPoint.x, endPoint.y, ballX + ballWidth / 2, ballY); // Draw the guide line from the ball to the mouse position
            // Set the color to red with semi-transparency
            Color semiTransparentRed = new Color(255, 0, 0, 128); // 128 is the alpha value for semi-transparency

            g2d.setColor(semiTransparentRed);

            // Draw the circle without a border
            Ellipse2D.Double circle = new Ellipse2D.Double(endPoint.x - 5, endPoint.y, 10, 10); // x, y, width, height
            g2d.fill(circle);
        }


        int itx = 0;
        for (Ball ball : balls) {

            g2d.drawImage(ball.getImage(),
                    ball.getX(),
                    ball.getY(),
                    ball.getWidth(),
                    ball.getHeight(),
                    this);

            // Set the color to red with semi-transparency
            g2d.setColor(ballColor);

            // Draw the circle without a border
            Ellipse2D.Double circle = new Ellipse2D.Double(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight()); // x, y, width, height
            g2d.fill(circle);

//            g2d.drawString("ball speed: %s, %s".formatted(ball.dx, ball.dy), 100, 30 + itx * 20);
            itx++;
        }

        for (Brick brick : bricks) {
            if (brick.getBlood() != 0 && brick.isVisible()) {

                g2d.drawImage(brick.getImage(),
                        (int) (brick.getX()-(shakingRatio-1)*brick.getWidth()/2),
                        (int) (brick.getY()-(shakingRatio-1)*brick.getHeight()/2),
                        (int) (brick.getWidth() * shakingRatio),
                        (int) (brick.getHeight() * shakingRatio),
                        this);


                g2d.setFont(new Font("TimesRoman", Font.BOLD, (int) (shakingRatio*25)));
                g2d.setColor(Color.WHITE);
                if(brick.type<=3)
                g2d.drawString(String.valueOf(brick.blood), (int) (brick.getX()+ (brick.getWidth() / 2 - 5)), (int) (brick.getY()+(shakingRatio)*(brick.getHeight()/2+8)));
            }
        }
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("TimesRoman", Font.BOLD, 20));
        g2d.drawImage(Constants.HEART,
                3,
                Constants.HEIGHT - 35,
                16,
                16,
                this);
        g2d.drawImage(Constants.STAR,
                33,
                Constants.HEIGHT - 35,
                16,
                16,
                this);

        g2d.drawImage(Constants.USER,
                3,
                5,
                16,
                16,
                this);

        g2d.drawString(String.valueOf(level), 20, Constants.HEIGHT - 20);
        g2d.drawString(name, 20, 20);
        g2d.drawString(String.valueOf(score), 52, Constants.HEIGHT - 20);
//        g2d.drawString("Mouse position:" + mouseX + "," + mouseY, 80, 20);
        g2d.drawString(String.valueOf(passedTime), 550, 20);

    }

    private void shootBall(int targetX, int targetY) {

        // Calculate direction towards target
        if (!shooting) {
            shooting = true;
            int it = balls.size();
            System.out.println(it);

            for (Ball ball : balls) {

                int deltaX = targetX - ball.getX();
                int deltaY = targetY - ball.getY();
                // Normalize direction
                if (isCrazy) {
                    deltaX += ThreadLocalRandom.current().nextInt(-20, 20);
                    deltaY += ThreadLocalRandom.current().nextInt(-20, 20);

                }


                double magnitude = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                double normalizedDeltaX = deltaX / magnitude;
                double normalizedDeltaY = deltaY / magnitude;

                // Set ball speed based on normalized direction


                int speed = 8; // Adjust ball speed as needed

                ball.setDx((int) (speed * (normalizedDeltaX)));

                ball.setDy((int) (speed * normalizedDeltaY));

                it--;



            }

        }

    }


    private void runPlayProcess() {
        passedTime=(System.nanoTime()-startTime)/1000000000;
        if (level == 0) {
            brickTimer.start();
            Timer first_step_timer = new Timer(500*brickSpeed/8, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    level = 1;
                    brickTimer.stop();

                }
            });
            first_step_timer.setRepeats(false);
            first_step_timer.start();

        }

        if (shooting) {
            for (Ball ball : balls) {
                ball.move();
            }
            ballDieChecker();
        } else {

            gameOverChecker();
        }

        collision();

        repaint();

    }

    void collision() {

        for (Iterator<Bonus> iterator = floatingBonuses.iterator(); iterator.hasNext(); ) {
            Bonus bonus = iterator.next();
            if (bonus.reachSouth()) iterator.remove();
            bonus.act(this, false);
            iterator.remove();


        }
        for (Ball ball : balls) {
            int bound = bricks.size();
            for (Brick brick : bricks) {
                brick.intersects(ball, this, startTime);
            }

        }
    }

    void ballDieChecker() {
        LinkedList<Ball> temptBalls = new LinkedList<>(balls);
        for (Ball ball : temptBalls) {
            if (ball.getY() >= Constants.HEIGHT - ball.getHeight() * 2) {
                ball.ballMover.stop();
                remainingBalls.add(ball);
                lastBallX = ball.getX();
                lastBallY = ball.getY();
                balls.remove(ball);
                repaint();


                if (balls.size() > 0) {
                    for (Ball ball_real : remainingBalls) {
                        ball_real.setX(lastBallX);
                        ball_real.setY(lastBallY);
                        repaint();
                    }

                } else {


                    brickTimer.start();
                    Timer moveTimer = new Timer(500*brickSpeed/8, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            level++;
                            balls = new LinkedList<>(remainingBalls);
                            remainingBalls = new LinkedList<>();
                            balls.add(new Ball(lastBallX, lastBallY));
                            brickTimer.stop();

                        }
                    });

                    moveTimer.setRepeats(false);
                    moveTimer.start();
                    shooting = false;


                }
            }
        }

    }
    public  void setParameters(int numberBricks, int brickSpeed, int brickNumberSpeed, double randomLevel,boolean saveInHistory, boolean musicEnabled, boolean aimingEnabled){
        this.numberBricks=numberBricks;
        this.brickSpeed=brickSpeed;
        this.brickNumberSpeed=brickNumberSpeed;
        this.randomLevel=randomLevel;
        customRandom=new CustomRandom(randomLevel);
        this.saveInHistory=saveInHistory;
this.musicEnabled=musicEnabled;
this.aimingEnabled=aimingEnabled;


    }
    public void pause() {
        shooting=!shooting;
//
        if (this.timer.isRunning()) {
            for (Timer timer : timers) {
                timer.stop();
            }
        } else {
            this.setFocusable(true);
            for (Timer timer : timers) {
                timer.start();
            }

        }
    }

    public void gameOverChecker() {
        for (Brick brick : bricks) {
            if (brick.getBlood() > 0) {
                if (brick.reachSouth()) {
                    this.pause();
                    JOptionPane.showMessageDialog(this, "Game over!\n Your score: " + this.score, "Game Over", JOptionPane.ERROR_MESSAGE);
                    if (saveInHistory){
                        PlayerScore playerScore = new PlayerScore(this.name, this.score, LocalDateTime.now());
                        MainPage.mainframe.scores.add(playerScore);
                        DataManager.scoreSaver(MainPage.mainframe.scores);
                    }
                    MainPage.mainframe.homeButtons.exitButton.doClick();
                }
            }
        }

    }

    public void reset(String name) {
        if (name == null) {
            name = "";
        }
        this.name = name;
        this.blood = 3;
        this.activeBonuses.clear();
        this.bricks.clear();
        this.balls.clear();
        this.balls.add(new Ball());
        this.pause();
        this.timers.clear();
        timer.restart();
        brickTimer.restart();
        this.timers.add(timer);
        this.timers.add(brickTimer);
        this.timers.add(invisibleBricksTimer);

        this.floatingBonuses.clear();
        this.score = 0;

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint(); // Repaint the panel to update the guide line

    }



    private
    class PlayProcess implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            runPlayProcess();
        }
    }
}
