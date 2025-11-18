package TP2.Scenarios;

import TP2.Bodies.CelestialBody;
import TP2.Bodies.Body;
import TP2.Bodies.Particle;
import TP2.Core.*;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class SolarSystem extends PApplet {

	private ArrayList<CelestialBody> bodies;
	private ArrayList<CelestialBody> asteroids;
	private ArrayList<Particle> particles;
	private ArrayList<PVector> stars;

	private static final float EARTH_DISTANCE = 1.496e11f;

	private static final float PLANET_RADIUS_SCALE = 1000;
	private static final float SUN_RADIUS_SCALE = 40;

	static final String[] NAMES = { "Sun", "Mercury", "Venus", "Earth", "Mars", "Asteroid Belt" };

	static final float[] DISTANCES = { 0.0f, 0.387f * EARTH_DISTANCE, 0.723f * EARTH_DISTANCE, 1.00f * EARTH_DISTANCE,
			1.524f * EARTH_DISTANCE, 2.5f * EARTH_DISTANCE };

	static final float[] ZOOM_FACTOR = { 0.0f, 0.387f, 0.723f, 1.00f, 1.524f, 5f };

	static final float[] SPEEDS = { 0.0f, 4.79e4f, 3.50e4f, 2.98e4f, 2.41e4f, 2.0e4f };

	static final float[] RADII = { 6.96e8f, 2.44e6f, 6.05e6f, 6.37e6f, 3.39e6f, 1.0e6f };

	static final float[] MASSES = { 1.989e30f, 3.30e23f, 4.87e24f, 5.97e24f, 6.42e23f, 1e12f };

	private float[] viewport = { 0f, 0f, 1f, 1f };
	private int zoomFactor = 3;
	private float maxViewDistance = DISTANCES[zoomFactor];
	private float currentZoomFactor = ZOOM_FACTOR[zoomFactor];
	private double[] window = { -1.2 * maxViewDistance, 1.2 * maxViewDistance, -1.2 * maxViewDistance,
			1.2 * maxViewDistance };
	private float speed = 1;
	private float lastUpdateTime;

	private SubPlot plt;
	private CelestialBody sun;

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		bodies = new ArrayList<>();
		asteroids = new ArrayList<>();
		particles = new ArrayList<>();
		initializeSolarSystem();
		background(0);

		textSize(16);
		textAlign(LEFT, TOP);
		fill(255);
	}

	public void draw() {
		int factor = 1;

		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		fill(0, 0, 0, 75);
		rect(0, 0, width, height);
		float[] pp = plt.getBoundingBox();
		rect(pp[0], pp[1], pp[2], pp[3]);

		noStroke();
		fill(255);

		sun.display(this, plt);

		for (CelestialBody asteroid : asteroids) {
			asteroid.setAcceleration(new PVector(0, 0));
			PVector force = sun.attraction(asteroid);
			asteroid.applyForce(force);
		}

		for (CelestialBody asteroid : asteroids) {
			asteroid.move(speed * dt);
			asteroid.display(this, plt);
			createParticle(asteroid);
		}

		for (Particle particle : particles)
			if (particle.isDead())
				particles.remove(particle);

		for (CelestialBody body : bodies) {
			body.setAcceleration(new PVector(0, 0));
			PVector force = sun.attraction(body);
			body.applyForce(force);
		}

		for (CelestialBody body : bodies) {
			body.move(speed * dt);
			body.display(this, plt);
		}

		for (PVector starPosition : stars) {
			float[] screenCoords = plt.getPixelCoord(starPosition.x, starPosition.y);

			float screenX = screenCoords[0];
			float screenY = screenCoords[1];

			circle(screenX, screenY, .5f);
		}

		fill(255);
		String speedText = "Velocity: " + speed + " times the normal speed";
		text(speedText, 10, 10);
		text("Controls: [↑] Speed Up, [↓] Slow Down", 10, 30);
		text("Controls: [C] 1s/s, [V] 1D/s, [B] 1M/s", 10, 50);
		text("Controls: [W] Zoom In, [S] Zoom Out,", 10, 70);

		setWindow(-1.2 * EARTH_DISTANCE * currentZoomFactor, 1.2 * EARTH_DISTANCE * currentZoomFactor,
				-1.2 * EARTH_DISTANCE * currentZoomFactor, 1.2 * EARTH_DISTANCE * currentZoomFactor);

		for (int i = ZOOM_FACTOR.length - 1; i >= 1; i--) {
			if (currentZoomFactor > ZOOM_FACTOR[i]) {
				factor = i;
				break;
			}
		}

		double adjustment = 0.01 * factor;

		if (currentZoomFactor > ZOOM_FACTOR[zoomFactor]) {
			currentZoomFactor -= adjustment;
			currentZoomFactor = max(ZOOM_FACTOR[zoomFactor], currentZoomFactor);
		} else {
			currentZoomFactor += adjustment;
			currentZoomFactor = min(ZOOM_FACTOR[zoomFactor], currentZoomFactor);
		}
	}

	private void initializeSolarSystem() {

		sun = new CelestialBody(NAMES[0], new PVector(0, -DISTANCES[0]), new PVector(SPEEDS[0], 0), MASSES[0],
				RADII[0] * SUN_RADIUS_SCALE, color(255, 180, 0));

		addCelestialBody(NAMES[1], DISTANCES[1], SPEEDS[1], MASSES[1], RADII[1], color(150, 150, 150));

		addCelestialBody(NAMES[2], DISTANCES[2], SPEEDS[2], MASSES[2], RADII[2], color(220, 150, 50));

		addCelestialBody(NAMES[3], DISTANCES[3], SPEEDS[3], MASSES[3], RADII[3], color(0, 180, 120));

		addCelestialBody(NAMES[4], DISTANCES[4], SPEEDS[4], MASSES[4], RADII[4], color(200, 50, 0));

		addAsteroidBelt();

		createStars();

	}

	private void addCelestialBody(String name, float distance, float speed, float mass, float radius, int planetColor) {

		PVector position = new PVector(0, -distance);
		PVector positionVector = position.copy();
		PVector perpendicularVector = new PVector(positionVector.y, -positionVector.x);
		perpendicularVector.normalize();
		PVector velocity = PVector.mult(perpendicularVector, speed);

		CelestialBody celestialBody = new CelestialBody(name, position, velocity, mass,
				(float) (radius * PLANET_RADIUS_SCALE), planetColor);
		bodies.add(celestialBody);
	}

	private void addAsteroidBelt() {

		float baseDistance = DISTANCES[5];
		float baseSpeed = SPEEDS[5];
		float baseRadius = RADII[5];
		float baseMass = MASSES[5];

		for (int i = 0; i < 300; i++) {
			float newDistance = randomizeValue(baseDistance);
			float newSpeed = randomizeValue(baseSpeed);
			float newRadius = randomizeValue(baseRadius);
			float newMass = randomizeValue(baseMass);

			String asteroidName = NAMES[5] + " " + (i + 1);

			float angle = random(TWO_PI);
			float x = newDistance * cos(angle);
			float y = newDistance * sin(angle);
			PVector position = new PVector(x, y);

			PVector positionVector = position.copy();
			PVector perpendicularVector = new PVector(positionVector.y, -positionVector.x);
			perpendicularVector.normalize();
			PVector velocity = PVector.mult(perpendicularVector, newSpeed);

			CelestialBody celestialBody = new CelestialBody(asteroidName, position, velocity, newMass,
					(float) (newRadius * PLANET_RADIUS_SCALE), color(100, 100, 100));
			bodies.add(celestialBody);
		}
	}

	private float randomizeValue(float baseValue) {
		float deviation = (-0.02f) + (0.1f * random(1));
		return baseValue * (1.0f + deviation);
	}

	private void createStars() {
		stars = new ArrayList<>();
		for (int i = 0; i < 500; i++) {
			float randomDistance = random(EARTH_DISTANCE * ZOOM_FACTOR[5] * 1.5f) * 1.05f;

			float randomAngle = random(TWO_PI);

			float x = randomDistance * cos(randomAngle);
			float y = randomDistance * sin(randomAngle);

			stars.add(new PVector(x, y));
		}
	}

	private void summonAsteroid(int mouseX, int mouseY) {
		double[] mouseWorldCoords = plt.getWorldCoord(mouseX, mouseY);

		PVector targetPosition = new PVector((float) mouseWorldCoords[0], (float) mouseWorldCoords[1]);

		double[] currentWindow = plt.getWindow();

		double xMin = currentWindow[0];
		double xMax = currentWindow[1];

		float x0 = random((float) xMin, (float) xMax);

		float y0 = (float) currentWindow[3] * 1.05f;
		PVector startPosition = new PVector(x0, y0);

		PVector direction = PVector.sub(targetPosition, startPosition);
		final float LAUNCH_SPEED = 5.0e4f;

		direction.normalize();
		PVector velocity = PVector.mult(direction, LAUNCH_SPEED);

		CelestialBody newParticle = new CelestialBody("", startPosition, velocity, MASSES[5] * 2,
				(float) (RADII[5] * 2 * PLANET_RADIUS_SCALE), color(100, 100, 100));

		asteroids.add(newParticle);
	}

	private void createParticle(CelestialBody asteroid) {
		final float PARTICLE_LIFESPAN = 2629744f;

		PVector pos = asteroid.getPosition();
		PVector vel = asteroid.getVelocity();
		PVector oppositeDirection = PVector.mult(vel, -1);

		final float MAX_ANGLE_RAD = PApplet.PI / 6f;
		float randomAngle = random(-MAX_ANGLE_RAD, MAX_ANGLE_RAD);

		PVector launchDirection = oppositeDirection.copy();
		launchDirection.rotate(randomAngle);

		final float LAUNCH_SPEED_FACTOR = 0.4f;
		float launchMagnitude = vel.mag() * LAUNCH_SPEED_FACTOR;

		PVector launchVelocity = PVector.mult(launchDirection, launchMagnitude);

		PVector finalVelocity = PVector.add(vel, launchVelocity);

		Particle newParticle = new Particle(pos, finalVelocity, (float) (asteroid.getRadius() / 2),
				color(150, 150, 150), PARTICLE_LIFESPAN);

		particles.add(newParticle);
	}

	private void setWindow(double x1, double y1, double x2, double y2) {
		window[0] = x1;
		window[1] = y1;
		window[2] = x2;
		window[3] = y2;
		plt.setWindow(window);
	}

	public void keyPressed() {
		float speedIncrement = 2629744;

		if (key == CODED)
			if (keyCode == UP) {
				speed += speedIncrement;
				speed = min(speed, 2629744 * 6);
			} else if (keyCode == DOWN) {
				speed -= speedIncrement;
				speed = max(speed, -2629744 * 6);
			}

		if (key == 'c')
			speed = 1;

		if (key == 'v')
			speed = 86400;

		if (key == 'b')
			speed = 2629744;

		if (key == 'w') {
			zoomFactor--;
			zoomFactor = max(1, zoomFactor);
		}

		if (key == 's') {
			zoomFactor++;
			zoomFactor = min(NAMES.length - 1, zoomFactor);
		}
	}

	public void mousePressed() {
		summonAsteroid(mouseX, mouseY);
	}
}