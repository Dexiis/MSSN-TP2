package CódigoDoStor;
import processing.core.PVector;

public abstract class Behaviour implements IBehaviour {

    protected float weight;
    public Behaviour(float weight) { this.weight = weight; }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        return null;
    }

    @Override
    public void setWeight(float weight) { this.weight = weight; }

    @Override
    public float getWeight() { return weight;}
}
