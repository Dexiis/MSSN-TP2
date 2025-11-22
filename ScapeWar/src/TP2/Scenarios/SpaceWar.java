package TP2.Scenarios;

import processing.core.PApplet;
import TP2.Bodies.*;
import TP2.Bodies.Attributes.*;
import TP2.Bodies.Attributes.Behaviours.*;
import TP2.Bodies.Types.*;
import TP2.Core.*;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class SpaceWar extends PApplet {

	private ArrayList<CelestialBody> celestalBodies;
	private ArrayList<CelestialBody> asteroids;
	private ArrayList<Particle> particles;
	private ArrayList<PVector> stars;
	private List<Body> allTargatableBodies;
	private List<Blue> blueTeam;
	private List<Red> redTeam;
	private List<Neutral> neutralTeam;

	private static final float EARTH_DISTANCE = 1.496e11f;
	private static final float PLANET_RADIUS_SCALE = 1500;
	private static final float SUN_RADIUS_SCALE = 40;

	static final String[] NAMES = { "Sun", "Mercury", "Venus", "Earth", "Mars", "Asteroid" };

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
	private float particleCreationTimer = 0;

	private SubPlot plt;
	private CelestialBody sun;
	
	public static void main(String[] args) {
		PApplet.main(SpaceWar.class.getName());
	}

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		celestalBodies = new ArrayList<>();
		asteroids = new ArrayList<>();
		particles = new ArrayList<>();
		stars = new ArrayList<>();
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

		for (PVector starPosition : stars) {
			float[] screenCoords = plt.getPixelCoord(starPosition.x, starPosition.y);
			circle(screenCoords[0], screenCoords[1], .5f);
		}

		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle particle = particles.get(i);
			if (particle.isDead())
				particles.remove(particle);
			else {
				particle.move(speed * dt);
				particle.display(this, plt);
			}
		}

		for (CelestialBody body : celestalBodies) {
			body.setAcceleration(new PVector(0, 0));
			PVector force = sun.attraction(body);
			body.applyForce(force);
		}

		for (CelestialBody body : celestalBodies) {
			body.move(speed * dt);
			body.display(this, plt);
		}

		for (CelestialBody asteroid : asteroids) {
			asteroid.setAcceleration(new PVector(0, 0));
			PVector force = sun.attraction(asteroid);
			asteroid.applyForce(force);
		}

		particleCreationTimer += speed * dt;
		for (int i = asteroids.size() - 1; i >= 0; i--) {
			CelestialBody asteroid = asteroids.get(i);
			asteroid.move(speed * dt);
			asteroid.display(this, plt);

			if (particleCreationTimer > 5)
				createParticle(asteroid, dt);

			float distance = PVector.dist(sun.getPosition(), asteroid.getPosition());
			if (distance > EARTH_DISTANCE * 8)
				asteroids.remove(asteroid);

			else if (distance < sun.getRadius() + asteroid.getRadius())
				asteroids.remove(asteroid);

		}
		if (particleCreationTimer > 126) // 10 a cada dia
			particleCreationTimer = 0;

		sun.display(this, plt);

		fill(255);
		String speedText = "Velocity: " + speed + " times the normal speed";
		text(speedText, 10, 10);
		text("Controls: [↑] Speed Up, [↓] Slow Down", 10, 30);
		text("Controls: [C] 1s/s, [V] 1D/s, [B] 1M/s", 10, 50);
		text("Controls: [W] Zoom In, [S] Zoom Out,", 10, 70);

		for (int i = ZOOM_FACTOR.length - 1; i >= 1; i--) {
			if (currentZoomFactor > ZOOM_FACTOR[i]) {
				factor = i;
				break;
			}
		}

		setWindow(-1.2 * EARTH_DISTANCE * currentZoomFactor, 1.2 * EARTH_DISTANCE * currentZoomFactor,
				-1.2 * EARTH_DISTANCE * currentZoomFactor, 1.2 * EARTH_DISTANCE * currentZoomFactor);
	}

	private void initializeSolarSystem() {

		sun = new CelestialBody(NAMES[0], new PVector(0, -DISTANCES[0]), new PVector(SPEEDS[0], 0), MASSES[0],
				RADII[0] * SUN_RADIUS_SCALE, color(255, 180, 0), "Resources/" + NAMES[0] + ".png", this);

		addCelestialBody(NAMES[1], DISTANCES[1], SPEEDS[1], MASSES[1], RADII[1], color(150, 150, 150),
				"Resources/" + NAMES[1] + ".png");

		addCelestialBody(NAMES[2], DISTANCES[2], SPEEDS[2], MASSES[2], RADII[2], color(220, 150, 50),
				"Resources/" + NAMES[2] + ".png");

		addCelestialBody(NAMES[3], DISTANCES[3], SPEEDS[3], MASSES[3], RADII[3], color(0, 180, 120),
				"Resources/" + NAMES[3] + ".png");

		addCelestialBody(NAMES[4], DISTANCES[4], SPEEDS[4], MASSES[4], RADII[4], color(200, 50, 0),
				"Resources/" + NAMES[4] + ".png");

		addAsteroidBelt();

		createStars();

	}
	
	private void initializeBoids() {}
	
	private void attackingBoidsBehaviour() {}
	
	private void defendingBoidsBehaviour() {}

	private void neutralBoidsBehaviour() {}
	
	private void display(List<Body> bodies) {}
	
	private void addCelestialBody(String name, float distance, float speed, float mass, float radius, int planetColor,
			String image) {

		PVector position = new PVector(0, -distance);
		PVector perpendicularVector = new PVector(position.y, -position.x);
		perpendicularVector.normalize();
		PVector velocity = PVector.mult(perpendicularVector, speed);

		CelestialBody celestialBody = new CelestialBody(name, position, velocity, mass,
				(float) (radius * PLANET_RADIUS_SCALE), planetColor, image, this);
		celestalBodies.add(celestialBody);
	}

	private void addAsteroidBelt() {
		for (int i = 0; i < 300; i++) {
			float newDistance = randomizeValue(DISTANCES[5]);
			float newSpeed = randomizeValue(SPEEDS[5]);
			float newRadius = randomizeValue(RADII[5]);
			float newMass = randomizeValue(MASSES[5]);

			float angle = random(TWO_PI);
			PVector position = new PVector(newDistance * cos(angle), newDistance * sin(angle));

			PVector perpendicularVector = new PVector(position.y, -position.x);
			perpendicularVector.normalize();
			PVector velocity = PVector.mult(perpendicularVector, newSpeed);

			CelestialBody celestialBody = new CelestialBody(NAMES[5] + " " + (i + 1), position, velocity, newMass,
					(float) (newRadius * PLANET_RADIUS_SCALE), color(100, 100, 100), null, this);
			celestalBodies.add(celestialBody);
		}
	}

	private float randomizeValue(float baseValue) {
		float deviation = (-0.02f) + (0.1f * random(1));
		return baseValue * (1.0f + deviation);
	}

	private void createStars() {
		for (int i = 0; i < 500; i++) {
			float x = random(-EARTH_DISTANCE * 5 * 1.2f, EARTH_DISTANCE * 5 * 1.2f);
			float y = random(-EARTH_DISTANCE * 5 * 1.2f, EARTH_DISTANCE * 5 * 1.2f);

			stars.add(new PVector(x, y));
		}
	}

	private void summonAsteroid(int mouseX, int mouseY) {
		double[] currentWindow = plt.getWindow();
		float x0 = random((float) currentWindow[0], (float) currentWindow[1]);
		float y0 = (float) currentWindow[3] * 1.05f;
		PVector startPosition = new PVector(x0, y0);

		double[] mouseWorldCoords = plt.getWorldCoord(mouseX, mouseY);
		PVector targetPosition = new PVector((float) mouseWorldCoords[0], (float) mouseWorldCoords[1]);
		PVector direction = PVector.sub(targetPosition, startPosition);
		final float LAUNCH_SPEED = 5.0e4f;
		direction.normalize();
		PVector velocity = PVector.mult(direction, LAUNCH_SPEED);

		CelestialBody newAsteroid = new CelestialBody("", startPosition, velocity, MASSES[5] * 2,
				(float) (RADII[5] * 2 * PLANET_RADIUS_SCALE), color(100, 100, 100), null, this);

		asteroids.add(newAsteroid);
	}

	private void createParticle(CelestialBody asteroid, float dt) {
		final float PARTICLE_LIFESPAN = 604800f; // 1 semana;
		final float LAUNCH_SPEED_FACTOR = 1.0f;
		final float MAX_ANGLE_RAD = PApplet.PI / 9f;
		float particleRadius = asteroid.getRadius() / 1.5f;

		PVector velocity = asteroid.getVelocity();
		PVector oppositeDirection = PVector.mult(velocity, -1).normalize();
		PVector positionOffset = PVector.mult(oppositeDirection, asteroid.getRadius());
		PVector position = PVector.add(asteroid.getPosition(), positionOffset);

		float randomAngle = random(-MAX_ANGLE_RAD, MAX_ANGLE_RAD);

		PVector launchDirection = oppositeDirection.copy();
		launchDirection.rotate(randomAngle);

		float launchMagnitude = velocity.mag() * LAUNCH_SPEED_FACTOR;
		PVector launchVelocity = PVector.mult(launchDirection, launchMagnitude);
		PVector finalVelocity = PVector.add(velocity, launchVelocity);

		Particle newParticle = new Particle(position, finalVelocity, particleRadius, color(150, 150, 150),
				PARTICLE_LIFESPAN);

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
		float speedIncrement = 604800;

		if (key == CODED)
			if (keyCode == UP) {
				speed += speedIncrement;
				speed = min(speed, 2629744 * 6);
			} else if (keyCode == DOWN) {
				speed -= speedIncrement;
				speed = max(speed, -2629744 * 6);
			}

		if (key == 'c' || key == 'C')
			speed = 60;

		if (key == 'v' || key == 'V')
			speed = 86400;

		if (key == 'b' || key == 'B')
			speed = 2629744;

		if (key == 'w' || key == 'W') {
			zoomFactor--;
			zoomFactor = max(1, zoomFactor);
		}

		if (key == 's' || key == 'S') {
			zoomFactor++;
			zoomFactor = min(NAMES.length - 1, zoomFactor);
		}
	}

	public void mousePressed() {
		summonAsteroid(mouseX, mouseY);
	}
}
