package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;

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
