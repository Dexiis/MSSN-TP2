package Apps.Physics;
import Setup.IProcessingApp;
import Tools.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class SolarSystemAlt implements IProcessingApp {

    private final float sunMass = 1.989e30f;
    private final float earthMass = 5.97e24f;

    private final float distEarthSun = 1.496e11f;
    private final float earthSpeed = 2.98e4f;

    private final float[] viewport = {0f, 0f, 1f, 1f};
    private final double[] window = {-1.2*distEarthSun, 1.2*distEarthSun, -1.2*distEarthSun, 1.2*distEarthSun};

    private SubPlot plt;
    private Body sun;
    private ParticleSystem earth;
    private PSControl psc;
    private float[] velControl = {0, (float) Math.PI, 2*earthSpeed, 2*earthSpeed};
    private float[] lifetime = {60*60*24*4, 60*60*24*5};
    private float[] radius = {distEarthSun/40, distEarthSun/40};
    private float flow = 10f/(60*60*24);
    private float speedUp = 60*60*24*30;

    @Override
    public void setup(PApplet parent) {
        plt = new SubPlot(window, viewport, parent.width, parent.height);
        sun = new Body(new PVector(), new PVector(), sunMass,
                distEarthSun/10, parent.color(255, 111, 0));
        psc = new PSControl(velControl, lifetime, radius, flow, parent.color(0, 153, 153));
        earth = new ParticleSystem(new PVector(0, distEarthSun),
                new PVector(earthSpeed, 0), earthMass,
                distEarthSun/20, psc);
    }

    @Override
    public void draw(PApplet parent, float dt) {
        float[] pp = plt.getBoundingBox();

        parent.fill(0, 24);
        parent.rect(pp[0], pp[1], pp[2], pp[3]);

        sun.display(parent, plt);

        PVector f = sun.attraction(earth);
        earth.applyForce(f);
        earth.move(dt*speedUp);
        earth.display(parent, plt);
    }

    @Override
    public void keyPressed(PApplet parent) {}

    @Override
    public void mousePressed(PApplet parent) {}
}
