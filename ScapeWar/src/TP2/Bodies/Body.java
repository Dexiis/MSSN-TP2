package TP2.Bodies;

import TP2.Core.*;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Body extends Movement {

	protected int color;
	protected float radius;
	protected float[] positions;

	protected Body(PVector posisiton, PVector velocity, float mass, float radius, int color) {
		super(posisiton, velocity, mass);
		this.color = color;
		this.radius = radius;
	}

	protected Body(PVector posisiton, PVector velocity, int color) {
		super(posisiton, velocity);
		this.color = color;
	}

	public float getRadius() {
		return radius;
	}

	public abstract void display(PApplet p, SubPlot plt);
}