package BasicPhysics;

import processing.core.PApplet;
import java.util.ArrayList;
import Physics.*;

public class SolarSystem extends PApplet {

    private static final double G = 0.001;
    private static final double TIME_STEP = 1.0;

    private ArrayList<Body> bodies;

    public void settings() {
        size(800, 800);
    }

    public void setup() {
        bodies = new ArrayList<>();
        initializeSolarSystem();
        background(0);
    }

    public void draw() {
        background(0);

        calculateGravityForces();

        updatePhysics(TIME_STEP);

        drawBodies();
    }

    private void initializeSolarSystem() {
        double centerX = width / 2.0;
        double centerY = height / 2.0;

        Body sun = new Body("Sol", 50000.0, 30.0, centerX, centerY, 0.0, 0.0);
        bodies.add(sun);

        bodies.add(new Body("Mercúrio", 5.0, 5.0, centerX + 50.0, centerY, 0.0, 5.0));

        bodies.add(new Body("Vénus", 10.0, 8.0, centerX + 100.0, centerY, 0.0, 3.5));

        bodies.add(new Body("Terra", 15.0, 10.0, centerX + 150.0, centerY, 0.0, 3.0));

        bodies.add(new Body("Marte", 7.0, 6.0, centerX + 200.0, centerY, 0.0, 2.5));

        bodies.add(new Body("Júpiter", 50.0, 20.0, centerX + 280.0, centerY, 0.0, 2.0));

        bodies.add(new Body("Saturno", 40.0, 18.0, centerX + 350.0, centerY, 0.0, 1.8));

        bodies.add(new Body("Úrano", 25.0, 15.0, centerX + 400.0, centerY, 0.0, 1.6));

        bodies.add(new Body("Neptuno", 20.0, 14.0, centerX + 450.0, centerY, 0.0, 1.4));
    }

    private void calculateGravityForces() {
        Body sun = bodies.get(0);

        for (int i = 1; i < bodies.size(); i++) {
            Body planet = bodies.get(i);
            planet.resetAcceleration();

            double rx = sun.posX - planet.posX;
            double ry = sun.posY - planet.posY;

            double distance = Math.sqrt(rx * rx + ry * ry);
            if (distance < sun.radius + planet.radius) {
                 distance = sun.radius + planet.radius;
            }

            double forceMagnitude = (G * sun.mass * planet.mass) / (distance * distance);

            double forceX = forceMagnitude * (rx / distance);
            double forceY = forceMagnitude * (ry / distance);

            planet.accX += forceX / planet.mass;
            planet.accY += forceY / planet.mass;
        }
    }

    private void updatePhysics(double dt) {
        for (Body body : bodies) {
            body.updateVelocity(dt);
            body.updatePosition(dt);
        }
    }

    private void drawBodies() {
        ellipseMode(CENTER);

        for (Body body : bodies) {
            if (body.name.equals("Sol")) {
                fill(255, 200, 0);
            } else {
                fill(100, 100, 255);
            }
            noStroke();

            ellipse((float)body.posX, (float)body.posY, (float)body.radius * 2, (float)body.radius * 2);
        }
    }

    public static void main(String[] args) {
        PApplet.main("SolarSystemSimulation");
    }
}