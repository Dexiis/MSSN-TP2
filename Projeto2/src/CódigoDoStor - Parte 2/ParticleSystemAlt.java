package Apps.Physics;
import Setup.IProcessingApp;
import Tools.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.List;

public class ParticleSystemAlt implements IProcessingApp {

    private List<ParticleSystem> pss;
    private PSControl psc;

    private float[] velControl = {PApplet.radians(180), PApplet.radians(20), 1f, 3};
    private float[] lifetimeC = {3, 5};
    private float[] radiusC = {0.1f, 0.2f};
    private float flow = 500;

    private float[] viewport = {0f, 0f, 1f, 1f};
    private double[] window = {-10, 10, -10, 10};
    private SubPlot plt;

    @Override
    public void setup(PApplet parent) {
        plt = new SubPlot(window, viewport, parent.width, parent.height);
        pss = new ArrayList<ParticleSystem>();
    }

    @Override
    public void draw(PApplet parent, float dt) {
        parent.background(255);

        for(ParticleSystem ps : pss) ps.applyForce(new PVector(0, 0));
        for(ParticleSystem ps : pss) {
            ps.move(dt);
            ps.display(parent, plt);
        }

        velControl[0] = PApplet.map(parent.mouseX, 0, parent.width,
                PApplet.radians(0), PApplet.radians(360));

        for(ParticleSystem ps : pss) {
            PSControl psc = ps.getPSControl();
            psc.setVelParams(velControl);
        }
    }

    @Override
    public void keyPressed(PApplet parent) {

    }

    @Override
    public void mousePressed(PApplet parent) {
        double[] ww = plt.getWorldCoord(parent.mouseX, parent.mouseY);
        int color = parent.color(parent.random(255), parent.random(255), parent.random(255));

        PSControl psc = new PSControl(velControl, lifetimeC, radiusC, flow, color);
        ParticleSystem ps = new ParticleSystem(new PVector((float) ww[0],
                (float) ww[1]), new PVector(), 1f, .2f, psc);
        pss.add(ps);
    }

}