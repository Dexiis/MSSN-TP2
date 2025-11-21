package TP2.Bodies.Attributes.Behaviours;

import java.util.ArrayList;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Patrol extends Behaviour {

	private ArrayList<PVector> path;
	private int currentIndex = 0;

	public Patrol(float weight) {
		super(weight);
		defineDefaultPath();
	}

	public void definePath(ArrayList<PVector> newPath) {
		this.path = newPath;
		if (!path.isEmpty()) 
			this.currentIndex = 0;
	}

	private void defineDefaultPath() {
		this.path = new ArrayList<>();
		path.add(new PVector(8f, 0f));
		path.add(new PVector(4f, 6.93f)); 
		path.add(new PVector(-4f, 6.93f)); 
		path.add(new PVector(-8f, 0f)); 
		path.add(new PVector(-4f, -6.93f)); 
		path.add(new PVector(4f, -6.93f));
		
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		if (path.isEmpty())
			return new PVector(0, 0);

		PVector vd = me.getToroidalDistanceVector(path.get(currentIndex));
		
		float distance = vd.mag();
		float radius = me.getDNA().radiusArrive;

		if (distance < radius) {
			vd.mult(distance / radius);
			currentIndex = (currentIndex + 1) % path.size();
		}
		
		return vd;
	}
}