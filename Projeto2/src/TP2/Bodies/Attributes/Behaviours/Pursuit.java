package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Pursuit extends Behaviour {

	public Pursuit(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.getEye().getTarget();
		
		PVector d = bodyTarget.getVelocity().mult(me.getDNA().deltaTPursuit);
		PVector target = PVector.add(bodyTarget.getPosition(), d);
		
		return me.getToroidalDistanceVector(target);
	}

}