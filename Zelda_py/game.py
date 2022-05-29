import pygame
import time

from time import sleep
from Model import Model
from View import View
from Controller import Controller
from pygame.locals import*


# Created a class object
pygame.init()

m = Model(pygame)
v = View(pygame, m)
c = Controller(pygame, m)

while c.keep_going:
    c.update()
    m.updateSprite();
    m.deleteBrokenPot();
    v.update()
    sleep(0.04)
