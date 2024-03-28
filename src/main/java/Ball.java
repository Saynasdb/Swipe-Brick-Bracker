import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

public
class Ball extends Thing{
    double dx=0;
    double dy=2;

    int power=1;

    Timer ballMover;
    public
    double getDx() {
        return dx;
    }

    public
    Ball setDx(double dx) {
        this.dx = dx;
        return this;
    }

    public
    double getDy() {
        return dy;
    }

    public
    Ball setDy(double dy) {
        this.dy = dy;
        return this;
    }

    public Ball(){
    initialize();
}
    void initialize() {
        image =new ImageIcon("src/main/resources/ball.png").getImage();
        getDimensions();
        ballMover=new Timer(5, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                move();
            }
        });
//        resetPosition();
    }
    public Ball(int x,int y){

        this.x=x;
        this.y=y;

        initialize();
    }
    public void setSpeed(int dx,int dy){
        this.dx=dx;
        this.dy=dy;
    }
    Timer fire(PlayPanel playPanel,Bonus bonus){

        Timer timer =new Timer(15000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                image =new ImageIcon("src/main/resources/ball.png").getImage();
                getDimensions();
                power=1;
                playPanel.activeBonuses.remove(bonus);



            }
        });
        timer.setRepeats(false);
        timer.start();


        image =new ImageIcon("src/main/resources/ball_fire.gif").getImage();
        power=2;
        getDimensions();

return timer;
    }


    public
    void resetPosition() {
        x=Constants.DEFAULT_BALL_X;
        y=Constants.DEFAULT_BALL_Y;
    }


    void move(){
//if(dx>6){
//    dx=6;
//}
//        if(dx<-6){
//            dx=-6;
//        }
//        if(dy>6){
//            dy=6;
//        }
//        if(dy<-6){
//            dy=-6;
//        }
        x+=dx;
        y+=dy;
        if(x<=0 ){
            x=0;
            dx=-dx;
        }
        if(x>=Constants.WIDTH-width){
            x=Constants.WIDTH-width;
            dx=-dx;
        }
        if(y>=Constants.HEIGHT-height){
            y=Constants.HEIGHT-height;
            dy=-dy;
        }

        if (y<=0 ){
            y=0;
            dy=-dy;
        }

    }



    Ellipse2D getCircle(){
    return new Ellipse2D.Double(x,y,image.getWidth(null),image.getHeight(null));

    }

    public
    int getPower() {
        return power;
    }

    public
    Ball setPower(int power) {
        this.power = power;
        return this;
    }
    public void changeSpeed(double ratio){
        this.setDx(this.getDx()*ratio);
        this.setDy(this.getDy()*ratio);
    }
    public Timer tempChangeSpeed(double ratio,PlayPanel playPanel,Bonus bonus){
        Ball ball=this;
        Timer timer =new Timer(15000, new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                ball.changeSpeed(1/ratio);
                playPanel.activeBonuses.remove(bonus);

            }
        });
        timer.setRepeats(false);
        timer.start();
        ball.changeSpeed(ratio);


return timer;

    }

    @Override
    public
    String toString() {
        return
                 dx +
                "," + dy +
                "," + power +
                "," + x +
                "," + y
                ;
    }
}
