package TP2.Scenarios;

import TP2.Bodies.Body;
import TP2.Bodies.Types.*;
import TP2.Bodies.Attributes.*;
import TP2.Bodies.Attributes.Behaviours.*;
import TP2.Core.*;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public class IndividualBehaviour extends PApplet {
	private double[] window = { -10f, 10f, -10f, 10f };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;
	private float lastUpdateTime;
	
	private Prey boid;
	private Predator boidPursuer;
	private Body target;

	private Seek seek;
	private Arrive arrive;
	private Patrol patrol;
	private Wander wander;
	private Flee flee;
	
	private List<Behaviour> preyBehaviours = new ArrayList<Behaviour>();
	private List<Behaviour> predatorBehaviours = new ArrayList<Behaviour>();

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
		targets.add(boidPursuer);
		targets.add(target);
		boid.setEye(new Eye(boid, targets));

		List<Body> pursuerTargets = new ArrayList<>();
		pursuerTargets.add(boid);
		boidPursuer.setEye(new Eye(boidPursuer, pursuerTargets));

		seek = new Seek(0.8f);
		arrive = new Arrive(1f);
		patrol = new Patrol(1f);
		wander = new Wander(0.2f);
		flee = new Flee(0.8f);
		
		preyBehaviours.add(flee);
		preyBehaviours.add(wander);
		
		predatorBehaviours.add(seek);
		predatorBehaviours.add(wander);
		boid.getEye().setTarget(target);
	}

	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(255);

		if (currentBehaviour == PERSUIT_EVADE_MODE) {
			
			boidPursuerMovement(dt);
			boidPursuer.display(this, plt);
			boidPursuer.getEye().display(this, plt);

			boidMovement(dt);
			
		} else {
			if (currentBehaviour == SEEK_MODE) {
				boid.applyBehaviour(seek, dt);
			} else if (currentBehaviour == ARRIVE_MODE) {
				boid.applyBehaviour(arrive, dt);
			} else if (currentBehaviour == PATROL_MODE) {
				boid.applyBehaviour(patrol, dt);
			} else if (currentBehaviour == WANDER_MODE) {
				boid.applyBehaviour(wander, dt);
			} else if (currentBehaviour == FLEE_MODE) {
				boid.applyBehaviour(flee, dt);
			}
			if (!staticDot) {
				double[] ww = plt.getWorldCoord(mouseX, mouseY);
				target.setPosition(new PVector((float) ww[0], (float) ww[1]));
			}
			target.display(this, plt);
		}

		boid.display(this, plt);
		boid.getEye().display(this, plt);

		pushStyle();
		fill(0);
		textSize(16);
		String behaviourName;
		if (currentBehaviour == SEEK_MODE) {
			behaviourName = "Seek";
		} else if (currentBehaviour == ARRIVE_MODE) {
			behaviourName = "Arrive";
		} else if (currentBehaviour == PATROL_MODE) {
			behaviourName = "Patrol";
		} else if (currentBehaviour == FLEE_MODE) {
			behaviourName = "Flee";
		} else if (currentBehaviour == PERSUIT_EVADE_MODE) {
			behaviourName = "Evade/Flee/Wander";
		} else {
			behaviourName = "Wander";
		}
		text("Comportamento Atual (C): " + behaviourName, 10, 20);
		text("Controlo Boid: 'W' (Acelerar) | 'S' (Travar)", 10, 40);
		text("Controlo BoidPersuer: 'R' (Acelerar) | 'F' (Travar)", 10, 60);
		text("Mudar Comportamento: 'C'", 10, 80);
		popStyle();
	}

	public void keyPressed() {
		int increment = 1;

		if (key == 'w' || key == 'W') {
			boid.modifyVelocity(increment, true);
			boid.modifyForce(increment, true);
		} else if (key == 's' || key == 'S') {
			boid.modifyVelocity(increment, false);
			boid.modifyForce(increment, false);
		} else if (key == 'r' || key == 'R') {
			boidPursuer.modifyVelocity(increment, true);
			boidPursuer.modifyForce(increment, true);
		} else if (key == 'f' || key == 'F') {
			boidPursuer.modifyVelocity(increment, false);
			boidPursuer.modifyForce(increment, false);
		} else if (key == 'c' || key == 'C') {

			if (currentBehaviour == SEEK_MODE) {
				currentBehaviour = ARRIVE_MODE;
			} else if (currentBehaviour == ARRIVE_MODE) {
				currentBehaviour = PATROL_MODE;
			} else if (currentBehaviour == PATROL_MODE) {
				currentBehaviour = WANDER_MODE;
			} else if (currentBehaviour == WANDER_MODE) {
				currentBehaviour = FLEE_MODE;
			} else if (currentBehaviour == FLEE_MODE) {
				currentBehaviour = PERSUIT_EVADE_MODE;
				boid.getEye().setTarget(boidPursuer);
				target.setPosition(new PVector(-100, -100));
			} else {
				currentBehaviour = SEEK_MODE;
				boid.getEye().setTarget(target);
			}
		}
	}
	
	private void boidPursuerMovement(float dt) {
		if (!boidPursuer.getEye().getNearSight().isEmpty()) {
			boidPursuer.applyBehaviour(seek, dt);
		} else if (!boidPursuer.getEye().getFarSight().isEmpty()) {
			boidPursuer.applyBehaviours(predatorBehaviours, dt);
		} else {
			boidPursuer.applyBehaviour(wander, dt);
		}
	}
	
	private void boidMovement(float dt) {
		if (!boid.getEye().getNearSight().isEmpty()) {
			boid.applyBehaviour(flee, dt);
		} else if (!boid.getEye().getFarSight().isEmpty()) {
			boid.applyBehaviours(preyBehaviours, dt);
		} else {
			boid.applyBehaviour(wander, dt);
		}
	}

	public void mousePressed() {
		staticDot = !staticDot;
	}
}