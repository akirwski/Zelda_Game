//Akira Tachibana
//02/16/2022
//Model.java holds a list of positions of bricks
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.util.Iterator;


import static java.lang.System.out;
class Model
{
	ArrayList<Sprite> sprites;
	ArrayList<Sprite> boomerangs;
	ArrayList<Sprite> moving_pots;
	MySoundClip sound;
	//index of Link Object in sprite arraylist, and in this case Link is always at index 0
	int il = 0;
	int dir;

	Model()
	{
		sprites = new ArrayList<Sprite>();
		boomerangs = new ArrayList<Sprite>();
		moving_pots = new ArrayList<Sprite>();
		Link link = new Link(75, 75);
		try {
			String crr_dir = System.getProperty("user.dir");
			sound = new MySoundClip(crr_dir + "/sound/brokensound.wav", 1);
		} catch (Exception e) {
	    	e.printStackTrace();
		}
		sprites.add(link);
	}

	public void delteBrokenPot(){
		for(int i = 0; i < moving_pots.size(); i++){
			if(moving_pots.get(i).isPot()){
				if(((Pot)moving_pots.get(i)).state == 1 && ((Pot)moving_pots.get(i)).took5 == true)
					moving_pots.remove(i);
			}
		}
	}

	public void update(int direction){
		dir = direction;
		if(sprites.get(il).isLink()){
			//set link current position and destination
			Link link = (Link)sprites.get(il);
			int lx = link.x, ly =  link.y;
			int lw = link.w, lh =  link.h;
			link.savePrevLocation();
			int changex = 0, changey = 0;
			if(direction / 5 == 0){//down
				changey += 75;
			} else if (direction / 5 == 1){//left
			 	changex -= 75;
			} else if (direction / 5 == 2){//right
				changex +=75;
			} else if (direction / 5 == 3){//up
				changey -= 75;
			}
			lx += changex;
			ly += changey;

			//to set pot setDestination
			Pot pot;
			int potx=0, poty=0, potw=0, poth=0, index_pot=0;
			boolean brick_collided = false;
			boolean pot_collided = false;

			//detect a collision between a link and a pot
			for(int i = 0; i < sprites.size(); i++){
				 if(sprites.get(i).isPot()){
					if(((Pot)sprites.get(i)).detectCollision(lx, ly, lw, lh)){
						//System.out.println("you want to push this pot");

						pot_collided = true;
						index_pot = i;
						pot = (Pot)sprites.get(index_pot);
						potx = pot.x + changex;
						poty = pot.y + changey;
						potw = pot.w;
						poth = pot.h;
					}
				}
			}
			//to avoid stepping on the broken pot or moving pot.
			for(int i = 0; i < moving_pots.size(); i++){
				if(((Pot)moving_pots.get(i)).detectCollision(lx, ly, lw, lh)){
					//System.out.println("Collided with broken pot");
					brick_collided = true;
				}
			}

			//detect collisions between a brick and a link or a brick and a pot, and collision between a pusshing pot and the latter pot
			for(int i = 0; i < sprites.size(); i++){
				if(sprites.get(i).isBrick()){
					if(((Brick)sprites.get(i)).detectCollision(lx, ly, lw, lh) || (pot_collided && ((Brick)sprites.get(i)).detectCollision(potx, poty, potw, poth))){
							if(pot_collided){
								//System.out.println("but Pot Collided with a brick");
								sound.play();
							}
							brick_collided = true;
							break;
					}
				}
				if(sprites.get(i).isPot()){
					if(pot_collided && ((Pot)sprites.get(i)).detectCollision(potx, poty, potw, poth)){
							if(pot_collided){
								//System.out.println("but Pot Collided with a pot");
							}
							pot_collided = false;
							brick_collided = true;
							break;
					}
				}
			}

			//if there is a brick next to a pushing pot, immediately break a pot
			if(pot_collided && brick_collided){
				pot = (Pot)sprites.get(index_pot);
				pot.breakPot();
				moving_pots.add(pot);
				sprites.remove(index_pot);
			}

			//if there are any space betweeen a pushing pot and a brick, update only the pot's direction, and add to moving_pot
			else if (pot_collided){
				pot = (Pot)sprites.get(index_pot);
				pot.update(direction, pot.x, pot.y);
				moving_pots.add(pot);
				sprites.remove(index_pot);
			}
			//if there is a pot next to a pusshing pot or
			//if the linke collided with brick, bounce him back to the previous location
			else if(brick_collided){
				link.update(direction, lx, ly);
				link.getOut();
			} else
				link.update(direction, lx, ly);
		}
	}

	//if you want to add a pot, brick = false
	public void addSprite(boolean brick, int X, int Y){
		//if there is no sprite in the list
		if(sprites.size() == 0){
			if(brick){
				Brick b = new Brick(X, Y);
				sprites.add(b);
			}
			else{
				Pot pot = new Pot(X, Y);
				sprites.add(pot);
			}
		}

		else {
			for(int i=0; i<sprites.size(); i++){
					//if thereis a collision with the existing sprite, do nothing
					if(sprites.get(i).detectCollision(X, Y, 74, 74)){
							//System.out.println("Clicked on the brick");
							sprites.remove(i);
							return;
					}
			}
			//if there is no collision, add either of them
		    if (brick == true){
				//System.out.println("adding the brick");
				Brick b = new Brick(X, Y);
				sprites.add(b);
			}
			else {
				//System.out.println("adding the POT");
				Pot pot = new Pot(X, Y);
				sprites.add(pot);
			}
		}
	}

