package TP2.Bodies.BoidAttributes;

import TP2.Bodies.Boid;

import processing.core.PVector;

public interface IBehaviour {
	public PVector getDesiredVelocity(Boid me);

	public void setWeight(float weight);

	public float getWeight();
}
