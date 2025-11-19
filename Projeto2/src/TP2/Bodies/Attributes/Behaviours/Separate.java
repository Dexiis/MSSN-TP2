package TP2.Bodies.Attributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.Attributes.Behaviour;
import processing.core.PVector;

public class Separate extends Behaviour {

	public Separate(float weight) {
		super(weight);
	}

	@Override
	public PVector getDesiredVelocity(Boid me) {
		PVector vd = new PVector();
		for (Body b : me.getEye().getNearSight()) {
			PVector r = PVector.sub(me.getPosition(), b.getPosition());
			float d = r.mag();
			r.div(d * d);
			vd.add(r);
		}
		return vd;
	}

}
