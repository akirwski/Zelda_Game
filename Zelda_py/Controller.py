class Controller():
    def __init__(self, pygame, model):
        self.pygame = pygame
        self.model = model
        self.index = 0
        self.keep_going = True

    def update(self):
        for event in self.pygame.event.get():
            if event.type == self.pygame.QUIT:
                self.keep_going = False
            elif event.type == self.pygame.KEYDOWN:
                if event.key == self.pygame.K_ESCAPE:
                    self.keep_going = False
            #elif event.type == self.pygame.MOUSEBUTTONUP:
            #self.model.set_dest(pygame.mouse.get_pos())

            direction = -1
            stop = -1
            keys = self.pygame.key.get_pressed()
            if(event.type == self.pygame.KEYDOWN):
                if keys[self.pygame.K_LEFT]:
                    direction = 6 + self.index
                elif keys[self.pygame.K_RIGHT]:
                    direction= 11 + self.index
                elif keys[self.pygame.K_UP]:
                    direction= 16 + self.index
                elif keys[self.pygame.K_DOWN]:
                    direction= 1 + self.index
                elif keys[self.pygame.K_RCTRL] or keys[self.pygame.K_LCTRL]:
                    self.model.throwBoomerang()
                    direction = -1
                if(self.index < 3):
                    self.index+=1
                else:
                    self.index = 0
                if(direction != -1):
                    self.model.update(direction)

            if event.type == self.pygame.KEYUP:
                if keys[self.pygame.K_LEFT]:
                    stop = 5
                elif keys[self.pygame.K_RIGHT]:
                    stop = 10
                elif keys[self.pygame.K_UP]:
                    stop = 15
                elif keys[self.pygame.K_DOWN]:
                    stop = 10
                if(stop != -1):
                    self.model.stopSprite(stop)
