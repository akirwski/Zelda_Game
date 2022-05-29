//Akira Tachibana
//02/16/2022
//View.java visualize bricks

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Color;

class View extends JPanel
{
	Model model;
	static int viewX;
	static int viewY;
	static int scrollPosX;
	static int scrollPosY;


	View(Controller c, Model m)
	{
		model = m;
		viewX = 750;
		viewY = 600;
		scrollPosX = 0;
		scrollPosY = 0;
		c.setView(this);

	}

	public void paintComponent(Graphics g){//Overriding method for JPanel's method
		g.setColor(new Color(128, 255, 255));//put this before fillRect
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		//draw bricks
		for(int i = 0; i < model.sprites.size(); i++)
			model.sprites.get(i).draw(g, scrollPosX, scrollPosY, viewX, viewY);

		//the reason why separate sprite array and those boomerangs and pots is not to save moving objects in map accidently
		//draw boomerangs
		for(int i = 0; i < model.boomerangs.size(); i++)
			model.boomerangs.get(i).draw(g, scrollPosX, scrollPosY, viewX, viewY);

		//drsaw pots
		for(int i = 0; i < model.moving_pots.size(); i++)
			model.moving_pots.get(i).draw(g, scrollPosX, scrollPosY, viewX, viewY);
	}

	void setModel(Model m){
		model = m;
	}

	static public void updateScrollX(int sx){
		if(sx >= viewX)
			scrollPosX = viewX;
		else
			scrollPosX = 0;
	}

	static public void updateScrollY(int sy){
		if(sy >= viewY)
			scrollPosY = viewY;
		else
			scrollPosY = 0;
	}


}
