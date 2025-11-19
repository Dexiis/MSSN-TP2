package TP2.Bodies.Attributes;

import java.util.ArrayList;
import java.util.List;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Core.*;
import processing.core.PApplet;
import processing.core.PVector;

public class Eye {
	private List<Body> allTrackingBodies = new ArrayList<>();
	private List<Body> farSight;
	private List<Body> nearSight;
	private Boid me;
	protected Body target;

	public Eye(Boid me, List<Body> allTrackingBodies) {
		this.me = me;
		this.allTrackingBodies = allTrackingBodies;
		this.target = allTrackingBodies.get(0);
	}

	public List<Body> getFarSight() {
		return farSight;
	}

	public List<Body> getNearSight() {
		return nearSight;
	}

	public Body getTarget() {
		return target;
	}

	public void setTargets(List<Body> allTrackingBodies) {
		this.allTrackingBodies = allTrackingBodies;
	}
	
	public void setTarget(Body target) {
		this.target = target;
		this.allTrackingBodies = new ArrayList<>();
		this.allTrackingBodies.add(target);
	}

	public void look() {
		farSight = new ArrayList<Body>();
		nearSight = new ArrayList<Body>();
		for (Body body : allTrackingBodies) {
			if (farSight(body.getPosition()))
				farSight.add(body);
			if (nearSight(body.getPosition()))
				nearSight.add(body);
		}
	}

	private boolean inSight(PVector t, float maxDistance, float maxAngle) {
		PVector r = PVector.sub(t, me.getPosition());
		float d = r.mag();
		float angle = PVector.angleBetween(r, me.getVelocity());
		return ((d > 0) && (d < maxDistance) && (angle < maxDistance));
	}

	private boolean farSight(PVector t) {
		return inSight(t, me.getDNA().visionDistance, me.getDNA().visionAngle);
	}

	private boolean nearSight(PVector t) {
		return inSight(t, me.getDNA().visionSafeDistance, (float) Math.PI);
	}

	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		p.pushMatrix();

		float[] pp = plt.getPixelCoord(me.getPosition().x, me.getPosition().y);
		p.translate(pp[0], pp[1]);

		p.rotate(-me.getVelocity().heading());
		p.noFill();
		p.stroke(255, 0, 0);
		p.strokeWeight(3);

		float[] dd1 = plt.getDimInPixel(me.getDNA().visionDistance, me.getDNA().visionDistance);
		float[] dd2 = plt.getDimInPixel(me.getDNA().visionSafeDistance, me.getDNA().visionSafeDistance);
		p.rotate(me.getDNA().visionAngle);
		p.line(0, 0, dd1[0], 0);
		p.rotate(-2 * me.getDNA().visionAngle);
		p.line(0, 0, dd1[0], 0);
		p.rotate(me.getDNA().visionAngle);
		p.arc(0, 0, 2 * dd1[0], 2 * dd1[0], -me.getDNA().visionAngle, me.getDNA().visionAngle);

		p.stroke(255, 0, 255);
		p.circle(0, 0, 2 * dd2[0]);

		p.popMatrix();
		p.popStyle();
	}
}
