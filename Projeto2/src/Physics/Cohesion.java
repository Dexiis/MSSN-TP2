package Physics;

import processing.core.PVector;

public class Cohesion extends Behaviour {

	public Cohesion(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector target = me.getPosition().copy();
		for (Body body : me.eye.getFarSight())
			target.add(body.getPosition());
		target.div(me.eye.getFarSight().size() + 1);
		return PVector.sub(target, me.getPosition());
	}

}
