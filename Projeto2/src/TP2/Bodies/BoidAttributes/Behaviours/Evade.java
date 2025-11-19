package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;

import processing.core.PVector;

public class Evade extends Behaviour {

	public Evade(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.getEye().getTarget();
		PVector d = bodyTarget.getVelocity().mult(me.getDNA().deltaTPursuit);
		PVector target = PVector.add(bodyTarget.getPosition(), d);
		PVector vd = PVector.sub(target, me.getPosition());
		return vd.mult(-1);
	}
}
