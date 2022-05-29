import time
from Brick import Brick
from Link import Link
from Pot import Pot
from Boomerang import Boomerang
class Model():
    def __init__(self, pygame):
        self.dest_x = 0
        self.dest_y = 0
        self.sprites = []
        self.moving_pots = []
        self.boomerangs = []
        self.pygame = pygame

        self.link = Link(pygame, 75, 75, 'images/0.jpg')
        self.link.x = 75
        self.link.y = 75
        self.link.w = 75 - 1
        self.link.h = 75 - 1
        self.link.dir = 0
        self.sprites.append(self.link)
        self.unmarshal()

    def update(self, direction):
        self.dir = direction
        #set link current position and destination
        lx = self.link.x
        ly =  self.link.y
        lw = self.link.w
        lh =  self.link.h
        self.link.savePrevLocation()
        changex = 0
        changey = 0

        if(int(direction / 5) == 0):#down
            changey += 75
        elif (int(direction / 5) == 1):#left
            changex -= 75
        elif (int(direction / 5) == 2):#right
            changex +=75
        elif (int(direction / 5) == 3):#up
            changey -= 75
        lx += changex
        ly += changey

        #to set pot setDestination
        potx=0
        poty=0
        potw=0
        poth=0
        index_pot=0
        brick_collided = False
        pot_collided = False

		#detect a collision between a link and a pot
        for i in range(0, len(self.sprites)):
            if(self.sprites[i].isPot()):
                if(self.sprites[i].detectCollision(lx, ly, lw, lh)):
                    #print("you want to append self pot")
                    pot_collided = True
                    index_pot = i
                    pot = self.sprites[i]
                    potx = pot.x + changex
                    poty = pot.y + changey
                    potw = pot.w
                    poth = pot.h


        #to avoid stepping on the broken pot or moving pot.
        for i in range(0, len(self.moving_pots)):
            if(self.moving_pots[i].detectCollision(lx, ly, lw, lh)):
                #print("Collided with broken pot")
                brick_collided = True

        #detect collisions between a brick and a link or a brick and a pot, and collision between a pusshing pot and the latter pot
        for i in range(0, len(self.sprites)):
            if(self.sprites[i].isBrick()):
                if(self.sprites[i].detectCollision(lx, ly, lw, lh) or (pot_collided and self.sprites[i].detectCollision(potx, poty, potw, poth))):
                    brick_collided = True
                    break
            if(self.sprites[i].isPot()):
                if(pot_collided and self.sprites[i].detectCollision(potx, poty, potw, poth)):
                    pot_collided = False
                    brick_collided = True
                    break

        #if there is a brick next to a appending pot, immediately break a pot
        if(pot_collided and brick_collided):
            pot = self.sprites[index_pot]
            pot.breakPot()
            self.moving_pots.append(pot)
            self.sprites.pop(index_pot)
        #if there are any space betweeen a appending pot and a brick, update only the pot's direction, and add to moving_pot
        elif(pot_collided):
            pot = self.sprites[index_pot]
            pot.update(direction, potx, poty)
            self.moving_pots.append(pot)
            self.sprites.pop(index_pot)

        #if there is a pot next to a pusshing pot or
        #if the linke collided with brick, bounce him back to the previous location
        elif(brick_collided):
            self.link.update(direction, lx, ly)
            self.link.getOut()
        else:
            self.link.update(direction, lx, ly)
            self.link.x = lx
            self.link.y = ly
            self.link.dir = direction

    def set_dest(self, pos):
        self.dest_x = pos[0]
        self.dest_y = pos[1]

    def unmarshal(self):
        #unmarshal bricks
        bricks = [225, 0, 300, 0, 375, 0, 450, 0, 525, 0, 600, 0, 675, 0, 0, 75, 0, 150, 0, 225, 0, 300, 0, 375, 0, 450, 0, 525, 0, 600, 0, 675, 0, 750, 0, 825, 0, 750, 0, 900, 0, 975, 0, 1050, 0, 1125, 75, 1125, 150, 1125, 225, 1125, 300, 1125, 375, 1125, 450, 1125, 525, 1125, 600, 1125, 675, 1125, 750, 1125, 825, 1125, 900, 1125, 975, 1125, 1050, 1125, 1125, 1125, 1200, 1125, 1275, 1125, 1350, 1125, 1425, 1125, 1425, 1050, 1425, 975, 1425, 900, 1425, 825, 1425, 750, 1425, 675, 1425, 600, 1350, 600, 1275, 600, 1200, 600, 750, 600, 825, 600, 900, 600, 750, 675, 750, 750, 1425, 450, 1425, 525, 1425, 375, 1425, 300, 1425, 150, 1425, 225, 1425, 75, 1425, 0, 1275, 0, 1350, 0, 1200, 0, 1125, 0, 975, 0, 1050, 0, 900, 0, 825, 0, 750, 0, 750, 375, 750, 525, 750, 450, 825, 525, 1350, 525, 1275, 525, 900, 525, 75, 525, 150, 525, 75, 600, 150, 600, 600, 600, 675, 600, 525, 600, 675, 525, 600, 525, 525, 525, 675, 675, 675, 825, 675, 750, 750, 825, 1200, 525, 675, 450, 675, 375, 750, 75, 675, 75, 225, 150, 300, 225, 300, 300, 225, 375, 300, 375, 375, 375, 225, 825, 300, 750, 375, 825, 375, 900, 300, 975, 225, 1050, 300, 1050, 375, 1050, 975, 1050, 1050, 1050, 1125, 975, 1050, 900, 975, 900, 1125, 825, 1050, 750, 975, 750, 1050, 150, 975, 225, 1050, 300, 1125, 300, 1200, 300, 1125, 225, 1125, 375, 1125, 150, 975, 300, 0, 0, 75, 0, 150, 0, 300, 150]

        for i in range(0, len(bricks),2):
            self.sprites.append(Brick(self.pygame, bricks[i], bricks[i+1], 'images/brick.jpg'))

        pots = [1125, 750, 1200, 975, 450, 900, 375, 300, 450, 300, 225, 75, 975, 150, 1050, 375, 1050, 450, 1125, 450, 1275, 975, 1350, 975, 1125, 1050, 1200, 1050, 1275, 1050, 1350, 1050, 225, 675, 300, 675, 375, 675, 525, 675, 525, 825, 600, 825, 600, 225, 1350, 225, 150, 450]
        for i in range(0, len(pots), 2):
            self.sprites.append(Pot(self.pygame, pots[i], pots[i+1], 'images/pot.jpg'))

    def stopSprite(self, stop):
        self.link.update(stop, self.link.x, self.link.y)

    def updateSprite(self):
        if(len(self.boomerangs) > 0):
            #detect a collision between a link and a pot
            for bi in range(0, len(self.boomerangs)):
                br = self.boomerangs[bi]
                bx = br.x
                by = br.y
                bw = br.w
                bh = br.h
                dir = br.dir
                s = br.state

                if(int(dir / 5) == 0):#down
                    by += 15
                elif (int(dir / 5) == 1):#left
                    bx -= 15
                elif (int(dir / 5) == 2):#right
                    bx += 15
                elif (int(dir / 5) == 3):#up
                    by -= 15


                if(self.detectCollisionForBoomerang(bx, by, bw, bh) == False):
                    if(s==3):
                        s=0
                    else:
                        s+=1
                    br.update(s, bx, by)
                else:
                    self.boomerangs.pop(bi)
                    break



        if(len(self.moving_pots) > 0):
            #detect a collision between a link and a pot
            for pi in range(0, len(self.moving_pots)):
                pot = self.moving_pots[pi]
                if(pot.state == 0):
                    potx = pot.x
                    poty = pot.y
                    potw = pot.w
                    poth = pot.h
                    dir = pot.dir
                    s = pot.state
                    if(int(dir / 5) == 0):#down
                        poty += 15
                    elif(int(dir / 5) == 1):#left
                        potx -= 15
                    elif(int(dir / 5) == 2):#right
                        potx += 15
                    elif(int(dir / 5) == 3):#up
                        poty -= 15

                    if(self.detectCollisionForPot(potx, poty, potw, poth, pi) == False):
                        pot.update(dir, potx, poty)


    def deleteBrokenPot(self):
        for i in range(0, len(self.moving_pots)):
            if(self.moving_pots[i].isPot()):
                if(self.moving_pots[i].state == 1 and self.moving_pots[i].t0 != 0.0):
                    t0 = self.moving_pots[i].t0
                    t1 = time.time()
                    if(t1 - t0 >= 3.0):
                        self.moving_pots.pop(i)
                        break



    def detectCollisionForPot(self, potx, poty, potw, poth, pi):
        for i in range(0, len(self.sprites)):
            if(self.sprites[i].isPot()):
                if(self.sprites[i].detectCollision(potx, poty, potw, poth)):
                    self.sprites[i].breakPot()
                    self.moving_pots[pi].breakPot()
                    return True
            elif(self.sprites[i].isBrick()):
                if(self.sprites[i].detectCollision(potx, poty, potw, poth)):
                    #console.log("pot hit brick")
                    self.moving_pots[pi].breakPot()
                    return True
        return False


    def throwBoomerang(self):
        #state for boomerang
        s = 0
        #set link current position and destination
        bx = self.link.x
        by = self.link.y
        bw = self.link.w
        bh = self.link.h
        dir = self.link.dir

        if(int(dir / 5) == 0):#down
            by += 15
        elif (int(dir / 5) == 1):#left
            bx -= 15
        elif (int(dir / 5) == 2):#right
            bx += 15
        elif (int(dir / 5) == 3):#up
            by -= 15

        #add  boomerang to sprites
        bi = len(self.boomerangs)
        self.boomerangs.append(Boomerang(self.pygame, dir, bx, by, 'images/boomerang1.jpg'))


        if(self.detectCollisionForBoomerang(bx, by, bw, bh) == False):
            self.boomerangs[bi].update(s, bx, by)
        else:
            self.boomerangs.pop(bi)

    def detectCollisionForBoomerang(self, bx, by, bw, bh):
        for i in range(0, len(self.sprites)):
            if(self.sprites[i].isPot()):
                if(self.sprites[i].state == 0):
                    if(self.sprites[i].detectCollision(bx, by, bw, bh)):
                        self.sprites[i].breakPot()
                        self.moving_pots.append(self.sprites[i])
                        self.sprites.pop(i)
                        return True



            elif(self.sprites[i].isBrick()):
               if(self.sprites[i].detectCollision(bx, by, bw, bh)):
                   #System.out.println("boomerang hit brick")
                   return True

        return False
