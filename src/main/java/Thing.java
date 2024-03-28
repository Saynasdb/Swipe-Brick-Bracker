import java.awt.*;
import java.awt.geom.Rectangle2D;

public
class Thing {
    int x;
    int y;
    int width;
    int height;
    Image image;

    public
    int getX() {
        return x;
    }

    protected
    Thing setX(int x) {
        this.x = x;
        return this;
    }

    public
    int getY() {
        return y;
    }

    protected
    Thing setY(int y) {
        this.y = y;
        return this;
    }

    public
    int getWidth() {
        return width;
    }



    public
    int getHeight() {
        return height;
    }



    public
    Image getImage() {
        return image;
    }

    public
    Thing setImage(Image image) {
        this.image = image;
        return this;
    }
    void getDimensions(){
        width=image.getWidth(null);
        height=image.getHeight(null);
    }

    Rectangle2D getRectangle() {
        return new Rectangle2D.Double(x, y,
                image.getWidth(null), image.getHeight(null));
    }
public boolean reachSouth(){
        if (this.getY()>=Constants.HEIGHT-this.getHeight())return true;
        else return false;
}

}
