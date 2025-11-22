package TP2.Scenarios;

import java.util.ArrayList;

import TP2.Bodies.Bird;
import TP2.Bodies.Body;
import TP2.Bodies.Soldier;
import TP2.Core.*;
import TP2.Fluids.*;

import processing.core.PApplet;
import processing.core.PVector;

public class NormandyLanding extends PApplet {

	private ArrayList<Soldier> soldiers = new ArrayList<>();
	private ArrayList<Bird> birds = new ArrayList<>();
	private ArrayList<Bird> birdsToRemove = new ArrayList<>();

	private static final int SOLDIER_NUMBER = 10;

	private static final int MIN_GRAVITY_INCREMENT = 1;
	private static final int MAX_GRAVITY_INCREMENT = 45;

	private static final float g = -9.8f;
	private int lastUpdateTime;
	private SubPlot plt;
	private double[] window = { 0f, 800, 0f, 600 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private Water water = new Water(5, color(0, 0, 255));
	private Air air = new Air();

	private static boolean isDragging = false;
	private static float[] dragStart = new float[2];
	private static PVector windVector = new PVector(0, 0);

	private int gravityIncrement = 1;

	public void settings() {
		size(800, 600);
	}

	public void setup() {
		plt = new SubPlot(window, viewport, width, height);
		generateSoldiers();

		lastUpdateTime = millis();
	}

	public void draw() {
		background(255);
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		fill(0);
		text("Gravity: " + gravityIncrement + " x Earth Gravity", 20, 30);
		text("Drag mouse to create wind currents", 20, 50);

		water.display(this, plt);

		if (birds.size() < 5)
			generateBird();
		
		// Birds Iteration
		for (Bird bird : birds) {

			if (bird.isOutOfBounds())
				birdsToRemove.add(bird);

			if (dt > 0)
				bird.applyForce(PVector.div(windVector, 1.2f));
			
			if (bird.getPosition().x < 0) {
				bird.getPosition().x = 0;
				bird.setVelocity(new PVector(-bird.getVelocity().x, bird.getVelocity().y));
			}

			if (bird.getPosition().x > width) {
				bird.getPosition().x = width;
				bird.setVelocity(new PVector(-bird.getVelocity().x, bird.getVelocity().y));
			}
			
			if(bird.getPosition().y < 0) {
				bird.getPosition().y = 0;
				bird.setVelocity(new PVector(bird.getVelocity().x, -bird.getVelocity().y));
			}
			
			if (bird.getPosition().y > height) {
				bird.getPosition().y = height;
				bird.setVelocity(new PVector(bird.getVelocity().x, -bird.getVelocity().y));
			}

			bird.move(dt);
			
			bird.display(this, plt);
		}
		
		birds.removeAll(birdsToRemove);
		birdsToRemove.clear();

		// Soldiers Iteration
		for (Soldier soldier : soldiers) {
			PVector weightForce = new PVector(0, soldier.getMass() * g * gravityIncrement); // P = mg

			if (soldier.isGrounded()) {
				/* Grounded */

				// SOLDIERS SWIM
				
			} else {
				/* Not grounded */

				PVector dragForce = air.drag(soldier);

				if (dt > 0)
					soldier.applyForce(PVector.mult(windVector, 1f / dt));

				soldier.applyForce(weightForce);
				soldier.applyForce(dragForce);

				soldier.checkParachute();
				soldier.updateRadius();

				soldier.move(dt);
			}

			if (soldier.getPosition().x < 0) {
				soldier.getPosition().x = 0;
				soldier.setVelocity(new PVector(-soldier.getVelocity().x, soldier.getVelocity().y));
			}

			if (soldier.getPosition().x > width) {
				soldier.getPosition().x = width;
				soldier.setVelocity(new PVector(-soldier.getVelocity().x, soldier.getVelocity().y));
			}

			soldier.display(this, plt);
		}

		windVector.mult(0.95f);
		println(windVector);

	}

	private void generateSoldiers() {
		for (int i = 0; i < SOLDIER_NUMBER; i++) {
			Soldier soldier = new Soldier(new PVector(random(0, width), 600), new PVector(0, 0), random(65, 85),
					random(2, 4), color(0, 0, 0), this);
			soldier.setParachuteHeight((int) random(200, 250));
			soldiers.add(soldier);
		}
	}

	private void generateBird() {
		Bird bird = new Bird(new PVector(0, random(200, 500)), new PVector(random(20, 40), 0), random(4, 8), 3, color(0, 0, 0),
				this);
		birds.add(bird);
	}

	public void keyPressed() {
		if (key == CODED)
			if (keyCode == UP) {
				if (gravityIncrement < MAX_GRAVITY_INCREMENT)
					gravityIncrement++;
			} else if (keyCode == DOWN) {
				if (gravityIncrement > MIN_GRAVITY_INCREMENT)
					gravityIncrement--;
			}
	}

	public void mousePressed() {
		println("Press at " + mouseX + ", " + mouseY);
		dragStart[0] = mouseX;
		dragStart[1] = mouseY;
		isDragging = true;
		println(plt.getWorldCoord(mouseX, mouseY));
	}

	public void mouseReleased() {
		if (isDragging) {
			double[] worldStartCoords = plt.getWorldCoord(dragStart[0], dragStart[1]);
			double[] worldEndCoords = plt.getWorldCoord(mouseX, mouseY);
			println("Released at " + mouseX + ", " + mouseY);
			windVector = new PVector((float) (worldEndCoords[0] - worldStartCoords[0]),
					(float) (worldEndCoords[1] - worldStartCoords[1]));
		}
		isDragging = false;
	}

	public void mouseDragged() {
		if (isDragging) {
			stroke(0);
			line(pmouseX, pmouseY, mouseX, mouseY);
		}
	}
}
