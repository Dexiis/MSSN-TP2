package TP2.Scenarios;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Target;
import TP2.Bodies.BoidAttributes.*;
import TP2.Bodies.BoidAttributes.Behaviours.*;
import TP2.Core.*;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public class IndividualBehaviour extends PApplet {
	private Boid boid;
	private double[] window = { -10, 10, -10, 10 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;
	private Body target;
	private float lastUpdateTime;

	private Seek seek;
	private Arrive arrive;

	private final float POTENCY_FACTOR = 20f;

	private static final int SEEK_MODE = 0;
	private static final int ARRIVE_MODE = 1;
	private int currentBehaviour = SEEK_MODE;

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		boid = new Boid(new PVector(), 1f, 0.5f, color(0), this, plt);
		target = new Target(new PVector(), new PVector(), 0.2f, color(255, 0, 0));

		List<Body> targets = new ArrayList<>();
		targets.add(target);
		boid.setEye(new Eye(boid, targets));

		seek = new Seek(1f);
		arrive = new Arrive(1f, POTENCY_FACTOR);

		boid.addBehaviour(seek);
	}

	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(255);

		boid.applyBehaviours(dt);

		boid.display(this, plt);
		target.display(this, plt);

		pushStyle();

		fill(0);
		textSize(16);

		String behaviourName = (currentBehaviour == SEEK_MODE) ? "Seek (Perseguir)" : "Arrive (Aproximar)";
		text("Comportamento Atual (C): " + behaviourName, 10, 20);
		text("Controlo: 'W' (Acelerar) | 'S' (Travar)", 10, 40);
		text("Mudar Comportamento: 'C'", 10, 60);

		popStyle();
	}

	public void keyPressed() {
		int increment = 1;

		if (key == 'w' || key == 'W') {
			boid.setVelocity(increment, true);
			boid.setForce(increment, true);
		} else if (key == 's' || key == 'S') {
			boid.setVelocity(increment, false);
			boid.setForce(increment, false);
		} else if (key == 'c' || key == 'C') {
			boid.removeBehaviour(seek);
			boid.removeBehaviour(arrive);

			if (currentBehaviour == SEEK_MODE) {
				currentBehaviour = ARRIVE_MODE;
				boid.addBehaviour(arrive);
			} else {
				currentBehaviour = SEEK_MODE;
				boid.addBehaviour(seek);
			}
		}
	}

	public void mousePressed() {
		double[] ww = plt.getWorldCoord(mouseX, mouseY);
		target.setPosition(new PVector((float) ww[0], (float) ww[1]));
	}
}