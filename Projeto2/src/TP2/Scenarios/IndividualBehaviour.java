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
import TP2.Bodies.BoidAttributes.Behaviours.Arrive;

public class IndividualBehaviour extends PApplet {
	private Boid boid, boidSeeker;
	private double[] window = { -20, 20, -20, 20 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;
	private Body target;
	private float lastUpdateTime;

	private Seek seek;
	private Arrive arrive;
	private Patrol patrol;
	private Wander wander;
	private Flee flee;

	private boolean staticDot = false;

	private static final int SEEK_MODE = 0;
	private static final int ARRIVE_MODE = 1;
	private static final int PATROL_MODE = 2;
	private static final int WANDER_MODE = 3;
	private static final int FLEE_MODE = 4;
	private int currentBehaviour = SEEK_MODE;

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		boid = new Boid(new PVector(), 1f, 0.5f, color(0), this, plt);
		boidSeeker = new Boid(new PVector(), 1f, 0.5f, color(255, 0, 0), this, plt);
		target = new Target(new PVector(), new PVector(), 0.2f, color(255, 0, 0));

		List<Body> targets = new ArrayList<>();
		targets.add(target);
		targets.add(boidSeeker);
		boid.setEye(new Eye(boid, targets));
		
		List<Body> targets2 = new ArrayList<>();
		targets2.add(boid);
		boidSeeker.setEye(new Eye(boidSeeker, targets2));

		seek = new Seek(1f);
		arrive = new Arrive(1f);
		patrol = new Patrol(1f);
		wander = new Wander(1f);
		flee = new Flee(1f);

		boid.addBehaviour(seek);
		boidSeeker.addBehaviour(seek);
	}

	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(255);

		boid.applyBehaviours(dt);
		boidSeeker.applyBehaviours(dt);

		boid.display(this, plt);

		if (currentBehaviour == FLEE_MODE) {
			PVector toroidalDistanceVector = boid.getToroidalDistanceVector(boidSeeker.getPosition());
			float distance = toroidalDistanceVector.mag();
			float SAFE_DISTANCE = 30f;
			float fleeWeight;
			float wanderWeight;

			if (distance >= SAFE_DISTANCE) {
				fleeWeight = 0f;
				wanderWeight = 1f;
			} else {
				fleeWeight = 1f - (distance / SAFE_DISTANCE);
				wanderWeight = distance / SAFE_DISTANCE;
			}

			flee.setWeight(fleeWeight);
			wander.setWeight(wanderWeight);

			boid.clearBehaviour();
			boid.addBehaviour(wander);
			boid.addBehaviour(flee);
			boidSeeker.display(this, plt);
		} else {
			if (!staticDot) {
				double[] ww = plt.getWorldCoord(mouseX, mouseY);
				target.setPosition(new PVector((float) ww[0], (float) ww[1]));
			}
			target.display(this, plt);
		}

		pushStyle();
		fill(0);
		textSize(16);
		String behaviourName;
		if (currentBehaviour == SEEK_MODE) {
			behaviourName = "Seek (Perseguir)";
		} else if (currentBehaviour == ARRIVE_MODE) {
			behaviourName = "Arrive (Aproximar)";
		} else if (currentBehaviour == PATROL_MODE) {
			behaviourName = "Patrol (Patrulha)";
		} else if (currentBehaviour == FLEE_MODE) {
			behaviourName = (flee.getWeight() > 0.05f)
					? "Flee/Wander (" + flee.getWeight() * 100 + "% / " + wander.getWeight() * 100 + "%)"
					: "Wander (100%)";
		} else {
			behaviourName = "Wander (Vaguear)";
		}
		text("Comportamento Atual (C): " + behaviourName, 10, 20);
		text("Controlo: 'W' (Acelerar) | 'S' (Travar)", 10, 40);
		text("Velocidade: " + boid.getVelocity().mag(), 10, 60);
		text("Mudar Comportamento: 'C'", 10, 80);
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
		} else if (key == 'r' || key == 'R') {
			boidSeeker.setVelocity(increment, true);
			boidSeeker.setForce(increment, true);
		} else if (key == 'f' || key == 'F') {
			boidSeeker.setVelocity(increment, false);
			boidSeeker.setForce(increment, false);
		} else if (key == 'c' || key == 'C') {

			boid.clearBehaviour();

			if (currentBehaviour == SEEK_MODE) {
				currentBehaviour = ARRIVE_MODE;
				boid.addBehaviour(arrive);
			} else if (currentBehaviour == ARRIVE_MODE) {
				currentBehaviour = PATROL_MODE;
				boid.addBehaviour(patrol);
			} else if (currentBehaviour == PATROL_MODE) {
				currentBehaviour = WANDER_MODE;
				boid.addBehaviour(wander);
			} else if (currentBehaviour == WANDER_MODE) {
				currentBehaviour = FLEE_MODE;
			} else {
				currentBehaviour = SEEK_MODE;
				boid.addBehaviour(seek);
			}
		}
	}

	public void mousePressed() {
		staticDot = !staticDot;
	}
}