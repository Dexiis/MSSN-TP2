package TP2.Bodies.Attributes;

import TP2.Bodies.Entity;

public class DNA {

	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionNearDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;
	public float visionNearAngle;

	public DNA(Entity entity) {
		if (entity == Entity.PREDATOR) {
			maxSpeed = random(3f, 5f);
			maxForce = random(7f, 10f);

			visionDistance = random(9f, 11f);
			visionNearDistance = 0.25f * visionDistance;
			visionAngle = (float) Math.PI * 0.2f;
			visionNearAngle = (float) Math.PI * 0.6f;

			deltaTPursuit = random(0.5f, 1f);

			radiusArrive = random(5, 10);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

			deltaPhiWander = (float) Math.PI / 8;
		} else if (entity == Entity.PREY) {
			maxSpeed = random(2f, 4f);
			maxForce = random(5f, 8f);

			visionDistance = random(3f, 5f);
			visionNearDistance = 0.60f * visionDistance;
			visionAngle = (float) Math.PI * 0.75f;
			visionNearAngle = (float) Math.PI;

			deltaTPursuit = random(0.5f, 1f);

			radiusArrive = random(5, 10);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

			deltaPhiWander = (float) Math.PI / 8;
		} else {
			maxSpeed = random(1f, 3f);
			maxForce = random(4f, 7f);

			visionDistance = random(1.5f, 2.5f);
			visionNearDistance = 0.75f * visionDistance;
			visionAngle = (float) Math.PI * 0.75f;
			visionNearAngle = (float) Math.PI;

			deltaTPursuit = random(0.5f, 1f);

			radiusArrive = random(5, 10);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(3f, 5f);

			deltaPhiWander = (float) Math.PI / 8;
		}
	}

	public DNA() {
		maxSpeed = random(4f, 7f);
		maxForce = random(4f, 7f);

		visionDistance = random(1.5f, 3f);
		visionNearDistance = 0.25f * visionDistance;
		visionAngle = (float) Math.PI * 0.5f;

		deltaTPursuit = random(0.5f, 1f);

		radiusArrive = random(5, 10);

		deltaTWander = random(.3f, .6f);
		radiusWander = random(1f, 3f);

		deltaPhiWander = (float) Math.PI / 8;
	}

	public DNA(DNA dna, boolean mutate) {
		maxSpeed = dna.maxSpeed;
		maxForce = dna.maxForce;

		visionDistance = dna.visionDistance;
		visionNearDistance = dna.visionNearDistance;
		visionAngle = dna.radiusArrive;

		deltaTPursuit = dna.deltaTPursuit;

		deltaTWander = dna.deltaTWander;
		radiusWander = dna.radiusWander;
		deltaPhiWander = dna.deltaPhiWander;

		if (mutate)
			mutate();
	}

	private void mutate() {
		maxSpeed += random(-0.2f, 0.2f);
		maxSpeed = Math.max(0, maxSpeed);
	}

	public static float random(float min, float max) {
		return (float) (min + (max - min) * Math.random());
	}

}
