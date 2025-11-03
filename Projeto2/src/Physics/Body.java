package Physics;

import processing.core.PApplet;
import processing.core.PVector;

public class Body extends Mover {

	protected int color;
	protected float radius;
	protected String name;
	protected float displayScaleFactor;
	protected float radiusScaleFactor;

	public Body(PVector pos, PVector vel, float mass, float radius, int color, float displayScaleFactor,
			float radiusScaleFactor) {
		super(pos, vel, mass);
		this.color = color;
		this.radius = radius;
		this.displayScaleFactor = displayScaleFactor;
		this.radiusScaleFactor = radiusScaleFactor;
	}

	public Body(PVector pos) {
		super(pos, new PVector(), 0f);
	}

	public float getDisplayScaleFactor() {
		return displayScaleFactor;
	}

	public void display(PApplet p, PVector screenPos) {
		p.pushStyle();
		p.noStroke();
		p.fill(color);
		
		// Tamanho correto
		// Colocar SUN_RADIUS_SCALE = 0.0000005f
		// Colocar ORBIT_SPACING_PIXELS = 70.0f;
		// p.circle(screenPos.x, screenPos.y, radius); 
		
		// Tamanho apenas para efeitos VISUAIS
		p.circle(screenPos.x, screenPos.y, 6.37e6f * 0.000001f * radiusScaleFactor);
		// Colocar SUN_RADIUS_SCALE = 0.00000005f;
		// Colocar ORBIT_SPACING_PIXELS = 50.0f
		p.popStyle();
	}
}