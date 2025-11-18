package TP2.Scenarios;

import java.util.ArrayList;

import TP2.Bodies.Soldier;
import TP2.Core.*;
import TP2.Fluids.*;

import processing.core.PApplet;
import processing.core.PVector;

public class NormandyLanding extends PApplet {

	private ArrayList<Soldier> soldiers = new ArrayList<>();

	private static final float g = -9.8f;
	private int lastUpdateTime;
	private SubPlot plt;
	private double[] window = { 0f, 800, 0f, 600 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private Water water = new Water(5, color(0, 0, 255));

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

		water.display(this, plt);

		for (Soldier soldier : soldiers) {
			PVector weightForce = new PVector(0, soldier.getMass() * g); // P = mg
			if (soldier.getPosition().y <= 0) {

			} else {
				soldier.applyForce(weightForce);
				soldier.move(dt);
			}

			soldier.display(this, plt);
		}
	}

	private void generateSoldiers() {
		Soldier body = new Soldier(new PVector(100, 600), new PVector(0, 0), 80, 10, color(0, 0, 0));
		soldiers.add(body);
	}

	public void keyPressed() {

	}

	public void mousePressed() {

	}
}
