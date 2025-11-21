package TP2.Bodies;

import java.util.List;

import TP2.Bodies.Attributes.*;
import TP2.Core.*;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Boid extends Body {

	private PShape shape;
	protected DNA dna;
	protected Eye eye;

	protected float phiWander;
	private double[] window;

	public Boid(PVector position, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(position, new PVector(), mass, radius, color);
		setShape(p, plt);
		dna = new DNA();
		window = plt.getWindow();

	}

	public Boid(PVector position, float mass, float radius, int color, Entity entity, PApplet p, SubPlot plt) {
		super(position, new PVector(), mass, radius, color);
		setShape(p, plt);
		dna = new DNA(entity);
		window = plt.getWindow();

	}

	public void setEye(Eye eye) {
		this.eye = eye;
	}

	public Eye getEye() {
		return this.eye;
	}

	public void setVelocity(int increment, boolean aux) {
		if (aux)
			dna.maxSpeed += increment;
		else {
			dna.maxSpeed -= increment;
			dna.maxSpeed = Math.max(0, dna.maxSpeed);
		}
	}

	public void setForce(int increment, boolean aux) {
		if (aux)
			dna.maxForce += increment;
		else {
			dna.maxForce -= increment;
			dna.maxForce = Math.max(0, dna.maxForce);
		}
	}

	public void setShape(PApplet p, SubPlot plt, float radius, int color) {
		this.radius = radius;
		this.color = color;
		setShape(p, plt);
	}

	public void setShape(PApplet p, SubPlot plt) {
		float[] rr = plt.getVectorCoord(radius, radius);
		shape = p.createShape();
		shape.beginShape();
		shape.noStroke();
		shape.fill(color);
		shape.vertex(-rr[0], rr[0] / 2);
		shape.vertex(rr[0], 0);
		shape.vertex(-rr[0], -rr[0] / 2);
		shape.vertex(-rr[0] / 2, 0);
		shape.endShape(PConstants.CLOSE);
	}

	public void setPhiWander(float newPhiWander) {
		this.phiWander = newPhiWander;
	}

	public DNA getDNA() {
		return dna;
	}

	public float getPhiWander() {
		return phiWander;
	}

	public void applyBehaviour(Behaviour behaviour, float dt) {
		if (eye != null)
			eye.look();
		PVector vd = behaviour.getDesiredVelocity(this);
		move(dt, vd);
	}

	public void applyBehaviours(List<Behaviour> behaviours, float dt) {
		if (eye != null)
			eye.look();
		PVector vd = new PVector();
		float sumWeights = 0;
		for (Behaviour behaviour : behaviours)
			sumWeights += behaviour.getWeight();

		for (Behaviour behaviour : behaviours) {
			PVector vdd = behaviour.getDesiredVelocity(this);
			vdd.mult(behaviour.getWeight() / sumWeights);
			vd.add(vdd);
		}
		move(dt, vd);
	}

	private void move(float dt, PVector vd) {
		vd.normalize().mult(dna.maxSpeed);
		PVector fs = PVector.sub(vd, velocity);
		applyForce(fs.limit(dna.maxForce));
		super.move(dt);

		if (position.x < window[0])
			position.x += window[1] - window[0];
		if (position.y < window[2])
			position.y += window[3] - window[2];
		if (position.x >= window[1])
			position.x -= window[1] - window[0];
		if (position.y >= window[3])
			position.y -= window[3] - window[2];
	}

	public PVector getToroidalDistanceVector(PVector targetPosition) {
		PVector distance = PVector.sub(targetPosition, position);

		double worldWidth = window[1] - window[0];
		double worldHeight = window[3] - window[2];

		if (Math.abs(distance.x) > worldWidth / 2) {
			if (distance.x > 0)
				distance.x -= worldWidth;
			else
				distance.x += worldWidth;
		}

		if (Math.abs(distance.y) > worldHeight / 2) {
			if (distance.y > 0)
				distance.y -= worldHeight;
			else
				distance.y += worldHeight;
		}

		return distance;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushMatrix();
		float[] pp = plt.getPixelCoord(position.x, position.y);
		float[] vv = plt.getVectorCoord(velocity.x, velocity.y);
		PVector vaux = new PVector(vv[0], vv[1]);
		p.translate(pp[0], pp[1]);
		p.rotate(-vaux.heading());
		p.shape(shape);
		p.popMatrix();
	}

}
