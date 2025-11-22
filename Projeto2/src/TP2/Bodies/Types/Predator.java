package TP2.Bodies.Types;

import TP2.Bodies.*;
import java.util.List;

import TP2.Bodies.Attributes.*;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class Predator extends Boid {

	public Predator(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt,
			List<Body> allTrackingBodies) {
		super(pos, mass, radius, color, p, plt);
		setEye(new Eye(this, allTrackingBodies));
	}

	public Predator(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, mass, radius, color, p, plt);
	}

}
