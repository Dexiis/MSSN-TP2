package TP2.Bodies.Types;

import TP2.Bodies.*;
import TP2.Core.SubPlot;
import processing.core.PApplet;
import processing.core.PVector;

public class Red extends Boid {
	
	protected int health;

	public Red(PVector position, int color, PApplet p, SubPlot plt) {
		super(position, color, p, plt);
		this.health = 100;
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
