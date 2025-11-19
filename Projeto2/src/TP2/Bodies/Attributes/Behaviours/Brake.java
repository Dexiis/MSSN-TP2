package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
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
