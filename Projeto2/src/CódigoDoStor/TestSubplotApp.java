package CódigoDoStor;
import processing.core.PApplet;
import processing.core.PVector;

public class TestSubplotApp implements IProcessingApp {

    private double[] window = {0, 5, 0, 7};
    private float[] viewport = {0.2f, 0.5f, 0.2f, 0.2f};
    private float[] viewport2 = {0.7f, 0.05f, 0.1f, 0.5f};
    private Body b;
    private SubPlot plt, plt2;

    @Override
    public void setup(PApplet parent) {

        plt = new SubPlot(window, viewport, parent.width, parent.height);
        plt2 = new SubPlot(window, viewport2, parent.width, parent.height);

        b = new Body(new PVector(4f, 6f), new PVector(), 10f, 0.5f, 0);

        float[] bb = plt.getBoundingBox();
        parent.fill(255, 0, 0);
        parent.rect(bb[0], bb[1], bb[2], bb[3]);

        float[] bb2 = plt2.getBoundingBox();
        parent.fill(255, 255, 0);
        parent.rect(bb2[0], bb2[1], bb2[2], bb2[3]);

        b.display(parent, plt);
    }

    @Override
    public void draw(PApplet parent, float dt) {

    }

    @Override
    public void keyPressed(PApplet parent) {

    }

    @Override
    public void mousePressed(PApplet parent) {

        double[] xy = plt.getWorldCoord(parent.mouseX, parent.mouseY);
        System.out.println(xy[0] + "   " + xy[1]);

    }
}
