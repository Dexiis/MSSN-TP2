package TP2.Scenarios;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Target;
import TP2.Bodies.BoidAttributes.*;
import TP2.Bodies.BoidAttributes.Behaviours.*;
import TP2.Core.*;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList; // Necessário para criar a lista de corpos para o Eye
import java.util.List;     // Necessário para a lista de corpos

public class IndividualBehaviour extends PApplet {
	private Boid boid;
	private double[] window = { -10, 10, -10, 10 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;
	private Body target;
	private float lastUpdateTime;
	private Seek seek; 

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
	}

	public void keyPressed() {
		int increment = 1;
		
		if(key == 'A') {
			boid.setVelocity(increment, true);
			boid.setForce(increment, true);
		} else if (key == 's') {
			boid.setVelocity(increment, false);
			boid.setForce(increment, false);
		}
	}

	public void mousePressed() {
		double[] ww = plt.getWorldCoord(mouseX, mouseY);
		target.setPosition(new PVector((float) ww[0], (float) ww[1]));
	}
}