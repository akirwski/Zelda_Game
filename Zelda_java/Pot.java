import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Pot extends Sprite
{
    static BufferedImage[] pot_images = null;
    //if the pot is broken = 1, otherwise state = 0
    int state;
    int dir;
    boolean took5;
    Timer timer;


    Pot(int X, int Y){
		super(X, Y);
        state = 0;
        timer = new Timer();
        dir = 0;
        took5 = false;
		loadImage();
	}

	Pot(Json ob){
        super(ob);
        state = 0;
        timer = new Timer();
        dir = 0;
        took5 = false;
        loadImage();
    }

	public void loadImage(){
		if(pot_images == null){
			pot_images = new BufferedImage[2];
            String crr_dir = System.getProperty("user.dir");
			
			try {
				pot_images[0] = ImageIO.read(new File(crr_dir + "/images/pot.jpg"));
                pot_images[1] = ImageIO.read(new File(crr_dir + "/images/pot_broken.jpg"));
			} catch(Exception e){
				e.printStackTrace(System.err);
				System.exit(1);
			}
		}
	}


    public void draw(Graphics g, int scrollPosX, int scrollPosY, int viewX, int viewY){
		BufferedImage image = pot_images[state];

		if(scrollPosX == 0 && scrollPosY == 0)
			g.drawImage(image, x, y, null);
		else if(scrollPosX == viewX && scrollPosY == 0)
			g.drawImage(image, x - scrollPosX , y, null);
		else if(scrollPosX == 0 && scrollPosY == viewY)
			g.drawImage(image, x, y - scrollPosY, null);
		else if(scrollPosX == viewX && scrollPosY == viewY)
			g.drawImage(image, x - scrollPosX , y - scrollPosY, null);
	}

    public void update(int direction, int potx, int poty){
        x = potx;
        y = poty;
        dir = direction;
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

	@Override
	public boolean isPot(){
		return true;
	}


    public void breakPot(){
        state = 1;
        timer.schedule(new TimerTask(){
            @Override public void run()
            {
                took5 = true;
                timer.cancel();
            }
        }, 2000, 2000);
    }

}
