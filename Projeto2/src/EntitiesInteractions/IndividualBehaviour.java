package EntitiesInteractions;

import Physics.*;
import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList; // Necessário para criar a lista de corpos para o Eye
import java.util.List;     // Necessário para a lista de corpos

public class IndividualBehaviour extends PApplet {
	private Boid boid;
	private double[] window = { -10, 10, -10, 10 };
	private float[] viewport = { 0f, 0f, 1f, 1f };
	private SubPlot plt;
	private Body target;
	private float lastUpdateTime;
	
	// Variáveis de Comportamento
	private Seek seek; 

	public void settings() {
		size(800, 800);
	}

	public void setup() {
		lastUpdateTime = millis();
		plt = new SubPlot(window, viewport, width, height);
		boid = new Boid(new PVector(), 1f, 0.5f, color(0), this, plt);
		target = new Body(new PVector(), new PVector(), 1f, 0.2f, color(255, 0, 0));
		
		// 1. Inicializar o Eye com uma lista contendo o alvo
		List<Body> targets = new ArrayList<>();
		targets.add(target);
		boid.setEye(new Eye(boid, targets));
		
		// 2. Adicionar o comportamento Seek
		seek = new Seek(1f); 
		boid.addBehaviour(seek);
	}

	public void draw() {
		int now = millis();
		float dt = (now - lastUpdateTime) / 1000f;
		lastUpdateTime = now;

		background(255);
		
		// 3. Aplicar o comportamento (Seek)
		boid.applyBehaviours(dt); 
		
		boid.display(this, plt);
		target.display(this, plt);
	}

	public void keyPressed() {
		// 4. Controlo por Teclas para Acelerar/Travar
		int increment = 1; // Valor de alteração
		
		if(key == 'a' || key == 'A') {
			// Acelerar: Aumenta a velocidade máxima e a força máxima
			boid.setVelocity(increment, true); // Aumenta a velocidade
			boid.setForce(increment, true);    // Aumenta a força
			// Adicionei uma pequena impressão para ver o estado atual (opcional)
			// System.out.println("Acelerando! MaxSpeed: " + boid.dna.maxSpeed + ", MaxForce: " + boid.dna.maxForce);
		} else if (key == 'z' || key == 'Z') {
			// Travar: Diminui a velocidade máxima e a força máxima
			boid.setVelocity(increment, false); // Diminui a velocidade
			boid.setForce(increment, false);    // Diminui a força
			// Adicionei uma pequena impressão para ver o estado atual (opcional)
			// System.out.println("Travando! MaxSpeed: " + boid.dna.maxSpeed + ", MaxForce: " + boid.dna.maxForce);
		}
	}

	public void mousePressed() {
		double[] ww = plt.getWorldCoord(mouseX, mouseY);
		target.setPosition(new PVector((float) ww[0], (float) ww[1]));
	}
}