package Simulations;

import java.util.ArrayList;

import CódigoDoStor.Body;
import CódigoDoStor.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class NormandyWar extends PApplet {

	private ArrayList<Body> soldiers = new ArrayList<>();
	
    private static final float g = -9.8f;
    private int lastUpdateTime;
    private SubPlot plt;
    private double[] window = {0f, 800, 0f, 600};
    private float[] viewport = {0f, 0f, 1f, 1f};

    public void settings() {
        size(800, 600);
    }

    public void setup() {
        plt = new SubPlot(window, viewport, width, height); 
        generateSoldiers();
        lastUpdateTime = millis();
    }

    public void draw() {
        background(255);
        int now = millis();
        float dt = (now - lastUpdateTime) / 1000f;
        lastUpdateTime = now;
        
        for(Body soldier : soldiers) {
        	PVector weightForce = new PVector(0, soldier.getMass() * g); // P = mg
        	soldier.applyForce(weightForce);
        	soldier.move(dt);
        	soldier.display(this, plt);
        }
    }

    private void generateSoldiers() {
    	Body body = new Body(new PVector(100, 600), new PVector(0, 0), 80, 10, color(0, 0, 0));
    	// Gerar soldados random
    	soldiers.add(body);
    }
    
    public void keyPressed() {
    	
    }

    public void mousePressed() {

    }
}

