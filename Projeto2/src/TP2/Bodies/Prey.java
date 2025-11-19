package TP2.Bodies;

import java.util.List;

import TP2.Bodies.Attributes.Eye;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class Prey extends Boid {

	public Prey(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt,
			List<Body> allTrackingBodies) {
		super(pos, mass, radius, color, Entity.PREY, p, plt);
		setEye(new Eye(this, allTrackingBodies));
	}

}
