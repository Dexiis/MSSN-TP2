import processing.core.PApplet;
import BasicPhysics.*;
import EntitiesInteractions.*;
import Simulations.*;

public class MainMenu extends PApplet {

	private String title = "Chose what to open:";
	private String option1 = "Solar System Simulation";
	private String option2 = "Individual Behaviours between identities";
	private String option3 = "Flocking";
	private String option4 = "Normandy War Simulation";
	private String option5 = "Space War Simulation";

	public void settings() {
		size(650, 360);
	}

	public void setup() {
		textAlign(CENTER, CENTER);
		textSize(16);
		rectMode(CENTER);
	}

	public void draw() {
		background(200);

		fill(0);
		text(title, width / 2, 30);

		drawButton(width / 2, 80, 300, 40, option1, 1);
		drawButton(width / 2, 140, 300, 40, option2, 2);
		drawButton(width / 2, 200, 300, 40, option3, 3);
		drawButton(width / 2, 260, 300, 40, option4, 4);
		drawButton(width / 2, 320, 300, 40, option5, 5);
	}

	void drawButton(float x, float y, float w, float h, String label, int id) {
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y - h / 2 && mouseY < y + h / 2)
			fill(150, 200, 255);
		else
			fill(180);

		rect(x, y, w, h, 5);

		fill(0);
		text(label, x, y);
	}

	public void mousePressed() {
		float x = width / 2;
		float w = 300;
		float h = 40;

		float y1 = 200;
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y1 - h / 2 && mouseY < y1 + h / 2)
			PApplet.main(SolarSystem.class);

		float y2 = 260;
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y2 - h / 2 && mouseY < y2 + h / 2)
			PApplet.main(IndividualBehaviour.class);

		float y3 = 320;
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y3 - h / 2 && mouseY < y3 + h / 2)
			PApplet.main(Flocking.class);
		
		float y4 = 380;
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y4 - h / 2 && mouseY < y4 + h / 2)
			PApplet.main(NormandyWar.class);
		
		float y5 = 440;
		if (mouseX > x - w / 2 && mouseX < x + w / 2 && mouseY > y5 - h / 2 && mouseY < y5 + h / 2)
			PApplet.main(SpaceWar.class);
	}

	public static void main(String[] args) {
		PApplet.main(MainMenu.class);
	}
}