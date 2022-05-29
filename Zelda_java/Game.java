//Akira Tachibana
//02/04/2022
//Game.java contains a main function

import javax.swing.JFrame;
import java.awt.Toolkit;

public class Game extends JFrame
{
	Model model;
	Controller controller;
	View view;
	Brick brick;

	public Game()
	{
		model = new Model();
		controller = new Controller(model);
		view = new View(controller, model);
		Json jsonfile = Json.load("map.json");
		model.unmarshal(jsonfile);
		view.setModel(model);


		this.setTitle("A5 - Added Pot and Boomerang");
		this.setSize(750, 600);
		this.setFocusable(true);
		this.getContentPane().add(view);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		view.addMouseListener(controller);
		this.addKeyListener(controller);
	}

	public static void main(String[] args)
	{
		Game g = new Game();
		g.run();
	}

	public void run()
	{
		while(true)
		{
			model.updateSprite();
			model.delteBrokenPot();
			view.repaint(); // Indirectly calls View.paintComponent
			Toolkit.getDefaultToolkit().sync(); // Updates screen

			// Go to sleep for 50 milliseconds
			try
			{
				Thread.sleep(50);
			} catch(Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	void setModel(Model m){
		model = m;
	}
}
