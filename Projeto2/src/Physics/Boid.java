package Physics;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Boid extends Body {

	private PShape shape;
	protected DNA dna;
	protected Eye eye;
	private List<Behaviour> behaviours;
	protected float phiWander;
	private double[] window;
	private float sumWeights;

	public Boid(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt) {
		super(pos, new PVector(), mass, radius, color);
		setShape(p, plt);
		dna = new DNA();
		behaviours = new ArrayList<Behaviour>();
		window = plt.getWindow();
	}

	public void setEye(Eye eye) {
		this.eye = eye;
	}
	
	public Eye getEye() {
		return this.eye;
	}
	
	public void setVelocity(int n, boolean aux) {
		if(aux) {
			dna.maxSpeed += n;
		}else {
			dna.maxSpeed -= n;
			if(dna.maxSpeed<0) {
				dna.maxSpeed = 0;
			}
		}
	}
	
	public void setForce(int n, boolean aux) {
		if(aux) {
			dna.maxForce += n;
		}else {
			dna.maxForce -= n;
			if(dna.maxForce<0) {
				dna.maxForce = 0;
			}
		}
	}
	
	public void setShape(PApplet p, SubPlot plt, float radius, int color) {
		this.radius = radius;
		this.color = color;
		setShape(p,plt);
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
	
	private void updateSumWeights() {
		sumWeights = 0;
		for(Behaviour beh : behaviours) {
			sumWeights += beh.getWeight();
		}
	}

	public void addBehaviour(Behaviour behaviour) {
		behaviours.add(behaviour);
		updateSumWeights();
	}

	public void removeBehaviour(Behaviour behaviour) {
		if (behaviours.contains(behaviour)) {
			behaviours.remove(behaviour);
		}
		updateSumWeights();
	}

	public void applyBehaviour(int i, float dt) {
		if(eye!= null) eye.look();
		Behaviour behaviour = behaviours.get(i);
		PVector vd = behaviour.getDesiredVelocity(this);
		move(dt, vd);
	}

	public void applyBehaviours(float dt) {
		if(eye!= null) eye.look();
		PVector vd = new PVector();
		for (Behaviour behaviour : behaviours) {
			PVector vdd = behaviour.getDesiredVelocity(this);
			vdd.mult(behaviour.getWeight()/sumWeights);
			vd.add(vdd);
		}
		move(dt, vd);
	}

	private void move(float dt, PVector vd) {
		vd.normalize().mult(dna.maxSpeed);
		PVector fs = PVector.sub(vd, velocity);
		applyForce(fs.limit(dna.maxForce));
		super.move(dt);

		if (position.x < window[0]) {
			position.x += window[1] - window[0];
		}
		if (position.y < window[2]) {
			position.y += window[3] - window[2];
		}
		if (position.x >= window[1]) {
			position.x -= window[1] - window[0];
		}
		if (position.y >= window[3]) {
			position.y -= window[3] - window[2];
		}
		
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
