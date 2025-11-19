package TP2.Bodies.BoidAttributes.Behaviours;

import java.util.ArrayList;

import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;

import processing.core.PVector;

public class Patrol extends Behaviour {

	private ArrayList<PVector> path;
	private int currentIndex = 0;

	public Patrol(float weight) {
		super(weight);
		path = definePath();
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector target = path.get(currentIndex);
		PVector vd = PVector.sub(target, me.getPosition());
		float dist = vd.mag();
		float R = me.getDNA().radiusArrive;
		if (dist < R) {
			vd.mult(dist / R);
			currentIndex = (currentIndex + 1) % path.size();
		}
		return vd;
	}

	private ArrayList<PVector> definePath() {
		ArrayList<PVector> p = new ArrayList<>();
		p.add(new PVector(8f, 0f));
		p.add(new PVector(4f, 6.93f));
		p.add(new PVector(-4f, 6.93f));
		p.add(new PVector(-8f, 0f));
		p.add(new PVector(-4f, -6.93f));
		p.add(new PVector(4f, -6.93f));
		return p;
	}

	public void getNearPos(Boid me) {
		float minDist = Float.MAX_VALUE;
		int nearestIndex = 0;

		for (int i = 0; i < path.size(); i++) {
			float d = PVector.dist(me.getPosition(), path.get(i));
			if (d < minDist) {
				minDist = d;
				nearestIndex = i;
			}
		}

		currentIndex = nearestIndex;
	}

}
