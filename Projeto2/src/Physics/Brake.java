package Physics;

import processing.core.PVector;

public class Brake extends Behaviour {
	
	public Brake(float weight) {
		super(weight);
	}
	
	@Override
	public PVector getDesiredVelocity(Boid me) {
		return new PVector();
	}

}
