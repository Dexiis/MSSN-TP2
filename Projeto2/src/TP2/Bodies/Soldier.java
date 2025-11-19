package TP2.Bodies;

import processing.core.PVector;

public class Soldier extends Body {
	
	private boolean parachuteOpen = false;
	private int parachuteOpenHeight;
	private final float initialRadius;
	
	public Soldier(PVector posititon, PVector velocity, float mass, float radius, int color) {
		super(posititon, velocity, mass, radius, color);
		this.initialRadius = radius;
		
	}
	
	public void setParachuteHeight(int height) { this.parachuteOpenHeight = height; }
	public void checkParachute() { this.parachuteOpen = this.getPosition().y <= this.parachuteOpenHeight; }
	public boolean isParachuteOpen() { return this.parachuteOpen; }
	public void updateRadius() {
		if(isParachuteOpen())
			this.radius = this.initialRadius + 5;
	}
	
}