	public void stopSprite(int stop){
		if(sprites.get(il).isLink())
			((Link)sprites.get(il)).update(stop, sprites.get(il).x, sprites.get(il).y);
	}

	//unmarshaling
	public void unmarshal(Json ob){
		sprites = new ArrayList<Sprite>();
		//add link
		Json linkList = ob.get("link");
		sprites.add(new Link(linkList.get(0)));
		//add bricks
		Json brickList = ob.get("bricks");
		for(int i = 0; i < brickList.size(); i++)
			sprites.add(new Brick(brickList.get(i)));
		//add pots
		Json potList = ob.get("pots");
		for(int i = 0; i < potList.size(); i++)
			sprites.add(new Pot(potList.get(i)));
	}

	//store sprites as a json list
	public Json marshal(){
		Json ob = Json.newObject();
		Json linkList = Json.newList();
		ob.add("link", linkList);
		Json brickList = Json.newList();
		ob.add("bricks", brickList);

		Json potList = Json.newList();
		ob.add("pots", potList);
		for(int i=0; i<sprites.size(); i++){
			if(sprites.get(i).isLink()){
				linkList.add(((Link)sprites.get(i)).marshal());
				il = i;
			}
			if(sprites.get(i).isBrick())
				brickList.add(((Brick)sprites.get(i)).marshal());
			if(sprites.get(i).isPot())
				potList.add(((Pot)sprites.get(i)).marshal());
		}
		return ob;

    }


	public void throwBoomerang(){
		//state for boomerang
		int s = 0;
		//set link current position and destination
		Link link = (Link)sprites.get(il);
		int bx = link.x, by = link.y;
		int bw = link.w, bh = link.h;

		if(dir / 5 == 0){//down
			by += 15;
		} else if (dir / 5 == 1){//left
			bx -= 15;
		} else if (dir / 5 == 2){//right
			bx += 15;
		} else if (dir / 5 == 3){//up
			by -= 15;
		}

		//add new boomerang to sprites
		int bi = boomerangs.size();
		boomerangs.add(new Boomerang(dir, bx, by));
		if(!detectCollisionForBoomerang(bx, by, bw, bh))
			((Boomerang)boomerangs.get(bi)).update(s, bx, by);
		else
			boomerangs.remove(bi);
	}

	//if collided, return true
	public boolean detectCollisionForBoomerang(int bx, int by, int bw, int bh){

		for(int i = 0; i < sprites.size(); i++){
			if(sprites.get(i).isPot()){
				if(((Pot)sprites.get(i)).state == 0){
					if(((Pot)sprites.get(i)).detectCollision(bx, by, bw, bh)){
						//System.out.println("boomerang hit pot");
						//sound.play();
						((Pot)sprites.get(i)).breakPot();
						moving_pots.add(((Pot)sprites.get(i)));
						sprites.remove(i);
						return true;
					}
				}
			}
			else if(sprites.get(i).isBrick()){
			   if(((Brick)sprites.get(i)).detectCollision(bx, by, bw, bh)){
				   //System.out.println("boomerang hit brick");
				   return true;
			   }
			}
		}
		return false;
	}

	public boolean detectCollisionForPot(int potx, int poty, int potw, int poth, int pi){

		for(int i = 0; i < sprites.size(); i++){
			if(sprites.get(i).isPot()){
				if(((Pot)sprites.get(i)).detectCollision(potx, poty, potw, poth)){
					//System.out.println("pot hit pot");
					sound.play();
					((Pot)sprites.get(i)).breakPot();
					((Pot)moving_pots.get(pi)).breakPot();
					return true;
				}
			}
			else if(sprites.get(i).isBrick()){
			   if(((Brick)sprites.get(i)).detectCollision(potx, poty, potw, poth)){
				   //System.out.println("pot hit brick");
				   sound.play();
				   ((Pot)moving_pots.get(pi)).breakPot();
				   return true;
			   }
			}
		}
		return false;
	}

	public void updateSprite(){
		if(boomerangs.size() > 0){
			//detect a collision between a link and a pot
			for(int bi=0; bi<boomerangs.size(); bi++){
				Boomerang br = (Boomerang)boomerangs.get(bi);
				int  bx = br.x, by = br.y;
				int  bw = br.w, bh = br.h;
				int  dir = br.dir;
				int  s = br.state;
				if(dir / 5 == 0){//down
					by += 15;
				} else if (dir / 5 == 1){//left
					bx -= 15;
				} else if (dir / 5 == 2){//right
					bx += 15;
				} else if (dir / 5 == 3){//up
					by -= 15;
				}


				if(!detectCollisionForBoomerang(bx, by, bw, bh)){
					if(s==3)
						s=0;
					else
						s+=1;
					br.update(s, bx, by);
				} else  {
					boomerangs.remove(bi);
				}
			}
		}

		if(moving_pots.size() > 0) {
			//detect a collision between a link and a pot
			for(int pi=0; pi<moving_pots.size(); pi++){
				Pot pot = (Pot)moving_pots.get(pi);
				if(pot.state == 0){
					//System.out.println("updating moving_pot");
					int potx = pot.x, poty = pot.y;
					int potw = pot.w, poth = pot.h;
					int dir = pot.dir;
					int s = pot.state;


					if(dir / 5 == 0){//down
						poty += 15;
					} else if (dir / 5 == 1){//left
						potx -= 15;
					} else if (dir / 5 == 2){//right
						potx += 15;
					} else if (dir / 5 == 3){//up
						poty -= 15;
					}

					if(!detectCollisionForPot(potx, poty, potw, poth, pi)){
						pot.update(dir, potx, poty);
					}
				}
			}
		}
	}


}
