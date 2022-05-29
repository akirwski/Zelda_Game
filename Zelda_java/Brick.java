//Akira Tachibana
//02/16/2022
//Brick.java is a brick object

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import static java.lang.System.out;
import java.awt.Graphics;
// import java.io.BufferedWriter;
// import java.io.FileWriter;
// import java.nio.file.Paths;
// import java.nio.file.Files;

public class Brick extends Sprite
{
    static BufferedImage brick_image = null;


    Brick(int X, int Y){
        super(X, Y);
        loadImage();
    }


    //unmashaling constructor
    Brick(Json ob){
        super(ob);
        loadImage();
    }

    public void loadImage(){
        //load brick image only once
        if(brick_image == null){
            try{
                String crr_dir = System.getProperty("user.dir");
                //System.out.println(crr_dir);
                this.brick_image = ImageIO.read(new File(crr_dir + "/images/brick.jpg"));
            } catch(Exception e){
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }


    public void draw(Graphics g, int scrollPosX, int scrollPosY, int viewX, int viewY){
		if(scrollPosX == 0 && scrollPosY == 0)
			g.drawImage(brick_image, x, y, null);
		else if(scrollPosX == viewX && scrollPosY == 0)
			g.drawImage(brick_image, x - scrollPosX , y, null);
		else if(scrollPosX == 0 && scrollPosY == viewY)
			g.drawImage(brick_image, x, y - scrollPosY, null);
		else if(scrollPosX == viewX && scrollPosY == viewY)
			g.drawImage(brick_image, x - scrollPosX , y - scrollPosY, null);
    }


    public void update(int dir, int lx, int ly){}


    public Json marshal(){
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

    @Override
    public String toString()
    {
    	return "Brick (x,y) = (" + x + ", " + y + ")";
    }


    @Override
    public boolean isBrick(){
        return true;
    }

}
