package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Flee extends Behaviour {

	public Flee(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.getEye().getTarget();
		PVector bodyTargetDistance = me.getToroidalDistanceVector(bodyTarget.getPosition());
		
		return bodyTargetDistance.mult(-1).setMag(me.getDNA().maxForce);
	}
}