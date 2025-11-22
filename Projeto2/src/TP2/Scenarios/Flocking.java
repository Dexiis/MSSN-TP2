package TP2.Scenarios;

import java.util.ArrayList;
import java.util.List;

import TP2.Bodies.*;
import TP2.Bodies.Types.*;
import TP2.Bodies.Attributes.*;
import TP2.Bodies.Attributes.Behaviours.*;
import TP2.Bodies.Body;
import TP2.Core.SubPlot;

import processing.core.PApplet;
import processing.core.PVector;
import processing.sound.*;

public class Flocking extends PApplet {

	private final int NB_PREY = 50;
	private float lastUpdateTime;
	private boolean playerIsDead = false;
	private boolean wKey = false;
	private boolean aKey = false;
	private boolean sKey = false;
	private boolean dKey = false;

	private List<Body> allBodies;
	private List<Body> preyFlock;

	private Predator predator;
	private Prey player, monitoredPrey;
	private Flee flee;
	private Flee evade;
	private Align align;
	private Cohesion cohesion;
	private Separate separate;
	private Seek seek;
	private Wander wander;

	private SoundFile deathSound;

	private List<Behaviour> preyBehaviours = new ArrayList<Behaviour>();
	private List<Behaviour> predatorBehaviours = new ArrayList<Behaviour>();
	private List<Particle> particles = new ArrayList<Particle>();

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

		deathSound = new SoundFile(this, "DeathSound.wav");

		for (int i = 0; i < NB_PREY; i++) {
			if (i == 0) {
				monitoredPrey = new Prey(randomPositionn(), 1f, 0.4f, color(0, 123, 122), this, plt);
				preyFlock.add(monitoredPrey);
				allBodies.add(monitoredPrey);
			} else {
				Prey prey = new Prey(randomPositionn(), 1f, 0.4f, color(0, 0, 255), this, plt);
				preyFlock.add(prey);
				allBodies.add(prey);
			}
		}

		player = new Prey(randomPositionn(), 1f, 0.4f, color(0, 255, 0), this, plt);
		preyFlock.add(player);
		allBodies.add(player);

		predator = new Predator(randomPositionn(), 2f, 0.8f, color(255, 0, 0), this, plt, preyFlock);
		allBodies.add(predator);

		for (Body body : preyFlock) {
			Boid prey = (Boid) body;
			if (prey == player)
				continue;
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

		predatorMovement(dt);

		for (int i = preyFlock.size() - 1; i >= 0; i--) {
			Boid prey = (Boid) preyFlock.get(i);
			if (prey == player) {
				isDead(prey);
				continue;
			}
			preyMovement(dt, prey);
			isDead(prey);
		}

		if (!playerIsDead)
			playerMovement(dt);

		for (Body body : allBodies)
			body.display(this, plt);
		
		monitoredPrey.getEye().display(this, plt);
		monitoredPrey.displayVelocityVector(this, plt);

		predator.getEye().display(this, plt);
		if (predator.getEye().getTarget() != null) {
			PVector targetPos = predator.getEye().getTarget().getPosition();
			float[] pp = plt.getPixelCoord(targetPos.x, targetPos.y);
			noFill();
			stroke(255, 0, 0);
			strokeWeight(3);
			ellipse(pp[0], pp[1], 10, 10);
		}

		for (int i = particles.size() - 1; i >= 0; i--) {
			Particle particle = particles.get(i);
			particle.display(this, plt);
			particle.move(dt);
			if (particle.isDead())
				particles.remove(particle);
		}

	}

	private void isDead(Boid prey) {
		PVector distance = prey.getToroidalDistanceVector(predator.getPosition());
		if (distance.mag() < (prey.getRadius() + predator.getRadius()) / 2) {
			preyFlock.remove(prey);
			allBodies.remove(prey);
			deathAnimation(prey.getPosition());
			predator.getEye().setAllTrackingBodies(preyFlock);
			if (prey == player)
				playerIsDead = true;
			deathSound.play();
		}

	}

	private void deathAnimation(PVector position) {
		final float PARTICLE_LIFESPAN = 5f;
		final int NUM_PARTICLES = 20;

		for (int i = 0; i < NUM_PARTICLES; i++) {
			PVector vel = PVector.random2D();
			vel.mult(random(0.1f, 1.5f));
			int particleColor = color(random(200, 255), 0, 0, 200);
			float lifespan = random(0.5f, PARTICLE_LIFESPAN);
			float radius = 0.1f;

			Particle particle = new Particle(position.copy(), vel, radius, particleColor, lifespan);
			particles.add(particle);
		}
	}

	private PVector randomPositionn() {
		float x = random((float) window[0], (float) window[1]);
		float y = random((float) window[2], (float) window[3]);
		return new PVector(x, y);
	}

	private void playerMovement(float dt) {
		PVector desiredVelocity = new PVector(0, 0);
		if (wKey)
			desiredVelocity.add(0, player.getDNA().getMaxSpeed());
		if (aKey)
			desiredVelocity.add(-player.getDNA().getMaxSpeed(), 0);
		if (sKey)
			desiredVelocity.add(0, -player.getDNA().getMaxSpeed());
		if (dKey)
			desiredVelocity.add(player.getDNA().getMaxSpeed(), 0);
		player.move(dt, desiredVelocity);
	}

	private void preyMovement(float dt, Boid prey) {
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
	}

	private void predatorMovement(float dt) {
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
	}

	public void keyPressed() {

		if (key == 'w' || key == 'W') {
			wKey = true;
		}
		if (key == 'a' || key == 'A') {
			aKey = true;
		}
		if (key == 's' || key == 'S') {
			sKey = true;
		}
		if (key == 'd' || key == 'D') {
			dKey = true;
		}
	}

	public void keyReleased() {
		if (key == 'w' || key == 'W') {
			wKey = false;
		}
		if (key == 'a' || key == 'A') {
			aKey = false;
		}
		if (key == 's' || key == 'S') {
			sKey = false;
		}
		if (key == 'd' || key == 'D') {
			dKey = false;
		}
	}

}