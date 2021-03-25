package strategy;

import java.util.ArrayList;

import agent.PositionAgent;
import motor.PacmanGame;
import neuralNetwork.TrainExample;

public abstract class QLearningStrategy implements Strategy{

	protected double epsilon;
	protected double gamma;
	protected double alpha;
	
	private double baseEpsilon;
	
	
	public QLearningStrategy(double epsilon, double gamma, double alpha) {
		
		this.epsilon = epsilon;
		this.gamma = gamma;
		this.alpha = alpha;
		
		this.baseEpsilon = epsilon;
		
	}
	
	public void modeTest() {
		this.epsilon = 0;
	}
	public void modeTrain() {
		this.epsilon = baseEpsilon;
	}
	
	
	public abstract void learn(ArrayList<TrainExample> trainExamples);
	
	
}
