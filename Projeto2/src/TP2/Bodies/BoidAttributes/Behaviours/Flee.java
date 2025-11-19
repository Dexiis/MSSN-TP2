package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;
import processing.core.PVector;

public class Flee extends Behaviour {

	public Flee(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.getEye().getTarget();
		PVector desiredVelocity = me.getToroidalDistanceVector(bodyTarget.getPosition());
        desiredVelocity.mult(-1).setMag(me.getDNA().maxSpeed);

		return desiredVelocity;
	}

}