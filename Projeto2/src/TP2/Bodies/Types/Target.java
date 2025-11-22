package TP2.Bodies.Types;

import TP2.Bodies.*;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class Target extends Body {

	public Target(PVector posisiton, PVector velocity, float radius, int color) {
		super(posisiton, velocity, 0f, radius, color);
	}

	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		positions = plt.getPixelCoord(position.x, position.y);
		float[] r = plt.getVectorCoord(radius, radius);
		p.noStroke();
		p.fill(color);
		p.circle(positions[0], positions[1], 2 * r[0]);
		p.popStyle();
	}
}
