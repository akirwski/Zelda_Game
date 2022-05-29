#!/bin/bash
set -u -e
javac Game.java View.java Controller.java Model.java Brick.java Json.java Link.java Sprite.java Pot.java Boomerang.java
java Game
