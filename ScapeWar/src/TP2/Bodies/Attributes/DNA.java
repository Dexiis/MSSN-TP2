package TP2.Bodies.Attributes;

import TP2.Bodies.Boid;
import TP2.Bodies.Types.*;

public class DNA {

	protected float maxSpeed;
	protected float maxForce;
	protected float visionDistance;
	protected float visionAngle;
	protected float visionNearDistance;
	protected float visionNearAngle;
	protected float visionShottingDistance;
	protected float visionShottingAngle;
	protected float deltaTPursuit;
	protected float deltaTWander;
	protected float radiusWander;
	protected float deltaPhiWander;
	protected float radius;
	protected float mass;

	public DNA(Boid boid) {

		if (boid instanceof Red) {
			maxSpeed = random(4.5f, 9f);
			maxForce = random(4.5f, 9f);

			visionDistance = random(15f, 30f);
			visionAngle = (float) Math.PI * 0.30f;

			visionNearDistance = 0.25f * visionDistance;
			visionNearAngle = (float) Math.PI * 0.4f;

			visionShottingDistance = random(12f, visionDistance);
			visionShottingAngle = (float) Math.PI * 0.2f;

			radius = random(1f, 2f);
			mass = random(1f, 2f);

			deltaTPursuit = random(0.5f, 1f);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

			deltaPhiWander = (float) Math.PI / 8;

		} else if (boid instanceof Blue) {
			maxSpeed = random(3f, 6f);
			maxForce = random(3f, 6f);

			visionDistance = random(10f, 20f);
			visionAngle = (float) Math.PI * 0.5f;

			visionNearDistance = 0.5f * visionDistance;
			visionNearAngle = (float) Math.PI * 0.6f;

			visionShottingDistance = random(7f, visionDistance);
			visionShottingAngle = (float) Math.PI * 0.4f;

			radius = random(2f, 4f);
			mass = random(2f, 4f);

			deltaTPursuit = random(0.5f, 1f);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

			deltaPhiWander = (float) Math.PI / 8;

		} else if (boid instanceof Neutral) {
			maxSpeed = random(1f, 2f);
			maxForce = random(1f, 2f);

			visionDistance = random(20f, 40f);
			visionAngle = (float) Math.PI * 0.2f;

			visionNearDistance = 0.25f * visionDistance;
			visionNearAngle = (float) Math.PI * 0.9f;

			visionShottingDistance = random(15f, visionDistance);
			visionShottingAngle = (float) Math.PI * 0.1f;

			radius = random(6f, 10f);
			mass = random(6f, 10f);

			deltaTPursuit = random(0.5f, 1f);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

		} else {
			System.err.println("Não existe esta classe definida.");
		}
	}

	public float getMaxSpeed() {
		return maxSpeed;
	}

	public void setMaxSpeed(float maxSpeed) {
		this.maxSpeed = maxSpeed;
	}

	public float getMaxForce() {
		return maxForce;
	}

	public void setMaxForce(float maxForce) {
		this.maxForce = maxForce;
	}

	public float getVisionDistance() {
		return visionDistance;
	}

	public void setVisionDistance(float visionDistance) {
		this.visionDistance = visionDistance;
	}

	public float getVisionNearDistance() {
		return visionNearDistance;
	}

	public void setVisionNearDistance(float visionNearDistance) {
		this.visionNearDistance = visionNearDistance;
	}

	public float getVisionAngle() {
		return visionAngle;
	}

	public void setVisionAngle(float visionAngle) {
		this.visionAngle = visionAngle;
	}

	public float getDeltaTPursuit() {
		return deltaTPursuit;
	}

	public void setDeltaTPursuit(float deltaTPursuit) {
		this.deltaTPursuit = deltaTPursuit;
	}

	public float getDeltaTWander() {
		return deltaTWander;
	}

	public void setDeltaTWander(float deltaTWander) {
		this.deltaTWander = deltaTWander;
	}

	public float getRadiusWander() {
		return radiusWander;
	}

	public void setRadiusWander(float radiusWander) {
		this.radiusWander = radiusWander;
	}

	public float getDeltaPhiWander() {
		return deltaPhiWander;
	}

	public void setDeltaPhiWander(float deltaPhiWander) {
		this.deltaPhiWander = deltaPhiWander;
	}

	public float getVisionNearAngle() {
		return visionNearAngle;
	}

	public void setVisionNearAngle(float visionNearAngle) {
		this.visionNearAngle = visionNearAngle;
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public float getMass() {
		return mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public static float random(float min, float max) {
		return (float) (min + (max - min) * Math.random());
	}

}
