package TP2.Fluids;

import TP2.Bodies.Movement;
import TP2.Core.*;

import processing.core.PApplet;

public class Water extends Fluid {

    private float waterHeight;
    private int color;

    public Water(float waterHeight, int color) {
        super(1000.0f);
        this.waterHeight = waterHeight;
        this.color = color;
    }

    public boolean isInside(Movement m) { return (m.getPosition().y <= waterHeight); }

    public void display(PApplet p, SubPlot plt) {
        p.pushStyle();
        p.noStroke();
        p.fill(color);
        float[] pp = plt.getBox(0, 0, plt.getWindow()[1], waterHeight);
        p.rect(pp[0], pp[1], pp[2], pp[3]);
        p.popStyle();
    }

}
