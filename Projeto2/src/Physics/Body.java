package Physics;

import processing.core.PApplet;
import processing.core.PVector;

public class Body extends Mover {

    protected int color;
    protected float radius;

    public Body(PVector pos, PVector vel, float mass, float radius, int color) {
        super(pos, vel, mass);
        this.color = color;
        this.radius = radius;
    }

    public Body(PVector pos) {
        super(pos, new PVector(), 0f);
    }

    public void display(PApplet p) {
        p.pushStyle();
        p.noStroke();
        p.fill(color);
        p.circle(pos.x, pos.y, 3);
        p.popStyle();
    }
}
