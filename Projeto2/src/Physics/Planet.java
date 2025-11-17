package Physics;

import processing.core.PVector;

public class Planet extends Body{
	
	protected String name;

	public Planet(String name, PVector position, PVector velocity, float mass, float radius, int color) {
		super(position, velocity, mass, radius, color);
		this.name = name;
		}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

}
