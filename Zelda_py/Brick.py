from Sprite import Sprite
class Brick(Sprite):
    def __init__(self, pygame, x, y, image_url):
        super().__init__(pygame,  x, y, image_url)

    def isBrick(self):
        return True
