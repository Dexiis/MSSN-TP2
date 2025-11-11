package Apps.AutonomousAgent;
import Apps.Physics.Body;
import Tools.SubPlot;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PShape;
import processing.core.PVector;

public class Boid extends Body {

    private PShape shape;
    protected DNA dna;
    protected Eye eye;

    protected Boid(PVector pos, float mass, float radius, int color,
                   PApplet p, SubPlot plt) {
        super(pos, new PVector(), mass, radius, color);
        dna = new DNA();
        setShape(p, plt);
    }

    public void setShape(PApplet p, SubPlot plt) {
        float[] rr = plt.getVectorCoord(radius, radius);

        shape = p.createShape();
        shape.beginShape();
        shape.noStroke();
        shape.fill(color);

        shape.vertex(-rr[0], rr[0]/2);
        shape.vertex(rr[0], 0);
        shape.vertex(-rr[0], -rr[0]/2);
        shape.vertex(-rr[0]/2, 0);
        shape.endShape(PConstants.CLOSE);
    }

    @Override
    public void display(PApplet p, SubPlot plt) {
        p.pushMatrix();
        float[] pp = plt.getPixelCoord(pos.x, pos.y);
        float[] vv = plt.getVectorCoord(vel.x, vel.y);
        PVector vaux = new PVector(vv[0], vv[1]);
        p.translate(pp[0], pp[1]);
        p.rotate(-vaux.heading());
        p.shape(shape);
        p.popMatrix();
    }

    public void setEye(Eye eye) { this.eye = eye; }
    public Eye getEye() { return eye; }

}
