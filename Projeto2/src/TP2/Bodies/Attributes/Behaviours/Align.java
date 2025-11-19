package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Align extends Behaviour {

	public Align(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector desiredVelocity = me.getVelocity().copy();
		for (Body body : me.getEye().getFarSight())
			desiredVelocity.add(body.getVelocity());

		return desiredVelocity.div(me.getEye().getFarSight().size() + 1);
	}

}
