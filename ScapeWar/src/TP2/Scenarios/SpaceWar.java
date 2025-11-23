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

	private Blue player; // MUDAR MANUALMENTE A EQUIPE

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
	private float currentZoomFactor = 1;
	private double[] window = { -1.2 * maxViewDistance, 1.2 * maxViewDistance, -1.2 * maxViewDistance,
			1.2 * maxViewDistance };

	private Cohesion cohesion;
	private Evade evade;
	private Flee flee;
	private Pursuit pursuit;
	private Seek seek;
	private Separate separate;
	private Wander wander;

	private boolean playerIsDead = false;
	private boolean wKey = false;
	private boolean aKey = false;
	private boolean sKey = false;
	private boolean dKey = false;

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

		Cohesion cohesion = new Cohesion(1f);
		Evade evade = new Evade(1f);
		Flee flee = new Flee(1f);
		Pursuit pursuit = new Pursuit(1f);
		Seek seek = new Seek(1f);
		Separate separate = new Separate(1f);
		Wander wander = new Wander(1f);

		background(0);
		textSize(16);
		textAlign(LEFT, TOP);
		fill(255);
	}

	public void draw() {
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

		setWindow(-1.2 * EARTH_DISTANCE * currentZoomFactor, 1.2 * EARTH_DISTANCE * currentZoomFactor,
				-1.2 * EARTH_DISTANCE * currentZoomFactor, 1.2 * EARTH_DISTANCE * currentZoomFactor);
	}

	private void createBluePlayer() {
	} // TODO

	private void createRedPlayer() {
	} // TODO

	private void initializeSolarSystem() {

		sun = new CelestialBody(NAMES[0], new PVector(0, -DISTANCES[0]), new PVector(SPEEDS[0], 0), MASSES[0],
				RADII[0] * SUN_RADIUS_SCALE, 10000, color(255, 180, 0), "Resources/" + NAMES[0] + ".png", this);

		addPlanet(NAMES[1], DISTANCES[1], SPEEDS[1], MASSES[1], RADII[1], color(150, 150, 150),
				"Resources/" + NAMES[1] + ".png");

		addPlanet(NAMES[2], DISTANCES[2], SPEEDS[2], MASSES[2], RADII[2], color(220, 150, 50),
				"Resources/" + NAMES[2] + ".png");

		addPlanet(NAMES[3], DISTANCES[3], SPEEDS[3], MASSES[3], RADII[3], color(0, 180, 120),
				"Resources/" + NAMES[3] + ".png");

		addPlanet(NAMES[4], DISTANCES[4], SPEEDS[4], MASSES[4], RADII[4], color(200, 50, 0),
				"Resources/" + NAMES[4] + ".png");

		createStars();

	}

	private void initializeBoids() {
	} // TODO

	private PVector randomPositionn() {
		float x = random((float) window[0], (float) window[1]);
		float y = random((float) window[2], (float) window[3]);
		return new PVector(x, y);
	}

	private void playerMovement(float dt) {
		PVector desiredVelocity = new PVector(0, 0);
		if (wKey)
			desiredVelocity.add(0, player.getDNA().getMaxSpeed());
		if (aKey)
			desiredVelocity.add(-player.getDNA().getMaxSpeed(), 0);
		if (sKey)
			desiredVelocity.add(0, -player.getDNA().getMaxSpeed());
		if (dKey)
			desiredVelocity.add(player.getDNA().getMaxSpeed(), 0);
		player.move(dt, desiredVelocity);
	}

	private void attackingBoidsBehaviour(float dt) { //TODO fazer os behaviours
		for (Red red : redTeam) {
			float closestDist = Float.MAX_VALUE;
			if (red.getEye().getNearSight().size() > 0) {
				for (int i = red.getEye().getNearSight().size() - 1; i >= 0; i--) {
					Boid prey = (Boid) red.getEye().getNearSight().get(i);
					PVector distance = prey.getToroidalDistanceVector(red.getPosition());

					if (distance.mag() < closestDist) {
						closestDist = distance.mag();
						red.getEye().setTarget(prey);
					}
				}
				red.applyBehaviour(seek, dt);

			} else if (red.getEye().getFarSight().size() > 0) {
				for (int i = red.getEye().getFarSight().size() - 1; i >= 0; i--) {
					Boid prey = (Boid) red.getEye().getFarSight().get(i);
					PVector distance = prey.getToroidalDistanceVector(red.getPosition());

					if (distance.mag() < closestDist) {
						closestDist = distance.mag();
						red.getEye().setTarget(prey);
					}
				}
				red.applyBehaviour(seek, dt);

			} else {
				red.applyBehaviour(wander, dt);

			}
		}
	}

	private void defendingBoidsBehaviour(float dt) { //TODO fazer os behaviours
		for (Blue blue : blueTeam) {
			float closestDist = Float.MAX_VALUE;
			if (blue.getEye().getNearSight().size() > 0) {
				for (int i = blue.getEye().getNearSight().size() - 1; i >= 0; i--) {
					Boid prey = (Boid) blue.getEye().getNearSight().get(i);
					PVector distance = prey.getToroidalDistanceVector(blue.getPosition());

					if (distance.mag() < closestDist) {
						closestDist = distance.mag();
						blue.getEye().setTarget(prey);
					}
				}
				blue.applyBehaviour(seek, dt);

			} else if (blue.getEye().getFarSight().size() > 0) {
				for (int i = blue.getEye().getFarSight().size() - 1; i >= 0; i--) {
					Boid prey = (Boid) blue.getEye().getFarSight().get(i);
					PVector distance = prey.getToroidalDistanceVector(blue.getPosition());

					if (distance.mag() < closestDist) {
						closestDist = distance.mag();
						blue.getEye().setTarget(prey);
					}
				}
				blue.applyBehaviour(seek, dt);

			} else {
				blue.applyBehaviour(wander, dt);

			}
		}
	}

	private void neutralBoidsBehaviour(float dt) { //TODO fazer os behaviours
		for (Neutral neutral : neutralTeam) {
			float closestDist = Float.MAX_VALUE;
			if (neutral.getEye().getNearSight().size() > 0) {
				for (int i = neutral.getEye().getNearSight().size() - 1; i >= 0; i--) {
					Boid prey = (Boid) neutral.getEye().getNearSight().get(i);
					PVector distance = prey.getToroidalDistanceVector(neutral.getPosition());

					if (distance.mag() < closestDist) {
						closestDist = distance.mag();
						neutral.getEye().setTarget(prey);
					}
				}
				neutral.applyBehaviour(seek, dt);

			} else if (neutral.getEye().getFarSight().size() > 0) {
				for (int i = neutral.getEye().getFarSight().size() - 1; i >= 0; i--) {
					Boid prey = (Boid) neutral.getEye().getFarSight().get(i);
					PVector distance = prey.getToroidalDistanceVector(neutral.getPosition());

					if (distance.mag() < closestDist) {
						closestDist = distance.mag();
						neutral.getEye().setTarget(prey);
					}
				}
				neutral.applyBehaviour(seek, dt);

			} else {
				neutral.applyBehaviour(wander, dt);

			}
		}
	}

	private void display(List<Body> bodies, float dt) { //TODO Não está completamente certo. Método sobrecarregado?
		for (Body body : bodies) {
			body.setAcceleration(new PVector(0, 0));
			PVector force = sun.attraction(body);
			body.applyForce(force);
		}

		for (Body body : bodies) {
			body.move(speed * dt);
			body.display(this, plt);
		}
	}

	private void addPlanet(String name, float distance, float speed, float mass, float radius, int planetColor,
			String image) {

		PVector position = new PVector(0, -distance);
		PVector perpendicularVector = new PVector(position.y, -position.x);
		perpendicularVector.normalize();
		PVector velocity = PVector.mult(perpendicularVector, speed);

		CelestialBody celestialBody = new CelestialBody(name, position, velocity, mass,
				(float) (radius * PLANET_RADIUS_SCALE), 1000, planetColor, image, this);
		celestalBodies.add(celestialBody);
	}

	private void createStars() {
		for (int i = 0; i < 500; i++) {
			float x = random(-EARTH_DISTANCE * 5 * 1.2f, EARTH_DISTANCE * 5 * 1.2f);
			float y = random(-EARTH_DISTANCE * 5 * 1.2f, EARTH_DISTANCE * 5 * 1.2f);

			stars.add(new PVector(x, y));
		}
	}

	private void summonAsteroid(int mouseX, int mouseY) { // TODO Mudar a lógica disto
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
				(float) (RADII[5] * 2 * PLANET_RADIUS_SCALE), 50, color(100, 100, 100), null, this);

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

		if (key == 'w' || key == 'W') {
			wKey = true;
		}
		if (key == 'a' || key == 'A') {
			aKey = true;
		}
		if (key == 's' || key == 'S') {
			sKey = true;
		}
		if (key == 'd' || key == 'D') {
			dKey = true;
		}
	}

	public void keyReleased() {
		if (key == 'w' || key == 'W') {
			wKey = false;
		}
		if (key == 'a' || key == 'A') {
			aKey = false;
		}
		if (key == 's' || key == 'S') {
			sKey = false;
		}
		if (key == 'd' || key == 'D') {
			dKey = false;
		}
	}
}
