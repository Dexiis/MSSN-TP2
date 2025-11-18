package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;

import processing.core.PVector;

public class Wander extends Behaviour {

	public Wander(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		float phiWander = me.getPhiWander();
		
		PVector center = me.getPosition().copy();
		center.add(me.getVelocity().copy().mult(me.getDNA().deltaTWander));
		
		PVector target = new PVector(me.getDNA().radiusWander * (float) Math.cos(phiWander),
				me.getDNA().radiusWander * (float) Math.sin(phiWander));
		target.add(center);
		
		phiWander += 2 * (Math.random() - 0.5) * me.getDNA().deltaPhiWander;
		return PVector.sub(target, me.getPosition());
	}

}
