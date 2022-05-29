import time
from Sprite import Sprite

class Pot(Sprite):
    def __init__(self, pygame, x, y, image_url):
        super().__init__(pygame, x, y, image_url)
        self.state = 0
        self.dir = 0
        self.pot_images = []
        self.loadImage()
        self.t0 = 0.0

    def loadImage(self):
        self.pot_images.append("images/pot.jpg")
        self.pot_images.append("images/pot_broken.jpg")

    def updateImage(self):
        self.image = self.pygame.image.load(self.pot_images[self.state])

    def update(self, direction, potx, poty):
        self.x = potx
        self.y = poty
        self.dir = direction

    def isPot(self):
        return True


    def breakPot(self):
        self.state = 1
        self.t0 = time.time()
