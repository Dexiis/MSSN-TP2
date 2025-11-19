package TP2.Bodies.Attributes;

import TP2.Bodies.Entity;

public class DNA {

	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionSafeDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;

	public DNA(Entity entity) {
		if (entity == Entity.PREY) {
			maxSpeed = random(1f, 3f);
			maxForce = random(4f, 7f);

			visionDistance = random(2f, 5f);
			visionSafeDistance = 0.75f * visionDistance;
			visionAngle = (float) Math.PI * 0.75f;

			deltaTPursuit = random(0.5f, 1f);

			radiusArrive = random(5, 10);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(1f, 5f);

			deltaPhiWander = (float) Math.PI / 8;
			
		} else if (entity == Entity.PREDATOR) {
			maxSpeed = random(2f, 4f);
			maxForce = random(5f, 8f);

			visionDistance = random(4f, 8f);
			visionSafeDistance = 0.75f * visionDistance;
			visionAngle = (float) Math.PI * 0.2f;

			deltaTPursuit = random(0.5f, 1f);

			radiusArrive = random(5, 10);

			deltaTWander = random(.3f, .6f);
			radiusWander = random(2f, 3f);

			deltaPhiWander = (float) Math.PI / 8;
		}
	}

	public DNA() {
		maxSpeed = random(1f, 3f);
		maxForce = random(4f, 7f);

		visionDistance = random(1.5f, 3f);
		visionSafeDistance = 0.25f * visionDistance;
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
		visionSafeDistance = dna.visionSafeDistance;
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
