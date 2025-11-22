package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Separate extends Behaviour {

	public Separate(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector desiredVelocity = new PVector();
		for (Body body : me.getEye().getNearSight()) {
			PVector direction = me.getToroidalDistanceVector(body.getPosition()).mult(-1);
			float dir = direction.mag();
			direction.div(dir * dir);
			desiredVelocity.add(direction);
		}
		
		return desiredVelocity;
	}

}
