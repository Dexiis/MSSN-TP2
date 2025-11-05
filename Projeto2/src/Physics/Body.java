package Physics;

import processing.core.PApplet;
import processing.core.PVector;

public class Body extends Mover {

	protected int color;
	protected float radius;
	protected String name;

	public Body(String name, PVector posisiton, PVector velocity, float mass, float radius, int color) {
		super(posisiton, velocity, mass);
		this.color = color;
		this.radius = radius;
		this.name = name;
	}

	public Body(PVector pos) {
		super(pos, new PVector(), 0f);
	}

	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		float[] pp = plt.getPixelCoord(position.x, position.y);
		p.noStroke();
		p.fill(color);
		p.circle(pp[0], pp[1], radius); 
		p.popStyle();
	}
}