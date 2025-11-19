package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Eye;
import TP2.Core.*;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Flock {

	private final List<Boid> boids;

	public Flock(int nBoids, float mass, float radius, int color, float[] sacWeights, PApplet p, SubPlot plt) {

		double[] w = plt.getWindow();
		boids = new ArrayList<>();
		for (int i = 0; i < nBoids; i++) {
			float x = p.random((float) w[0], (float) w[1]);
			float y = p.random((float) w[2], (float) w[3]);

			Boid boid = new Boid(new PVector(x, y), mass, radius, color, p, plt);
			boid.addBehaviour(new Separate(sacWeights[0]));
			boid.addBehaviour(new Align(sacWeights[1]));
			boid.addBehaviour(new Cohesion(sacWeights[2]));

			boids.add(boid);
		}

		List<Body> bodies = boidList2BodyList(boids);
		for (Boid boid : boids)
			boid.setEye(new Eye(boid, bodies));
	}

	private List<Body> boidList2BodyList(List<Boid> boids) {
		return new ArrayList<Body>(boids);
	}

	public void applyBehaviour(float dt) {
		for (Boid boid : boids)
			boid.applyBehaviour(1 , dt);
	}

	public Boid getBoid(int i) {
		return boids.get(i);
	}

	public void display(PApplet p, SubPlot plt) {
		for (Boid boid : boids)
			boid.display(p, plt);
	}
}
