//Akira Tachibana
//02/16/2022
//Controller.java interacts with I/O

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Files;
//Action listener is the interface
class Controller implements ActionListener, MouseListener, KeyListener
{
	View view;
	Model model;


	boolean keyLeft;
	boolean keyRight;
	boolean keyUp;
	boolean keyDown;
	boolean keyCtrl;

	boolean brick_editing;
	boolean pot_editing;
	int index = 0;

	Controller(Model m)
	{
		model = m;
		brick_editing = false;
		pot_editing = false;
		System.out.println("playing mode");
	}
	//you must implement this function because the ActionListener is an interface
	public void actionPerformed(ActionEvent e){}

	void setView(View v){
		view = v;
	}
	public void mousePressed(MouseEvent e){}
	public void mouseReleased(MouseEvent e) {    }
	public void mouseEntered(MouseEvent e) {    }
	public void mouseExited(MouseEvent e) {    }


	public void mouseClicked(MouseEvent e) {
		//add a brick
		int x = e.getX() - (e.getX() % 75) + view.scrollPosX;
		int y = e.getY() - (e.getY() % 75) + view.scrollPosY;


		if(brick_editing == true){
			model.addSprite(brick_editing, x, y);
		}
		if(pot_editing == true){
			model.addSprite(false, x, y);
		}

	}

	public void keyPressed(KeyEvent e)
	{
		int direction = -1;
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_DOWN: keyDown = true; direction=1 + index;break;
			case KeyEvent.VK_LEFT: keyLeft = true; direction=6 + index;break;
			case KeyEvent.VK_RIGHT: keyRight = true; direction=11 + index; break;
			case KeyEvent.VK_UP: keyUp = true; direction=16 + index;break;
		}
		if(index < 3)
			index++;
		else
			index = 0;

		if(direction !=-1)
			model.update(direction);

	}

	public void keyReleased(KeyEvent e)
	{
		int stop = -1;
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_DOWN: keyDown = true; stop=0;break;
			case KeyEvent.VK_LEFT: keyLeft = true; stop=5;break;
			case KeyEvent.VK_RIGHT: keyRight = true; stop=10; break;
			case KeyEvent.VK_UP: keyUp = true; stop=15;break;

			case KeyEvent.VK_CONTROL: keyCtrl = true; model.throwBoomerang();break;

		}

		if(stop !=-1)
			model.stopSprite(stop);


		char c = e.getKeyChar();
		if('q' == c)//terminate program
			System.exit(0);
		if('s' == c){//scropp down
			Json json = model.marshal();
			json.save("map.json");
			System.out.println("File saved");
		}
		if('l' == c){
			Json jsonfile = Json.load("map.json");
			model.unmarshal(jsonfile);
			view.setModel(model);
		}
		if('e' == c){
			if(pot_editing == false){
				if(brick_editing == false){
					brick_editing = true;
					System.out.println("Swiched to editing mode");
				} else{
					brick_editing = false;
					System.out.println("Switched to playing Mode");
				}
			} else {
				System.out.println("You have to type 'p' to finish the pot editing mode first.");
			}
		}

		if('p' == c){
			if(brick_editing == false){
				if(pot_editing == false){
					pot_editing = true;
					System.out.println("Swiched to pot_editing mode");
				} else{
					pot_editing = false;
					System.out.println("Switched to playing Mode");
				}
			} else {
				System.out.println("You have to type 'e' to finish the brick editing mode first.");
			}
		}

	}

	public void keyTyped(KeyEvent e)
	{
	}



}
