package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class AvoidObstacle extends Behaviour {

	public AvoidObstacle(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		float s = hasObstacle(me);
		if (s == 0)
			return me.getVelocity().copy();
		PVector vd = new PVector(me.getVelocity().y, -me.getVelocity().x);
		if (s > 0)
			vd.mult(-1);
		return vd;
	}

	private float hasObstacle(Boid me) {
		for (Body b : me.getEye().getFarSight()) {
			PVector r = PVector.sub(b.getPosition(), me.getPosition());
			PVector vd = new PVector(me.getVelocity().y, -me.getVelocity().x);
			return PVector.dist(vd, r);
		}
		return 0;
	}
}