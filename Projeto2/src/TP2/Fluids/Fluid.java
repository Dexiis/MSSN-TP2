package TP2.Fluids;

import TP2.Bodies.Body;
import processing.core.PApplet;
import processing.core.PVector;

public class Fluid {

	private float density;

	protected Fluid(float density) {
		this.density = density;
	}

	public PVector drag(Body body) {
		float area = PApplet.pow(body.getRadius(), 2.0f) * PApplet.PI;
		float magnitude = body.getVelocity().mag();
		return PVector.mult(body.getVelocity(), -0.5f * density * area * magnitude);
	}

}
