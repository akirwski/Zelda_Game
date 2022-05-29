class View():
    viewX = 750
    viewY = 600
    scrollPosX = 0
    scrollPosY = 0
    def __init__(self, pygame, model):
        screen_size = (750,600)
        self.pygame = pygame
        self.model = model
        self.screen = self.pygame.display.set_mode(screen_size, 32)
        View.scrollPosX = 0
        View.scrollPosY = 0

    def update(self):
        self.screen.fill([0,200,100])

        #update link
        for sprite in self.model.sprites:
            if(sprite.isBrick() == False):
                sprite.updateImage()
            self.updateOnView(sprite)

        #moving pots
        for sprite in self.model.moving_pots:
            sprite.updateImage()
            self.updateOnView(sprite)

        for sprite in self.model.boomerangs:
            sprite.updateImage()
            self.updateOnView(sprite)


        self.pygame.display.flip()

    def updateOnView(self, sprite):
        if(View.scrollPosX == 0 and View.scrollPosY == 0):
            self.screen.blit(sprite.image, (sprite.x , sprite.y))
        elif(View.scrollPosX == View.viewX and View.scrollPosY == 0):
            self.screen.blit(sprite.image, (sprite.x - View.scrollPosX , sprite.y))
        elif(View.scrollPosX == 0 and View.scrollPosY == View.viewY):
            self.screen.blit(sprite.image, (sprite.x, sprite.y - View.scrollPosY))
        elif(View.scrollPosX == View.viewX and View.scrollPosY == View.viewY):
            self.screen.blit(sprite.image, ((sprite.x - View.scrollPosX) , (sprite.y - View.scrollPosY)))

    def updateScrollX(sx):
        if(sx >= View.viewX):
            View.scrollPosX = View.viewX
        else:
            View.scrollPosX = 0

    def updateScrollY(sy):
        if(sy >= View.viewY):
            View.scrollPosY = View.viewY
        else:
            View.scrollPosY = 0
