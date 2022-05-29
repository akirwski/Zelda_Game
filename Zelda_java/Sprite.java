//Akira Tachibana
//3/15/2022
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


abstract class Sprite{
    int x;
    int y;
    int w;
    int h;
    int imageSize = 75;

    public Sprite(int X, int Y){
        x = X;
        y = Y;
        w = imageSize - 1;
        h = imageSize - 1;

    }

    public Sprite(Json ob){
        x = (int)ob.getLong("x");
        y = (int)ob.getLong("y");
        w = imageSize - 1;
        h = imageSize - 1;
    }

    //abstract methods because each object has different implementation
    abstract public void loadImage();
    abstract public void draw(Graphics g, int scrollPosX, int scrollPosY, int viewX, int viewY);
    abstract public void update(int dir, int lx, int ly);
    abstract public Json marshal();

    public String toString()
    {
    	return "Sprite (x,y) = (" + x + ", " + y + ")";
    }

    public boolean isLink(){
        return false;
    }

    public boolean isBrick(){
        return false;
    }

    public boolean isPot(){
        return false;
    }

    //if there is collision, return true
    public boolean detectCollision(int X, int Y, int W, int H){
        if(this.y + this.h < Y)
            return false;
        if(this.y > Y + H)
            return false;
         if(this.x + this.w < X)
            return false;
        if(this.x > X + W)
            return false;

        return true;
    }
}
