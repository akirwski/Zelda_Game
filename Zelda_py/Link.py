from View import View
from Sprite import Sprite
class Link(Sprite):
    def __init__(self, pygame, x, y, image_url):
        super().__init__(pygame, x, y, image_url)
        self.direction = 0
        self.px = self.x
        self.py = self.y
        self.link_images = []
        self.loadImage()

    def loadImage(self):
        for i in range(0, 20):
            self.link_images.append("images/" + str(i) + ".jpg")

    def update(self, dir, x, y):
        self.x = x
        self.y = y
        self.direction = dir

        View.updateScrollX(x)
        View.updateScrollY(y)

    def updateImage(self):
        self.image = self.pygame.image.load(self.link_images[self.direction])

    def getOut(self):
        self.x = self.px
        self.y = self.py

    def isLink(self):
        return True

    def savePrevLocation(self):
        self.px = self.x
        self.py = self.y
