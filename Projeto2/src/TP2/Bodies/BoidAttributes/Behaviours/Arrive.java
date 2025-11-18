package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;

import processing.core.PVector;

public class Arrive extends Behaviour {

	public Arrive(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector vd = PVector.sub(me.getEye().getTarget().getPosition(), me.getPosition());
		float distance = vd.mag();
		float R = me.getDNA().radiusArrive;
		if (distance < R)
			vd.mult(distance / R);
		return vd;
	}

}
