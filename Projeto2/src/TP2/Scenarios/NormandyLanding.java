package TP2.Scenarios;

import java.util.ArrayList;

import TP2.Bodies.Soldier;
import TP2.Core.*;
import TP2.Fluids.*;

import processing.core.PApplet;
import processing.core.PVector;

public class NormandyLanding extends PApplet {

	private ArrayList<Soldier> soldiers = new ArrayList<>();

	private static final int SOLDIER_NUMBER = 10;
	
	private static final float g = -9.8f;
	private int lastUpdateTime;
	private SubPlot plt;
	private double[] window = { 0f, 800, 0f, 600 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private Water water = new Water(5, color(0, 0, 255));
	private Air air = new Air();
	
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
		
		water.display(this, plt);

		for (Soldier soldier : soldiers) {
			PVector weightForce = new PVector(0, soldier.getMass() * g * gravityIncrement); // P = mg
			if (soldier.isGrounded()) {
				/* Grounded */
				
			} else {
				/* Not grounded */
				
				// TODO: Add wall collisions
				
				PVector dragForce = air.drag(soldier);
				
				soldier.applyForce(weightForce);
				soldier.applyForce(dragForce);
				
				soldier.checkParachute();
				soldier.updateRadius();
				
				soldier.move(dt);
			}

			soldier.display(this, plt);
		}
	}

	private void generateSoldiers() {
		for(int i = 0; i < SOLDIER_NUMBER; i++) {
			Soldier soldier = new Soldier(new PVector(random(0, width), 600), new PVector(0, 0), random(65, 85), random(2, 4), color(0, 0, 0), this);
			soldier.setParachuteHeight((int) random(200, 250));
			soldiers.add(soldier);
		}
	}

	public void keyPressed() {
		if (key == CODED)
			if (keyCode == UP) {
				if(gravityIncrement < 30)
					gravityIncrement++;
			} else if (keyCode == DOWN) {
				if(gravityIncrement > 1)
					gravityIncrement--;
			}
	}

	public void mousePressed() {

	}
}
