package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Cohesion extends Behaviour {

	public Cohesion(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector target = me.getPosition().copy();
		for (Body body : me.getEye().getFarSight())
			target.add(body.getPosition());
		target.div(me.getEye().getFarSight().size() + 1);
		return me.getToroidalDistanceVector(target);
	}

}
