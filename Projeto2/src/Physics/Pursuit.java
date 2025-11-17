package Physics;

import processing.core.PVector;

public class Pursuit extends Behaviour {

	public Pursuit(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.eye.target;
		PVector d = bodyTarget.getVelocity().mult(me.dna.deltaTPursuit);
		PVector target = PVector.add(bodyTarget.getPosition(), d);
		return PVector.sub(target, me.getPosition());
	}

}
