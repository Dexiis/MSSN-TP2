package TP2.Bodies;

import TP2.Core.*;

import processing.core.PApplet;
import processing.core.PVector;

public class Body extends Movement {

	protected int color;
	protected float radius;
	protected float[] positions;

	protected Body(PVector posisiton, PVector velocity, float mass, float radius, int color) {
		super(posisiton, velocity, mass);
		this.color = color;
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		positions = plt.getPixelCoord(position.x, position.y);
		float[] r = plt.getVectorCoord(radius, radius);
		p.noStroke();
		p.fill(color);
		p.circle(positions[0], positions[1], 2 * r[0]);
		p.popStyle();
	}
}