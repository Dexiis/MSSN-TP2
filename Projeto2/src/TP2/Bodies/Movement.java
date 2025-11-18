package TP2.Bodies;

import processing.core.PVector;

public abstract class Movement {

    protected PVector position;
    protected PVector velocity;
    protected PVector acceleration;
    protected float mass;
    private static final double G = 6.67e-11;

    protected Movement(PVector position, PVector velocity, float mass) {
        this.position = position.copy();
        this.velocity = velocity;
        acceleration = new PVector();
        this.mass = mass;
    }

    public void applyForce(PVector force) { acceleration.add(PVector.div(force, mass)); }

    public void move(float time) {
        velocity.add(acceleration.mult(time));
        position.add(PVector.mult(velocity, time));
        acceleration.mult(0);
    }

    public PVector attraction(Movement body) {
        PVector direction = PVector.sub(this.position, body.position); 
        float distance = direction.mag();
        
        float minDistance = 1e6f;
        distance = Math.max(distance, minDistance);
        
        float strength = (float) (G * this.mass * body.mass / Math.pow(distance, 2));
        
        return direction.normalize().mult(strength);
    }

    public PVector getPosition() { return position; }
    public PVector getVelocity() { return velocity; }
    public PVector getAcceleration() { return acceleration; }
    public float getMass() { return mass; }

    public void setAcceleration(PVector acceleration) {this.acceleration = acceleration;}
    public void setVelocity(PVector velocity) {this.velocity = velocity;}
    public void setPosition(PVector position) {this.position = position;}

}