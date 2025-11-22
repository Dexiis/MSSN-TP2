package TP2.Bodies.Types;

import TP2.Bodies.*;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

public class CelestialBody extends Body {

	protected String name;
	protected PImage image = null;
	protected int health;

	public CelestialBody(String name, PVector position, PVector velocity, float mass, float radius, int health,
			int color, String image, PApplet p) {
		super(position, velocity, mass, radius, color);
		this.name = name;
		this.health = health;

		if (image != null)
			this.image = p.loadImage(image);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public PImage getImage() {
		return image;
	}

	public void setImage(PImage image) {
		this.image = image;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}
	
	public void hitBy(EnergyBullet bullet) {
		health -= bullet.getDamage();
	}

	public boolean isDead() {
		if (health <= 0)
			return true;
		return false;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();

		positions = plt.getPixelCoord(position.x, position.y);
		float[] r = plt.getVectorCoord(radius, radius);
		if (image != null) {
			p.imageMode(PConstants.CENTER);
			p.image(this.image, positions[0], positions[1], 2 * r[0], 2 * r[0]);
		} else {
			p.noStroke();
			p.fill(color);
			p.circle(positions[0], positions[1], 2 * r[0]);
		}

		p.popStyle();
	}

}
