import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Boomerang extends Sprite
{
    static BufferedImage[] boomerang_images = null;
    int dir;
    int state;


    Boomerang(int direction, int X, int Y){
		super(X, Y);
        state = 0;
        dir = direction;
        w = 30;
        h = 30;
		loadImage();
	}

	Boomerang(Json ob){
        super(ob);
        state = 0;
        w = 30;
        h = 30;
        loadImage();
    }

	public void loadImage(){
		if(boomerang_images == null){
			boomerang_images = new BufferedImage[4];
            String crr_dir = System.getProperty("user.dir");
			//System.out.println(crr_dir);
            for(int i=0; i<4; i++){
                try {
                    boomerang_images[i] = ImageIO.read(new File(crr_dir + "/images/boomerang"+(i+1)+".jpg"));
    			} catch(Exception e){
    				e.printStackTrace(System.err);
    				System.exit(1);
    			}
            }
		}
	}


    public void draw(Graphics g, int scrollPosX, int scrollPosY, int viewX, int viewY){
		BufferedImage image = boomerang_images[state];

		if(scrollPosX == 0 && scrollPosY == 0)
			g.drawImage(image, x, y, null);
		else if(scrollPosX == viewX && scrollPosY == 0)
			g.drawImage(image, x - scrollPosX , y, null);
		else if(scrollPosX == 0 && scrollPosY == viewY)
			g.drawImage(image, x, y - scrollPosY, null);
		else if(scrollPosX == viewX && scrollPosY == viewY)
			g.drawImage(image, x - scrollPosX , y - scrollPosY, null);
	}

    public void update(int s, int bx, int by){
        x = bx;
        y = by;
        state = s;
    }


    public Json marshal(){
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

	@Override
    public String toString()
    {
    	return "Pot (x,y) = (" + x + ", " + y + ")";
    }



}
