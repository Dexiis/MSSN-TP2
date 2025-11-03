package EntitiesInteractions;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

import Physics.Body;

public class SolarSystem extends PApplet {

	private ArrayList<Body> bodies;

	private final float SUN_MASS = 1.989e30f;
	private final float EARTH_DIST_AU = 1.496e11f;

	private final float AU_TO_PIXELS = 15f;

	private final float PLANET_RADIUS_SCALE = 0.0000005f;
	private final float SUN_RADIUS_SCALE = 0.00000005f; // x10 menor que os planetas
	private final float ORBIT_SPACING_PIXELS = 50.0f; // Isto serve para todos os planetas estarem à mesma distância
														// VISUALMENTE

	private float speed = 1;

	private PVector center;

	public void settings() {
		size(1550, 850);
	}

	public void setup() {
		bodies = new ArrayList<>();
		center = new PVector(width / 2.0f, height / 2.0f);
		initializeSolarSystem();
		background(0);

		textSize(16);
		textAlign(LEFT, TOP);
		fill(255);
	}

	public void draw() {
		fill(0, 0, 0, 75);
		rect(0, 0, width, height);

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
			body.move(speed);

			PVector physicalPosition = body.getPosition();
			float scaleRatio = AU_TO_PIXELS / EARTH_DIST_AU * body.getDisplayScaleFactor();
			PVector screenPosition = PVector.add(center, PVector.mult(physicalPosition, scaleRatio));

			body.display(this, screenPosition);
		}

		fill(255);
		String speedText = "Velocity: " + speed + " times the normal speed";
		text(speedText, 10, 10);
		text("Controls: [W] Speed Up, [S] Slow Down", 10, 30);
		text("Controls: [C] 1m/s, [V] 1D/s, [B] 1M/s, [N] 1Y/s", 10, 50);
		text("[M] 10Y/s (Not Recommended)", 10, 70);
	}

	private void initializeSolarSystem() {

		float currentVisualPosition = 0;

		Body sun = new Body(new PVector(0, 0), new PVector(0, 0), SUN_MASS, (float) (6.957e8 * SUN_RADIUS_SCALE),
				color(255, 180, 0), 1.0f, 8.0f);
		bodies.add(sun);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(3.30e23f, 2.44e6f, 0.387f * EARTH_DIST_AU, 4.79e4f, color(150, 150, 150), currentVisualPosition,
				0.3f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;
		
		// 0.387f * EARTH_DIST_AU / AU_TO_PIXELS <-- Distancia real

		addPlanet(4.87e24f, 6.05e6f, 0.723f * EARTH_DIST_AU, 3.50e4f, color(220, 150, 50), currentVisualPosition, 1f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(5.97e24f, 6.37e6f, 1.00f * EARTH_DIST_AU, 2.98e4f, color(0, 180, 120), currentVisualPosition, 1f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(6.42e23f, 3.39e6f, 1.524f * EARTH_DIST_AU, 2.41e4f, color(200, 50, 0), currentVisualPosition, 0.5f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(1.90e27f, 6.99e7f, 5.20f * EARTH_DIST_AU, 1.31e4f, color(180, 150, 100), currentVisualPosition, 4f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(5.68e26f, 5.82e7f, 9.58f * EARTH_DIST_AU, 9.69e3f, color(255, 210, 150), currentVisualPosition, 3f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(8.68e25f, 2.54e7f, 19.2f * EARTH_DIST_AU, 6.81e3f, color(0, 190, 220), currentVisualPosition, 2f);
		currentVisualPosition += ORBIT_SPACING_PIXELS;

		addPlanet(1.02e26f, 2.46e7f, 30.1f * EARTH_DIST_AU, 5.43e3f, color(50, 100, 255), currentVisualPosition, 2f);
	}

	private void addPlanet(float mass, float radius, float distance, float speed, int planetColor,
			float targetVisualDistance, float customRadiusScale) {

		PVector position = new PVector(0, -distance);
		PVector velocity = new PVector(speed, 0);

		float distanceInAU = distance / EARTH_DIST_AU;
		float realVisualDistanceAtScale = distanceInAU * AU_TO_PIXELS;

		float scaleFactor = targetVisualDistance / realVisualDistanceAtScale;

		Body planet = new Body(position, velocity, mass, (float) (radius * PLANET_RADIUS_SCALE), planetColor,
				scaleFactor, customRadiusScale);
		bodies.add(planet);
	}

	public void keyPressed() {
		float speedIncrement = 5000;

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
			speed = 1400;
		}
		if (key == 'b') {
			speed = 42500;
		}
		if (key == 'n') {
			speed = 520000;
		}
		if (key == 'm') {
			speed = 5200000;
		}
	}
}