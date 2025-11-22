package TP2.Bodies.Attributes;

import TP2.Bodies.Boid;

import processing.core.PVector;

public interface IBehaviour {
	public PVector getDesiredVelocity(Boid me);

	public void setWeight(float weight);

	public float getWeight();
}
