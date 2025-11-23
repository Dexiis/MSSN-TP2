package TP2.Bodies.Types;

import TP2.Bodies.Boid;
import TP2.Bodies.EnergyBullet;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class Neutral extends Boid {
	
	protected int health;

	public Neutral(PVector position, int color, PApplet p, SubPlot plt) {
		super(position, color, p, plt);
		this.health = 500;
	}
	
	public void hitBy(EnergyBullet bullet) {
		health -= bullet.getDamage();
	}

	public boolean isDead() {
		if (health <= 0)
			return true;
		return false;
	}

}
