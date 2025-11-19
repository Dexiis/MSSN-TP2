package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;
import processing.core.PVector;

public class Arrive extends Behaviour {

	private float potency;

	public Arrive(float weight, float k) {
		super(weight);
		this.potency = k;
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.getEye().getTarget();
		PVector direction = me.getToroidalDistanceVector(bodyTarget.getPosition());

		float distance = direction.mag();
		float breakingDistance = me.getDNA().radiusArrive;
		float desiredSpeed;

		if (distance >= breakingDistance)
			desiredSpeed = me.getDNA().maxSpeed;
		else {
			float speedRatio = (float) Math.pow(distance / breakingDistance, potency);
			desiredSpeed = me.getVelocity().mag() * speedRatio;
		}

		PVector desiredVelocity = direction.normalize();
		desiredVelocity.mult(desiredSpeed);

		return desiredVelocity;
	}
}