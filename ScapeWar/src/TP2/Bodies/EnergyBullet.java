package TP2.Bodies;

import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class EnergyBullet extends Body {
	protected float radius;
	protected int color;
	protected int damage;

	public EnergyBullet(PVector posisiton, PVector velocity, float radius, float mass, int color) {
		super(posisiton, velocity, radius, 0f, color);
		this.color = color;
		this.radius = radius;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		// TODO Auto-generated method stub

	}
}
