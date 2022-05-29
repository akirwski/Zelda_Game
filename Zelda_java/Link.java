import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;


public class Link extends Sprite
{
	int px;
	int py;
	double speed;
	int direction = 0;
	static BufferedImage[] link_images = null;


	Link(int X, int Y){
		super(X, Y);
		speed = 10;
		//loading link character images
		loadImage();
	}

	Link(Json ob){
        super(ob);
        loadImage();
    }

	public void loadImage(){
		if(link_images == null){
			link_images = new BufferedImage[20];
			String crr_dir = System.getProperty("user.dir");
			//System.out.println(crr_dir);
			for(int i=0; i<20; i++){
				try {
					link_images[i] = ImageIO.read(new File(crr_dir + "/images/"+i+".jpg"));
				} catch(Exception e){
					e.printStackTrace(System.err);
					System.exit(1);
				}
			}
		}
	}

	public void draw(Graphics g, int scrollPosX, int scrollPosY, int viewX, int viewY){
		BufferedImage image = link_images[direction];

		if(scrollPosX == 0 && scrollPosY == 0)
			g.drawImage(image, x, y, null);
		else if(scrollPosX == viewX && scrollPosY == 0)
			g.drawImage(image, x - scrollPosX , y, null);
		else if(scrollPosX == 0 && scrollPosY == viewY)
			g.drawImage(image, x, y - scrollPosY, null);
		else if(scrollPosX == viewX && scrollPosY == viewY)
			g.drawImage(image, x - scrollPosX , y - scrollPosY, null);
	}

	public void update(int dir, int lx, int ly){
        x = lx;
        y = ly;
        direction = dir;

        View.updateScrollX(x);
        View.updateScrollY(y);
    }

    public Json marshal(){
        Json ob = Json.newObject();
        ob.add("x", x);
        ob.add("y", y);
        return ob;
    }

    public String toString()
    {
    	return "Link (x,y) = (" + x + ", " + y + ")";
    }

	@Override
	public boolean isLink(){
		return true;
	}


	public void savePrevLocation(){
		//save privious position
		px = x;
		py = y;
	}

	public void getOut(){
		//if link and brick collided, link should be at privious position
		x = px;
		y = py;
	}

};
