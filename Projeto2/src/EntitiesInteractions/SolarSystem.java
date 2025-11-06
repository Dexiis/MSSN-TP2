package EntitiesInteractions;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import Physics.*;

public class SolarSystem extends PApplet {

	private ArrayList<Body> bodies;

	private static final float EARTH_DISTANCE = 1.496e11f;

	// private static final float PLANET_RADIUS_SCALE = 0.000005f;
	// private static final float SUN_RADIUS_SCALE = 0.0000005f;

	private static final float PLANET_RADIUS_SCALE = 10;
	private static final float SUN_RADIUS_SCALE = 10;

	static final String[] NAMES = { "Sun", "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus",
			"Neptune" };

	static final float[] DISTANCES = { 0.0f, 0.387f * EARTH_DISTANCE, 0.723f * EARTH_DISTANCE, 1.00f * EARTH_DISTANCE,
			1.524f * EARTH_DISTANCE, 5.20f * EARTH_DISTANCE, 9.58f * EARTH_DISTANCE, 19.2f * EARTH_DISTANCE,
			30.1f * EARTH_DISTANCE };

	static final float[] SPEEDS = { 0.0f, 4.79e4f, 3.50e4f, 2.98e4f, 2.41e4f, 1.31e4f, 9.69e3f, 6.81e3f, 5.43e3f };

	static final float[] RADII = { 6.96e8f, 2.44e6f, 6.05e6f, 6.37e6f, 3.39e6f, 6.99e7f, 5.82e7f, 2.54e7f, 2.46e7f };

	static final float[] MASSES = { 1.989e30f, 3.30e23f, 4.87e24f, 5.97e24f, 6.42e23f, 1.90e27f, 5.68e26f, 8.68e25f,
			1.02e26f };

	private float[] viewport = { 0f, 0f, 1f, 1f };
	private float maxViewDistance = DISTANCES[8];
	private double[] window = { -1.2 * maxViewDistance, 1.2 * maxViewDistance, -1.2 * maxViewDistance,
			1.2 * maxViewDistance };
	private float speed = 1;
	private int lastUpdateTime;

	private SubPlot plt;

	public void settings() {
		size(1600, 900);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		bodies = new ArrayList<>();
		initializeSolarSystem();
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

		for (Body body1 : bodies) {
			body1.setAcceleration(new PVector(0, 0));

			for (Body body2 : bodies) {
				if (body1 != body2) {
					PVector force = body2.attraction(body1);
					body1.applyForce(force);
				}
			}
		}

		for (Body body : bodies) {
			body.move(speed * dt);
			body.display(this, plt);
		}

		fill(255);
		String speedText = "Velocity: " + speed + " times the normal speed";
		text(speedText, 10, 10);
		text("Controls: [↑] Speed Up, [↓] Slow Down", 10, 30);
		text("Controls: [C] 1s/s, [V] 1D/s, [B] 1M/s, [N] 1Y/s", 10, 50);
	}

	private void initializeSolarSystem() {

		Body sun = new Body("Sun", new PVector(0, -DISTANCES[0]), new PVector(SPEEDS[0], 0), MASSES[0],
				RADII[0] * SUN_RADIUS_SCALE, color(255, 180, 0));
		bodies.add(sun);

		addPlanet("Mercury", DISTANCES[1], SPEEDS[1], MASSES[1], RADII[1], color(150, 150, 150));

		addPlanet("Venus", DISTANCES[2], SPEEDS[2], MASSES[2], RADII[2], color(220, 150, 50));

		addPlanet("Earth", DISTANCES[3], SPEEDS[3], MASSES[3], RADII[3], color(0, 180, 120));

		addPlanet("Mars", DISTANCES[4], SPEEDS[4], MASSES[4], RADII[4], color(200, 50, 0));

		addPlanet("Jupiter", DISTANCES[5], SPEEDS[5], MASSES[5], RADII[5], color(180, 150, 100));

		addPlanet("Saturn", DISTANCES[6], SPEEDS[6], MASSES[6], RADII[6], color(255, 210, 150));

		addPlanet("Uranus", DISTANCES[7], SPEEDS[7], MASSES[7], RADII[7], color(0, 190, 220));

		addPlanet("Neptune", DISTANCES[8], SPEEDS[8], MASSES[8], RADII[8], color(50, 100, 255));
	}

	private void addPlanet(String name, float distance, float speed, float mass, float radius, int planetColor) {

		PVector position = new PVector(0, -distance);
		PVector velocity = new PVector(speed, 0);

		Body planet = new Body(name, position, velocity, mass, (float) (radius * PLANET_RADIUS_SCALE), planetColor);
		bodies.add(planet);
	}

	private void setViewport(float x1, float y1, float x2, float y2) {
		viewport[0] = x1;
		viewport[1] = y1;
		viewport[2] = x2;
		viewport[3] = y2;
		plt.setViewport(viewport);
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

		if (key == CODED) {
			if (keyCode == UP) {
				speed += speedIncrement;
			} else if (keyCode == DOWN) {
				speed -= speedIncrement;
			}
		}
		if (key == 'c') {
			speed = 1;
		}
		if (key == 'v') {
			speed = 86400;
		}
		if (key == 'b') {
			speed = 2629744;
		}
		if (key == 'n') {
			speed = 31556926;
		}
		if (key == 'w') {
			setViewport(0.4f, 0.4f, 0.6f, 0.6f);
			setWindow(-1.2 * DISTANCES[1], 1.2 * DISTANCES[1], -1.2 * DISTANCES[1], 1.2 * DISTANCES[1]);
		}
	}
}