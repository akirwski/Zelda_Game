from Sprite import Sprite

class Boomerang(Sprite):
    def __init__(self, pygame, direction, x, y, image_url):
        super().__init__(pygame, x, y, image_url)
        self.state = 0
        self.w = 30
        self.h = 30
        self.boomerang_images = []
        self.dir = direction
        self.loadImage()

    def loadImage(self):
        for i in range(0, 4):
            self.boomerang_images.append("images/boomerang" + str(i+1) + ".jpg")

    #update
    def updateImage(self):
        self.image = self.pygame.image.load(self.boomerang_images[self.state])


    def update(self, s, bx, by):
        self.x = bx
        self.y = by
        self.state = s
