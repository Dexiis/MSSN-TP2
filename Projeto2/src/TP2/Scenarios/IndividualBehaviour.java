package TP2.Scenarios;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Predator;
import TP2.Bodies.Prey;
import TP2.Bodies.Target;
import TP2.Bodies.Attributes.*;
import TP2.Bodies.Attributes.Behaviours.*;
import TP2.Core.*;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public class IndividualBehaviour extends PApplet {
	private Prey boid;
	private Predator boidPursuer;
	private double[] window = { -10f, 10f, -10f, 10f };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;
	private Body target;
	private float lastUpdateTime;

	private Seek seek;
	private Arrive arrive;
	private Patrol patrol;
	private Wander wander;
	private Flee flee;
	private Evade evade;
	private Pursuit pursuit;

	private boolean staticDot = false;

	private static final int SEEK_MODE = 0;
	private static final int ARRIVE_MODE = 1;
	private static final int PATROL_MODE = 2;
	private static final int WANDER_MODE = 3;
	private static final int FLEE_MODE = 4;
	private static final int PERSUIT_EVADE_MODE = 5;
	private int currentBehaviour = SEEK_MODE;

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		boid = new Prey(new PVector(), 1f, 0.5f, color(0), this, plt);
		boidPursuer = new Predator(new PVector(), 1f, 0.5f, color(255, 0, 0), this, plt);
		target = new Target(new PVector(), new PVector(), 0.2f, color(255, 0, 0));

		List<Body> targets = new ArrayList<>();
		targets.add(target);
		targets.add(boidPursuer);
		boid.setEye(new Eye(boid, targets));
		
		List<Body> pursuerTargets = new ArrayList<>();
		pursuerTargets.add(boid);
		boidPursuer.setEye(new Eye(boidPursuer, pursuerTargets));

		seek = new Seek(1f);
		arrive = new Arrive(1f);
		patrol = new Patrol(1f);
		wander = new Wander(1f);
		flee = new Flee(1f);
		evade = new Evade(1f);
		pursuit = new Pursuit(1f);

		boid.addBehaviour(seek);
		boidPursuer.addBehaviour(pursuit);
	}

	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(255);

		boid.getEye().display(this, plt);
		boidPursuer.getEye().display(this, plt);
		
		boid.applyBehaviours(dt);
		boidPursuer.applyBehaviours(dt);

		boid.display(this, plt);

		if (currentBehaviour == PERSUIT_EVADE_MODE) {
			float distance = boid.getToroidalDistanceVector(boidPursuer.getPosition()).mag();
			float SAFE_DISTANCE = boid.getDNA().visionSafeDistance;
			float evadeWeight;

			if (distance >= SAFE_DISTANCE) {
				evadeWeight = 0f;
			} else {
				evadeWeight = 5*30 - (float) Math.pow((distance / SAFE_DISTANCE), 3);
			}

			evade.setWeight(evadeWeight);

			boid.clearBehaviour();
			boid.addBehaviour(wander);
			boid.addBehaviour(evade);
			boidPursuer.display(this, plt);
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
			behaviourName = "Flee (Fugir)";
		} else if (currentBehaviour == PERSUIT_EVADE_MODE) {
			behaviourName = (evade.getWeight() > 0.05f)
					? "Evade/Wander (" + evade.getWeight() / boid.getSumWeights() * 100 + "%)"
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
		int increment = 5;

		if (key == 'w' || key == 'W') {
			boid.setVelocity(increment, true);
			boid.setForce(increment, true);
		} else if (key == 's' || key == 'S') {
			boid.setVelocity(increment, false);
			boid.setForce(increment, false);
		} else if (key == 'r' || key == 'R') {
			boidPursuer.setVelocity(increment, true);
			boidPursuer.setForce(increment, true);
		} else if (key == 'f' || key == 'F') {
			boidPursuer.setVelocity(increment, false);
			boidPursuer.setForce(increment, false);
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
				boid.addBehaviour(flee);
			} else if (currentBehaviour == FLEE_MODE) {
				currentBehaviour = PERSUIT_EVADE_MODE;
				boid.getEye().setTarget(boidPursuer);
			} else {
				boid.getEye().setTarget(target);
				currentBehaviour = SEEK_MODE;
				boid.addBehaviour(seek);
			}
		}
	}

	public void mousePressed() {
		staticDot = !staticDot;
	}
}