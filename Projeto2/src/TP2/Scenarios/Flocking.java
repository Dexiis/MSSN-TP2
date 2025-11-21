package TP2.Scenarios;

import java.util.ArrayList;
import java.util.List;

import TP2.Bodies.Boid;
import TP2.Bodies.Predator;
import TP2.Bodies.Prey;
import TP2.Bodies.Attributes.*;
import TP2.Bodies.Attributes.Behaviours.*;
import TP2.Bodies.Body;
import TP2.Core.SubPlot;

import processing.core.PApplet;
import processing.core.PVector;

public class Flocking extends PApplet {

	private final int NB_PREY = 50;
	private float lastUpdateTime;

	private List<Body> allBodies;
	private List<Body> preyFlock;
	private Predator predator;
	private Flee flee;
	private Flee evade;
	private Align align;
	private Cohesion cohesion;
	private Separate separate;
	private Seek seek;
	private Wander wander;
	private List<Behaviour> preyBehaviours = new ArrayList<Behaviour>();
	private List<Behaviour> predatorBehaviours = new ArrayList<Behaviour>();

	private double[] window = { -10f, 10f, -10f, 10f };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;

	@Override
	public void settings() {
		size(800, 800);
	}

	@Override
	public void setup() {
		plt = new SubPlot(window, viewport, width, height);
		allBodies = new ArrayList<Body>();
		preyFlock = new ArrayList<Body>();
		lastUpdateTime = millis();

		for (int i = 0; i < NB_PREY; i++) {
			float x = random((float) window[0], (float) window[1]);
			float y = random((float) window[2], (float) window[3]);

			Prey prey = new Prey(new PVector(x, y), 1f, 0.4f, color(0, 0, 255), this, plt);
			preyFlock.add(prey);
			allBodies.add(prey);
		}

		float x = random((float) window[0], (float) window[1]);
		float y = random((float) window[2], (float) window[3]);
		PVector pos = new PVector(x, y);

		predator = new Predator(pos, 2f, 0.8f, color(255, 0, 0), this, plt, preyFlock);
		allBodies.add(predator);

		for (Body body : preyFlock) {
			Boid prey = (Boid) body;
			prey.setEye(new Eye(prey, allBodies));
			prey.getEye().setTarget(predator);
		}

		align = new Align(1.0f);
		cohesion = new Cohesion(1.0f);
		separate = new Separate(1.0f);
		preyBehaviours.add(align);
		preyBehaviours.add(cohesion);
		preyBehaviours.add(separate);

		flee = new Flee(9.0f);
		evade = new Flee(6.0f);
		
		seek = new Seek(0.8f);
		wander = new Wander(0.2f);
		predatorBehaviours.add(seek);
		predatorBehaviours.add(wander);
	}

	@Override
	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(200);

		float closestDist = Float.MAX_VALUE;
		if (predator.getEye().getNearSight().size() > 0) {
			for (int i = predator.getEye().getNearSight().size() - 1; i >= 0; i--) {
				Boid prey = (Boid) predator.getEye().getNearSight().get(i);
				PVector distance = prey.getToroidalDistanceVector(predator.getPosition());

				if (distance.mag() < closestDist) {
					closestDist = distance.mag();
					predator.getEye().setTarget(prey);
				}
			}
			predator.applyBehaviour(seek, dt);

		} else if (predator.getEye().getFarSight().size() > 0) {
			for (int i = predator.getEye().getFarSight().size() - 1; i >= 0; i--) {
				Boid prey = (Boid) predator.getEye().getFarSight().get(i);
				PVector distance = prey.getToroidalDistanceVector(predator.getPosition());

				if (distance.mag() < closestDist) {
					closestDist = distance.mag();
					predator.getEye().setTarget(prey);
				}
			}
			predator.applyBehaviours(predatorBehaviours, dt);

		} else {
			predator.applyBehaviour(wander, dt);
		}

		for (int i = preyFlock.size() - 1; i >= 0; i--) {
			Boid prey = (Boid) preyFlock.get(i);

			if (prey.getEye().getNearSight().contains(predator)) {
				if (!preyBehaviours.contains(flee))
					preyBehaviours.add(flee);
				if (preyBehaviours.contains(evade))
					preyBehaviours.remove(evade);
				prey.applyBehaviours(preyBehaviours, dt);
			} else if (prey.getEye().getFarSight().contains(predator)) {
				if (!preyBehaviours.contains(evade))
					preyBehaviours.add(evade);
				if (preyBehaviours.contains(flee))
					preyBehaviours.remove(flee);
				prey.applyBehaviours(preyBehaviours, dt);
			} else {
				if (preyBehaviours.contains(evade))
					preyBehaviours.remove(evade);
				if (preyBehaviours.contains(flee))
					preyBehaviours.remove(flee);
				prey.applyBehaviours(preyBehaviours, dt);
			}

			PVector distance = prey.getToroidalDistanceVector(predator.getPosition());

			if (distance.mag() < (prey.getRadius() + predator.getRadius()) / 2) {
				preyFlock.remove(prey);
				allBodies.remove(prey);
				predator.getEye().setTargets(preyFlock);
			}

		}

		for (Body body : allBodies) {
			Boid prey = (Boid) body;
			body.display(this, plt);
			//prey.getEye().display(this, plt);
		}
		predator.getEye().display(this, plt);

		if (predator.getEye().getTarget() != null) {
			PVector targetPos = predator.getEye().getTarget().getPosition();
			float[] pp = plt.getPixelCoord(targetPos.x, targetPos.y);
			noFill();
			stroke(255, 0, 0);
			strokeWeight(3);
			ellipse(pp[0], pp[1], 10, 10);
		}
	}
}