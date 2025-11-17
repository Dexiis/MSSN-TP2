package Physics;
import processing.core.PVector;

public class Seek extends Behaviour {

    public Seek(float weight) { super(weight); }

    @Override
    public PVector getDesiredVelocity(Boid me) {
        Body bodyTarget = me.eye.target;
        return PVector.sub(bodyTarget.getPosition(), me.getPosition());
    }

}
