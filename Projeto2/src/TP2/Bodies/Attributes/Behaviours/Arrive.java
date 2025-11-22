package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Arrive extends Behaviour {

	private float breakPotency = 30f;

	public Arrive(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		float desiredSpeed;

		Body bodyTarget = me.getEye().getTarget();
		PVector direction = me.getToroidalDistanceVector(bodyTarget.getPosition());
		float distance = direction.mag();
		float breakingDistance = me.getDNA().getRadiusArrive();

		if (distance >= breakingDistance)
			desiredSpeed = me.getDNA().getMaxSpeed();
		else {
			float speedRatio = (float) Math.pow(distance / breakingDistance, breakPotency);
			desiredSpeed = me.getVelocity().mag() * speedRatio;
		}

		PVector desiredVelocity = direction.normalize();
		desiredVelocity.mult(desiredSpeed);

		return desiredVelocity;
	}
}