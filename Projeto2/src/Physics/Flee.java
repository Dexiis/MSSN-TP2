package Physics;

import processing.core.PVector;

public class Flee extends Behaviour {
	
	public Flee(float weight) {
		super(weight);
	}
	
	@Override
	public PVector getDesiredVelocity(Boid me) {
		Body bodyTarget = me.eye.target;
		PVector vd = PVector.sub(bodyTarget.getPosition(),me.getPosition());
		return vd.mult(-1);
	}

}
