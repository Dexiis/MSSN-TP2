package CódigoDoStor;

public class DNA {

    public float maxSpeed;
    public float maxForce;
    public float visionDistance;
    public float visionSafeDistance;
    public float visionAngle;
    public float deltaTPursuit;
    public float radiusArrive;
    public float deltaTWander;
    public float radiusWander;
    public float deltaPhiWander;

    public DNA() {

        maxSpeed = random(1f, 2f);
        maxForce = random(4f, 7f);

        visionDistance = random(1.5f, 2.5f);
        visionSafeDistance = 0.25f*visionDistance;
        visionAngle = (float) Math.PI*0.3f;

        deltaTPursuit = random(0.5f, 1f);
        radiusArrive = random(3, 5);

        deltaTWander = random(.3f, .6f);
        radiusWander = random(1f, 3f);

        deltaPhiWander = (float) Math.PI/8;
    }

    public DNA(DNA dna, boolean mutate) {
        //Inacabado
    }

    public static float random(float min, float max) {
        return (float) (min + (max-min)*Math.random());
    }

}
