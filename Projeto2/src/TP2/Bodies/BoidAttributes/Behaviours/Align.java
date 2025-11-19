package TP2.Bodies.BoidAttributes.Behaviours;

import TP2.Bodies.Body;
import TP2.Bodies.Boid;
import TP2.Bodies.BoidAttributes.Behaviour;

import processing.core.PVector;

public class Align extends Behaviour {

    public Align(float weight) { super(weight); }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        PVector vd = me.getVelocity().copy();
        for(Body b : me.getEye().getFarSight()) vd.add(b.getVelocity());
        return vd.div(me.getEye().getFarSight().size()+1);
    }

}
