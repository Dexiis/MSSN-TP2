package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Wander extends Behaviour {

	public Wander(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {

		float newPhiWander = me.getPhiWander();
		newPhiWander += 2 * (Math.random() - 0.5) * me.getDNA().getDeltaPhiWander();
		me.setPhiWander(newPhiWander);

		PVector center = me.getVelocity().copy();
		center.normalize().mult(me.getDNA().getDeltaTWander());
		center.add(me.getPosition());

		PVector targetDisplacement = new PVector(me.getDNA().getRadiusWander() * (float) Math.cos(newPhiWander),
				me.getDNA().getRadiusWander() * (float) Math.sin(newPhiWander));
		PVector targetPosition = PVector.add(center, targetDisplacement);

		PVector desiredVelocity = me.getToroidalDistanceVector(targetPosition);

		desiredVelocity.setMag(me.getDNA().getMaxSpeed());

		return desiredVelocity;
	}
}