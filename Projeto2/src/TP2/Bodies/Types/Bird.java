package TP2.Bodies.Types;

import TP2.Bodies.*;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Bird extends Body {

	private PImage[] birdImages = new PImage[2];

	private boolean imageFlag = false;
	private int lastUpdateTime = 0;
	
	private PApplet parent;

	public Bird(PVector posisiton, PVector velocity, float mass, float radius, int color, PApplet p) {
		super(posisiton, velocity, mass, radius, color);
		
		this.parent = p;

		birdImages[0] = p.loadImage("Resources/Bird1.png");
		birdImages[1] = p.loadImage("Resources/Bird2.png");
	}
	
	public boolean isOutOfBounds() {
		return this.getPosition().x > parent.width;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		positions = plt.getPixelCoord(position.x, position.y);

		p.pushMatrix();

		// Set the anchor point to the center of the soldier
		p.translate(positions[0], positions[1]);

		if (p.millis() - lastUpdateTime > 200) {
			imageFlag = !imageFlag;
			lastUpdateTime = p.millis();
		}

		p.imageMode(PConstants.CENTER);
		p.image(this.birdImages[imageFlag ? 1 : 0], 0, 0, 25, 25);

		p.popMatrix();

		p.popStyle();
	}
}