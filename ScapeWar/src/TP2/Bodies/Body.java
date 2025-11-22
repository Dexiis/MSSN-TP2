package TP2.Bodies;

import TP2.Core.*;

import processing.core.PApplet;
import processing.core.PVector;

public abstract class Body extends Movement {

	protected int color;
	protected float radius;
	protected float[] positions;

	protected Body(PVector posisiton, PVector velocity, float radius, float mass, int color) {
		super(posisiton, velocity, mass);
		this.color = color;
		this.radius = radius;
	}

	protected Body(PVector posisiton, PVector velocity, float mass, int color) {
		super(posisiton, velocity, mass);
		this.color = color;
	}

	protected Body(PVector posisiton, PVector velocity, int color) {
		super(posisiton, velocity);
		this.color = color;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float[] getPositions() {
		return positions;
	}

	public void setPositions(float[] positions) {
		this.positions = positions;
	}

	public abstract void display(PApplet p, SubPlot plt);
}