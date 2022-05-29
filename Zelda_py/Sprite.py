class Sprite():
    def __init__(self, pygame, x, y, image_url):
        self.pygame = pygame
        self.image = self.pygame.image.load(image_url)
        self.rect = self.image.get_rect()
        self.w = 75 - 1
        self.h = 75 - 1
        self.x = x
        self.y = y

    def isLink(self):
        return False

    def isPot(self):
        return False

    def isBrick(self):
        return False

    def detectCollision(self, X, Y, W, H):
        if(self.y + self.h < Y):
            return False;
        if(self.y > Y + H):
            return False;
        if(self.x + self.w < X):
            return False;
        if(self.x > X + W):
            return False;

        return True;
