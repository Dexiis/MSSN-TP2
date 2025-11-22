package TP2.Bodies.Types;

import TP2.Bodies.*;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class Soldier extends Body {

	private boolean parachuteOpen = false;
	private int parachuteOpenHeight;
	private final float initialRadius;

	private PImage soldierImage = null;
	private PImage parachuteImage = null;

	public Soldier(PVector posititon, PVector velocity, float mass, float radius, int color, PApplet p) {
		super(posititon, velocity, mass, radius, color);
		this.initialRadius = radius;

		this.soldierImage = p.loadImage("Resources/Stickman.png");
		this.parachuteImage = p.loadImage("Resources/Parachute.png");
	}

	public void setParachuteHeight(int height) {
		this.parachuteOpenHeight = height;
	}

	public void checkParachute() {
		this.parachuteOpen = this.getPosition().y <= this.parachuteOpenHeight;
	}

	public boolean isParachuteOpen() {
		return this.parachuteOpen;
	}

	public boolean isGrounded() {
		return this.getPosition().y <= 10;
	}

	public void updateRadius() {
		if (isParachuteOpen())
			this.radius = this.initialRadius + 5;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();

		positions = plt.getPixelCoord(position.x, position.y);

		p.pushMatrix();

		// Set the anchor point to the center of the soldier
		p.translate(positions[0], positions[1]);

		if (!isGrounded()) {
			if (!isParachuteOpen()) {
				p.scale(1, -1);
			} else {
				p.imageMode(PConstants.CENTER);
				p.image(this.parachuteImage, 0, -20, 50, 50);
			}
		}

		p.imageMode(PConstants.CENTER);
		p.image(this.soldierImage, 0, 0, 30, 30);

		p.popMatrix();

		p.popStyle();
	}

}