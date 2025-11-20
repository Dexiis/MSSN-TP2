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
	private Evade evade;
	private Align align;
	private Cohesion cohesion;
	private Separate separate;
	private Seek seek;
	private Wander wander;

	private double[] window = { -20f, 20f, -20f, 20f };
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

		align = new Align(1.0f);
		cohesion = new Cohesion(1.0f);
		separate = new Separate(1.0f);
		evade = new Evade(9.0f);

		for (Body body : preyFlock) {
			Boid prey = (Boid) body;

			prey.setEye(new Eye(prey, allBodies));

			prey.addBehaviour(align);
			prey.addBehaviour(cohesion);
			prey.addBehaviour(separate);

			// NOTA: Para que o Evade funcione, a Prey precisa de ter o Predador como
			// 'Target'.
			// E o 'allTrackingBodies' do olho da Prey precisa de ser o bando.
			// A forma mais simples é fazer com que o Evade utilize o Predador como Target,
			// e o Align/Cohesion usem os outros Boids.

			// Uma implementação mais robusta de Evade faria com que a presa procurasse
			// o predador na sua lista de 'farSight' e o definisse como 'target' se o visse.
			// Para os fins desta refatoração, vamos focar-nos na atribuição de
			// comportamentos.
		}

		seek = new Seek(1.0f);
		wander = new Wander(1.0f);
		predator.addBehaviour(seek);
		predator.addBehaviour(wander);

	}

	@Override
	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(200);

		predator.getEye().look();
		predator.clearBehaviour();
		predator.getEye().display(this, plt);

		if (predator.getEye().getFarSight().size() > 0) {
			predator.getEye().setTarget(predator.getEye().getFarSight().get(0));
			predator.addBehaviour(seek);
			
		} else {
			predator.addBehaviour(wander);
		}
		
		for (int i = preyFlock.size() - 1; i >= 0; i--) {
			Boid prey = (Boid) preyFlock.get(i);

			prey.getEye().look();

			prey.clearBehaviour();
			if (prey.getEye().getTarget() == predator) {
				prey.addBehaviour(evade);
			}
			prey.addBehaviour(align);
			prey.addBehaviour(cohesion);
			prey.addBehaviour(separate);

			PVector distance = prey.getToroidalDistanceVector(predator.getPosition());

			if (distance.mag() < (prey.getRadius() + predator.getRadius()) / 2) {
				preyFlock.remove(prey);
				allBodies.remove(prey);
				predator.getEye().setTargets(preyFlock);
			}

		}

		for (Body body : allBodies) {
			Boid boid = (Boid) body;
			boid.applyBehaviours(dt);
		}

		for (Body body : allBodies) {
			body.display(this, plt);
		}
		
		System.out.println(predator.getBehaviours());

	}
}