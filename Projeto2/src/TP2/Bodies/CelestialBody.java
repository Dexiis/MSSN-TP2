package TP2.Bodies;

import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

public class CelestialBody extends Body {

	protected String name;
	protected PImage image = null;

	public CelestialBody(String name, PVector position, PVector velocity, float mass, float radius, int color,
			String image, PApplet p) {
		super(position, velocity, mass, radius, color);
		this.name = name;

		if (image != null)
			this.image = p.loadImage(image);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();

		positions = plt.getPixelCoord(position.x, position.y);

		float[] r = plt.getVectorCoord(radius, radius);

		if (image != null) {
			p.imageMode(p.CENTER);
			p.image(this.image, positions[0], positions[1], 2 * r[0], 2 * r[0]);
		} else {
			p.noStroke();
			p.fill(color);
			p.circle(positions[0], positions[1], 2 * r[0]);
		}

		p.popStyle();
	}

}
