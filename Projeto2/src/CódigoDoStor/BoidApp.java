package CódigoDoStor;
import processing.core.PApplet;
import processing.core.PVector;

public class BoidApp implements IProcessingApp {

    private Boid b;
    private double[] window = {-10, 10, -10, 10};
    private float[] viewport = {0f, 0f, 1f, 1f};
    private SubPlot plt;
    private Body target;

    @Override
    public void setup(PApplet parent) {
        plt = new SubPlot(window, viewport, parent.width, parent.height);
        b = new Boid(new PVector(), 1, 0.5f, parent.color(0),
                parent, plt);
        target = new Body(new PVector(), new PVector(), 1f, 0.2f,
                parent.color(255, 0, 0));
    }

    @Override
    public void draw(PApplet parent, float dt) {
        parent.background(255);
        b.display(parent, plt);
        target.display(parent, plt);
    }

    @Override
    public void keyPressed(PApplet parent) {}

    @Override
    public void mousePressed(PApplet parent) {
        double[] ww = plt.getWorldCoord(parent.mouseX, parent.mouseY);
        target.setPos(new PVector((float) ww[0], (float) ww[1]));
    }
}
